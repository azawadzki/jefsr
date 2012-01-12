package az.jefsr.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.CoderFixture;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;
import az.jefsr.crypto.fixtures.FSParanoidBlowfish;

public class CodersTests {

	@BeforeClass
	public static void setUpClass() throws Exception {

		fsFixtures = new ArrayList<FSFixture>();
		fsFixtures.add(new FSParanoidAes());
		fsFixtures.add(new FSParanoidBlowfish());
	}

	@Test
	public void testDecodeStream() throws CipherConfigException, CipherDataException {
		for (FSFixture fix: fsFixtures) {
			performDecodeStreamTests(fix);
		}
	}   

	private void performDecodeStreamTests(FSFixture fsFixture) throws CipherConfigException, CipherDataException {
		for (CoderFixture f: fsFixture.getStreamTestVectors()) {
			Config config = fsFixture.getConfig();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(config.getCipherAlg().getName());
			Coder coder = CoderFactory.getInstance().createInstance(f.getKey(), cipher, config);
			byte[] out = coder.decodeStream(f.getInput(), f.getIv());
			assertThat(out, equalTo(f.getOutput()));
		}
	}
	
	@Test
	public void testDecodeBlock() throws CipherConfigException, CipherDataException {
		for (FSFixture fix: fsFixtures) {
			performDecodeBlockTests(fix);
		}
	}   

	private void performDecodeBlockTests(FSFixture fsFixture) throws CipherConfigException, CipherDataException {
		for (CoderFixture f: fsFixture.getBlockTestVectors()) {
			Config config = fsFixture.getConfig();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(config.getCipherAlg().getName());
			Coder coder = CoderFactory.getInstance().createInstance(f.getKey(), cipher, config);
			byte[] out = coder.decodeBlock(f.getInput(), f.getIv());
			assertThat(out, equalTo(f.getOutput()));
		}
	}
	
	static List<FSFixture> fsFixtures;

}