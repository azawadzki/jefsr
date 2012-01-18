package az.jefsr.file;

import java.io.IOException;
import java.io.InputStream;

class NullFileDecoder implements FileDecoder {

	NullFileDecoder(InputStream in) {
		this.in = in;
	}
	
	@Override
	public int read(byte[] buf) throws IOException {
		return in.read(buf);
	}
	
	private InputStream in;

}
