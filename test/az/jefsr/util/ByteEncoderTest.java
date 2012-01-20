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
package az.jefsr.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ByteEncoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChangeBase2() {
		byte[] in = { 9, 62, 53, 7, 55, 32, 47, 58, 9, 34, 21, 63, 2, 19, 54,
				61, 52, 40, 38, 34, 27, 50, 11, 22 };
		byte[] out = { -119, 95, 31, 55, -8, -22, -119, 88, -3, -62, 100, -9,
				52, 106, -118, -101, -68, 88 };
		assertThat(ByteEncoder.changeBase2(in, 6, 8, false), equalTo(out));
	}

	@Test
	public void testAsciiToB64() {
		byte[] in = { 55, 121, 112, 53, 114, 85, 106, 117, 55, 87, 74, 122, 48,
				72, 113, 120, 111, 99, 97, 87, 80, 109, 57, 75 };
		byte[] out = { 9, 62, 53, 7, 55, 32, 47, 58, 9, 34, 21, 63, 2, 19, 54,
				61, 52, 40, 38, 34, 27, 50, 11, 22 };
		assertThat(ByteEncoder.asciiToB64(in), equalTo(out));

	}

	@Test
	public void testB64ToB256Bytes() {
		assertThat(ByteEncoder.b64ToB256Bytes(24), equalTo(18));
	}

}
