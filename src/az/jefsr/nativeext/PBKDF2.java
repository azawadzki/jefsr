package az.jefsr.nativeext;

import java.util.ArrayList;
import java.util.List;

public class PBKDF2 {

	public interface Callback {
		boolean onProgress(int requestNumber, int percentCompleted);
	}

	private PBKDF2() {

	}

	public static PBKDF2 getInstance() {
		if (sInstance == null) {
			sInstance = new PBKDF2();
		}
		return sInstance;
	}

	public byte[] deriveKey(byte[] password, byte[] salt, int iterCount, int keyLengthInBit) {
		return deriveKey(generateRequestNumber(), password, salt, iterCount, keyLengthInBit, new PBKDF2.Callback() {
			@Override
			public boolean onProgress(int requestNumber, int percentCompleted) {
				return PBKDF2.this.onProgress(requestNumber, percentCompleted);
			}
		});
	}

	public void registerCallback(Callback cb) {
		mCallbacks.add(cb);
	}

	public void unregisterCallback(Callback cb) {
		mCallbacks.remove(cb);
	}

	private boolean onProgress(int requestNumber, int percentCompleted) {
		boolean continueLooping = true;
		for (Callback cb: mCallbacks) {
			continueLooping &= cb.onProgress(requestNumber, percentCompleted);
		}
		return continueLooping;
	}

	private synchronized int generateRequestNumber() {
		return ++mRequestNumber;
	}

	private native byte[] deriveKey(int requestNumber, byte[] password, byte[] salt, int iterCount, int keyLengthInBit, Callback cb);

	private static PBKDF2 sInstance;
	private List<Callback> mCallbacks = new ArrayList<Callback>();
	private int mRequestNumber;
}
