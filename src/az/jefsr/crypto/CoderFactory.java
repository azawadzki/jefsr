package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public class CoderFactory extends FactoryBase<Coder> {

	static CoderFactory instance = new CoderFactory();
	static {
		CoderFactory.getInstance().registerType(AesCipher.NAME, BaseCoder.class);
		CoderFactory.getInstance().registerType(BlowfishCipher.NAME, BaseCoder.class);
		CoderFactory.getInstance().registerType(NullCipher.NAME, NullCoder.class);
	}	
	public static CoderFactory getInstance() {
		return instance;
	}
	
	public Coder createInstance(Key k, CipherAlgorithm cipher, Config config) {
		Coder ret = null;
		Class<? extends Coder> cls = fetchType(cipher.getName());
		try {
			Class<?>[] ctrArgs = { Key.class, CipherAlgorithm.class, Config.class };
			ret = cls.getConstructor(ctrArgs).newInstance(k, cipher, config);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return ret;	
	}
}
