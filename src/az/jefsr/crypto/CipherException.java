package az.jefsr.crypto;

public abstract class CipherException extends Exception {

	private static final long serialVersionUID = -5427843236187604976L;
	
	CipherException() {
		super();
	}
	
    CipherException(String message) {
    	super(message);
    }
    
    CipherException(String message, Throwable cause) {
    	super(message, cause);
    }
    
    CipherException(Throwable cause) {
    	super(cause);
    }

}
