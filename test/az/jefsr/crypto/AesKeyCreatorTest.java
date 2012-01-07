package az.jefsr.crypto;

import java.util.Arrays;

import org.bouncycastle.util.encoders.Base64;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.Paranoid;

public class AesKeyCreatorTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		keyCreator = new AesCoder.AesKeyCreator();
		fsParanoid = new Paranoid();
	}

	@Test
	public void testCreateUserKey() {
		Key referenceKey = fsParanoid.getUserKey();
		Key k = keyCreator.createUserKey(fsParanoid.getUserPassword(), fsParanoid.getConfig());
		assertThat(k, equalTo(referenceKey));
	}

	@Test
	public void testCreateVolumeKey() {
		Config config = fsParanoid.getConfig();
		byte[] b64Decoded = Base64.decode(config.getEncodedKeyData());
		Coder nullCoder = new NullCoder(null, null);
		Key k = keyCreator.createVolumeKey(nullCoder, config);
		int keyBytes = config.getKeySize() / 8;
		Key nullCoderKey = new Key(Arrays.copyOf(b64Decoded, keyBytes), Arrays.copyOfRange(b64Decoded, keyBytes, keyBytes + AesCoder.MAX_IVEC_BYTES));
		assertThat(k, equalTo(nullCoderKey));
	}
	
	@Test
	public void testChecksumDeserialization() {
		byte[] buf = new byte[100];
		buf[0] = (byte) 0xa1;
		buf[1] = (byte) 0xb2;
		buf[2] = (byte) 0xc3;
		buf[3] = (byte) 0xd4;
		String encoded = new String(Base64.encode(buf));
		Config config = new Config();
		config.setEncodedKeyData(encoded);
		Coder nullCoder = new NullCoder(null, null) {
			public byte[] decodeStream(byte[] stream, long iv) {
				assertThat(iv, equalTo(0xa1b2c3d4L));
				return stream;
			}
		};
		keyCreator.createVolumeKey(nullCoder, config);
	}
		
	@Test
	public void testCreateVolumeKeyCoderIntegration() {
		Config config = fsParanoid.getConfig();
		Coder coder = Coder.Factory.getInstance().createInstance(config.getCipherAlg().getName(), fsParanoid.getUserKey(), config);
		Key k = keyCreator.createVolumeKey(coder, config);
		Key referenceKey = fsParanoid.getVolumeKey();
		assertThat(k, equalTo(referenceKey));
	}
	
	static KeyCreator keyCreator;
	static FSFixture fsParanoid;

}
