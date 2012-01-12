package az.jefsr.crypto;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;

public class NameDecoderFactory extends FactoryBase<NameDecoder> {
	
	static NameDecoderFactory instance = new NameDecoderFactory();
	static {
		NameDecoderFactory.getInstance().registerType("nameio/block", BlockNameDecoder.class);
		NameDecoderFactory.getInstance().registerType("nameio/stream", StreamNameDecoder.class);
		NameDecoderFactory.getInstance().registerType("nameio/null", NullNameDecoder.class);
	}	
	public static NameDecoderFactory getInstance() {
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