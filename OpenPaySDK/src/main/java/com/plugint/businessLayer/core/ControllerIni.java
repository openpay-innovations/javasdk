package com.plugint.businessLayer.core;

import java.io.IOException;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;
/**
 * Common controller to load property files
 */
public class ControllerIni {

	/**
	 * Function to load particular section of property file.
	 * 
	 * @param configFileObj File object which will contain property file. Ex:
	 *                      merchantConfig.ini
	 * @param section       String value to load particular section of propertyFile
	 *                      Ex: authentication
	 * @return All keys and values of that section.
	 */
	public static Section loadProperties(Wini configFileObj, String section) {
		return configFileObj.get(section);
	}

	/**
	 * Function to load particular value of given section of property file.
	 * 
	 * @param configFileObj File object which will contain property file. Ex:
	 *                      merchantConfig.ini
	 * @param key           String key to load particular value. Ex:password
	 * @param section       String value to load particular section of propertyFile
	 *                      Ex: authentication
	 * @return value of that key in the particular section.
	 */
	public static String loadPropertyValue(Wini configFileObj, String key, String section) {
		return configFileObj.get(section, key);
	}

	/**
	 * Function to load property file.
	 * 
	 * @param fileName file with this fileName to be loaded . Ex:
	 *                 merchantConfig.ini
	 * @return File object which will contain property file.
	 * 
	 * @throws IOException if file is not loaded successfully
	 */
	public static Wini loadPropertyFile(String fileName) throws IOException {
		FileLoader fileLoader = new FileLoader();
		return new Wini(fileLoader.loadFileFromResources(fileName));
	}
}
