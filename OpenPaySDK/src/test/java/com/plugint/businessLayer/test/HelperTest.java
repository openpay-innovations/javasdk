package com.plugint.businessLayer.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

/**
 * This is Helper Test class which will provide some utility methods:
 * 
 * Ex: Redirect to browser configuration setup, Get transaction token from Create
 * new order response
 */
public class HelperTest {

	private static final Logger logger = Logger.getLogger(HelperTest.class);

	/**
	 * This is a test function used by Test Class PlugintSDKPositiveTest.java to
	 * redirect URL to browser to test successful purchase flow
	 * 
	 * @param url Sending redirect URL from mappingApiConfig.ini
	 */
	public static void redirectToBrowser(String url) {
		WebDriver driver = null;
		try {
			Helper.isNotEmpty(url);
			logger.info("Opening browser for automation testing");
			Wini configFileName = ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE);
			System.setProperty("webdriver.chrome.driver", Helper.loadPropertyValue(configFileName,
					PlugintConstants.CHROMEDRIVERFILENAME, PlugintConstants.CHROMEDRIVER));
			driver = new ChromeDriver();
			driver.get(url);
			String timeOutValueString = Helper.loadPropertyValue(configFileName, PlugintConstants.CHROMEDRIVERTIMEOUT,
					PlugintConstants.CHROMEDRIVER);
			long timeOutValue = Long.parseLong(timeOutValueString);
			Thread.sleep(timeOutValue);
		} catch (Exception e) {
			logger.error("Exception occured while reading file", e.getCause());
			logger.error("Exception occurred in method redirectToBrowser- ", e);
		} finally {
			Helper.isNotNull(driver);
			driver.close();
		}
	}

	/**
	 * This is test function to get transaction token from response coming from
	 * CreateNewOrderApi
	 * 
	 * @param response Response coming from CreateNewOrderApi
	 * 
	 * @return transaction token string from the response.In case of Exception it
	 *         will return null
	 */
	public static String getTransactionTokenTest(Map response) {
		try {
			Helper.isNotNull(response);
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

}
