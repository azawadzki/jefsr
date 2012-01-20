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

import java.io.InputStream;

import az.jefsr.config.Config;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;

public class FileDecoderFactory {
	
	private static FileDecoderFactory instance = new FileDecoderFactory();
	
	public static FileDecoderFactory getInstance() {
		return instance;
	}
	
	public FileDecoder createInstance(InputStream in, String path, Coder coder, Config config) throws CipherDataException {
		NameDecoder nameDecoder = NameDecoderFactory.getInstance().createInstance(config.getNameAlg().getName(), coder, config);
		PathInfo pathInfo = nameDecoder.decodePathInfo(path);
		return createInstance(in, pathInfo, coder, config);
	}
	
	public FileDecoder createInstance(InputStream in, PathInfo pathInfo, Coder coder, Config config) {
		FileDecoder decoder = new CipherFileDecoder(new NullFileDecoder(in), pathInfo, coder, config);
		if (config.getBlockMACBytes() > 0 || config.getBlockMACRandBytes() > 0) {
			decoder = new MacFileDecoder(decoder, coder, config);
		}
		return decoder;
	}

}
