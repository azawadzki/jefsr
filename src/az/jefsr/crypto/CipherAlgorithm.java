package az.jefsr.crypto;

public interface CipherAlgorithm {
	
	abstract String getName();
	abstract int getChecksumBytesNumber();
	abstract int getIvecByteLength();
	
	abstract String getStreamAlgorithmName();
	abstract String getBlockAlgorithmName();

}
