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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

class V6ConfigReader extends ConfigReader {

	public Config parseData(String configText) {
		XStream xstream = createParser();
		return (Config) xstream.fromXML(configText);
	}
	
	private static XStream createParser() {
		XStream xstream = new XStream(new DomDriver());
		xstream.registerConverter(new BoostSerializationTag.Converter());
		xstream.alias("boost_serialization", BoostSerializationTag.class);
		xstream.alias("cfg", Config.class);
		xstream.alias("cipherAlg", Config.CipherDescription.class);
		xstream.alias("nameAlg", Config.CipherDescription.class);
		
		return xstream;
	}
	
}

class BoostSerializationTag {
	
	public static class Converter implements com.thoughtworks.xstream.converters.Converter {
		
		@Override
		public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
			return type == BoostSerializationTag.class;
		}
		@Override
		public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		}
		@Override
		public Object unmarshal(HierarchicalStreamReader reader,
				UnmarshallingContext context) {
			reader.moveDown();
			return context.convertAnother(reader, Config.class);
		}
		
	}
	
}
