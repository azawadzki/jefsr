package az.jefsr.crypto.fixtures;

import java.util.ArrayList;
import java.util.List;

import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;

public class FSStandard extends FSFixture {

	private byte[] userKeyBytes = { 67, 19, 65, -122, 0, 64, -118, -98, -111, -125, -26, -91, 105, 21, -67, 115, 14, 109, -78, -57, 31, 120, 117, 109, 77, 54, -123, -61, -31, 6, -116, 4, -15, 13, -25, -111, 97, 122, 10, -118, };
	private byte[] volumeKeyBytes = { -10, -49, 73, 90, 91, 117, -79, 61, -108, -18, -16, 127, 102, -113, 93, -51, 105, 37, -26, -119, 104, 44, 17, 84, 57, 100, 11, -67, 92, -105, -117, -65, 30, -58, -68, -92, 64, -37, -54, 56 };

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
		return 192;
	}

	@Override
	public String getConfigFilePath() {
		return "config_files/standard/.encfs6.xml";
	}

	@Override
	public List<CoderFixture> getStreamTestVectors() throws CipherConfigException {
		Key k1 = getUserKey();
		byte[] i1 = { 118, -122, -69, 127, -13, -37, 21, -46, 41, 8, -27, 21, 68, 62, -83, -126, -18, 46, 96, 94, 12, 37, -28, 25, -31, -93, -43, -69, -18, 111, 67, -115, -128, 46, 33, 2, -99, -72, 31, 2 };
		long iv = 272820254;
		byte[] o1 = { -10, -49, 73, 90, 91, 117, -79, 61, -108, -18, -16, 127, 102, -113, 93, -51, 105, 37, -26, -119, 104, 44, 17, 84, 57, 100, 11, -67, 92, -105, -117, -65, 30, -58, -68, -92, 64, -37, -54, 56 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}

	@Override
	public List<CoderFixture> getBlockTestVectors() throws CipherConfigException {
		Key k1 = getVolumeKey();
		final byte[] i1 = { 78, 69, -36, -2, -48, 96, 34, -94, 80, -14, 33, -57, 35, -48, -119, 9 };
		long iv = 43564;
		byte[] o1 = { 76, 101, 110, 110, 97, 46, 112, 110, 103, 7, 7, 7, 7, 7, 7, 7 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}

}
