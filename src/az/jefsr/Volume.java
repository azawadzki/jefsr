package az.jefsr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.config.UnsupportedFormatException;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.Key;
import az.jefsr.crypto.KeyCreator;
import az.jefsr.crypto.NameDecoder;

public class Volume {
	
	public Volume(String configFilePath) throws FileNotFoundException, UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configFilePath).parse(configFilePath);
		rootFolder = new File(configFilePath).getParent();
	}
	
	public Volume(String configTag, InputStream in) throws UnsupportedFormatException {
		config = ConfigReader.Factory.getInstance().createInstance(configTag).parse(in);
	}
	
	public void init(String userPassword) throws CipherConfigException {
		String cipherAlg = config.getCipherAlg().getName();
		KeyCreator keyCreator = KeyCreator.Factory.getInstance().createInstance(cipherAlg);
		Key userKey = keyCreator.createUserKey(userPassword, config);

		Coder volumePswdCoder = Coder.Factory.getInstance().createInstance(cipherAlg, userKey, config);
		Key volumeKey = keyCreator.createVolumeKey(volumePswdCoder, config);

		cryptoCoder = Coder.Factory.getInstance().createInstance(cipherAlg, volumeKey, config);
		String nameAlg = config.getNameAlg().getName();
		nameDecoder = NameDecoder.Factory.getInstance().createInstance(nameAlg, cryptoCoder, config);
	}
		
	private String getRootFolderRelativePath(String cipheredPath) {
		File f = new File(cipheredPath);
		if (f.isAbsolute()) {
			return cipheredPath.replaceFirst(String.format("^%s", rootFolder), "");
		} else {
			return cipheredPath;
		}
	}
	 
	String decryptPath(String path) throws CipherDataException {
		String cipheredRelPath = getRootFolderRelativePath(path);
		return nameDecoder.decodePath(cipheredRelPath);
	}
	
	private Config config;
	private String rootFolder = "";
	private Coder cryptoCoder;
	private NameDecoder nameDecoder;
}
