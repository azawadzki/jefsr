package az.jefsr.file;

import az.jefsr.config.Config;
import az.jefsr.crypto.ChainedIv;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;

class NullNameDecoder extends NameDecoder {

	public NullNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}

	@Override
	protected String decodePathElements(String path, ChainedIv iv)
			throws CipherDataException {
		return path;
	}

}
