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
package az.jefsr.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherAlgorithm;
import az.jefsr.crypto.CipherAlgorithmFactory;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.CoderFactory;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;
import az.jefsr.file.NameDecoder;
import az.jefsr.file.NameDecoderFactory;

public class BlockNameDecoderTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		fsFixtureAes = new FSParanoidAes();
	}

	@Test
	public void testDecodeCorrectPath() throws CipherConfigException,
			CipherDataException {
		Config config = fsFixtureAes.getConfig();
		String algName = config.getCipherAlg().getName();
		String nameAlg = config.getNameAlg().getName();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
				.createInstance(algName);
		Coder coder = CoderFactory.getInstance().createInstance(
				fsFixtureAes.getVolumeKey(), cipher, config);
		NameDecoder nameDec = NameDecoderFactory.getInstance().createInstance(
				nameAlg, coder, config);

		String in1 = "7yp5rUju7WJz0HqxocaWPm9K";
		String out1 = "readme.txt";
		assertThat(nameDec.decodePath(in1), equalTo(out1));

		String in2 = "YKGAxrfWfbxeek7,t-L35lZN" + File.separator
				+ "TTUy3UG3HqBkN5cOTP,s0PLf" + File.separator
				+ "PBU6Fr0tBITS53aZLnmNQJL7";
		String out2 = "1" + File.separator + "2b" + File.separator + "3.txt";
		assertThat(nameDec.decodePath(in2), equalTo(out2));
	}

	@Test(expected = CipherDataException.class)
	public void testDecodeWrongPath() throws CipherDataException,
			CipherConfigException {
		Config config = fsFixtureAes.getConfig();
		String algName = config.getCipherAlg().getName();
		String nameAlg = config.getNameAlg().getName();
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
				.createInstance(algName);

		Coder coder = CoderFactory.getInstance().createInstance(
				fsFixtureAes.getVolumeKey(), cipher, config);
		NameDecoder nameDec = NameDecoderFactory.getInstance().createInstance(
				nameAlg, coder, config);

		nameDec.decodePath("dummy");
	}

	static FSFixture fsFixtureAes;
}
