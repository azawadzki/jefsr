package az.jefsr.crypto;

class BlowfishCipher implements CipherAlgorithm {
	
	static final public String NAME = "Blowfish";

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
		return 8;
	}

	@Override
	public String getStreamAlgorithmName() {
		return "Blowfish/CFB/NoPadding";
	}

	@Override
	public String getBlockAlgorithmName() {
		return "Blowfish/CBC/NoPadding";
	}

}
