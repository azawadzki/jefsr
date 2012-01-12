package az.jefsr.crypto;

import az.jefsr.config.Config;

public abstract class Coder {

	public Coder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
        this.cipher = cipher;
        this.key = key;
        this.config = config;
	}

	public abstract byte[] decodeStream(byte[] stream, long iv) throws CipherDataException;
	public abstract byte[] decodeBlock(byte[] block, long iv) throws CipherDataException;

	public Key getKey() {
		return key;
	}
	
	public Config getConfig() {
		return config;
	}

    public CipherAlgorithm getCipher() {
        return cipher;    
    }

	private CipherAlgorithm cipher;
	private Key key;
	private Config config;
}
