package az.jefsr.file;

import java.io.IOException;

import az.jefsr.crypto.CipherDataException;

abstract class BlockFileDecoder implements FileDecoder {

	BlockFileDecoder(FileDecoder in, int blockSize) {
		this.in = in;
		this.blockSize = blockSize;
		cacheBuffer = new byte[blockSize];
		inputBuffer = new byte[blockSize];
		blocksRead = 0;
		reachedInputEnd = false;
	}
	
	@Override
	public int read(byte[] buf) throws IOException, CipherDataException {
		if (reachedInputEnd && bytesCached == 0) {
			return -1;
		}
		int flushed = flushCached(buf);
		int dstPos = flushed;

		int remainingBytes = buf.length - flushed;
		int requestedBlocks = (int) Math.ceil(1.0 * remainingBytes / blockSize);
		for (int i = 0; remainingBytes > 0 && i < requestedBlocks; ++i) {
			int read = in.read(inputBuffer);
			if (read != inputBuffer.length) {
				reachedInputEnd = true;
				if (read == -1) {
					return dstPos != 0 ? dstPos : -1;
				}
			}
			byte[] decoded = decodeBlock(blocksRead, inputBuffer, read);
			int toWrite = Math.min(remainingBytes, decoded.length);
			System.arraycopy(decoded, 0, buf, dstPos, toWrite);
			remainingBytes -= toWrite;
			dstPos += toWrite;
			++blocksRead;
			if (toWrite != decoded.length) {
				cacheLeftovers(decoded, toWrite, decoded.length - toWrite);
				break;
			} 
		}
		return dstPos;
	}
	
	protected abstract byte[] decodeBlock(long blockNum, byte[] in, int inputLen) throws CipherDataException;
	
	private int flushCached(byte[] buf) {
		// flush as much as will fit into output buffer
		final int toFlush = Math.min(bytesCached, buf.length);
		int bytesLeft = toFlush;
		// firstly, flush from current pos till the end of cache buffer
		int bytesFlushedFirstly = Math.min(toFlush, cacheBuffer.length - cachePos);
		System.arraycopy(cacheBuffer, cachePos, buf, 0, bytesFlushedFirstly);
		// flash any wrapping contents from cache buffer;
		bytesLeft -= bytesFlushedFirstly;
		System.arraycopy(cacheBuffer, 0, buf, bytesFlushedFirstly, bytesLeft);
		bytesCached -= toFlush;
		cachePos = bytesCached == 0 ? 0 : (cachePos + toFlush) % cacheBuffer.length;
		return toFlush;
	}
	
	private void cacheLeftovers(byte[] buf, int pos, int length) {
		assert(bytesCached == 0);
		assert(length < blockSize);
		assert(cachePos == 0);
		bytesCached = length;
		System.arraycopy(buf, pos, cacheBuffer, 0, length);
	}
	
	protected FileDecoder getInputDecoder() {
		return in;
	}
	private byte[] cacheBuffer;
	private int cachePos;
	private int bytesCached;
	private byte[] inputBuffer;
	private FileDecoder in;
	private int blockSize;
	private long blocksRead;
	private boolean reachedInputEnd;
}
