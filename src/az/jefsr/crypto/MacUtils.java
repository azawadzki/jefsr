package az.jefsr.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.crypto.Mac;
public class MacUtils {

	public static long mac64(byte[] data, Key k, ChainedIv chainedIv) {
		long tmp = MacUtils.checksum64(data, k, chainedIv);
		if (chainedIv != null) {
			chainedIv.value = tmp;
		}
		return tmp;
	}

	private static long checksum64(byte[] data, Key k, ChainedIv chainedIv) {
		Mac mac = k.getHMacCounter();
		mac.reset();
		mac.update(data);
		
		if (chainedIv != null) {
			ByteBuffer bb = ByteBuffer.allocate(Long.SIZE / 8);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.putLong(chainedIv.value);
			mac.update(bb.array());
		}
	
		byte[] md = mac.doFinal();	
		// chop this down to a 64bit value..
		byte h[] = new byte[8];
		for (int i=0; i < md.length - 1; ++i) {
			h[i % 8] ^= md[i];
		}
		return ByteBuffer.wrap(h).getLong();
	}
	
	public static int mac32(byte[] data, Key k, ChainedIv chainedIv) {
		long m64 = mac64(data, k, chainedIv);
		int m32 = (int) (((m64 >> 32) & 0xffffffff) ^ (m64 & 0xffffffff));
		return m32;
	}

	public static int mac16(byte[] data, Key k, ChainedIv chainedIv) {
		int m32 = mac32(data, k, chainedIv);
		int m16 = ((m32 >> 16) & 0xffff) ^ (m32 & 0xffff);
		return m16;
	}

}
