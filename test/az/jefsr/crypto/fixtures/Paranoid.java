package az.jefsr.crypto.fixtures;

public class Paranoid extends FSFixture {
	private byte[] userKeyBytes = { 75, 82, -40, -82, 85, -81, -20, 56, 53, -63, 25, -19, -128, 0, -124, -128, -39, 83, 120, -28, -44, 11, 67, -15, 47, -8, -12, 49, 125, -27, 127, -41, -55, 56, 24, -78, 35, -125, -102, 0, -10, -80, -114, -5, 78, 50, -27, -25 };
	private byte[] volumeKeyBytes = { 50, -83, 119, -8, 117, 48, -83, 9, -124, -48, 111, 124, -102, 78, -7, 121, -73, 1, -79, 76, -51, -61, -92, -48, 74, 24, -12, -125, -39, 32, 87, 18, -52, 25, 96, -52, 75, -35, -36, -95, -108, 121, -51, 1, 43, 20, -77, -27 };
	

	@Override
	public byte[] getUserKeyBytes() {
		return userKeyBytes;
	}
	
	@Override
	public String getUserPassword() {
		return "test";
	}
	
	@Override
	public byte[] getVolumeKeyBytes() {
		return volumeKeyBytes;
	}
	
	@Override
	public  int getKeySize() {
		return 256;
	}

	@Override
	public String getConfigFilePath() {
		return "paranoid/.encfs6.xml";
	}

}
