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
package az.jefsr.util;

import java.util.ArrayList;

public class ByteEncoder {

	private static char[] Ascii2B64Table;
	static {
		ByteEncoder.Ascii2B64Table =
	       "                                            01  23456789:;       ".toCharArray();
	    //  0123456789 123456789 123456789 123456789 123456789 123456789 1234
	}

	public static byte[] changeBase2(byte[] src, int src2Pow, int dst2Pow, boolean outputPartialLastByte) {
	    long work = 0;
	    int workBits = 0;
	    int mask = (1 << dst2Pow) - 1;
	
	    ArrayList<Byte> dst = new ArrayList<Byte>();
	    int srcIdx = 0;
	    while(srcIdx != src.length) {
			work |= ((long)(src[srcIdx++])) << workBits;
			workBits += src2Pow;
			while (workBits >= dst2Pow) {
				dst.add((byte)(work & mask));
			    work >>= dst2Pow;
			    workBits -= dst2Pow;
			}
	    }
	    if ((workBits != 0) && outputPartialLastByte) {
	    	dst.add((byte)(work & mask));
	    }
	    byte[] out = new byte[dst.size()];
	    for (int i = 0; i < out.length; ++i) {
	    	out[i] = dst.get(i);
	    }
	    return out;
	}

	public static byte[] changeBase2(byte[] src, int src2Pow, int dst2Pow) {
		return changeBase2(src, src2Pow, dst2Pow, true);
	}

	public static byte[] asciiToB64(byte[] in) {
		byte[] out = new byte[in.length];
	    for (int i = 0; i < in.length; ++i) {
			byte ch = in[i];
			if (ch >= 'A') {
			    if (ch >= 'a') {
			    	ch += 38 - 'a';
			    } else {
			    	ch += 12 - 'A';
			    }
			} else {
			    ch = (byte)(ByteEncoder.Ascii2B64Table[ch] - '0');
			}
			out[i] = ch;
	    }
	    return out;
	}

	public static int b64ToB256Bytes(int numB64Bytes) {
	    return (numB64Bytes * 6) / 8;
	}

}
