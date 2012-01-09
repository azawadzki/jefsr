package az.jefsr.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.crypto.Mac;

import az.jefsr.Main;


public class MacUtils {

	public static long mac64(byte[] data, Key k, ChainedIV chainedIv) {
		long tmp = MacUtils.checksum64(data, k, chainedIv);
		if (chainedIv != null) {
			chainedIv.value = tmp;
		}
		return tmp;
	}

	public static long checksum64(byte[] data, Key k, ChainedIV chainedIV) {
		Main.print(data, "checksum64");
		Mac mac = k.getHMacCounter();
		mac.reset();
		mac.update(data);
		
		if (chainedIV != null) {
			ByteBuffer bb = ByteBuffer.allocate(Long.SIZE / 8);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.putLong(chainedIV.value);
			Main.print(bb.array(), "tossed iv");
			mac.update(bb.array());
		}
	
		byte[] md = mac.doFinal();
		Main.print(md, "hmac");
	
		// chop this down to a 64bit value..
		byte h[] = new byte[8];
		for (int i=0; i < md.length - 1; ++i) {
			h[i % 8] ^= md[i];
		}
		System.out.printf("final value: %d\n", ByteBuffer.wrap(h).getLong());
		return ByteBuffer.wrap(h).getLong();
	}
	
	public static int mac32(byte[] data, Key k, ChainedIV chainedIv) {
		long m64 = mac64(data, k, chainedIv);
		int m32 = (int) (((m64 >> 32) & 0xffffffff) ^ (m64 & 0xffffffff));
		return m32;

	}

	public static int mac16(byte[] data, Key k, ChainedIV chainedIv) {
		int m32 = mac32(data, k, chainedIv);
		int m16 = ((m32 >> 16) & 0xffff) ^ (m32 & 0xffff);
		return m16;
	}

}
