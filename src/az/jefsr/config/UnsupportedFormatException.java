package az.jefsr.config;

public class UnsupportedFormatException extends Exception {

	private static final long serialVersionUID = 6671639943669176097L;

	UnsupportedFormatException(String configFile) {
		super("Couldn't find config reader for " + configFile);
	}

}