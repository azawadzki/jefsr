package az.jefsr.file;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.MacUtils;

class MacFileDecoder extends BlockFileDecoder {

	public MacFileDecoder(FileDecoder in, Coder coder, Config config) {
		super(in, config.getBlockSize(), config.getAllowHoles());
		this.coder = coder;
		macBytes = config.getBlockMACBytes();
		macRandBytes = config.getBlockMACRandBytes();
		headerSize = macBytes + macRandBytes;
	}

	@Override
	protected byte[] decodeBlock(long blockNum, byte[] in, int inputLen)
			throws CipherDataException {
		if (inputLen <= headerSize) {
			throw new CipherDataException("No data in block found when performing MAC validation.");
		}
		if (!isBlockAHole(in, inputLen)) {
			long mac = ByteBuffer.wrap(in, 0, macBytes).order(ByteOrder.LITTLE_ENDIAN).getLong();
			long checksum = MacUtils.mac64(Arrays.copyOfRange(in, macBytes, inputLen), coder.getKey(), null);
			if (mac != checksum) {
				throw new CipherDataException("MAC validation failed");
			}
		}
		return Arrays.copyOfRange(in, headerSize, inputLen);
	}


	private Coder coder;
	private int macBytes;
	private int macRandBytes;
	private int headerSize;
}
