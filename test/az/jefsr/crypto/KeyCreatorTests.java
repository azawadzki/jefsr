package az.jefsr.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.iharder.base64.Base64;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;
import az.jefsr.crypto.fixtures.FSParanoidBlowfish;
import az.jefsr.crypto.fixtures.FSStandard;

public class KeyCreatorTests {

	@BeforeClass
	public static void setUpClass() throws Exception {
		fsFixtures = new ArrayList<FSFixture>();
		fsFixtures.add(new FSStandard());
		fsFixtures.add(new FSParanoidAes());
		fsFixtures.add(new FSParanoidBlowfish());
	}

	@Test
	public void testCreateUserKey() throws CipherConfigException {
		for (FSFixture fix : fsFixtures) {
			Key referenceKey = fix.getUserKey();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
					.createInstance(fix.getConfig().getCipherAlg().getName());
			KeyCreator keyCreator = new KeyCreator(cipher, fix.getConfig());
			Key k = keyCreator.createUserKey(fix.getUserPassword());
			assertThat(k, equalTo(referenceKey));
		}
	}

	@Test(expected = CipherConfigException.class)
	public void testChecksumVerification() throws CipherConfigException,
			IOException {
		FSParanoidAes fix = new FSParanoidAes();
		Config config = fix.getConfig();
		byte[] buf = Base64.decode(config.getEncodedKeyData());
		buf[0] += 0xbad;
		config.setEncodedKeyData(new String(Base64.encodeBytes(buf)));
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
				.createInstance(config.getCipherAlg().getName());

		Coder coder = CoderFactory.getInstance().createInstance(
				fix.getUserKey(), cipher, config);
		KeyCreator keyCreator = new KeyCreator(cipher, config);
		keyCreator.createVolumeKey(coder);
	}

	@Test
	public void testCreateVolumeKeyCoderIntegration()
			throws CipherConfigException {
		for (FSFixture fix : fsFixtures) {
			Config config = fix.getConfig();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
					.createInstance(config.getCipherAlg().getName());
			Coder coder = CoderFactory.getInstance().createInstance(
					fix.getUserKey(), cipher, config);
			KeyCreator keyCreator = new KeyCreator(cipher, config);
			Key k = keyCreator.createVolumeKey(coder);
			Key referenceKey = fix.getVolumeKey();
			assertThat(k, equalTo(referenceKey));
		}
	}

	static List<FSFixture> fsFixtures;

}
