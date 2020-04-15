package com.plugint.businessLayer.core;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * Class to load config files from resources
 */
public class FileLoader {
	private static final Logger logger = Logger.getLogger(FileLoader.class);

	/**
	 * Load file from src/main/resources
	 * 
	 * @param fileName Provide fileName as param that has to be loaded.
	 * @return InputStream of the file which is sent for loading
	 * @throws IOException if file is not loaded successfully
	 */
	public InputStream loadFileFromResources(String fileName) throws IOException {

		Helper.isNotEmpty(fileName);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResourceAsStream(fileName);
	}

}
