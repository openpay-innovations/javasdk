package com.plugint.businessLayer.test;

import static org.junit.Assert.assertNotNull;

import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.businessLayer.util.Util;

/**
 * Refund test class is created to do partial or full refund using negative test
 * data or wrong test data
 */
@RunWith(JUnit4.class)
public class PlugintSDKNegativeGetRefundTest {
	private static final Logger logger = Logger.getLogger(PlugintSDKNegativeGetRefundTest.class);
	private final PlugintSDK sdk = new PlugintSDK();

	/**
	 * Requests that an order is partially or fully refunded. This is negative test
	 * case while fails the refund
	 *
	 * 
	 *
	 * @throws Exception if the Api call fails
	 */
	@Test
	public void ordersOrderIdRefundPostTestNegative() {
		try {
			String orderId = Helper.readText("testData/testOrderNegative.txt");
			String requestJson = Helper.readJson("testData/getRefundNegative.json");
			assertNotNull("Request body for refund cannot be null", Util.convertStringToMap(requestJson));
			Map<String, Object> response;
			response = sdk.refund(Util.convertStringToMap(requestJson), orderId,1);
			assertNotNull("Response map for refund cannot be null", response);
			logger.info("Response with error body due to wrong test data" + response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

}
