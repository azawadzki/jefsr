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
