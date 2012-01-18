package az.jefsr.crypto;

public abstract class CipherException extends Exception {

	private static final long serialVersionUID = -5427843236187604976L;
	
	public CipherException() {
		super();
	}
	
    public CipherException(String message) {
    	super(message);
    }
    
    public CipherException(String message, Throwable cause) {
    	super(message, cause);
    }
    
    public CipherException(Throwable cause) {
    	super(cause);
    }

}
