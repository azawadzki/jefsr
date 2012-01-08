package az.jefsr.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

import az.jefsr.config.Config;

class AesCoder extends Coder {

	final static int KEY_CHECKSUM_BYTES = 4;
	final static int MAX_IVEC_BYTES = 16;

	public AesCoder(Key key, Config config) throws CipherConfigException {
		super(key, config);
		try {
			streamCipher = Cipher.getInstance("AES/CFB/NoPadding");
			blockCipher = Cipher.getInstance("AES/CBC/NoPadding");
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

	static class AesKeyCreator extends KeyCreator {

		@Override
		public Key createUserKey(String password, Config config) throws CipherConfigException {
			try {
				byte[] salt = Base64.decode(config.getSaltData());
				int iterationCount = config.getKdfIterations();
				int keyLength = config.getKeySize();
				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

				final int keyByteLen = keyLength / 8;
				KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength + MAX_IVEC_BYTES*8);
				SecretKey secret;

				secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
				byte[] keyBytes = Arrays.copyOf(secret.getEncoded(), keyByteLen);
				byte[] ivBytes = Arrays.copyOfRange(secret.getEncoded(), keyByteLen, keyByteLen + MAX_IVEC_BYTES);
				
				return new Key(keyBytes, ivBytes);
			} catch (NoSuchAlgorithmException e) {
				throw new CipherConfigException(e);
			} catch (InvalidKeySpecException e) {
				throw new CipherConfigException(e);
			}
		}

		@Override
		public Key createVolumeKey(Coder passwordCoder, Config config) throws CipherConfigException {
			long checksum = 0;
			byte[] xmlKeyData = Base64.decode(config.getEncodedKeyData());

			for(int i = 0; i < KEY_CHECKSUM_BYTES; ++i) {
				checksum = (checksum << 8) | (0xff & xmlKeyData[i]);
			}
			byte[] encodedKeyBytes = Arrays.copyOfRange(xmlKeyData, KEY_CHECKSUM_BYTES, xmlKeyData.length);

			final int keyByteLen = config.getKeySize() / 8;
			
			byte[] deciphered;
			try {
				deciphered = passwordCoder.decodeStream(encodedKeyBytes, checksum);
			} catch (CipherDataException e) {
				throw new CipherConfigException("Volume config and key data inconsistent");
			}
			
			byte[] keyBytes = Arrays.copyOf(deciphered, keyByteLen);
			byte[] ivBytes = Arrays.copyOfRange(deciphered, keyByteLen, keyByteLen + MAX_IVEC_BYTES);
			return new Key(keyBytes, ivBytes);
		}
		
	}
	
	@Override
	public byte[] decodeStream(byte[] stream, long iv) throws CipherDataException {
		byte[] iv1 = updateIv(iv + 1, MAX_IVEC_BYTES);
		try {
			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), "AES"), new IvParameterSpec(iv1));
			byte[] decipheredStep1 = streamCipher.doFinal(stream);
			unshuffle(decipheredStep1);
			flip(decipheredStep1);
			byte[] iv2 = updateIv(iv, MAX_IVEC_BYTES);
			streamCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), "AES"), new IvParameterSpec(iv2));
			byte[] decipheredStep2 = streamCipher.doFinal(decipheredStep1);
			unshuffle(decipheredStep2);
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

	@Override
	public byte[] decodeBlock(byte[] block, long iv) throws CipherDataException {
		byte[] ivec = updateIv(iv, MAX_IVEC_BYTES);
		try {
			blockCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey().getBytes(), "AES"), new IvParameterSpec(ivec));
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
	
	@Override
	public KeyCreator getKeyCreationStrategy() {
		return new AesKeyCreator();
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
	
	Cipher streamCipher;
	Cipher blockCipher;
	Mac mac;

}
