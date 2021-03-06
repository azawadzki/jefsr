/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package az.jefsr;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.junit.BeforeClass;
import org.junit.Test;

import az.jefsr.config.UnsupportedFormatException;
import az.jefsr.crypto.CipherConfigException;
import az.jefsr.crypto.CipherDataException;
import az.jefsr.util.Arrays;

public class VolumeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testStandard() throws UnsupportedFormatException,
			CipherDataException, CipherConfigException, IOException {
		VolumeCrawler.validate("test_data/test_fs/standard",
				"test_data/plaintext_data", "test");
	}

	@Test
	public void testStandardWithExternalIvChaining()
			throws UnsupportedFormatException, CipherDataException,
			CipherConfigException, IOException {
		VolumeCrawler.validate(
				"test_data/test_fs/standard_with_external_ivchaining",
				"test_data/plaintext_data", "test");
	}

	@Test
	public void testParanoid() throws UnsupportedFormatException,
			CipherDataException, CipherConfigException, IOException {
		VolumeCrawler.validate("test_data/test_fs/paranoid",
				"test_data/plaintext_data", "test");
	}

	@Test
	public void testParanoidWithBlowfish() throws UnsupportedFormatException,
			CipherDataException, CipherConfigException, IOException {
		VolumeCrawler.validate("test_data/test_fs/paranoid_with_blowfish",
				"test_data/plaintext_data", "test");
	}

	@Test
	public void testParanoidWithoutNameIvChaining()
			throws UnsupportedFormatException, CipherDataException,
			CipherConfigException, IOException {
		VolumeCrawler.validate(
				"test_data/test_fs/paranoid_without_name_ivchaining",
				"test_data/plaintext_data", "test");
	}

	@Test
	public void testParanoidWithStreamNames()
			throws UnsupportedFormatException, CipherDataException,
			CipherConfigException, IOException {
		VolumeCrawler.validate("test_data/test_fs/paranoid_with_streamnames",
				"test_data/plaintext_data", "test");
	}

}

class VolumeCrawler {

	private final static String CONFIG = ".encfs6.xml";

	static void validate(String encryptedVolume, String referenceDir,
			String password) throws UnsupportedFormatException,
			CipherDataException, CipherConfigException, IOException {
		new VolumeCrawler(encryptedVolume, referenceDir, password).validate();
	}

	private VolumeCrawler(String encryptedVolume, String referenceDir,
			String password) throws FileNotFoundException,
			UnsupportedFormatException, CipherConfigException {
		this.encryptedVolume = encryptedVolume;
		this.referenceDir = referenceDir;
		String config = new File(encryptedVolume, CONFIG).getPath();
		volume = new Volume(config);
		volume.init(password);
	}

	private void validate() throws CipherDataException, IOException {
		crawl(new File(encryptedVolume));
	}

	// nio.files would work nicely, but let's stay compatible with j6.
	private void crawl(File dir) throws CipherDataException, IOException {
		File[] elems = dir.listFiles();
		for (File f : elems) {
			if (f.isDirectory()) {
				validateDirectory(f);
				crawl(f);
			} else {
				validateFile(f);
			}
		}
	}

	private void validateFile(File f) throws CipherDataException, IOException {
		if (f.getName().equals(CONFIG)) {
			return;
		}
		validatePath(f);
		validateContents(f);
		validateContentsInputStream(f);
	}

	private void validateDirectory(File f) throws CipherDataException {
		validatePath(f);
	}

	private void validatePath(File f) throws CipherDataException {
		String intraVolumePath = f.getPath().replaceFirst(encryptedVolume, "");
		String decryptedName = volume.decryptPath(intraVolumePath);
		if (!new File(referenceDir, decryptedName).exists()) {
			throw new CipherDataException(
					String.format(
							"Error decoding %s. Obtained %s which doesn't match reference set",
							f.getPath(), decryptedName));
		}
	}

	private void validateContents(File f) throws CipherDataException,
			IOException {
		String intraVolumePath = f.getPath().replaceFirst(encryptedVolume, "");
		FileInputStream fileStream = new FileInputStream(f.getPath());
		BufferedInputStream bufStream = new BufferedInputStream(fileStream);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		volume.decryptFile(intraVolumePath, bufStream, byteOut);
		byteOut.flush();

		String decryptedName = volume.decryptPath(intraVolumePath);
		RandomAccessFile ref = new RandomAccessFile(new File(referenceDir,
				decryptedName).getPath(), "r");
		byte[] refBuf = new byte[(int) ref.length()];
		ref.readFully(refBuf);
		if (!Arrays.equals(byteOut.toByteArray(), refBuf)) {
			throw new CipherDataException(String.format(
					"Error decoding %s (%s) contents.", f.getPath(),
					decryptedName));
		}
	}

	private void validateContentsInputStream(File f) throws CipherDataException,
			IOException {
		String intraVolumePath = f.getPath().replaceFirst(encryptedVolume, "");
		FileInputStream fileStream = new FileInputStream(f.getPath());
		BufferedInputStream bufStream = new BufferedInputStream(fileStream);
		InputStream in = volume.getFileDecryptionStream(intraVolumePath, bufStream);
		
		ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
		while (true) {
			int b = in.read();
			if (b == -1) {
				break;
			}
			outBuf.write(b);
		}
		outBuf.flush();
		
		String decryptedName = volume.decryptPath(intraVolumePath);
		RandomAccessFile ref = new RandomAccessFile(new File(referenceDir,
				decryptedName).getPath(), "r");
		byte[] refBuf = new byte[(int) ref.length()];
		ref.readFully(refBuf);
		
		if (!Arrays.equals(outBuf.toByteArray(), refBuf)) {
			throw new CipherDataException(String.format(
					"Error decoding %s (%s) contents.", f.getPath(),
					decryptedName));
		}
	}

	String encryptedVolume;
	String referenceDir;
	Volume volume;

}
