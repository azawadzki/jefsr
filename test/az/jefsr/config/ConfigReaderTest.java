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
		paranoidConfigFile = "test_data/config_files/paranoid/.encfs6.xml";
		nonexistentConfigFile = "dummy";
		unsupportedConfigFile = "test_data/readme.txt";
	}

	@Test
	public void testParsePath() throws FileNotFoundException,
			UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance()
				.createInstance(paranoidConfigFile);
		reader.parse(paranoidConfigFile);
	}

	@Test
	public void testParseFile() throws FileNotFoundException,
			UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance()
				.createInstance(paranoidConfigFile);
		reader.parse(new File(paranoidConfigFile));
	}

	@Test
	public void testParseInputStream() throws FileNotFoundException,
			UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance()
				.createInstance(paranoidConfigFile);
		reader.parse(new BufferedInputStream(new FileInputStream(
				paranoidConfigFile)));
	}

	@Test(expected = FileNotFoundException.class)
	public void testParseNonexistent() throws FileNotFoundException,
			UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance()
				.createInstance(nonexistentConfigFile);
		reader.parse(nonexistentConfigFile);
	}

	@Test(expected = UnsupportedFormatException.class)
	public void testParseUnsupported() throws FileNotFoundException,
			UnsupportedFormatException {
		ConfigReader reader = ConfigReader.Factory.getInstance()
				.createInstance(unsupportedConfigFile);
		reader.parse(unsupportedConfigFile);
	}

	static String paranoidConfigFile;
	static String nonexistentConfigFile;
	static String unsupportedConfigFile;
}
