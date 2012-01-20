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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Key {

	public Key(byte[] key, byte[] iv) throws CipherConfigException {
		this.key = key;
		this.iv = iv;
		
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(getBytes(),"HmacSHA1"));
		} catch (NoSuchAlgorithmException e) {
			throw new CipherConfigException(e);
		} catch (InvalidKeyException e) {
			throw new CipherConfigException(e);
		}
	}

	public byte[] getBytes() {
		return key;
	}

	public byte[] getIv() {
		return iv;
	}
	
	public Mac getHMacCounter() {
		return mac;
	}

	@Override
	public boolean equals(Object other) {
		boolean ret = false;
		if (other instanceof Key) {
			Key otherKey = (Key) other;
			ret = Arrays.equals(getBytes(), otherKey.getBytes()) &&
				Arrays.equals(getIv(), otherKey.getIv());
		}
		return ret;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(getBytes()) + Arrays.hashCode(getIv());
	}
	
	private final byte[] key;
	private final byte[] iv;
	private Mac mac;
}
