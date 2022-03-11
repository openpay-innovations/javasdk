package com.plugint.businessLayer.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.PlugintSDK;
import com.plugint.businessLayer.util.Util;
import com.plugint.client.ApiException;

/**
 * Post order test class is created to create new order request using negative
 * test data or wrong test data
 */
@RunWith(JUnit4.class)
public class PlugintSDKNegativeOrderPostTest {

	private static final Logger logger = Logger.getLogger(PlugintSDKNegativeOrderPostTest.class);
	private final PlugintSDK sdk = new PlugintSDK("OP 3340","EC3C629D-CF23-4045-A07A-38A21D39AC16","https://api.training.myopenpay.com.au/v1/merchant","1.20210320");

	/**
	 * Requests creation of a new order. This is negative test case to fail the
	 * creation of new order using wrong test data from
	 * testData/getTokenNegative.json
	 */
	@Test
	public void ordersPostTestNegative() {
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			String createNewOrderJsonStr = Helper.readJson("testData/getTokenNegative.json");
			body = Util.convertStringToMap(createNewOrderJsonStr);
			assertNotNull("Converted request map cannot be null", body);
			Map<String, Object> response = sdk.getToken(body,1);
			assertNotNull("Response map for create new order cannot be null", response);
			logger.info("Response with error body due to wrong test data" + response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e);
		}
	}

}
