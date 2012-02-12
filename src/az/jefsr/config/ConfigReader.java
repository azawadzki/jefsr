/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.jefsr.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

import az.jefsr.util.FactoryBase;

public abstract class ConfigReader {
	
	abstract protected Config parseData(String configText) throws UnsupportedFormatException;
	
	public Config parse(String configFilePath) throws FileNotFoundException, UnsupportedFormatException {
		return parse(new File(configFilePath));
	}
	
	public Config parse(File configFile) throws FileNotFoundException, UnsupportedFormatException {
		return parse(new BufferedInputStream(new FileInputStream(configFile)));
	}
	
	public Config parse(InputStream in) throws UnsupportedFormatException {
		// TODO: handle cases other than utf8
		String data = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
		return parseData(data);
	}
	
	public static class Factory extends FactoryBase<ConfigReader> {
		
		static Factory instance = new Factory();
		static {
			ConfigReader.Factory.getInstance().registerType(".encfs6.xml", V6ConfigReader.class);
		}	
		public static Factory getInstance() {
			return instance;
		}
		
		public Set<String> getSupportedConfigs() {
			return Collections.unmodifiableSet(getRegisteredTypesMap().keySet());
		}
		
		public ConfigReader createInstance(String configFile) {
			String token = new File(configFile).getName();
			Class<? extends ConfigReader> cls = fetchType(token);
			if (cls == null) {
				cls = UnsupportedVersionConfigReader.class;
			}
			try {
				return cls.newInstance();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
}
