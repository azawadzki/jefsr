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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import az.jefsr.config.Config;
import az.jefsr.crypto.ChainedIv;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.crypto.Coder;


public abstract class NameDecoder {
	
	public NameDecoder(Coder coder, Config config) {
		this.coder = coder;
		this.config = config;
	}
	
	public String decodePath(String path) throws CipherDataException {
		return decodePathInfo(path).getDecryptedPath();
	}
	
	public PathInfo decodePathInfo(String path) throws CipherDataException {
		ChainedIv iv = null;
		if (getConfig().getChainedNameIV()) {
			iv = new ChainedIv();
		}
		String output = "";
		for (String el: getPathElements(path)) {
			if (!output.isEmpty()) {
				output += File.separator;
			}
			output += decodePathElements(el, iv);
		}
		long finalIv = iv == null ? 0 : iv.value;
		return new PathInfo(path, output, finalIv);
	}
	
	public Coder getCoder() {
		return coder;
	}

	public Config getConfig() {
		return config;
	}
	
	protected abstract String decodePathElements(String path, ChainedIv iv) throws CipherDataException;

	private List<String> getPathElements(String path) {
		List<String> elems = new ArrayList<String>();
		try {
			String onlySlashes = path.replace("\\", "/");
			String normalized = new URI(onlySlashes).normalize().getPath();
			elems.addAll(Arrays.asList(normalized.split("/")));
			for (Iterator<String> it = elems.iterator(); it.hasNext(); ) {
			    if (it.next().isEmpty()) {
			        it.remove();
			    }
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			// returning empty list is enough for error handling 
		}
		return elems;
	}
	
	private Coder coder;
	private Config config;
}
