package az.jefsr.crypto;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import az.jefsr.config.Config;
import az.jefsr.util.ByteEncoder;

class BlockNameDecoder extends NameDecoder {

	final private static int MAC_BYTES = 2;
	
	public BlockNameDecoder(Coder coder, Config config) {
		super(coder, config);
	}
	
	@Override	
	public String decodePath(String path) {
		try {
			System.out.printf("!! canonical: %s\n", new File(path).getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] elements = path.split("/");
		String output = "";
		ChainedIV seed = new ChainedIV();
		for (String el: elements) {
			System.out.printf("decoding: %s\n", el);
			if (el.isEmpty()) {
				continue;
			}
			if (!output.isEmpty()) {
				output += "/";
			}
			try {
				output += decryptFilename(el, seed);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return output;
	}

	private byte[] decodeFilenameData(byte[] data) {
		return ByteEncoder.changeBase2(ByteEncoder.asciiToB64(data), 6, 8, false);
	}

	private byte[] decodeFilenameData(String data) {
		return decodeFilenameData(data.getBytes());
	}

	private int getDecipheredFilenameSize(String filename, byte[] decipheredData) {
		int decipheredStreamLen = ByteEncoder.b64ToB256Bytes(filename.length()) - MAC_BYTES;
		int padding = decipheredData[decipheredStreamLen - 1];
		return decipheredStreamLen - padding;
	}

	private String decryptFilename(String filename, ChainedIV seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] encFilenameData = decodeFilenameData(filename);
		long mac = 0xffff & ByteBuffer.wrap(encFilenameData, 0, MAC_BYTES).asShortBuffer().get();
		byte[] encFilename = Arrays.copyOfRange(encFilenameData, MAC_BYTES, encFilenameData.length);
		long ivValue = seed.value ^ mac;

		Coder coder = getCoder();
		byte[] deciphered = coder.decodeBlock(encFilename, ivValue);
		MacUtils.mac16(deciphered, coder.getKey(), seed);		
		int finalSize = getDecipheredFilenameSize(filename, deciphered);
		
		return new String(Arrays.copyOfRange(deciphered, 0, finalSize));
	}

}
