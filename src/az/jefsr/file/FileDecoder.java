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

public interface FileDecoder {
	
	/* Decode buf.length portion of wrapped data and write it to buf.
	 * Be aware that this method can signal the end of underlying stream in two ways:
	 * # returning -1
	 * # returning n < buf.length.
	 * This is an important difference in comparison to java.io.InputStream interface.
	 * @return number of bytes written. This function 
	 */
	int read(byte[] buf) throws IOException, CipherDataException;

}
