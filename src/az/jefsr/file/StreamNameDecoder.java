package az.jefsr.file;

import java.nio.ByteBuffer;
import java.util.Arrays;

import az.jefsr.config.Config;
import az.jefsr.crypto.ChainedIv;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.MacUtils;
import az.jefsr.util.ByteEncoder;

public class StreamNameDecoder extends NameDecoder {

	final private static int MAC_BYTES = 2;
	
	public StreamNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}

	//TODO: support older versions
	@Override
	protected String decodePathElements(String filename, ChainedIv iv) throws CipherDataException {
		byte[] encFilenameData = decodeFilenameData(filename);
		long mac = 0xffff & ByteBuffer.wrap(encFilenameData, 0, MAC_BYTES).asShortBuffer().get();
		byte[] encFilename = Arrays.copyOfRange(encFilenameData, MAC_BYTES, encFilenameData.length);
		long seed = getUpdateIvSeed(iv, mac);
		
		Coder coder = getCoder();
		byte[] deciphered = coder.decodeStream(encFilename, seed);
		int mac2 = MacUtils.mac16(deciphered, coder.getKey(), iv);
		if (mac != mac2) {
			throw new CipherDataException("Checksum error in decoded path element!");
		}
		int finalSize = getDecipheredFilenameSize(filename);
		
		return new String(Arrays.copyOfRange(deciphered, 0, finalSize));
	}
	
	private long getUpdateIvSeed(ChainedIv iv, long mac) {
		long currentIvValue = 0;
		if (getConfig().getChainedNameIV()) {
			currentIvValue = iv.value;
		}
		return currentIvValue ^ mac;
	}
	
	private byte[] decodeFilenameData(String filename) {
		byte[] data = filename.getBytes();
		return ByteEncoder.changeBase2(ByteEncoder.asciiToB64(data), 6, 8, false);
	}

	private int getDecipheredFilenameSize(String filename) {
		return ByteEncoder.b64ToB256Bytes(filename.length()) - MAC_BYTES;
	}
}
