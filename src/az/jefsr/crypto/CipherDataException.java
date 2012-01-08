package az.jefsr.crypto;

public class CipherDataException extends CipherException {

	private static final long serialVersionUID = 6981416200665979800L;

	CipherDataException() {
		super();
	}
	
	CipherDataException(String message) {
    	super(message);
    }
    
	CipherDataException(String message, Throwable cause) {
    	super(message, cause);
    }
    
	CipherDataException(Throwable cause) {
    	super(cause);
    }
}
