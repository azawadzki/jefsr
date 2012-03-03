package az.jefsr.nativeext;

import java.util.ArrayList;
import java.util.List;

public class PBKDF2 {

	public interface Callback {
		void onProgress(int requestNumber, int percentCompleted);
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
			public void onProgress(int requestNumber, int percentCompleted) {
				PBKDF2.this.onProgress(requestNumber, percentCompleted);
			}
		});
	}

	public void registerCallback(Callback cb) {
		mCallbacks.add(cb);
	}

	public void unregisterCallback(Callback cb) {
		mCallbacks.remove(cb);
	}

	private void onProgress(int requestNumber, int percentCompleted) {
		for (Callback cb: mCallbacks) {
			cb.onProgress(requestNumber, percentCompleted);
		}
	}

	private synchronized int generateRequestNumber() {
		return ++mRequestNumber;
	}

	private native byte[] deriveKey(int requestNumber, byte[] password, byte[] salt, int iterCount, int keyLengthInBit, Callback cb);

	private static PBKDF2 sInstance;
	private List<Callback> mCallbacks = new ArrayList<Callback>();
	private int mRequestNumber;
}
