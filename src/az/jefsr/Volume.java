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

/**
 * Volume objects store information about EncFS filesystem and give access to decrypted file names and contents.
 * This class is the only type which is mandatory for the user to handle in order to access the data. Note, that since
 * Volume initialization is a lengthy process (it requires generating volume key, possibly in many hundreds of
 * key derivation function iterations) it is split into two separate steps:
 * <ol>
 * <li>Volume instance creation.</li>
 * <li>init() method call.</li>
 * </ol>
 * Having created Volume instance you are able to access volume config information. In order to access decrypted data you must call init().
 * 
 * All API calls in this library are synchronous, so take care not to block your application when e.g. deciphering
 * large files. It is assumed that the caller should do all jefsr calls in a worker thread, outside of main application
 * loop.
 */
public class Volume {
	
	/** Creates new Volume object using given configuration file.
	 * @param configFilePath Path to V6 configuration file.
	 * @throws FileNotFoundException If configFilePath is not correct.
	 * @throws UnsupportedFormatException If using config format other than V6 or if the config data is corrupted.
	 */
	public Volume(String configFilePath) throws FileNotFoundException, UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configFilePath).parse(configFilePath);
	}
	
	/**	Creates new Volume object using input stream which provides configuration file contents.
	 * This function can be used to create Volume using remote config file.
	 * @param configTag Filename of the original config. (i.e. in case of V6 this should be .encfs6.xml.) 
	 * @param in Stream providing config data.
	 * @throws UnsupportedFormatException If using config format other than V6 or if the config data is corrupted.
	 */
	public Volume(String configTag, InputStream in) throws UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configTag).parse(in);
	}
	
	/** Perform Volume initialization after which the instance can be used to access encrypted data.
	 * @param userPassword User password (as normally provided to encfs when mounting) used to derive master volume key.
	 * @throws CipherConfigException If using unsupported EncFS config.
	 */
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

	/** Decrypt given full path of a file (relative to encrypted FS root dir). Requires preceding init() invocation. 
	 * @param path Encrypted path (both '/' and '\' are supported as path elements separators).
	 * @return Decrypted path
	 * @throws CipherDataException If an error occurred during deciphering.
	 */
	public String decryptPath(String path) throws CipherDataException {
		return nameDecoder.decodePath(path);
	}
	
	/** Decrypt given file and write its contents to given output stream. Requires preceding init() invocation. 
	 * Note that both input stream providing file data and file path (relative to encrypted FS root dir) are necessary.
	 * @param path Encrypted path (both '/' and '\' are supported as path elements separators).
	 * @throws CipherDataException If an error occurred during deciphering.
	 * @throws IOException If there were problems with I/O using the streams provided.
	 */
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
	
	/** Provide config information of the file system. This function does not require that init() is called beforehand.
	 * @return Config object holding FS config.
	 */
	public Config getConfig() {
		return config;
	}
	
	private Config config;
	private Coder cryptoCoder;
	private NameDecoder nameDecoder;
}
