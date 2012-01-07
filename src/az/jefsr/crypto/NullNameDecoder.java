package az.jefsr.crypto;

import az.jefsr.config.Config;

public class NullNameDecoder extends NameDecoder {

	public NullNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}

	@Override
	public String decodePath(String path) {
		return path;
	}

}
