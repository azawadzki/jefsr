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
package az.jefsr.crypto;

import az.jefsr.config.Config;

class NullCoder extends Coder {

	public NullCoder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
		super(key, cipher, config);
	}
	
	public byte[] decodeStream(byte[] stream, long iv) throws CipherDataException {
		return stream;
	}

	public byte[] decodeBlock(byte[] block, long iv) throws CipherDataException {
		return block;
	}

	@Override
	public int decodeStream(byte[] stream, int inputLength, long iv, byte[] output)
			throws CipherDataException {
		System.arraycopy(stream, 0, output, 0, inputLength);
		return inputLength;
	}

	@Override
	public int decodeBlock(byte[] block, int inputLength, long iv,
			byte[] output) throws CipherDataException {
		System.arraycopy(block, 0, output, 0, inputLength);
		return inputLength;
	}

}

