package az.jefsr.crypto;

public class CipherDataException extends CipherException {

	private static final long serialVersionUID = 6981416200665979800L;

	public CipherDataException() {
		super();
	}
	
	public CipherDataException(String message) {
    	super(message);
    }
    
	public CipherDataException(String message, Throwable cause) {
    	super(message, cause);
    }
    
	public CipherDataException(Throwable cause) {
    	super(cause);
    }
}
