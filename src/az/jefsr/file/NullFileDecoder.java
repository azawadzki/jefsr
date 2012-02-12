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
import java.io.InputStream;

class NullFileDecoder implements FileDecoder {

	NullFileDecoder(InputStream in) {
		this.in = in;
	}
	
	@Override
	public int read(byte[] buf) throws IOException {
		int totalRead = 0;
		do {
			assert(totalRead < buf.length);
			int read = in.read(buf, totalRead, buf.length - totalRead);
			if (read == -1) {
				break;
			}
			totalRead += read;
		} while (totalRead != buf.length);
		return (totalRead == 0) ? -1 : totalRead;
	}
	
	private InputStream in;

}
