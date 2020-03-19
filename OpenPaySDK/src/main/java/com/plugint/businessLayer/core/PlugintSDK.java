package com.plugint.businessLayer.core;

import java.util.Map;
import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.features.CapturePayment;
import com.plugint.businessLayer.features.Limits;
import com.plugint.businessLayer.features.Orders;
import com.plugint.businessLayer.features.Refund;
import com.plugint.businessLayer.features.Tokenisation;
import com.plugint.businessLayer.util.Util;
import com.plugint.client.ApiClient;
import com.plugint.client.ApiException;
import com.plugint.client.Configuration;
import com.plugint.client.auth.HttpBasicAuth;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.ini4j.Wini;

/*
 * Base Core class to class Api Methods
 */
public class PlugintSDK {
	private static final Logger logger = Logger.getLogger(PlugintSDK.class);
	static Class<?> sdkClass;

	/*
	 * Default constructor to authenticate api's
	 */
	public PlugintSDK() {
		doAuthentication();
	}

	/*
	 * GetToken method to get token for creating new orders in payment gateway
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.lang.Object
	 */
	public Map<String, Object> getToken(Map<String, Object> cartData) throws Exception {
		Helper.isNotNull(cartData);
		logger.info("GET TOKEN Call BEGINS");
		try {
			Map<String, Object> bodyMap = Tokenisation.createBody(cartData);
			if (bodyMap.isEmpty()) {
				return null;
			}
			Object response = Tokenisation.sendTokenisationRequest(sdkClass, bodyMap);
			if (response == null) {
				return null;
			}
			return Util.convertObjectToMap(response);

		} catch (Exception e) {
			logger.error("Exception occured while sending tokenisation request - " + e.getMessage());
			if (e.getCause() instanceof ApiException) {
				logger.error("Error code for getTokenisation is " + ((ApiException) e.getCause()).getCode());
				logger.error(
						"Error response message for getTokenisation is " + ((ApiException) e.getCause()).getMessage());
				return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
			}
			logger.error("Exception occurred in getToken method- ", e);
			return Util.convertStringToMap(e.getMessage());
		}

	}

	/*
	 * Refund function to call refund Api
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.util.Map
	 */
	public Map<String, Object> refund(Map<String, Object> refundData, Object orderId) throws Exception {
		Helper.isNotNull(refundData);
		Helper.isNotNull(orderId);
		logger.debug("REFUND CALL BEGINS");
		try {
			Map<String, Object> bodyMap = Refund.createBody(refundData);
			if (bodyMap.isEmpty()) {
				return null;
			}
			Object response = Refund.sendRefundRequest(sdkClass, bodyMap, orderId);
			if (response == null) {
				return null;
			}
			return Util.convertObjectToMap(response);

		} catch (Exception e) {
			logger.error("Exception occured while sending refund request - " + e.getMessage());
			if (e.getCause() instanceof ApiException) {
				logger.error("Error code for refund is " + ((ApiException) e.getCause()).getCode());
				logger.error("Error response message for refund is " + ((ApiException) e.getCause()).getResponseBody());
				return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
			}
			logger.error("Exception occurred in refund method- ", e);
			return Util.convertStringToMap(e.getMessage());
		}
	}

	/*
	 * GetLimit Function
	 * 
	 * @return java.Util.Map
	 */
	public Map<String, Object> getPSPConfig() throws Exception {
		logger.info("LIMIT CONFIG CALL BEGINS");
		try {
			Object response = Limits.getLimits(sdkClass);
			if (response == null) {
				return null;
			}
			return Util.convertObjectToMap(response);
		} catch (Exception e) {
			logger.error("Exception occured while getting limits - " + e.getMessage());
			if (e.getCause() instanceof ApiException) {
				logger.error("Error code for getLimits is " + ((ApiException) e.getCause()).getCode());
				logger.error(((ApiException) e.getCause()).getMessage());
				logger.error(
						"Error response message for getLimits is " + ((ApiException) e.getCause()).getResponseBody());
				return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
			}
			logger.error("Exception occurred in getLimits function- ", e);
			return Util.convertStringToMap(e.getMessage());
		}

	}

	/*
	 * GetOrders Function
	 * 
	 * @return java.Util.Map
	 */
	public Map<String, Object> updateShopOrder(Object orderId) throws Exception {
		Helper.isNotNull(orderId);
		logger.info("UPDATING SHOP ORDERS");
		try {
			Object response = Orders.getOrders(sdkClass, orderId);
			if (response == null) {
				return null;
			}
			return Util.convertObjectToMap(response);
		} catch (Exception e) {
			logger.error("Exception occured while getting orders - " + e.getMessage());
			if (e.getCause() instanceof ApiException) {
				logger.error("Error code for getOrders is " + ((ApiException) e.getCause()).getCode());
				logger.error("Error response message for getOrders is " + ((ApiException) e.getCause()).getMessage());
				return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
			}
			logger.error("Exception occurred in getOrders function- ", e);
			return Util.convertStringToMap(e.getMessage());
		}
	}

	/*
	 * capture payment Function
	 * 
	 * @return java.Util.Map
	 */
	public Map<String, Object> capturePayment(Object orderId) throws Exception {
		Helper.isNotNull(orderId);
		logger.info("CAPTURING PAYMENT");
		try {
			Object response = CapturePayment.capturePayment(sdkClass, orderId);
			if (response == null) {
				return null;
			}
			return Util.convertObjectToMap(response);
		} catch (Exception e) {
			logger.error("Exception occured while capturing payments - " + e.getMessage());
			if (e.getCause() instanceof ApiException) {
				logger.error("Error code for capturing payments is " + ((ApiException) e.getCause()).getCode());
				logger.error("Error response message for capturing payments is "
						+ ((ApiException) e.getCause()).getMessage());
				return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
			}
			logger.error("Exception occurred in capture payment- ", e);
			return Util.convertStringToMap(e.getMessage());

		}
	}

	/*
	 * Method to do authentication
	 * 
	 */
	public static void doAuthentication() {
		try {
			BasicConfigurator.configure();
			ApiClient defaultClient = Configuration.getDefaultApiClient();
			// Configure HTTP basic authorization: Basic auth
			HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("Basic auth");
			Wini configFile = ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE);
			String userName = Helper.loadPropertyValue(configFile, PlugintConstants.USER,
					PlugintConstants.AUTHENTICATION);
			basicAuth.setUsername(userName);
			String password = Helper.loadPropertyValue(configFile, PlugintConstants.PASSWORD,
					PlugintConstants.AUTHENTICATION);
			basicAuth.setPassword(password);
			String sdkClassString = Helper.loadPropertyValue(configFile, PlugintConstants.API_CLASS_KEY,
					PlugintConstants.API_CLASS_SECTION);
			sdkClass = Class.forName(sdkClassString);
		} catch (Exception e) {
			logger.error("Exception occured during authentication");
			logger.error("Exception occurred in constructor while authenticating- ", e);
		}
	}
}
