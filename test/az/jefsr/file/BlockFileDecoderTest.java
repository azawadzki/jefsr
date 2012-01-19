package az.jefsr.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.crypto.CipherDataException;

class BlockFileDecoderImpl extends BlockFileDecoder {

	BlockFileDecoderImpl(FileDecoder in, int blockSize, boolean holesAllowed) {
		super(in, blockSize, holesAllowed);
	}

	@Override
	protected byte[] decodeBlock(long blockNum, byte[] in, int inputLen)
			throws CipherDataException {
		return Arrays.copyOf(in, inputLen);
	}

}

public class BlockFileDecoderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testReadEqualBlocks() throws CipherDataException, IOException {
		testReadBlocks(16, 16, 221);
		testReadBlocks(16, 16, 16 * 10);
		testReadBlocks(16, 16, 15);
		testReadBlocks(16, 16, 0);
	}

	@Test
	public void testReadSmallerOutputBlocks() throws CipherDataException,
			IOException {
		testReadBlocks(16, 9, 221);
		testReadBlocks(16, 9, 16 * 10);
		testReadBlocks(16, 9, 9 * 10);
		testReadBlocks(16, 1, 221);
		testReadBlocks(222, 9, 221);
		testReadBlocks(17, 15, 221);
	}

	@Test
	public void testReadLargerOutputBlocks() throws CipherDataException,
			IOException {
		testReadBlocks(16, 20, 221);
		testReadBlocks(16, 20, 16 * 10);
		testReadBlocks(16, 20, 20 * 10);
		testReadBlocks(16, 200, 221);
		testReadBlocks(16, 499, 221);
		testReadBlocks(8, 16, 221);
		testReadBlocks(15, 17, 221);
		testReadBlocks(1, 16, 221);
	}

	private void testReadBlocks(int decoderBlockSize, int readerBlockSize,
			int inputSize) throws CipherDataException, IOException {
		byte[] input = new byte[inputSize];
		byte[] expectedOutput = input;
		byte[] outputBuffer = new byte[input.length];
		for (int i = 0; i < input.length; ++i) {
			input[i] = (byte) i;
		}
		ByteArrayInputStream ins = new ByteArrayInputStream(input);
		BlockFileDecoder decoder = new BlockFileDecoderImpl(
				new NullFileDecoder(ins), decoderBlockSize, false);
		int bytesRead;
		int dstPos = 0;
		byte[] buffer = new byte[readerBlockSize];
		do {
			if ((bytesRead = decoder.read(buffer)) > 0) {
				System.arraycopy(buffer, 0, outputBuffer, dstPos, bytesRead);
				dstPos += bytesRead;
			}

		} while (bytesRead != -1);

		assertThat(outputBuffer, equalTo(expectedOutput));
	}
}
