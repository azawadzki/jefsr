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
package az.jefsr.crypto.fixtures;

import az.jefsr.crypto.Key;

public class CoderFixture {

	public CoderFixture(Key key, byte[] input, long iv, byte[] output) {
		this.key = key;
		this.iv = iv;
		this.input = input;
		this.output = output;
	}

	public Key getKey() {
		return key;
	}

	public long getIv() {
		return iv;
	}

	public byte[] getInput() {
		return input;
	}

	public byte[] getOutput() {
		return output;
	}

	private Key key;
	private long iv;
	private byte[] input;
	private byte[] output;
}
