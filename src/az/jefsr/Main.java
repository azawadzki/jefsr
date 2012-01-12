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
		//String file = "test-data/paranoid/.encfs6.xml";
		//String file = "test-data/paranoid_wout_name_ivchaining/.encfs6.xml";
		//String file = "test-data/paranoid_with_blowfish/.encfs6.xml";
		//String file = "test-data/paranoid_with_streamnames/.encfs6.xml";
		String file = "test-data/standard/.encfs6.xml";

    	String userPassword = "test";
    	
    	Volume v = new Volume(file);
    	v.init(userPassword);

    	String[] files = {
    			//paranoid
    			//"YKGAxrfWfbxeek7,t-L35lZN/TTUy3UG3HqBkN5cOTP,s0PLf/PBU6Fr0tBITS53aZLnmNQJL7",
    			//"7yp5rUju7WJz0HqxocaWPm9K"
    			
    			//paranoid_wout_name_ivchaining
    			//"pqW447x1v6JOeeyyr-pCswIe",
    			//"/dJzawbcZtrPxVSJmvj,tf4rp////../dJzawbcZtrPxVSJmvj,tf4rp/./g9hJRg6V97eEtutYr8Zn-Eg5/yU,RRKp4frFbotJJt6tdl-oP"
    			
    			//paranoid_with_blowfish*/
    			//"LNYp9njYE5P91oyK1U1bxEK6",
    			//"LztuRTXf9NA901/JFuxAg2CSSubN-/jvaSz-VrswGR0-"

    			//paranoid_with_streamnames
    			//"IuysUk832hermXD",
    			//"Q0QK/fviHq0/EnshWB9WI1"
    			
    			//standard
    			//"emWH3lhzE1a6W0ZwVQw6EbM0",
    			//"eSP1eCF1HVHgocrYKyit2nsU/S6-Zg,4Nto0febpMZg-OtG4w/WquaQqMP9N5bl-qcwgXz,9rq"
    			
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
