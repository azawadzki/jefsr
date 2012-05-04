/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.jefsr.crypto;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;
import az.jefsr.util.Arrays;

public class KeyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fsParanoidAes = new FSParanoidAes();
	}

	@Test
	public void testEqualsObject() throws CipherConfigException {
		Key k1 = fsParanoidAes.getUserKey();
		Key k2 = fsParanoidAes.getVolumeKey();
		assertFalse(Arrays.equals(k1.getBytes(), k2.getBytes())
				&& Arrays.equals(k1.getIv(), k2.getIv()));
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
		assertFalse(Arrays.equals(k1.getBytes(), k2.getBytes())
				&& Arrays.equals(k1.getIv(), k2.getIv()));
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
