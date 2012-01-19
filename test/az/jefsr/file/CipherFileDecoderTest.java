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
import az.jefsr.crypto.fixtures.FSStandard;
import az.jefsr.crypto.fixtures.FSStandardExternalChaining;

public class CipherFileDecoderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testStandard() throws CipherDataException, IOException, CipherConfigException {
		byte[] input = { 88, -37, -33, -71, -28, 8, -95, -21, 121, 7, -115, -49, 79, -118, -120, 82, 36, 46, 122, 38, -25, -37, -40, 120 };
		byte[] expectedOutput = "Test 1/2b/3.txt\n".getBytes();
		byte[] outputBuffer = new byte[256];

		FSFixture fix = new FSStandard();
		Key volumeKey = fix.getVolumeKey();
		Config config = fix.getConfig();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(config.getCipherAlg().getName());
		Coder cryptoCoder = CoderFactory.getInstance().createInstance(volumeKey, cipher, config);
		
		String nameAlg = config.getNameAlg().getName();
		NameDecoder nameDecoder = NameDecoderFactory.getInstance().createInstance(nameAlg, cryptoCoder, config);
		
		PathInfo pathInfo = nameDecoder.decodePathInfo("eSP1eCF1HVHgocrYKyit2nsU/hEf8OO-fEb9rXNsUZISTaqC6/gCxa-NgPncEzpIXHsIKtsFR5");
		
		ByteArrayInputStream ins = new ByteArrayInputStream(input);
		FileDecoder decoder = new CipherFileDecoder(new NullFileDecoder(ins), pathInfo, cryptoCoder, config);
		
		int bytesRead;
		int dstPos = 0;
		byte[] buffer = new byte[256];
		do {
			if ((bytesRead = decoder.read(buffer)) > 0) {
				System.arraycopy(buffer, 0, outputBuffer, dstPos, bytesRead);
				dstPos += bytesRead;
			}
			
		} while (bytesRead != -1);
		assertThat(Arrays.copyOfRange(outputBuffer, 0, dstPos), equalTo(expectedOutput));
	}
	


	@Test
	public void testStandardWithExternalChaining() throws CipherDataException, IOException, CipherConfigException {
		byte[] input = { 4, 103, 9, 119, -98, -38, 8, -119, -51, -111, 2, 63, -23, 0, -73, 68, -66, -65, 127, 9, 15, -25, 66, 84 };
		byte[] expectedOutput = "Test 1/2b/3.txt\n".getBytes();
		byte[] outputBuffer = new byte[256];

		FSFixture fix = new FSStandardExternalChaining();
		Key volumeKey = fix.getVolumeKey();
		Config config = fix.getConfig();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(config.getCipherAlg().getName());
		Coder cryptoCoder = CoderFactory.getInstance().createInstance(volumeKey, cipher, config);
		
		String nameAlg = config.getNameAlg().getName();
		NameDecoder nameDecoder = NameDecoderFactory.getInstance().createInstance(nameAlg, cryptoCoder, config);
		
		PathInfo pathInfo = nameDecoder.decodePathInfo("eSP1eCF1HVHgocrYKyit2nsU/hEf8OO-fEb9rXNsUZISTaqC6/gCxa-NgPncEzpIXHsIKtsFR5");
		
		ByteArrayInputStream ins = new ByteArrayInputStream(input);
		FileDecoder decoder = new CipherFileDecoder(new NullFileDecoder(ins), pathInfo, cryptoCoder, config);
		
		int bytesRead;
		int dstPos = 0;
		byte[] buffer = new byte[256];
		do {
			if ((bytesRead = decoder.read(buffer)) > 0) {
				System.arraycopy(buffer, 0, outputBuffer, dstPos, bytesRead);
				dstPos += bytesRead;
			}
			
		} while (bytesRead != -1);
		assertThat(Arrays.copyOfRange(outputBuffer, 0, dstPos), equalTo(expectedOutput));
	}
}
