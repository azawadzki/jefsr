package az.jefsr.crypto;

import az.jefsr.util.FactoryBase;

public class CipherAlgorithmFactory extends FactoryBase<CipherAlgorithm> {

	static CipherAlgorithmFactory instance = new CipherAlgorithmFactory();
	static {
		CipherAlgorithmFactory.getInstance().registerType("ssl/aes", AesCipher.class);
		CipherAlgorithmFactory.getInstance().registerType("ssl/blowfish", BlowfishCipher.class);
	}	
	public static CipherAlgorithmFactory getInstance() {
		return instance;
	}
	
	public CipherAlgorithm createInstance(String nameAlg) {
		CipherAlgorithm ret = null;
		Class<? extends CipherAlgorithm> cls = fetchType(nameAlg);
		try {
			ret = cls.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return ret;	
	}
}

