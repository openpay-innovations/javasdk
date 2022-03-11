package com.plugint.businessLayer.test;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.businessLayer.util.Util;

/**
 * Refund test class is created to do partial or full refund using positive test
 * data
 */
@RunWith(JUnit4.class)
public class PlugintSDKPositiveRefundTest {
	private static final Logger logger = Logger.getLogger(PlugintSDKPositiveRefundTest.class);

	/**
	 * Requests that an order is partially or fully refunded.
	 *
	 */
	@Test
	public void ordersOrderIdRefundPostTestPositive() {
		try {
			PlugintSDK sdk = new PlugintSDK("OP 3340","EC3C629D-CF23-4045-A07A-38A21D39AC16","https://api.training.myopenpay.com.au/v1/merchant","1.20210320");
			String orderId = Helper.readText("testData/testOrderPositive.txt");
			String requestJson = Helper.readJson("testData/getRefundPositive.json");
			assertNotNull("Request body for refund cannot be null", Util.convertStringToMap(requestJson));
			assertNotNull("Order id for refund cannot be null", orderId);
			Map<String, Object> response;
			response = sdk.refund(Util.convertStringToMap(requestJson), orderId, 1);
			assertNotNull("Response map for refund cannot be null", response);
			logger.info(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

}
