package az.jefsr.crypto.fixtures;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import az.jefsr.config.Config;
import az.jefsr.config.ConfigReader;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.Key;

public abstract class FSFixture {
	public abstract String getUserPassword();
	public abstract byte[] getUserKeyBytes();
	public abstract byte[] getVolumeKeyBytes();
	public abstract int getKeySize();
	public abstract String getConfigFilePath();
	public abstract List<CoderFixture> getStreamTestVectors() throws CipherConfigException;
	public abstract List<CoderFixture> getBlockTestVectors() throws CipherConfigException;
	
	protected FSFixture() {
		String configPath = new File("test-data", getConfigFilePath()).getPath();
		try {
			config = ConfigReader.Factory.getInstance().createInstance(configPath).parse(configPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	public Key getVolumeKey() throws CipherConfigException {
		return createKeyFromRawBytes(getVolumeKeyBytes());
	}
	
	public Key getUserKey() throws CipherConfigException {
		return createKeyFromRawBytes(getUserKeyBytes());
	}
	
	private Key createKeyFromRawBytes(byte[] buf) throws CipherConfigException {
		byte[] keyBytes = Arrays.copyOf(buf, getKeySize() / 8);
		byte[] ivBytes = Arrays.copyOfRange(buf, getKeySize() / 8, buf.length);
		return new Key(keyBytes, ivBytes);
	}
	
	Config config;
}
