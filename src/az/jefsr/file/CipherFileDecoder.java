package az.jefsr.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.PathInfo;

class CipherFileDecoder extends BlockFileDecoder {

	final static private int HEADER_LENGTH = 8;
	
	CipherFileDecoder(FileDecoder in, PathInfo pathInfo, Coder coder, Config config) {
		super(in, config.getBlockSize(), config.getAllowHoles());
		this.pathInfo = pathInfo;
		this.coder = coder;
		this.config = config;
		this.containsIvHeader = config.getUniqueIV();
	}
	
	@Override
	public int read(byte[] buf) throws IOException, CipherDataException {
		if (iv == 0 && containsIvHeader) {
			readHeader();
		}
		return super.read(buf);
	}
	
	private void readHeader() throws CipherDataException, IOException {
		byte[] buf = new byte[HEADER_LENGTH];
		if (getInputDecoder().read(buf) != HEADER_LENGTH) {
			throw new CipherDataException("Input stream size too small");
		}
		long externalIv = config.getExternalIVChaining() ? pathInfo.getIv() : 0;
		byte[] header = coder.decodeStream(buf, externalIv);
		iv = ByteBuffer.wrap(header).getLong();
	}
	
	@Override
	protected byte[] decodeBlock(long blockNum, byte[] in, int inputLen) throws CipherDataException {
		if (isBlockAHole(in, inputLen)) {
			return in;
		}
		long blockIv = blockNum ^ iv;
		if (inputLen == getBlockSize()) {
			return coder.decodeBlock(in, blockIv);
		} else {
			return coder.decodeStream(Arrays.copyOf(in, inputLen), blockIv);
		}
	}

	private int getBlockSize() {
		return config.getBlockSize();
	}
	
	private boolean containsIvHeader;
	private long iv = 0;
	private PathInfo pathInfo;
	private Coder coder;
	private Config config;

}
