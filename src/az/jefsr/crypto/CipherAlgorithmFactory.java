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

import az.jefsr.util.FactoryBase;

public class CipherAlgorithmFactory extends FactoryBase<CipherAlgorithm> {

	static CipherAlgorithmFactory instance = new CipherAlgorithmFactory();
	static {
		CipherAlgorithmFactory.getInstance().registerType("ssl/aes", AesCipher.class);
		CipherAlgorithmFactory.getInstance().registerType("ssl/blowfish", BlowfishCipher.class);
		CipherAlgorithmFactory.getInstance().registerType("nullCipher", NullCipher.class);
	}	
	public static CipherAlgorithmFactory getInstance() {
		return instance;
	}
	
	public CipherAlgorithm createInstance(String nameAlg) {
		CipherAlgorithm ret = null;
		Class<? extends CipherAlgorithm> cls = fetchType(nameAlg);
		try {
			ret = cls.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return ret;	
	}
}

