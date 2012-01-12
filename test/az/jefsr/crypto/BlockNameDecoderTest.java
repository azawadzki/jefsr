package az.jefsr.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;

public class BlockNameDecoderTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		fsFixtureAes = new FSParanoidAes();
	}

	@Test
	public void testDecodeCorrectPath() throws CipherConfigException, CipherDataException {
		Config config = fsFixtureAes.getConfig();
		String algName = config.getCipherAlg().getName();
		String nameAlg = config.getNameAlg().getName();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(algName);
		Coder coder = CoderFactory.getInstance().createInstance(fsFixtureAes.getVolumeKey(), cipher, config);
		NameDecoder nameDec = NameDecoderFactory.getInstance().createInstance(nameAlg, coder, config);
		
		String in1 = "7yp5rUju7WJz0HqxocaWPm9K";
		String out1 = "readme.txt";
		assertThat(nameDec.decodePath(in1), equalTo(out1));
		
		String in2 = "YKGAxrfWfbxeek7,t-L35lZN/TTUy3UG3HqBkN5cOTP,s0PLf/PBU6Fr0tBITS53aZLnmNQJL7";
		String out2 = "1/2b/3.txt";
		assertThat(nameDec.decodePath(in2), equalTo(out2));
	}
	
	@Test(expected=CipherDataException.class)
	public void testDecodeWrongPath() throws CipherDataException, CipherConfigException {
		Config config = fsFixtureAes.getConfig();
		String algName = config.getCipherAlg().getName();
		String nameAlg = config.getNameAlg().getName();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(algName);

		Coder coder = CoderFactory.getInstance().createInstance(fsFixtureAes.getVolumeKey(), cipher, config);
		NameDecoder nameDec = NameDecoderFactory.getInstance().createInstance(nameAlg, coder, config);
		
		nameDec.decodePath("dummy");
	}
	
	static FSFixture fsFixtureAes;
}
