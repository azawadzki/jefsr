package az.jefsr.crypto;

import az.jefsr.config.Config;

public class NullNameDecoder extends NameDecoder {

	public NullNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}

	@Override
	protected String decodePathComponent(String path, ChainedIV iv)
			throws CipherDataException {
		return path;
	}

}
