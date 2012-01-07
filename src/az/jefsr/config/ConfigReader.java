package az.jefsr.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import az.jefsr.util.FactoryBase;

public abstract class ConfigReader {
	
	abstract protected Config parseData(String configText);
	
	public Config parse(String configFilePath) throws FileNotFoundException {
		return parse(new File(configFilePath));
	}
	
	public Config parse(File configFile) throws FileNotFoundException {
		return parse(new BufferedInputStream(new FileInputStream(configFile)));
	}
	
	public Config parse(InputStream in) {
		// TODO: handle cases other than utf8
		String data = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
		return parseData(data);
	}
	
	public static class UnsupportedFormat extends Exception {

		private static final long serialVersionUID = 6671639943669176097L;

		private UnsupportedFormat(String configFile) {
			super("Couldn't find config reader for " + configFile);
		}
	}
	
	public static class Factory extends FactoryBase<ConfigReader> {
		
		static Factory instance = new Factory();
		static {
			ConfigReader.Factory.getInstance().registerType(".encfs6.xml", V6ConfigReader.class);
		}	
		public static Factory getInstance() {
			return instance;
		}
		
		public ConfigReader createInstance(String configFile) throws UnsupportedFormat {
			String token = new File(configFile).getName();
			Class<? extends ConfigReader> cls = fetchType(token);
			if (cls == null) {
				throw new UnsupportedFormat(configFile);
			}
			try {
				return cls.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
}
