package az.jefsr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.Key;
import az.jefsr.crypto.KeyCreator;
import az.jefsr.crypto.NameDecoder;

public class Volume {
	
	public Volume(String configFilePath) throws FileNotFoundException, ConfigReader.UnsupportedFormat {
		config = ConfigReader.Factory.getInstance().createInstance(configFilePath).parse(configFilePath);
		rootFolder = new File(configFilePath).getParent();
	}
	
	public Volume(String configTag, InputStream in) throws ConfigReader.UnsupportedFormat {
		config = ConfigReader.Factory.getInstance().createInstance(configTag).parse(in);
	}
	
	public boolean init(String userPassword) {
		try {
			String cipherAlg = config.getCipherAlg().getName();
			KeyCreator keyCreator = KeyCreator.Factory.getInstance().createInstance(cipherAlg);
			Key userKey = keyCreator.createUserKey(userPassword, config);
			Coder volumePswdCoder = Coder.Factory.getInstance().createInstance(cipherAlg, userKey, config);
			Key volumeKey = keyCreator.createVolumeKey(volumePswdCoder, config);
			cryptoCoder = Coder.Factory.getInstance().createInstance(cipherAlg, volumeKey, config);
			String nameAlg = config.getNameAlg().getName();
			nameDecoder = NameDecoder.Factory.getInstance().createInstance(nameAlg, cryptoCoder, config);
			// nameDecoder = new NullNameDecoder(cryptoCoder, config);

			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e);
			//return false;
		}
	}
		
	private String getRootFolderRelativePath(String cipheredPath) {
		File f = new File(cipheredPath);
		if (f.isAbsolute()) {
			return cipheredPath.replaceFirst(String.format("^%s", rootFolder), "");
		} else {
			return cipheredPath;
		}
	}
	 
	String decryptPath(String path) {
		String cipheredRelPath = getRootFolderRelativePath(path);
		return nameDecoder.decodePath(cipheredRelPath);
	}
	
	private Config config;
	private String rootFolder = "";
	private Coder cryptoCoder;
	private NameDecoder nameDecoder;
}
