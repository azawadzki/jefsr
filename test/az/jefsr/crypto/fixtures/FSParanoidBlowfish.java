package az.jefsr.crypto.fixtures;

import java.util.ArrayList;
import java.util.List;

import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;

public class FSParanoidBlowfish extends FSFixture {

	private byte[] userKeyBytes = { 14, 66, -53, 18, 86, 102, 89, -16, 105,
			104, -81, 81, 117, 10, 20, -10, -37, 104, -67, 54, 53, -35, 69,
			-60, 101, 103, -37, 52, 76, 60, 59, 110, 79, -57, -48, -103, 70,
			-13, 1, 30 };
	private byte[] volumeKeyBytes = { -56, -49, 68, -54, 63, 64, 101, 76, -101,
			60, -14, -54, 65, 37, -102, -17, -111, 75, -72, -78, 29, -84, 77,
			-117, 9, -8, 84, 70, 34, 12, 67, 4, -39, 36, 41, 125, 116, 101, 86,
			-2 };

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
	public int getKeySize() {
		return 256;
	}

	@Override
	public String getConfigFilePath() {
		return "config_files/paranoid_with_blowfish/.encfs6.xml";
	}

	@Override
	public List<CoderFixture> getStreamTestVectors()
			throws CipherConfigException {
		Key k1 = getUserKey();
		byte[] i1 = { 91, 5, -70, 77, 48, -78, -67, 94, -96, -28, 119, 35, 34,
				119, 105, -2, 115, 122, 89, -40, 102, -122, -72, 78, 103, -127,
				-15, -40, 80, 113, 56, 58, -121, 112, 19, -98, 100, 6, -86, -92 };
		long iv = 3035181961L;
		byte[] o1 = { -56, -49, 68, -54, 63, 64, 101, 76, -101, 60, -14, -54,
				65, 37, -102, -17, -111, 75, -72, -78, 29, -84, 77, -117, 9,
				-8, 84, 70, 34, 12, 67, 4, -39, 36, 41, 125, 116, 101, 86, -2 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}

	@Override
	public List<CoderFixture> getBlockTestVectors()
			throws CipherConfigException {
		Key k1 = getVolumeKey();
		final byte[] i1 = { -42, -53, -4, -110, -48, -79, 45, 3, -19, 91, 3,
				56, -100, 61, 100, 33 };
		long iv = 22342;
		byte[] o1 = { 114, 101, 97, 100, 109, 101, 46, 116, 120, 116, 6, 6, 6,
				6, 6, 6 };
		List<CoderFixture> r = new ArrayList<CoderFixture>();
		r.add(new CoderFixture(k1, i1, iv, o1));
		return r;
	}
}
