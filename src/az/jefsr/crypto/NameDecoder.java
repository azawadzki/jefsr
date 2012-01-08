package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;


public abstract class NameDecoder {
	
	public NameDecoder(Coder coder, Config config) {
		this.coder = coder;
		this.config = config;
	}
	
	public abstract String decodePath(String path) throws CipherDataException;
	
	public Coder getCoder() {
		return coder;
	}

	public Config getConfig() {
		return config;
	}
	
	public static class Factory extends FactoryBase<NameDecoder> {
		
		static Factory instance = new Factory();
		static {
			NameDecoder.Factory.getInstance().registerType("nameio/block", BlockNameDecoder.class);
			NameDecoder.Factory.getInstance().registerType("nameio/null", NullNameDecoder.class);
		}	
		public static Factory getInstance() {
			return instance;
		}
		
		public NameDecoder createInstance(String nameAlg, Coder coder, Config config) {
			NameDecoder ret = null;
			Class<? extends NameDecoder> cls = fetchType(nameAlg);
			try {
				Class<?>[] ctrArgs = { Coder.class, Config.class };
				ret = cls.getConstructor(ctrArgs).newInstance(coder, config);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			return ret;	
		}
		
	}
	
	private Coder coder;
	private Config config;
}
