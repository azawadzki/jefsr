package az.jefsr;

import java.io.FileNotFoundException;

import az.jefsr.config.UnsupportedFormatException;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws UnsupportedFormatException 
	 * @throws CipherDataException 
	 * @throws CipherConfigException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedFormatException, CipherDataException, CipherConfigException {
		//Security.addProvider(new BouncyCastleProvider());
		String file = "test-data/paranoid_wout_name_ivchaining/.encfs6.xml";
    	String userPassword = "test";
    	
    	Volume v = new Volume(file);
    	v.init(userPassword);

    	String[] files = {
    			/*
    			"YKGAxrfWfbxeek7,t-L35lZN/TTUy3UG3HqBkN5cOTP,s0PLf/PBU6Fr0tBITS53aZLnmNQJL7",
    			"7yp5rUju7WJz0HqxocaWPm9K"
    			*/
    			"pqW447x1v6JOeeyyr-pCswIe",
    			"dJzawbcZtrPxVSJmvj,tf4rp/g9hJRg6V97eEtutYr8Zn-Eg5/yU,RRKp4frFbotJJt6tdl-oP"
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
