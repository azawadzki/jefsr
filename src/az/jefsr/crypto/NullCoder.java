package az.jefsr.crypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import az.jefsr.config.Config;

public class NullCoder extends Coder {

	public NullCoder(Key key, Config config) {
		super(key, config);	}

	@Override
	public byte[] decodeStream(byte[] stream, long iv)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			IOException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException {
		return stream;
	}

	@Override
	public byte[] decodeBlock(byte[] stream, long iv)
			throws InvalidKeyException, IOException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException {
		return stream;
	}

	static class NullKeyCreator extends KeyCreator {

		@Override
		public Key createUserKey(String password, Config config) {
			return new Key(new byte[0], new byte[0]);
		}

		@Override
		public Key createVolumeKey(Coder passwordCoder, Config config) {
			return new Key(new byte[0], new byte[0]);
		}
		
	}
	
	@Override
	public KeyCreator getKeyCreationStrategy() {
		return new NullKeyCreator();
	}

}
