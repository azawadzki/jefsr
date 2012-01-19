package az.jefsr.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherAlgorithm;
import az.jefsr.crypto.CipherAlgorithmFactory;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.CoderFactory;
import az.jefsr.crypto.Key;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;

public class MacFileDecoderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testParanoidAes() throws CipherDataException, IOException,
			CipherConfigException {
		byte[] input = { -49, 73, -69, -44, 8, -126, -28, 120, 71, 71, 113,
				-110, 77, -68, 5, 92, 18, -101, 82, 17, 115, 122, 41, 42, -55,
				-45, 1, -15, 64, -99, 37, 62 };
		byte[] expectedOutput = "Test 1/2b/3.txt\n".getBytes();
		byte[] outputBuffer = new byte[256];

		FSFixture fix = new FSParanoidAes();
		Key volumeKey = fix.getVolumeKey();
		Config config = fix.getConfig();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
				.createInstance(config.getCipherAlg().getName());
		Coder cryptoCoder = CoderFactory.getInstance().createInstance(
				volumeKey, cipher, config);

		String nameAlg = config.getNameAlg().getName();
		NameDecoder nameDecoder = NameDecoderFactory.getInstance()
				.createInstance(nameAlg, cryptoCoder, config);

		PathInfo pathInfo = nameDecoder
				.decodePathInfo("YKGAxrfWfbxeek7,t-L35lZN/TTUy3UG3HqBkN5cOTP,s0PLf/PBU6Fr0tBITS53aZLnmNQJL7");

		ByteArrayInputStream ins = new ByteArrayInputStream(input);
		FileDecoder fileDecipherer = new CipherFileDecoder(new NullFileDecoder(
				ins), pathInfo, cryptoCoder, config);
		FileDecoder decoder = new MacFileDecoder(fileDecipherer, cryptoCoder,
				config);

		int bytesRead;
		int dstPos = 0;
		byte[] buffer = new byte[256];
		do {
			if ((bytesRead = decoder.read(buffer)) > 0) {
				System.arraycopy(buffer, 0, outputBuffer, dstPos, bytesRead);
				dstPos += bytesRead;
			}

		} while (bytesRead != -1);
		assertThat(Arrays.copyOfRange(outputBuffer, 0, dstPos),
				equalTo(expectedOutput));
	}

}
