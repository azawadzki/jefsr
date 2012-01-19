package az.jefsr.crypto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.iharder.base64.Base64;
import az.jefsr.config.Config;

public class KeyCreator {
	
	public KeyCreator(CipherAlgorithm cipher, Config config) {
		this.cipher = cipher;
		this.config = config;
	}
	
	public Key createUserKey(String password) throws CipherConfigException {
		byte[] salt;
		try {
			salt = Base64.decode(getConfig().getSaltData());
		} catch (IOException e) {
			throw new CipherConfigException("Salt data inconsistent");
		}
		int iterationCount = getConfig().getKdfIterations();
		int keyLength = getConfig().getKeySize();
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new CipherConfigException(e);
		}

		final int keyByteLen = keyLength / 8;
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength + getCipher().getIvecByteLength()*8);
		SecretKey secret;
		
		try {
			secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), getCipher().getName());
		} catch (InvalidKeySpecException e) {
			throw new CipherConfigException(e);

		}
		byte[] keyBytes = Arrays.copyOf(secret.getEncoded(), keyByteLen);
		byte[] ivBytes = Arrays.copyOfRange(secret.getEncoded(), keyByteLen, keyByteLen + getCipher().getIvecByteLength());

		return new Key(keyBytes, ivBytes);
	}

	public Key createVolumeKey(Coder passwordCoder) throws CipherConfigException {
		byte[] xmlKeyData;
		try {
			xmlKeyData = Base64.decode(getConfig().getEncodedKeyData());
		} catch (IOException e1) {
			throw new CipherConfigException("Key data inconsistent");
		}

		ByteBuffer bb = ByteBuffer.wrap(xmlKeyData, 0, getCipher().getChecksumBytesNumber());
		int checksum = bb.order(ByteOrder.BIG_ENDIAN).asIntBuffer().get();
		byte[] encodedKeyBytes = Arrays.copyOfRange(xmlKeyData, getCipher().getChecksumBytesNumber(), xmlKeyData.length);

		final int keyByteLen = getConfig().getKeySize() / 8;
		
		byte[] deciphered;
		try {
			deciphered = passwordCoder.decodeStream(encodedKeyBytes, 0x00000000ffffffffL & checksum);
		} catch (CipherDataException e) {
			throw new CipherConfigException("Volume config and key data inconsistent");
		}
		int checksum2 = MacUtils.mac32(deciphered, passwordCoder.getKey(), null);
		if (checksum != checksum2) {
			throw new CipherConfigException("Checksum error in decoded volume key!");
		}
		
		byte[] keyBytes = Arrays.copyOf(deciphered, keyByteLen);
		byte[] ivBytes = Arrays.copyOfRange(deciphered, keyByteLen, keyByteLen + getCipher().getIvecByteLength());
		return new Key(keyBytes, ivBytes);
	}
	
	public CipherAlgorithm getCipher() {
		return cipher;
	}
	
	public Config getConfig() {
		return config;
	}
	
	private CipherAlgorithm cipher;
	private Config config;
}
