package com.plugint.businessLayer.core;

import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.ini4j.Wini;

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
	public Map<String, Object> getToken(final Map<String, Object> cartData, int maxRetry) throws Exception {
		Helper.isNotNull(cartData);
		logger.info("GET TOKEN Call BEGINS");
		try {
			final Map<String, Object> bodyMap = Tokenisation.createBody(cartData);
			if (bodyMap.isEmpty()) {
				return null;
			}
			final Object response = Tokenisation.sendTokenisationRequest(sdkClass, bodyMap);
			if (response == null) {
				return null;
			}
			logger.info("Response during create token is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					while (maxRetry > 0) {
						logger.info("Retrying again due to timeout exception");
						maxRetry--;
						return getToken(cartData, maxRetry);
					}
					if (maxRetry == 0) {
						throw e;
					}
				} else {
					logger.error("Error code for getTokenisation is " + ((ApiException) e.getCause()).getCode());
					logger.error("Error response message for getTokenisation is "
							+ ((ApiException) e.getCause()).getMessage());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
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
	public Map<String, Object> refund(final Map<String, Object> refundData, final Object orderId, int maxRetry)
			throws Exception {
		Helper.isNotNull(refundData);
		Helper.isNotNull(orderId);
		logger.info("REFUND CALL BEGINS");
		try {
			final Map<String, Object> bodyMap = Refund.createBody(refundData);
			if (bodyMap.isEmpty()) {
				return null;
			}
			final Object response = Refund.sendRefundRequest(sdkClass, bodyMap, orderId);
			if (response == null) {
				return null;
			}
			logger.info("Response during refund request is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					while (maxRetry > 0) {
						logger.info("Retrying again due to timeout exception");
						maxRetry--;
						return refund(refundData, orderId, maxRetry);
					}
					if (maxRetry == 0) {
						throw e;
					}
				} else {
					logger.error("Error code for refund is " + ((ApiException) e.getCause()).getCode());
					logger.error(
							"Error response message for refund is " + ((ApiException) e.getCause()).getResponseBody());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
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
	public Map<String, Object> getPSPConfig(int maxRetry) throws Exception {
		logger.info("LIMIT CONFIG CALL BEGINS");
		try {
			final Object response = Limits.getLimits(sdkClass);
			if (response == null) {
				return null;
			}
			logger.info("Response during get limits configured is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					while (maxRetry > 0) {
						logger.info("Retrying again due to timeout exception");
						maxRetry--;
						return getPSPConfig(maxRetry);
					}
					if (maxRetry == 0) {
						throw e;
					}
				} else {
					logger.error("Error code for getLimits is " + ((ApiException) e.getCause()).getCode());
					logger.error(((ApiException) e.getCause()).getMessage());
					logger.error("Error response message for getLimits is "
							+ ((ApiException) e.getCause()).getResponseBody());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
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
	public Map<String, Object> updateShopOrder(final Object orderId, int maxRetry) throws Exception {
		Helper.isNotNull(orderId);
		logger.info("UPDATING SHOP ORDERS");
		try {
			final Object response = Orders.getOrders(sdkClass, orderId);
			if (response == null) {
				return null;
			}
			logger.info("Response during order details is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					while (maxRetry > 0) {
						logger.info("Retrying again due to timeout exception");
						maxRetry--;
						return updateShopOrder(orderId, maxRetry);
					}
					if (maxRetry == 0) {
						throw e;
					}
				} else {
					logger.error("Error code for getOrders is " + ((ApiException) e.getCause()).getCode());
					logger.error(
							"Error response message for getOrders is " + ((ApiException) e.getCause()).getMessage());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
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
	public Map<String, Object> capturePayment(final Object orderId, int maxRetry) throws Exception {
		Helper.isNotNull(orderId);
		logger.info("CAPTURING PAYMENT");
		try {
			final Object response = CapturePayment.capturePayment(sdkClass, orderId);
			if (response == null) {
				return null;
			}
			logger.info("Response during capture payment is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					while (maxRetry > 0) {
						logger.info("Retrying again due to timeout exception");
						maxRetry--;
						return capturePayment(orderId, maxRetry);
					}
					if (maxRetry == 0) {
						throw e;
					}
				} else {
					logger.error("Error code for capturing payments is " + ((ApiException) e.getCause()).getCode());
					logger.error("Error response message for capturing payments is "
							+ ((ApiException) e.getCause()).getMessage());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
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
			final ApiClient defaultClient = Configuration.getDefaultApiClient();
			// Configure HTTP basic authorization: Basic auth
			final HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("Basic auth");
			final Wini configFile = ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE);
			final String userName = Helper.loadPropertyValue(configFile, PlugintConstants.USER,
					PlugintConstants.AUTHENTICATION);
			basicAuth.setUsername(userName);
			final String password = Helper.loadPropertyValue(configFile, PlugintConstants.PASSWORD,
					PlugintConstants.AUTHENTICATION);
			basicAuth.setPassword(password);
			final String sdkClassString = Helper.loadPropertyValue(Helper.loadApiConfigFile(),
					PlugintConstants.API_CLASS_KEY, PlugintConstants.API_CLASS_SECTION);
			sdkClass = Class.forName(sdkClassString);
		} catch (final Exception e) {
			logger.error("Exception occured during authentication");
			logger.error("Exception occurred in constructor while authenticating- ", e);
		}
	}
}
