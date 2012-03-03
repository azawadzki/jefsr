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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.iharder.base64.Base64;
import az.jefsr.config.Config;
import az.jefsr.nativeext.PBKDF2;

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

		final int keyByteLen = keyLength / 8;
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength + getCipher().getIvecByteLength()*8);
		SecretKey secret;
		try {
			secret = new SecretKeySpec(createEncodedUserSecretKey(spec), getCipher().getName());
		} catch (InvalidKeySpecException e) {
			throw new CipherConfigException(e);
		}
		byte[] keyBytes = Arrays.copyOf(secret.getEncoded(), keyByteLen);
		byte[] ivBytes = Arrays.copyOfRange(secret.getEncoded(), keyByteLen, keyByteLen + getCipher().getIvecByteLength());

		return new Key(keyBytes, ivBytes);
	}

	byte[] createEncodedUserSecretKey(PBEKeySpec spec) throws InvalidKeySpecException, CipherConfigException {
		try {
			Charset utf8 = Charset.forName("UTF-8");
			CharBuffer cb = CharBuffer.wrap(spec.getPassword());
			byte[] password = utf8.encode(cb).array();
			return PBKDF2.getInstance().deriveKey(password, spec.getSalt(), spec.getIterationCount(), spec.getKeyLength());
		} catch (UnsatisfiedLinkError linkerError) {
			// no native functionality provided, fall back to Java version...
			SecretKeyFactory factory;
			try {
				factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				return factory.generateSecret(spec).getEncoded();
			} catch (NoSuchAlgorithmException e) {
				throw new CipherConfigException(e);
			}
		}
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
