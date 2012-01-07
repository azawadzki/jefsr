package az.jefsr.crypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public abstract class Coder {

	public abstract byte[] decodeStream(byte[] stream, long iv) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException;
	public abstract byte[] decodeBlock(byte[] stream, long iv) throws InvalidKeyException, IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException;
	public abstract KeyCreator getKeyCreationStrategy();

	public Coder(Key key, Config config) {
		this.key = key;
		this.config = config;
	}

	public Key getKey() {
		return key;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public static class Factory extends FactoryBase<Coder> {
		
		static Factory instance = new Factory();
		static {
			Coder.Factory.getInstance().registerType("ssl/aes", AesCoder.class);
		}	
		public static Factory getInstance() {
			return instance;
		}
		
		public Coder createInstance(String cipherAlg, Key key, Config config) {
			Class<? extends Coder> cls = fetchType(cipherAlg);
			Class<?>[] ctrArgs = { Key.class, Config.class };
			try {
				return cls.getConstructor(ctrArgs).newInstance(key, config);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}

	private Key key;
	private Config config;
}
