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

/*
 * Class to read data from file
 */
public class Helper {

	private static final Logger logger = Logger.getLogger(Helper.class);

	/*
	 * Read json data from json file
	 * 
	 * @param java.lang.String
	 * 
	 * @return java.lang.String
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
		}
		return null;
	}

	/*
	 * Read text data from txt file
	 * 
	 * @param java.lang.String
	 * 
	 * @return java.lang.String
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
		} finally {
			isNotNull(sc);
			sc.close();
		}
		return null;
	}

	/*
	 * Write data to file
	 * 
	 * @param java.lang.String,java.lang.String
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

	/*
	 * Function to redirect url to browser
	 * 
	 * @param java.lang.String
	 */
	public static void redirectToBrowser(String url) {
		WebDriver driver = null;
		try {
			isNotEmpty(url);
			logger.info("Opening browser for automation testing");
			Wini configFileName = ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE);
			System.setProperty("webdriver.chrome.driver", loadPropertyValue(configFileName,
					PlugintConstants.CHROMEDRIVERFILENAME, PlugintConstants.CHROMEDRIVER));
			driver = new ChromeDriver();
			driver.get(url);
			String timeOutValueString = loadPropertyValue(configFileName, PlugintConstants.CHROMEDRIVERTIMEOUT,
					PlugintConstants.CHROMEDRIVER);
			long timeOutValue = Long.parseLong(timeOutValueString);
			Thread.sleep(timeOutValue);
		} catch (Exception e) {
			logger.error("Exception occured while reading file", e.getCause());
			logger.error("Exception occurred in method redirectToBrowser- ", e);
		} finally {
			isNotNull(driver);
			driver.close();
		}
	}

	/*
	 * function to get transaction token from response
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.lang.String
	 */
	public static String getTransactionTokenTest(Map response) {
		try {
			isNotNull(response);
			logger.debug("Sending transaction token from response");
			LinkedHashMap nextActionJson = (LinkedHashMap) response.get("nextAction");
			LinkedHashMap formPostJson = (LinkedHashMap) nextActionJson.get("formPost");
			ArrayList<Map> formFieldsList = (ArrayList) formPostJson.get("formFields");
			for (Map formFieldMap : formFieldsList) {
				if (formFieldMap.get("fieldName").equals("TransactionToken")) {
					return formFieldMap.get("fieldValue").toString();
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while fetching transaction token", e.getCause());
			logger.error("Exception occurred in method getTransactionToken - ", e);
		}

		return null;
	}

	/*
	 * Function to check not null
	 * 
	 * @param java.lang.Object
	 * 
	 * @return java.lang.IllegalArgumentException
	 */
	public static void isNotNull(Object object) {
		if (object == null)
			throw new IllegalArgumentException("illegal null");
	}

	/*
	 * Function to check empty string
	 * 
	 * @param java.lang.String
	 * 
	 * @return java.lang.IllegalArgumentException
	 */
	public static void isNotEmpty(String string) {
		if (string == null || string.equals(""))
			throw new IllegalArgumentException("illegal empty string");
	}

	/*
	 * load property values
	 * 
	 * @param org.ini4j.Wini,java.lang.String, java.lang.String
	 * 
	 * @return java.lang.String
	 */
	public static String loadPropertyValue(Wini file, String key, String section) {
		isNotNull(file);
		isNotEmpty(key);
		isNotEmpty(section);
		return ControllerIni.loadPropertyValue(file, key, section);
	}

	/*
	 * returning config file
	 * 
	 * @return org.ini4j.Wini
	 */
	public static Wini loadApiConfigFile() {
		try {
			isNotEmpty(PlugintConstants.MAPPING_API_FILE);
			return ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_API_FILE);
		} catch (Exception e) {
			logger.error("Exception occured while fetching config file" + e.getMessage());
			logger.error(e.getStackTrace());
		}
		return null;
	}
}
