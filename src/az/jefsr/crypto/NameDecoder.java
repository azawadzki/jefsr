package az.jefsr.crypto;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import az.jefsr.config.Config;
import az.jefsr.util.FactoryBase;


public abstract class NameDecoder {
	
	public NameDecoder(Coder coder, Config config) {
		this.coder = coder;
		this.config = config;
	}
	
	public String decodePath(String path) throws CipherDataException {
		ChainedIv iv = null;
		if (getConfig().getChainedNameIV()) {
			iv = new ChainedIv();
		}
		String output = "";
		for (String el: getPathElements(path)) {
			if (!output.isEmpty()) {
				output += File.separator;
			}
			output += decodePathElements(el, iv);
		}
		return output;
	}
		
	public Coder getCoder() {
		return coder;
	}

	public Config getConfig() {
		return config;
	}
	
	protected abstract String decodePathElements(String path, ChainedIv iv) throws CipherDataException;

	private List<String> getPathElements(String path) {
		List<String> elems = new ArrayList<String>();
		try {
			String normalized = new URI(path).normalize().getPath();
			elems.addAll(Arrays.asList(normalized.split(File.separator)));
			for (Iterator<String> it = elems.iterator(); it.hasNext(); ) {
			    if (it.next().isEmpty()) {
			        it.remove();
			    }
			}
		} catch (URISyntaxException e) {
			// returning empty list is enough for error handling 
		}
		return elems;
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
