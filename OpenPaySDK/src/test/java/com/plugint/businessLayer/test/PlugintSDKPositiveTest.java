package com.plugint.businessLayer.test;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.businessLayer.constant.PlugintConstants;

import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.util.Util;


/**
 * API tests cases are handled for positive test data. All positive test cases
 * are handled in this class
 */
@RunWith(JUnit4.class)
public class PlugintSDKPositiveTest {
	private static final Logger logger = Logger.getLogger(PlugintSDKPositiveTest.class);
	private final PlugintSDK sdk = new PlugintSDK("OP 3340","EC3C629D-CF23-4045-A07A-38A21D39AC16","https://api.training.myopenpay.com.au/v1/merchant","1.20210320");
	private static String orderId;

	/**
	 * Gets the configured minimum and maximum purchase price for orders with the
	 * authenticated retailer
	 */
	@Test
	public void ordersLimitsGetTest() {
		Map<String, Object> response;
		try {
			response = sdk.getPSPConfig(1);
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
	 * Requests creation of a new order. Request data is loaded from
	 * testData/getTokenPositive.json. After successful API call its automatically
	 * redirected to chrome browser to complete further purchase steps to thrid
	 * party by using redirect URL value from mappingApiConfig.ini.Once successful
	 * purchase is completed browser will be closed automatically after certain
	 * timeout value which is configured in merchantConfig.ini. The positive order
	 * id is stored in testData/testOrderPositive.txt and further calls are made to
	 * capture payment, check order status and do refund.
	 *
	 */

	@Test
	public void ordersPostTestPositive() {
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Wini configFileName = Helper.loadApiConfigFile();
			String createNewOrderJsonStr = Helper.readJson("testData/getTokenPositive.json");
			body = Util.convertStringToMap(createNewOrderJsonStr);
			assertNotNull("Converted request map cannot be null", body);
			Map<String, Object> response = sdk.getToken(body,1);
			assertNotNull("Response map for create new order cannot be null", response);
			String transactionToken = HelperTest.getTransactionTokenTest(response);
			String redirecturl = Helper.loadPropertyValue(configFileName, PlugintConstants.REDIRECTURL,
					PlugintConstants.REDIRECTURLSECTION) + transactionToken;
			HelperTest.redirectToBrowser(redirecturl);
			assertNotNull("order id cannot be null", response.get("orderId"));
			orderId = (String) response.get("orderId");
			Helper.usingBufferedWritter("testData/testOrderPositive.txt", orderId);
			logger.info(response);
			logger.info("Capturing payment");
			ordersOrderIdCapturePostTestPositive();
			ordersOrderIdGetTestPositive();
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Requests that the payment for an order is captured.
	 */
	public void ordersOrderIdCapturePostTestPositive() {
		try {
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			assertNotNull("Order Id cannot be null", orderId);
			String requestJson = Helper.readJson("testData/capturePayment.json");
			assertNotNull("Request body for capture payment cannot be null", Util.convertStringToMap(requestJson));
			Map<String, Object> response;
			response = sdk.capturePayment(orderId,Util.convertStringToMap(requestJson),1);
			assertNotNull("Response map for create new order cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Gets an order by order id.
	 */
	public void ordersOrderIdGetTestPositive() {
		try {
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			assertNotNull("Order Id cannot be null", orderId);
			Map<String, Object> response;
			response = sdk.updateShopOrder(orderId,1);
			assertNotNull("Response map for get orders cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}
}
