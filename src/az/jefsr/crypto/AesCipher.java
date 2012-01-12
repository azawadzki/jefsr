package az.jefsr.crypto;

public class AesCipher implements CipherAlgorithm {

	@Override
	public String getName() {
		return "AES";
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
