package az.jefsr.crypto;

public class CipherConfigException extends CipherException {

	private static final long serialVersionUID = 5201052454556319402L;

	public CipherConfigException() {
		super();
	}
	
	public CipherConfigException(String message) {
    	super(message);
    }
    
	public CipherConfigException(String message, Throwable cause) {
    	super(message, cause);
    }
    
	public CipherConfigException(Throwable cause) {
    	super(cause);
    }
}
