package az.jefsr.file;

import java.io.IOException;
import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;

public class FileDecoderInputStream extends InputStream {

	public FileDecoderInputStream(FileDecoder fileDecoder, Config config) {
		mFileDecoder = fileDecoder;
		mBuffer = new byte[config.getBlockSize()];
		mAvailable = 0;
		mPosition = 0;
	}
	
	@Override 
	public int read() throws IOException {
		if (mAvailable == 0 || mAvailable == mPosition) {
			int read;
			try {
				read = mFileDecoder.read(mBuffer);
				if (read == -1) {
					return -1;
				}
			} catch (CipherDataException e) {
				throw new IOException(e.toString());
			}
			mAvailable = read;
			mPosition = 0;
		}
		return 0xff & mBuffer[mPosition++];
	}
	
	private FileDecoder mFileDecoder;
	private byte[] mBuffer;
	private int mAvailable;
	private int mPosition;
}
