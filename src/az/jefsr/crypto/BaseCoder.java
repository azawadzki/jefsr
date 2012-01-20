/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.jefsr.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import az.jefsr.config.Config;

class BaseCoder extends Coder {

	public BaseCoder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
        super(key, cipher, config);
		try {
			streamCipher = Cipher.getInstance(cipher.getStreamAlgorithmName());
			blockCipher = Cipher.getInstance(cipher.getBlockAlgorithmName());
			mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key.getBytes(),"HmacSHA1"));
		} catch (NoSuchAlgorithmException e) {
			throw new CipherConfigException(e);
		} catch (NoSuchPaddingException e) {
			throw new CipherConfigException(e);
		} catch (InvalidKeyException e) {
			throw new CipherConfigException(e);
		}
	}

	@Override
	public byte[] decodeStream(byte[] stream, long iv) throws CipherDataException {
		byte[] buf = new byte[stream.length];
		int read = decodeStream(stream, stream.length, iv, buf);
		if (read != buf.length) {
			buf = Arrays.copyOf(buf, read);
		}
		return buf;
	}

	@Override
	public int decodeStream(byte[] stream, int inputLength, long iv, byte[] output)
			throws CipherDataException {
		assert(output.length >= inputLength);
		try {		
			byte[] iv1 = updateIv(iv + 1, getCipher().getIvecByteLength());
			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), getCipher().getName()), new IvParameterSpec(iv1));
			int decipheredInStep1 = streamCipher.doFinal(stream, 0, inputLength, output);
			unshuffle(output, decipheredInStep1);
			flip(output, decipheredInStep1);

			byte[] iv2 = updateIv(iv, getCipher().getIvecByteLength());
			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), getCipher().getName()), new IvParameterSpec(iv2));
			int decipheredInStep2 = streamCipher.doFinal(output, 0, decipheredInStep1, output);
			unshuffle(output, decipheredInStep2);
			return decipheredInStep2;
		} catch (InvalidKeyException e) {
			// not a config error per se, as they should have been reported by KeyCreator already.
			throw new CipherDataException("Volume key corrupted", e);
		} catch (InvalidAlgorithmParameterException e) {
			// as above.
			throw new CipherDataException("Volume configuration corrupted", e);
		} catch (IllegalBlockSizeException e) {
			throw new CipherDataException(e);
		} catch (BadPaddingException e) {
			throw new CipherDataException(e);
		} catch (ShortBufferException e) {
			throw new CipherDataException("Incorrect buffer size for decrypted data", e);
		}
	}

	@Override
	public byte[] decodeBlock(byte[] block, long iv) throws CipherDataException {
		byte[] buf = new byte[block.length];
		int read = decodeBlock(block, block.length, iv, buf);
		if (read != buf.length) {
			buf = Arrays.copyOf(buf, read);
		}
		return buf;
	}

	@Override
	public int decodeBlock(byte[] block, int inputLength, long iv, byte[] output) throws CipherDataException {
		assert(output.length >= inputLength);
		byte[] ivec = updateIv(iv, getCipher().getIvecByteLength());
		try {
			blockCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), getCipher().getName()), new IvParameterSpec(ivec));
			return blockCipher.doFinal(block, 0, inputLength, output);
		} catch (InvalidKeyException e) {
			// not a config error per se, as they should have been reported by KeyCreator already
			throw new CipherDataException("Volume key corrupted", e);
		} catch (InvalidAlgorithmParameterException e) {
			// as above
			throw new CipherDataException("Volume configuration corrupted", e);
		} catch (IllegalBlockSizeException e) {
			throw new CipherDataException(e);
		} catch (BadPaddingException e) {
			throw new CipherDataException(e);
		} catch (ShortBufferException e) {
			throw new CipherDataException("Incorrect buffer size for decrypted data", e);
		}
	}

	private void unshuffle(byte[] buf, int length) {
		for (int i = length - 1; i != 0; --i) {
			buf[i] ^= buf[i - 1];
		}
	}
	
	private void flip(byte[] buf, int length) {
		byte[] revBuf = new byte[64];
		int bytesLeft = length;
		int flipPos = 0;
		while (bytesLeft != 0) {
			int toFlip = Math.min(revBuf.length, bytesLeft);
			for (int i = 0; i < toFlip; ++i) {
				revBuf[i] = buf[flipPos + toFlip - (i + 1)];
			}
			System.arraycopy(revBuf, 0, buf, flipPos, toFlip);
			bytesLeft -= toFlip;
			flipPos += toFlip;
		}
	}

	private byte[] updateIv(long seed, int size) {
		mac.reset();
		mac.update(getKey().getIv());
		
		ByteBuffer bb = ByteBuffer.allocate(Long.SIZE / 8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putLong(seed);
		mac.update(bb.array());
		
		byte[] dig = mac.doFinal();
		return Arrays.copyOf(dig, size);
	}
	
	private Cipher streamCipher;
	private Cipher blockCipher;
	private Mac mac;
}
