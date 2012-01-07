package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public abstract class KeyCreator {
	
	public abstract Key createUserKey(String password, Config config);
	public abstract Key createVolumeKey(Coder passwordCoder, Config config);
	
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
			Class<? extends KeyCreator> cls = fetchType(cipherAlg);
			try {
				return cls.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
}
