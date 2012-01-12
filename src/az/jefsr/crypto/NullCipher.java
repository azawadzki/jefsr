package az.jefsr.crypto;

class NullCipher implements CipherAlgorithm {

	static final public String NAME = "NULL";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getChecksumBytesNumber() {
		return 0;
	}

	@Override
	public int getIvecByteLength() {
		return 0;
	}

	@Override
	public String getStreamAlgorithmName() {
		return "NULL";
	}

	@Override
	public String getBlockAlgorithmName() {
		return "NULL";
	}

}
