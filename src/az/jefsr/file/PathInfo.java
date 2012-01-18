package az.jefsr.file;

public class PathInfo {

	PathInfo(String encryptedPath, String decryptedPath, long iv) {
		this.encryptedPath = encryptedPath;
		this.decryptedPath = decryptedPath;
		this.iv = iv;
	}
	
	public String getEncryptedPath() {
		return encryptedPath;
	}
	public String getDecryptedPath() {
		return decryptedPath;
	}
	public long getIv() {
		return iv;
	}

	private String encryptedPath;
	private String decryptedPath;
	private long iv;
}
