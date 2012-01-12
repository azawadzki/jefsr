package az.jefsr.crypto.fixtures;

import java.util.ArrayList;
import java.util.List;

import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;

public class FSParanoidAes extends FSFixture {
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

	@Override
	public List<CoderFixture> getStreamTestVectors() throws CipherConfigException {
		Key k1 = getUserKey();
		byte[] i1 = { 45, 98, -3, -12, 67, -8, -83, -69, -85, 37, 33, -115, -119, -103, -111, -13, -20, -84, 0, 10, -110, -61, -25, -17, 113, 79, 44, -91, -44, 43, 56, 107, -69, -82, -37, -56, 80, 123, -95, -34, -29, -32, 97, 37, -119, -22, -5, 83 };
		long iv = 1004524580;
		byte[] o1 = { 50, -83, 119, -8, 117, 48, -83, 9, -124, -48, 111, 124, -102, 78, -7, 121, -73, 1, -79, 76, -51, -61, -92, -48, 74, 24, -12, -125, -39, 32, 87, 18, -52, 25, 96, -52, 75, -35, -36, -95, -108, 121, -51, 1, 43, 20, -77, -27 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}

	@Override
	public List<CoderFixture> getBlockTestVectors() throws CipherConfigException {
		Key k1 = getVolumeKey();
		final byte[] i1 = { 31, 55, -8, -22, -119, 88, -3, -62, 100, -9, 52, 106, -118, -101, -68, 88 };
		long iv = 35167;
		byte[] o1 = { 114, 101, 97, 100, 109, 101, 46, 116, 120, 116, 6, 6, 6, 6, 6, 6 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}

}
