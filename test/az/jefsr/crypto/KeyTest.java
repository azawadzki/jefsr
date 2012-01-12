package az.jefsr.crypto;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;

public class KeyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fsParanoidAes = new FSParanoidAes();
	}

	@Test
	public void testEqualsObject() throws CipherConfigException {
		Key k1 = fsParanoidAes.getUserKey();
		Key k2 = fsParanoidAes.getVolumeKey();
		assertFalse(Arrays.equals(k1.getBytes(), k2.getBytes()) && Arrays.equals(k1.getIv(), k2.getIv()));
		assertThat(k1, not(equalTo(k2)));
		assertThat(k2, not(equalTo(k1)));
		assertThat(k1, not(equalTo(null)));
		assertThat(k2, not(equalTo(null)));
		assertThat(k1, equalTo(k1));
		assertThat(k2, equalTo(k2));
	}

	@Test
	public void testHashCode() throws CipherConfigException {
		Key k1 = fsParanoidAes.getUserKey();
		Key k2 = fsParanoidAes.getVolumeKey();
		assertFalse(Arrays.equals(k1.getBytes(), k2.getBytes()) && Arrays.equals(k1.getIv(), k2.getIv()));
		int k1h = k1.hashCode();
		int k2h = k2.hashCode();
		assertThat(k1h, not(equalTo(k2h)));
		assertThat(k2h, not(equalTo(k1h)));
		assertThat(k1h, equalTo(k1h));
		assertThat(k2h, equalTo(k2h));
		assertThat(k1h, equalTo(k1.hashCode()));
		assertThat(k2h, equalTo(k2.hashCode()));
	}
	
	static FSFixture fsParanoidAes;

}
