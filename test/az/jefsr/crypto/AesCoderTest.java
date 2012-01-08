package az.jefsr.crypto;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.crypto.fixtures.CoderFixture;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoid;

public class AesCoderTest {

	@BeforeClass
	public static void setUpClass() throws Exception {

		fsParanoid = new FSParanoid();
	}

	@Test
	public void testDecodeStream() throws CipherConfigException, CipherDataException {
		for (CoderFixture f: fsParanoid.getStreamTestVectors()) {
			Coder coder = new AesCoder(f.getKey(), fsParanoid.getConfig());
			byte[] out = coder.decodeStream(f.getInput(), f.getIv());
			assertTrue(Arrays.equals(out, f.getOutput()));
		}
	}   

	@Test
	public void testDecodeBlock() throws CipherConfigException, CipherDataException {
		for (CoderFixture f: fsParanoid.getBlockTestVectors()) {
			Coder coder = new AesCoder(f.getKey(), fsParanoid.getConfig());
			byte[] out = coder.decodeBlock(f.getInput(), f.getIv());
			assertTrue(Arrays.equals(out, f.getOutput()));
		}
	}
	
	static FSFixture fsParanoid;

}