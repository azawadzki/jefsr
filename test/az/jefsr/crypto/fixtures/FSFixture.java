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
package az.jefsr.crypto.fixtures;

import java.io.File;
import java.util.List;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;
import az.jefsr.util.Arrays;

public abstract class FSFixture {
	public abstract String getUserPassword();

	public abstract byte[] getUserKeyBytes();

	public abstract byte[] getVolumeKeyBytes();

	public abstract int getKeySize();

	public abstract String getConfigFilePath();

	public abstract List<CoderFixture> getStreamTestVectors()
			throws CipherConfigException;

	public abstract List<CoderFixture> getBlockTestVectors()
			throws CipherConfigException;

	protected FSFixture() {
		String configPath = new File("test_data", getConfigFilePath())
				.getPath();
		try {
			config = ConfigReader.Factory.getInstance()
					.createInstance(configPath).parse(configPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Config getConfig() {
		return config;
	}

	public Key getVolumeKey() throws CipherConfigException {
		return createKeyFromRawBytes(getVolumeKeyBytes());
	}

	public Key getUserKey() throws CipherConfigException {
		return createKeyFromRawBytes(getUserKeyBytes());
	}

	private Key createKeyFromRawBytes(byte[] buf) throws CipherConfigException {
		byte[] keyBytes = Arrays.copyOf(buf, getKeySize() / 8);
		byte[] ivBytes = Arrays.copyOfRange(buf, getKeySize() / 8, buf.length);
		return new Key(keyBytes, ivBytes);
	}

	Config config;
}
