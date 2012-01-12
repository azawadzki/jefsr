package az.jefsr.crypto;

public class BlowfishCipher implements CipherAlgorithm {

	@Override
	public String getName() {
		return "Blowfish";
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
