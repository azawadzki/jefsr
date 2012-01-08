package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public abstract class Coder {

	public abstract byte[] decodeStream(byte[] stream, long iv) throws CipherDataException;
	public abstract byte[] decodeBlock(byte[] stream, long iv) throws CipherDataException;
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
			Coder ret = null;
			Class<? extends Coder> cls = fetchType(cipherAlg);
			if (cls != null) {
				try {
					Class<?>[] ctrArgs = { Key.class, Config.class };
					ret = cls.getConstructor(ctrArgs).newInstance(key, config);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
			return ret;
		}
		
	}

	private Key key;
	private Config config;
}
