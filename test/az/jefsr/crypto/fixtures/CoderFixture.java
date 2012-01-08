package az.jefsr.crypto.fixtures;

import az.jefsr.crypto.Key;

public class CoderFixture {

	public CoderFixture(Key key, byte[] input, long iv, byte[] output) {
		this.key = key;
		this.iv = iv;
		this.input = input;
		this.output = output;
	}
	
	public Key getKey() {
		return key;
	}
	
	public long getIv() {
		return iv;
	}
	
	public byte[] getInput() {
		return input;
	}
	
	public byte[] getOutput() {
		return output;
	}
	
	private Key key;
	private long iv;
	private byte[] input;
	private byte[] output;
}
