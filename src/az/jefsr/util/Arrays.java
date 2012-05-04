package az.jefsr.util;

import java.util.List;

public class Arrays {

	public static byte[] copyOfRange(byte[] original, int start, int end) {
		byte[] out = new byte[end - start];
		for (int i = 0; i < out.length; ++i) {
			out[i] = original[start + i];
		}
		return out;
	}

	public static boolean equals(byte[] array1, byte[] array2) {
		return java.util.Arrays.equals(array1, array2);
	}

	public static byte[] copyOf(byte[] original, int newLength) {
		return copyOfRange(original, 0, newLength);
	}

	public static <T> List<T> asList(T... array) {
		return java.util.Arrays.asList(array);
	}

	public static int hashCode(byte[] bytes) {
		return java.util.Arrays.hashCode(bytes);
	}
	
}
