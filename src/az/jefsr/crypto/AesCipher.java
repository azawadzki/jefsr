package az.jefsr.crypto;

class AesCipher implements CipherAlgorithm {

	static final public String NAME = "AES";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getChecksumBytesNumber() {
		return 4;
	}

	@Override
	public int getIvecByteLength() {
		return 16;
	}

	@Override
	public String getStreamAlgorithmName() {
		return "AES/CFB/NoPadding";
	}

	@Override
	public String getBlockAlgorithmName() {
		return "AES/CBC/NoPadding";
	}

}
