package az.jefsr.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import net.iharder.base64.Base64;

import org.junit.BeforeClass;
import org.junit.Test;

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
	
	@Test(expected=CipherConfigException.class)
	public void testChecksumVerification() throws CipherConfigException, IOException {
		Config config = new FSParanoid().getConfig();
		byte[] buf = Base64.decode(config.getEncodedKeyData());
		buf[0] += 0xbad;
		config.setEncodedKeyData(new String(Base64.encodeBytes(buf)));
		Coder coder = Coder.Factory.getInstance().createInstance(config.getCipherAlg().getName(), fsParanoid.getUserKey(), config);
		keyCreator.createVolumeKey(coder, config);
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
