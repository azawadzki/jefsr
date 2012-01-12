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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import az.jefsr.Main;
import az.jefsr.config.Config;

public class Coder {

	public Coder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
		try {
			streamCipher = Cipher.getInstance(cipher.getStreamAlgorithmName());
			blockCipher = Cipher.getInstance(cipher.getBlockAlgorithmName());
			mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key.getBytes(),"HmacSHA1"));
			this.cipher = cipher;
			this.key = key;
			this.config = config;
		} catch (NoSuchAlgorithmException e) {
			throw new CipherConfigException(e);
		} catch (NoSuchPaddingException e) {
			throw new CipherConfigException(e);
		} catch (InvalidKeyException e) {
			throw new CipherConfigException(e);
		}
	}

	public byte[] decodeStream(byte[] stream, long iv) throws CipherDataException {
		try {		
			byte[] iv1 = updateIv(iv + 1, cipher.getIvecByteLength());
			Main.print(iv1, "ivec");
			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), cipher.getName()), new IvParameterSpec(iv1));
			byte[] decipheredStep1 = streamCipher.doFinal(stream);
			Main.print(decipheredStep1, "first stage");
			unshuffle(decipheredStep1);
			Main.print(decipheredStep1, "unshuffled");
			
			flip(decipheredStep1);
			Main.print(decipheredStep1, "flipped");

			byte[] iv2 = updateIv(iv, cipher.getIvecByteLength());
			Main.print(iv2, "ivec");

			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), cipher.getName()), new IvParameterSpec(iv2));
			byte[] decipheredStep2 = streamCipher.doFinal(decipheredStep1);
			Main.print(decipheredStep2, "second stage");

			unshuffle(decipheredStep2);
			Main.print(decipheredStep1, "unshuffled2");

			
			return decipheredStep2;		
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
		}
	}

	public byte[] decodeBlock(byte[] block, long iv) throws CipherDataException {
		byte[] ivec = updateIv(iv, cipher.getIvecByteLength());
		try {
			blockCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), cipher.getName()), new IvParameterSpec(ivec));
			return blockCipher.doFinal(block);
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
		}
	}

	private void unshuffle(byte[] buf) {
		for (int i = buf.length - 1; i != 0; --i) {
			buf[i] ^= buf[i - 1];
		}
	}
	
	private void flip(byte[] buf) {
		byte[] revBuf = new byte[64];
		int bytesLeft = buf.length;
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
	

	public Key getKey() {
		return key;
	}
	
	public Config getConfig() {
		return config;
	}
	
	private Cipher streamCipher;
	private Cipher blockCipher;
	private Mac mac;
	private CipherAlgorithm cipher;
	private Key key;
	private Config config;
}
