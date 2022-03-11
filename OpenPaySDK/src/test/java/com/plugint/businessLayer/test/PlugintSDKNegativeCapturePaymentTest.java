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
import com.plugint.client.ApiException;

/**
 * Capture payment test class is created to test capture payment api using
 * negative test data or wrong test data
 */
@RunWith(JUnit4.class)
public class PlugintSDKNegativeCapturePaymentTest {

	private static final Logger logger = Logger.getLogger(PlugintSDKNegativeCapturePaymentTest.class);
	private final PlugintSDK sdk = new PlugintSDK();

	/**
	 * Requests that the payment for an order is captured.Testing negative test case
	 *
	 * 
	 *
	 * @throws Exception if the Api call fails
	 */
	@Test
	public void ordersOrderIdCapturePostTestNegative() {
		try {
			String orderId = Helper.readText("testData/testOrderNegative.txt");
			Map<String, Object> response;
			response = sdk.capturePayment(orderId,1);
			assertNotNull("Response map for create new order cannot be null", response);
			logger.info("Response with error body due to wrong test data" + response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}
}
