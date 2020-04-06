package com.plugint.businessLayer.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.util.Util;
import com.plugint.client.ApiException;

import junit.framework.Assert;

/**
 * API tests for SDK
 */
@RunWith(JUnit4.class)
public class PlugintSDKPositiveTest {
	private static final Logger logger = Logger.getLogger(PlugintSDKPositiveTest.class);
	private final PlugintSDK sdk = new PlugintSDK();
	private static String orderId;

	/**
	 * Gets the configured minimum and maximum purchase price for orders with the
	 * authenticated retailer
	 *
	 * 
	 *
	 * @throws Exception if the Api call fails
	 */
	@Test
	public void ordersLimitsGetTest() {

		Map<String, Object> response;
		try {
			String maxRetry = Helper.loadPropertyValue(Helper.loadApiConfigFile(),PlugintConstants.APIRETRYVALUE , PlugintConstants.APIRETRYSECTION);
			assertNotNull("Max retry config value cannot be null",maxRetry);
			response = sdk.getPSPConfig(Integer.parseInt(maxRetry));
			assertNotNull("Response for get Limit api cannot be null", response);
			assertNotNull(response.get("minPrice"));
			assertNotNull(response.get("maxPrice"));
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Requests creation of a new order.
	 *
	 * 
	 *
	 * @throws ApiException         if the Api call fails
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */

	@Test
	public void ordersPostTestPositive() {
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Wini configFileName = Helper.loadApiConfigFile();
			String createNewOrderJsonStr = Helper.readJson("testData/getTokenPositive.json");
			body = Util.convertStringToMap(createNewOrderJsonStr);
			assertNotNull("Converted request map cannot be null", body);
			String maxRetry = Helper.loadPropertyValue(Helper.loadApiConfigFile(),PlugintConstants.APIRETRYVALUE , PlugintConstants.APIRETRYSECTION);
			assertNotNull("Max retry config value cannot be null",maxRetry);
			Map<String, Object> response = sdk.getToken(body, Integer.parseInt(maxRetry));
			assertNotNull("Response map for create new order cannot be null", response);
			String transactionToken = Helper.getTransactionTokenTest(response);
			String redirecturl = Helper.loadPropertyValue(configFileName, PlugintConstants.REDIRECTURL,
					PlugintConstants.REDIRECTURLSECTION) + transactionToken;
			Helper.redirectToBrowser(redirecturl);
			assertNotNull("order id cannot be null", response.get("orderId"));
			orderId = (String) response.get("orderId");
			Helper.usingBufferedWritter("testData/testOrderPositive.txt", orderId);
			logger.info(response);
			logger.info("Capturing payment");
			ordersOrderIdCapturePostTestPositive(Integer.parseInt(maxRetry));
			ordersOrderIdGetTestPositive(Integer.parseInt(maxRetry));
			ordersOrderIdRefundPostTestPositive(Integer.parseInt(maxRetry));
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Requests that the payment for an order is captured.
	 *
	 * 
	 *
	 * @throws ApiException if the Api call fails
	 */
	public void ordersOrderIdCapturePostTestPositive(int maxRetry) {
		try {
			assertNotNull("max retry count cannot be null",maxRetry);
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			assertNotNull("Order Id cannot be null", orderId);
			Map<String, Object> response;
			response = sdk.capturePayment(orderId, maxRetry);
			assertNotNull("Response map for create new order cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Gets an order by order id.
	 *
	 * 
	 *
	 * @throws ApiException if the Api call fails
	 */
	public void ordersOrderIdGetTestPositive(int maxRetry) {
		try {
			assertNotNull("max retry count cannot be null",maxRetry);
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			assertNotNull("Order Id cannot be null", orderId);
			Map<String, Object> response;
			response = sdk.updateShopOrder(orderId, maxRetry);
			assertNotNull("Response map for get orders cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Requests that an order is partially or fully refunded.
	 *
	 * 
	 *
	 * @throws Exception if the Api call fails
	 */
	public void ordersOrderIdRefundPostTestPositive(int maxRetry) {
		try {
			assertNotNull("max retry count cannot be null",maxRetry);
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			String requestJson = Helper.readJson("testData/getRefundPositive.json");
			assertNotNull("Request body for refund cannot be null", Util.convertStringToMap(requestJson));
			assertNotNull("Order id for refund cannot be null", orderId);
			Map<String, Object> response;
			response = sdk.refund(Util.convertStringToMap(requestJson), orderId, maxRetry);
			assertNotNull("Response map for refund cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}
}
