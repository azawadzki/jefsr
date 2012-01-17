package az.jefsr.file;

import java.io.IOException;

import az.jefsr.crypto.CipherDataException;

interface FileDecoder {
	
	int read(byte[] buf) throws IOException, CipherDataException;

}
