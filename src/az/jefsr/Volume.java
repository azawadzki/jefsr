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
package az.jefsr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.config.UnsupportedFormatException;
import az.jefsr.crypto.CipherAlgorithm;
import az.jefsr.crypto.CipherAlgorithmFactory;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.CoderFactory;
import az.jefsr.crypto.KeyCreator;
import az.jefsr.crypto.Key;
import az.jefsr.file.FileDecoder;
import az.jefsr.file.FileDecoderFactory;
import az.jefsr.file.NameDecoder;
import az.jefsr.file.NameDecoderFactory;
import az.jefsr.file.PathInfo;

public class Volume {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedFormatException, CipherDataException, CipherConfigException {
		String config = "test_data/config_files/standard/.encfs6.xml";
		String userPassword = "test";

		Volume v = new Volume(config);
		v.init(userPassword);

		String[] files = {
				"emWH3lhzE1a6W0ZwVQw6EbM0",
				"eSP1eCF1HVHgocrYKyit2nsU/S6-Zg,4Nto0febpMZg-OtG4w/WquaQqMP9N5bl-qcwgXz,9rq" 			
		};
		for (String f: files) {
			System.out.printf("%s\n%s\n--\n", f, v.decryptPath(f));
		}

	}
	public Volume(String configFilePath) throws FileNotFoundException, UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configFilePath).parse(configFilePath);
	}
	
	public Volume(String configTag, InputStream in) throws UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configTag).parse(in);
	}
	
	public void init(String userPassword) throws CipherConfigException {
		CipherAlgorithm cipher = CipherAlgorithmFactory.getInstance().createInstance(config.getCipherAlg().getName());
		KeyCreator keyCreator = new KeyCreator(cipher, config);
		Key userKey = keyCreator.createUserKey(userPassword);

		Coder volumePswdCoder = CoderFactory.getInstance().createInstance(userKey, cipher, config);
		Key volumeKey = keyCreator.createVolumeKey(volumePswdCoder);

		cryptoCoder = CoderFactory.getInstance().createInstance(volumeKey, cipher, config);
		String nameAlg = config.getNameAlg().getName();
		nameDecoder = NameDecoderFactory.getInstance().createInstance(nameAlg, cryptoCoder, config);
	}

	public String decryptPath(String path) throws CipherDataException {
		return nameDecoder.decodePath(path);
	}
	
	public void decryptFile(String path, InputStream in, OutputStream out) throws CipherDataException, IOException {
		PathInfo pathInfo = nameDecoder.decodePathInfo(path);
		FileDecoder decoder = FileDecoderFactory.getInstance().createInstance(in, pathInfo, cryptoCoder, config);
		byte[] buf = new byte[config.getBlockSize()];
		while (true) {
			int read = decoder.read(buf);
			if (read == -1) {
				break;
			}
			out.write(buf, 0, read);
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	private Config config;
	private Coder cryptoCoder;
	private NameDecoder nameDecoder;
}
