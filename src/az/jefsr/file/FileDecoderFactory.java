package az.jefsr.file;

import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;

public class FileDecoderFactory {
	
	private static FileDecoderFactory instance = new FileDecoderFactory();
	
	public static FileDecoderFactory getInstance() {
		return instance;
	}
	
	public FileDecoder createInstance(InputStream in, String path, Coder coder, Config config) throws CipherDataException {
		NameDecoder nameDecoder = NameDecoderFactory.getInstance().createInstance(config.getNameAlg().getName(), coder, config);
		PathInfo pathInfo = nameDecoder.decodePathInfo(path);
		return createInstance(in, pathInfo, coder, config);
	}
	
	public FileDecoder createInstance(InputStream in, PathInfo pathInfo, Coder coder, Config config) {
		FileDecoder decoder = new CipherFileDecoder(new NullFileDecoder(in), pathInfo, coder, config);
		if (config.getBlockMACBytes() > 0 || config.getBlockMACRandBytes() > 0) {
			decoder = new MacFileDecoder(decoder, coder, config);
		}
		return decoder;
	}

}
