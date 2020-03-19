package com.plugint.businessLayer.core;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.ini4j.Profile.Section;

import com.plugint.businessLayer.features.Tokenisation;

/*
 * Common controller to load property files
 */
public class ControllerIni {

	/*
	 * function to load particular section of property file
	 * 
	 * @param org.ini4j.Wini, String
	 * 
	 * @return org.ini4j.Profile.Section
	 */
	public static Section loadProperties(Wini ini, String section) {
		return ini.get(section);
	}

	/*
	 * function to load particular value of property file
	 * 
	 * @param org.ini4j.Wini, String,String
	 * 
	 * @return String
	 */
	public static String loadPropertyValue(Wini ini, String key, String section) {
		return ini.get(section, key);
	}

	/*
	 * function to load property file
	 * 
	 * @param String
	 * 
	 * @return org.ini4j.Wini
	 */
	public static Wini loadPropertyFile(String fileName) throws IOException {
		return new Wini(new File(fileName));
	}
}
