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

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.Config;
import az.jefsr.crypto.fixtures.CoderFixture;
import az.jefsr.crypto.fixtures.FSFixture;
import az.jefsr.crypto.fixtures.FSParanoidAes;
import az.jefsr.crypto.fixtures.FSParanoidBlowfish;
import az.jefsr.crypto.fixtures.FSStandard;

public class CodersTests {

	@BeforeClass
	public static void setUpClass() throws Exception {
		fsFixtures = new ArrayList<FSFixture>();
		fsFixtures.add(new FSStandard());
		fsFixtures.add(new FSParanoidAes());
		fsFixtures.add(new FSParanoidBlowfish());
	}

	@Test
	public void testDecodeStream() throws CipherConfigException,
			CipherDataException {
		for (FSFixture fix : fsFixtures) {
			performDecodeStreamTests(fix);
		}
	}

	private void performDecodeStreamTests(FSFixture fsFixture)
			throws CipherConfigException, CipherDataException {
		for (CoderFixture f : fsFixture.getStreamTestVectors()) {
			Config config = fsFixture.getConfig();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
					.createInstance(config.getCipherAlg().getName());
			Coder coder = CoderFactory.getInstance().createInstance(f.getKey(),
					cipher, config);
			byte[] out = coder.decodeStream(f.getInput(), f.getIv());
			assertThat(out, equalTo(f.getOutput()));
		}
	}

	@Test
	public void testDecodeBlock() throws CipherConfigException,
			CipherDataException {
		for (FSFixture fix : fsFixtures) {
			performDecodeBlockTests(fix);
		}
	}

	private void performDecodeBlockTests(FSFixture fsFixture)
			throws CipherConfigException, CipherDataException {
		for (CoderFixture f : fsFixture.getBlockTestVectors()) {
			Config config = fsFixture.getConfig();
			CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance()
					.createInstance(config.getCipherAlg().getName());
			Coder coder = CoderFactory.getInstance().createInstance(f.getKey(),
					cipher, config);
			byte[] out = coder.decodeBlock(f.getInput(), f.getIv());
			assertThat(out, equalTo(f.getOutput()));
		}
	}

	static List<FSFixture> fsFixtures;

}
