package com.plugint.businessLayer.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.plugint.businessLayer.constant.PlugintConstants;

/**
 * This is Helper class which will provide some utility methods:
 * 
 * Ex:
 * 
 * Reading property file. Writing property file. Check null values etc
 */
public class Helper {

	private static final Logger logger = Logger.getLogger(Helper.class);

	/**
	 * Read json data from json file
	 * 
	 * @param fileName Sending file name as String to read json data from file
	 * 
	 * @return jsonString read from the file. In case of Exception it will return
	 *         null
	 */
	public static String readJson(String fileName) {
		try {
			isNotEmpty(fileName);
			logger.info("Reading json from file " + fileName);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject.toJSONString();
		} catch (Exception e) {
			logger.error("Exception occured while reading file", e.getCause());
			logger.error("Exception occurred in readJson - ", e);
			return null;
		}

	}

	/**
	 * Read text data from txt file
	 * 
	 * @param fileName Sending file name as String to read text data from file
	 * 
	 * @return String read from the file.In case of Exception it will return null
	 */
	public static String readText(String fileName) {
		Scanner sc = null;
		try {
			isNotEmpty(fileName);
			logger.info("Reading text from file " + fileName);
			File file = new File(fileName);
			sc = new Scanner(file);
			String textToBeSent = "";
			while (sc.hasNextLine())
				textToBeSent = sc.nextLine();
			return textToBeSent;
		} catch (Exception e) {
			logger.error("Exception occured while reading file", e.getCause());
			logger.error("Exception occurred in method readText while reading the file- ", e);
			return null;
		} finally {
			isNotNull(sc);
			sc.close();
		}

	}

	/**
	 * Write data to file
	 * 
	 * @param fileName Sending file name as String to write text data into file
	 * 
	 * @param content  Sending content as param which has to be written in file
	 * 
	 */
	public static void usingBufferedWritter(String fileName, String content) {
		BufferedWriter writer = null;
		try {
			isNotEmpty(fileName);
			isNotEmpty(content);
			logger.info("Writing content to file " + fileName);
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(content);

		} catch (Exception e) {
			logger.error("Exception occured while reading file", e.getCause());
			logger.error("Exception occurred in method usingBufferedWritter while writing file  - ", e);
		} finally {
			try {
				isNotNull(writer);
				writer.close();
			} catch (IOException e) {
				logger.error("Exception occured while closing buffer writer", e.getCause());
				logger.error("Exception occurred in method usingBufferedWritter while closing buffer writer  - ", e);
			}
		}

	}

	/**
	 * Function to check not null
	 * 
	 * @param checkObjectNull Generic Object is sent as param to check if not null
	 * 
	 */
	public static void isNotNull(Object checkObjectNull) {
		if (checkObjectNull == null)
			throw new IllegalArgumentException("illegal null");
	}

	/**
	 * Function to check empty string
	 * 
	 * @param checkStringEmpty String is sent as param to check if not Empty or null
	 * 
	 */
	public static void isNotEmpty(String checkStringEmpty) {
		if (checkStringEmpty == null || checkStringEmpty.equals(""))
			throw new IllegalArgumentException("illegal empty string");
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
		isNotNull(configFileObj);
		isNotEmpty(key);
		isNotEmpty(section);
		return ControllerIni.loadPropertyValue(configFileObj, key, section);
	}

	/**
	 * Function to load Api config property file.
	 * 
	 * @return File object which will contain property mappingApiConfig.ini file. In
	 *         case of Exception return null
	 * 
	 */
	public static Wini loadApiConfigFile() {
		try {
			isNotEmpty(PlugintConstants.MAPPING_API_FILE);
			return ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_API_FILE);
		} catch (Exception e) {
			logger.error("Exception occured while fetching config file" + e.getMessage());
			logger.error(e.getStackTrace());
			return null;
		}
	}

	/**
	 * Get max retry value from mappingApiConfig.ini from [APIRetry] section. This
	 * integer value is used by functions of PlugintSDK.java to retry API calls max
	 * upto this value if socket timeout exception occurs. Set maxRetry value as 0
	 * if its not configured in property file
	 * 
	 * @return int max retry value
	 */
	public static int getMaxRetryValue() {
		String maxRetry = Helper.loadPropertyValue(Helper.loadApiConfigFile(), PlugintConstants.APIRETRYVALUE,
				PlugintConstants.APIRETRYSECTION);
		if (maxRetry.isEmpty()) {
			maxRetry = "0";
		}
		return Integer.parseInt(maxRetry);
	}

}
