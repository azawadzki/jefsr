package az.jefsr.crypto;

import az.jefsr.config.Config;

public class NullNameDecoder extends NameDecoder {

	public NullNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}

	@Override
	protected String decodePathElements(String path, ChainedIv iv)
			throws CipherDataException {
		return path;
	}

}
