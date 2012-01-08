package az.jefsr.crypto;

import java.io.IOException;
import java.util.Arrays;

import net.iharder.base64.Base64;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoid;

public class AesKeyCreatorTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		keyCreator = new AesCoder.AesKeyCreator();
		fsParanoid = new FSParanoid();
	}

	@Test
	public void testCreateUserKey() throws CipherConfigException {
		Key referenceKey = fsParanoid.getUserKey();
		Key k = keyCreator.createUserKey(fsParanoid.getUserPassword(), fsParanoid.getConfig());
		assertThat(k, equalTo(referenceKey));
	}

	@Test
	public void testCreateVolumeKey() throws CipherConfigException, IOException {
		Config config = fsParanoid.getConfig();
		byte[] b64Decoded = Base64.decode(config.getEncodedKeyData());
		byte[] decodedWoutChecksum = Arrays.copyOfRange(b64Decoded, AesCoder.KEY_CHECKSUM_BYTES, b64Decoded.length);
		Coder nullCoder = new NullCoder(null, null);
		Key k = keyCreator.createVolumeKey(nullCoder, config);
		int keyBytes = config.getKeySize() / 8;
		Key nullCoderKey = new Key(Arrays.copyOf(decodedWoutChecksum, keyBytes), Arrays.copyOfRange(decodedWoutChecksum, keyBytes, keyBytes + AesCoder.MAX_IVEC_BYTES));
		assertThat(k, equalTo(nullCoderKey));
	}
	
	@Test
	public void testChecksumDeserialization() throws CipherConfigException {
		byte[] buf = new byte[100];
		buf[0] = (byte) 0xa1;
		buf[1] = (byte) 0xb2;
		buf[2] = (byte) 0xc3;
		buf[3] = (byte) 0xd4;
		String encoded = new String(Base64.encodeBytes(buf));
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
	public void testCreateVolumeKeyCoderIntegration() throws CipherConfigException {
		Config config = fsParanoid.getConfig();
		Coder coder = Coder.Factory.getInstance().createInstance(config.getCipherAlg().getName(), fsParanoid.getUserKey(), config);
		Key k = keyCreator.createVolumeKey(coder, config);
		Key referenceKey = fsParanoid.getVolumeKey();
		assertThat(k, equalTo(referenceKey));
	}
	
	static KeyCreator keyCreator;
	static FSFixture fsParanoid;

}
