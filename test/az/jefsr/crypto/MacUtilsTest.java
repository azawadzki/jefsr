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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;

public class MacUtilsTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		fsParanoidAes = new FSParanoidAes();
	}

	@Test
	public void testMac64() throws CipherConfigException {
		Key k1 = fsParanoidAes.getVolumeKey();
		byte[] input1 = { 114, 101, 97, 100, 109, 101, 46, 116, 120, 116, 6, 6,
				6, 6, 6, 6 };
		long output1 = 444659516657715308L;
		ChainedIv iv = new ChainedIv();
		assertThat(MacUtils.mac64(input1, k1, iv), equalTo(output1));
		assertThat(iv.value, equalTo(444659516657715308L));

		Key k2 = fsParanoidAes.getUserKey();
		byte[] input2 = { 50, -83, 119, -8, 117, 48, -83, 9, -124, -48, 111,
				124, -102, 78, -7, 121, -73, 1, -79, 76, -51, -61, -92, -48,
				74, 24, -12, -125, -39, 32, 87, 18, -52, 25, 96, -52, 75, -35,
				-36, -95, -108, 121, -51, 1, 43, 20, -77, -27, };
		long output2 = -6238161955975967516L;
		assertThat(MacUtils.mac64(input2, k2, null), equalTo(output2));
	}

	@Test
	public void testMac32() throws CipherConfigException {
		Key k = fsParanoidAes.getUserKey();
		byte[] input = { 50, -83, 119, -8, 117, 48, -83, 9, -124, -48, 111,
				124, -102, 78, -7, 121, -73, 1, -79, 76, -51, -61, -92, -48,
				74, 24, -12, -125, -39, 32, 87, 18, -52, 25, 96, -52, 75, -35,
				-36, -95, -108, 121, -51, 1, 43, 20, -77, -27, };
		int output = 1004524580;
		assertThat(MacUtils.mac32(input, k, null), equalTo(output));
	}

	@Test
	public void testMac16() throws CipherConfigException {
		Key k = fsParanoidAes.getVolumeKey();
		byte[] input = { 114, 101, 97, 100, 109, 101, 46, 116, 120, 116, 6, 6,
				6, 6, 6, 6 };
		int output = 35167;
		ChainedIv iv = new ChainedIv();
		assertThat(MacUtils.mac16(input, k, iv), equalTo(output));
		assertThat(iv.value, equalTo(444659516657715308L));
	}

	static FSFixture fsParanoidAes;
}
