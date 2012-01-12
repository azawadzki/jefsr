package az.jefsr.crypto;

import az.jefsr.config.Config;

class NullCoder extends Coder {

	public NullCoder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
		super(key, cipher, config);
	}
	
	public byte[] decodeStream(byte[] stream, long iv) throws CipherDataException {
		return stream;
	}

	public byte[] decodeBlock(byte[] block, long iv) throws CipherDataException {
		return block;
	}

}

