package az.jefsr.crypto;

public class CipherConfigException extends CipherException {

	private static final long serialVersionUID = 5201052454556319402L;

	CipherConfigException() {
		super();
	}
	
	CipherConfigException(String message) {
    	super(message);
    }
    
	CipherConfigException(String message, Throwable cause) {
    	super(message, cause);
    }
    
	CipherConfigException(Throwable cause) {
    	super(cause);
    }
}
