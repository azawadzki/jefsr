package az.jefsr.crypto;

import java.util.Arrays;

public class Key {

	public Key(byte[] key, byte[] iv) {
		this.key = key;
		this.iv = iv;
	}

	public byte[] getKey() {
		return key;
	}

	public byte[] getIv() {
		return iv;
	}

	@Override
	public boolean equals(Object other) {
		boolean ret = false;
		if (other instanceof Key) {
			Key otherKey = (Key) other;
			ret = Arrays.equals(getKey(), otherKey.getKey()) &&
				Arrays.equals(getIv(), otherKey.getIv());
		}
		return ret;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(getKey()) + Arrays.hashCode(getIv());
	}
	
	private final byte[] key;
	private final byte[] iv;
	
}