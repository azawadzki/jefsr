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
package az.jefsr.config;


/** Object containing EncFS volume configuration data. Currently, it mirrors the structure of .encfs6.xml file.
 */
public class Config {

	public Config() {

	}
	
	public static class CipherDescription {

		public String getName() {
			return name;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		@Override
		public boolean equals(Object other) {
			boolean ret = false;
			if (other instanceof CipherDescription) {
				CipherDescription otherDescr = (CipherDescription) other;
				ret = this.name.equals(otherDescr.name) &&
					this.major == otherDescr.major &&
					this.minor == otherDescr.minor;
			}
			return ret;
		}

		@Override
		public int hashCode() {
			return name.hashCode() + major + 1024 * minor;
		}

		private String name;
		private int major;
		private int minor;
		
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public CipherDescription getCipherAlg() {
		return cipherAlg;
	}

	public void setCipherAlg(CipherDescription cipherAlg) {
		this.cipherAlg = cipherAlg;
	}

	public CipherDescription getNameAlg() {
		return nameAlg;
	}

	public void setNameAlg(CipherDescription nameAlg) {
		this.nameAlg = nameAlg;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public boolean getUniqueIV() {
		return uniqueIV != 0;
	}

	public void setUniqueIV(boolean uniqueIV) {
		this.uniqueIV = uniqueIV ? 1 : 0;
	}

	public boolean getChainedNameIV() {
		return chainedNameIV != 0;
	}

	public void setChainedNameIV(boolean chainedNameIV) {
		this.chainedNameIV = chainedNameIV ? 1 : 0;
	}

	public boolean getExternalIVChaining() {
		return externalIVChaining != 0;
	}

	public void setExternalIVChaining(boolean externalIVChaining) {
		this.externalIVChaining = externalIVChaining ? 1 : 0;
	}

	public int getBlockMACBytes() {
		return blockMACBytes;
	}

	public void setBlockMACBytes(int blockMACBytes) {
		this.blockMACBytes = blockMACBytes;
	}

	public int getBlockMACRandBytes() {
		return blockMACRandBytes;
	}

	public void setBlockMACRandBytes(int blockMACRandBytes) {
		this.blockMACRandBytes = blockMACRandBytes;
	}

	public boolean getAllowHoles() {
		return allowHoles != 0;
	}

	public void setAllowHoles(boolean allowHoles) {
		this.allowHoles = allowHoles ? 1 : 0;;
	}

	public int getEncodedKeySize() {
		return encodedKeySize;
	}

	public void setEncodedKeySize(int encodedKeySize) {
		this.encodedKeySize = encodedKeySize;
	}

	public String getEncodedKeyData() {
		return encodedKeyData.trim();
	}

	public void setEncodedKeyData(String encodedKeyData) {
		this.encodedKeyData = encodedKeyData;
	}

	public int getSaltLen() {
		return saltLen;
	}

	public void setSaltLen(int saltLen) {
		this.saltLen = saltLen;
	}

	public String getSaltData() {
		return saltData.trim();
	}

	public void setSaltData(String saltData) {
		this.saltData = saltData;
	}

	public int getKdfIterations() {
		return kdfIterations;
	}

	public void setKdfIterations(int kdfIterations) {
		this.kdfIterations = kdfIterations;
	}

	public int getDesiredKDFDuration() {
		return desiredKDFDuration;
	}

	public void setDesiredKDFDuration(int desiredKDFDuration) {
		this.desiredKDFDuration = desiredKDFDuration;
	}

	@Override
	public boolean equals(Object other) {
		boolean ret = false;
		if (other instanceof Config) {
			Config otherVolume = (Config) other;
			ret = this.version.equals(otherVolume.version) &&
                this.creator.equals(otherVolume.creator) &&
                this.cipherAlg.equals(otherVolume.cipherAlg) &&
                this.nameAlg.equals(otherVolume.nameAlg) &&
                this.keySize == otherVolume.keySize &&
                this.blockSize == otherVolume.blockSize &&
                this.uniqueIV == otherVolume.uniqueIV &&
                this.chainedNameIV == otherVolume.chainedNameIV &&
                this.externalIVChaining == otherVolume.externalIVChaining &&
                this.blockMACBytes == otherVolume.blockMACBytes &&
                this.blockMACRandBytes == otherVolume.blockMACRandBytes &&
                this.allowHoles == otherVolume.allowHoles &&
                this.encodedKeySize == otherVolume.encodedKeySize &&
                this.encodedKeyData.equals(otherVolume.encodedKeyData) &&
                this.saltLen == otherVolume.saltLen &&
                this.saltData.equals(otherVolume.saltData) &&
                this.kdfIterations == otherVolume.kdfIterations &&
                this.desiredKDFDuration == otherVolume.desiredKDFDuration;
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return version.hashCode() +
            creator.hashCode() +
            cipherAlg.hashCode() +
            nameAlg.hashCode() +
            keySize * 16 +
            blockSize * 32 +
            uniqueIV * 64 +
            chainedNameIV * 128 +
            externalIVChaining * 256 +
            blockMACBytes * 512 +
            blockMACRandBytes * 1024 +
            allowHoles * 2048 +
            encodedKeySize * 4096 +
            encodedKeyData.hashCode() +
            saltLen * 8192 +
            saltData.hashCode() +
            kdfIterations + 16384 +
            desiredKDFDuration + 32768;
	}

	private String version;
	private String creator;
	private CipherDescription cipherAlg;
	private CipherDescription nameAlg;
	private int keySize;
	private int blockSize;
	private int uniqueIV;
	private int chainedNameIV;
	private int externalIVChaining;
	private int blockMACBytes;
	private int blockMACRandBytes;
	private int allowHoles;
	private int encodedKeySize;
	private String encodedKeyData;
	private int saltLen;
	private String saltData;
	private int kdfIterations;
	private int desiredKDFDuration;
}
