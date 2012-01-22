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


/** Description of cipher used by the library.
 * If you provide a new Cipher take care to register it in CipherAlgorithmFactory
 */
public interface CipherAlgorithm {
	
	/** Provide human-readable name of the cipher algorithm.
	 * @return Stream algorithm human-readable name
	 */
	abstract String getName();
	abstract int getChecksumBytesNumber();
	abstract int getIvecByteLength();
	
	/** Provide JCE-compatible name of stream algorithm which can be used to instantiate proper javax.crypto.Cipher object in Cipher.getInstance call.
	 * @return Stream algorithm name
	 */
	abstract String getStreamAlgorithmName();
	/** Provide JCE-compatible name of block algorithm which can be used to instantiate proper javax.crypto.Cipher object in Cipher.getInstance call.
	 * @return Block algorithm name
	 */
	abstract String getBlockAlgorithmName();

}
