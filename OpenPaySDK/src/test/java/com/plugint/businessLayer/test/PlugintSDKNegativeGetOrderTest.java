package com.plugint.businessLayer.test;

import static org.junit.Assert.assertNotNull;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.client.ApiException;

/*
 * Test class with negative test cases
 */
@RunWith(JUnit4.class)
public class PlugintSDKNegativeGetOrderTest {

	private static final Logger logger = Logger.getLogger(PlugintSDKNegativeGetOrderTest.class);
	private final PlugintSDK sdk = new PlugintSDK();
	
	/**
	 * Gets an order by order id.
	 *
	 * 
	 *
	 * @throws ApiException if the Api call fails
	 */
	@Test
	public void ordersOrderIdGetTestNegative() {
		try {
			String orderId = Helper.readText("testData/testOrderNegative.txt");
			Map<String, Object> response;
			response = sdk.updateShopOrder(orderId);
			assertNotNull("Response map for get orders cannot be null", response);
			logger.info("Response with error body due to wrong test data" + response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}
}
