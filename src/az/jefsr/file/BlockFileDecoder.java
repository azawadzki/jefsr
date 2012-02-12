/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.jefsr.file;

import java.io.IOException;

import az.jefsr.crypto.CipherDataException;

abstract class BlockFileDecoder implements FileDecoder {

	BlockFileDecoder(FileDecoder in, int blockSize, boolean holesAllowed) {
		this.in = in;
		this.blockSize = blockSize;
		cacheBuffer = new byte[blockSize];
		inputBuffer = new byte[blockSize];
		decodedBuffer = new byte[blockSize];
		blocksRead = 0;
		reachedInputEnd = false;
		this.holesAllowed = holesAllowed;
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
			int decodedBytes = decodeBlock(blocksRead, inputBuffer, read, decodedBuffer);
			assert(read >= decodedBytes);
			int toWrite = Math.min(remainingBytes, decodedBytes);
			System.arraycopy(decodedBuffer, 0, buf, dstPos, toWrite);
			remainingBytes -= toWrite;
			dstPos += toWrite;
			++blocksRead;
			if (toWrite != decodedBytes) {
				cacheLeftovers(decodedBuffer, toWrite, decodedBytes - toWrite);
				break;
			} 
		}
		return dstPos;
	}
	
	protected abstract int decodeBlock(long blockNum, byte[] in, int inputLen, byte[] output) throws CipherDataException;
	

	protected boolean isBlockAHole(byte[] in, int inputLen) {
		if (!holesAllowed) {
			return false;
		}
		for (int i = 0; i < inputLen; ++i) {
			if (in[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
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
	private byte[] decodedBuffer;
	private FileDecoder in;
	private int blockSize;
	private long blocksRead;
	private boolean reachedInputEnd;
	private boolean holesAllowed;
}
