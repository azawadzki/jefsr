package az.jefsr.crypto.fixtures;

import java.util.ArrayList;
import java.util.List;

import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;

public class FSStandardExternalChaining extends FSStandard {

	@Override
	public String getConfigFilePath() {
		return "standard_with_external_ivchaining/.encfs6.xml";
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
}
