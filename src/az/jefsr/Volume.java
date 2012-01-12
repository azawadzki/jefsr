package az.jefsr;

import java.io.FileNotFoundException;
import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.config.UnsupportedFormatException;
import az.jefsr.crypto.CipherAlgorithm;
import az.jefsr.crypto.CipherAlgorithmFactory;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.CoderFactory;
import az.jefsr.crypto.NameDecoderFactory;
import az.jefsr.crypto.KeyCreator;
import az.jefsr.crypto.Key;
import az.jefsr.crypto.NameDecoder;

public class Volume {
	
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
	
	public Config getConfig() {
		return config;
	}
	
	private Config config;
	private Coder cryptoCoder;
	private NameDecoder nameDecoder;
}
