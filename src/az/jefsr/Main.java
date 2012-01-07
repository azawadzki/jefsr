package az.jefsr;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import az.jefsr.config.ConfigReader.UnsupportedFormat;

public class Main {

	/**
	 * @param args
	 * @throws NoSuchProviderException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 * @throws InvalidParameterSpecException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws IOException 
	 * @throws UnsupportedFormat 
	 */
	public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException, UnsupportedFormat {
    	//Security.addProvider(new BouncyCastleProvider());
		String file = "test-data/paranoid/.encfs6.xml";
    	String userPassword = "test";
    	
    	Volume v = new Volume(file);
    	v.init(userPassword);

    	String[] files = {
    			"YKGAxrfWfbxeek7,t-L35lZN/TTUy3UG3HqBkN5cOTP,s0PLf/PBU6Fr0tBITS53aZLnmNQJL7",
    			"7yp5rUju7WJz0HqxocaWPm9K"
    	};
    	for (String f: files) {
    		System.out.printf("%s\n%s\n--\n", f, v.decryptPath(f));
    	}

	}
	
	public static void print(byte[] arg) {
		print(arg, null);
	}
	
	public static void print(byte[] arg, String msg) {
		if (msg != null) {
			System.out.printf("%s: ", msg);
		}
		for (byte b: arg) {
			System.out.printf("%d, ", b);
		}
		System.out.println();
	}

}
