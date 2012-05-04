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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;
import az.jefsr.crypto.MacUtils;
import az.jefsr.util.Arrays;

class MacFileDecoder extends BlockFileDecoder {

	public MacFileDecoder(FileDecoder in, Coder coder, Config config) {
		super(in, config.getBlockSize(), config.getAllowHoles());
		this.coder = coder;
		macBytes = config.getBlockMACBytes();
		macRandBytes = config.getBlockMACRandBytes();
		headerSize = macBytes + macRandBytes;
	}

	@Override
	protected int decodeBlock(long blockNum, byte[] in, int inputLen, byte[] out)
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
		int outputLen = inputLen - headerSize;
		System.arraycopy(in, headerSize, out, 0, outputLen);
		return outputLen;
	}


	private Coder coder;
	private int macBytes;
	private int macRandBytes;
	private int headerSize;
}
