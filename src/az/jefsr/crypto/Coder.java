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

/** Objects of Coder class are used to perform actual decryption in jefsr. Coders glue together volume key and cipher instances as defined by FS config.
 */
public abstract class Coder {

	public Coder(Key key, CipherAlgorithm cipher, Config config) throws CipherConfigException {
        this.cipher = cipher;
        this.key = key;
        this.config = config;
	}
	
	/** Decrypt data in the provided array using stream algorithm.
	 * @param stream Array of data to decode.
	 * @param iv Initialization vector used to seed the cipher.
	 * @return Array containing decrypted data.
	 * @throws CipherDataException If an error occurred during deciphering.
	 */
	public abstract byte[] decodeStream(byte[] stream, long iv) throws CipherDataException;
	/** Decrypt data in the provided array using stream algorithm.
	 * @param stream Array of data to decode.
	 * @param inputLEngth length of the data portion in stream array that should be used.
	 * @param iv Initialization vector used to seed the cipher.
	 * @param output Output buffer (output.length must not be smaller than stream.length)
	 * @return number of bytes in output array that are the outcome of decryption.
	 * @throws CipherDataException If an error occurred during deciphering.
	 */
	public abstract int decodeStream(byte[] stream, int inputLength, long iv, byte[] output) throws CipherDataException;
	/** Decrypt data in the provided array using block algorithm.
	 * @param block Array of data to decode.
	 * @param iv Initialization vector used to seed the cipher.
	 * @return Array containing decrypted data.
	 * @throws CipherDataException If an error occurred during deciphering.
	 */
	public abstract byte[] decodeBlock(byte[] block, long iv) throws CipherDataException;
	/** Decrypt data in the provided array using block algorithm.
	 * @param block Array of data to decode.
	 * @param inputLEngth length of the data portion in stream array that should be used.
	 * @param iv Initialization vector used to seed the cipher.
	 * @param output Output buffer (output.length must not be smaller than stream.length)
	 * @return number of bytes in output array that are the outcome of decryption.
	 * @throws CipherDataException If an error occurred during deciphering.
	 */
	public abstract int decodeBlock(byte[] block, int inputLength, long iv, byte[] output) throws CipherDataException;
	
	public Key getKey() {
		return key;
	}
	
	public Config getConfig() {
		return config;
	}

    public CipherAlgorithm getCipher() {
        return cipher;    
    }

	private CipherAlgorithm cipher;
	private Key key;
	private Config config;
}
