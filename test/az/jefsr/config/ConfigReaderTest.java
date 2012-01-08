package az.jefsr.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		paranoidConfigFile = "test-data/paranoid/.encfs6.xml";
		nonexistentConfigFile = "test-data/plaintext/.encfs6.xml";
		unsupportedConfigFile = "test-data/plaintext/readme.txt";
	}

	@Test
	public void testParsePath() throws FileNotFoundException, UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance().createInstance(paranoidConfigFile);
		reader.parse(paranoidConfigFile);
	}

	@Test
	public void testParseFile() throws FileNotFoundException, UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance().createInstance(paranoidConfigFile);
		reader.parse(new File(paranoidConfigFile));
	}
	
	@Test
	public void testParseInputStream() throws FileNotFoundException, UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance().createInstance(paranoidConfigFile);
		reader.parse(new BufferedInputStream(new FileInputStream(paranoidConfigFile)));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testParseNonexistent() throws FileNotFoundException, UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance().createInstance(nonexistentConfigFile);
		reader.parse(nonexistentConfigFile);
	}
	
	@Test(expected=UnsupportedFormatException.class)
	public void testParseUnsupported() throws FileNotFoundException, UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance().createInstance(unsupportedConfigFile);
		reader.parse(unsupportedConfigFile);
	}
	
	static String paranoidConfigFile;
	static String nonexistentConfigFile;
	static String unsupportedConfigFile;
}
