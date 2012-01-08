package az.jefsr.config;

public class UnsupportedVersionConfigReader extends ConfigReader {

	@Override
	protected Config parseData(String configText) throws UnsupportedFormatException {
		throw new UnsupportedFormatException(configText);
	}

}
