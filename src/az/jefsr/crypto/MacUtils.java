package az.jefsr.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import az.jefsr.Main;


public class MacUtils {

	public static long mac64(byte[] data, Key k, ChainedIV chainedIv) throws InvalidKeyException, NoSuchAlgorithmException {
		long tmp = MacUtils.checksum64(data, k, chainedIv);
		if (chainedIv != null) {
			System.out.printf("iv before assignment: %d\n", chainedIv.value);
			chainedIv.value = tmp;
			System.out.printf("iv after assignment: %d\n", chainedIv.value);
		}
		return tmp;
	}

	public static long checksum64(byte[] data, Key k, ChainedIV chainedIV) throws InvalidKeyException, NoSuchAlgorithmException {
		Main.print(data, "checksum64");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(k.getKey(),"HmacSHA1"));
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

	public static int mac16(byte[] data, Key k, ChainedIV chainedIv) throws InvalidKeyException, NoSuchAlgorithmException {
		long m64 = mac64(data, k, chainedIv);
		int m32 = (int) (((m64 >> 32) & 0xffffffff) ^ (m64 & 0xffffffff));
		int m16 = ((m32 >> 16) & 0xffff) ^ (m32 & 0xffff);
	
		System.out.printf("mac64: %d\n", m64);
		System.out.printf("mac32: %d\n", m32);
		System.out.printf("mac16: %d\n", m16);
		return m16;
	}

}
