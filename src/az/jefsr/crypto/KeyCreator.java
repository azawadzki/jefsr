package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public abstract class KeyCreator {
	
	public abstract Key createUserKey(String password, Config config) throws CipherConfigException;
	public abstract Key createVolumeKey(Coder passwordCoder, Config config) throws CipherConfigException;
	
	public static class Factory extends FactoryBase<KeyCreator> {
		
		static Factory instance = new Factory();
		static {
			KeyCreator.Factory.getInstance().registerType("ssl/aes", AesCoder.AesKeyCreator.class);
			KeyCreator.Factory.getInstance().registerType("nullCipher", NullCoder.NullKeyCreator.class);
		}	
		public static Factory getInstance() {
			return instance;
		}
		
		public KeyCreator createInstance(String cipherAlg) {
			KeyCreator ret = null;;
			Class<? extends KeyCreator> cls = fetchType(cipherAlg);
			if (cls != null) {
				try {
					ret = cls.newInstance();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
			return ret; 
		}
		
	}
}
