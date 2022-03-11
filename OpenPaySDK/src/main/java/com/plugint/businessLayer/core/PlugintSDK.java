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

/**
 * Business Layer Entry Class which contains all calls To SDK Request body is
 * created in each function and sending response back to calling function.
 */
public class PlugintSDK {
	private static final Logger logger = Logger.getLogger(PlugintSDK.class);
	static Class<?> sdkClass;

	/**
	 * Default constructor to authenticate api's. It makes call to
	 * doAuthentication() function to set basic authentication headers
	 */
	public PlugintSDK(String userName, String password, String endPointUrl, String version) {
		Helper.isNotEmpty(userName);
		Helper.isNotEmpty(password);
		Helper.isNotEmpty(endPointUrl);
		Helper.isNotEmpty(version);
		doAuthentication(userName,password,endPointUrl,version);
	}

	/**
	 * GetToken method creates request from @param cartData and make a call to api
	 * method in SDK layer. As this is a Generic method created to be used on
	 * multiple platforms, Api method ([Method] section) and class([ApiClass]
	 * section are loaded from mappingApiconfig.ini using reflections
	 * 
	 * Tokenisation method basically creates a new order request
	 *
	 * @param cartData      It is a Map object coming from calling function which
	 *                      contains relevant data required to make create new order
	 *                      call with key value pair map where keys can be relevant
	 *                      to the attributes.For creating new order you can send
	 *                      cart data object(if it contains all relevant information
	 *                      else send data based on shop system) in map along with
	 *                      other fields like callbackURL,failURL etc Please refer
	 *                      API document to check attributes related information
	 *                      Please refer API documentation for fields related
	 *                      information {key, value} : {String, Object}
	 * 
	 * @param attemptNumber Integer to be sent from calling function (of shop
	 *                      system) with constant value as 1.This attribute is used
	 *                      as counter to check number of attempts made for
	 *                      retrying.
	 *
	 * @return Response in form of map object. Calling function can use the response
	 *         map for further implementations
	 * 
	 * @throws Exception Generic Exception which is parent class and can handle
	 *                   multiple exceptions all together, main exception coming
	 *                   from API is custom Exception named as APIException
	 */
	public Map<String, Object> getToken(final Map<String, Object> cartData, int attemptNumber) throws Exception {
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
					int maxRetry = Helper.getMaxRetryValue();
					if (attemptNumber <= maxRetry) {
						logger.info("Retrying again due to timeout exception");
						attemptNumber++;
						return getToken(cartData, attemptNumber);
					} else {
						throw e;
					}
				} else {
					logger.error("Error code for getTokenisation is " + ((ApiException) e.getCause()).getCode());
					logger.error("Error response message for getTokenisation is "
							+ ((ApiException) e.getCause()).getResponseBody().toString());
					return Util.convertStringToMap(((ApiException) e.getCause()).getResponseBody());
				}
			}
			logger.error("Exception occurred in getToken method- ", e);
			return Util.convertStringToMap(e.getMessage());
		}

	}

	/**
	 * Refund method creates request from @param refundData @param orderId and make
	 * a call to api method in SDK layer. As this is a Generic method created to be
	 * used on multiple platforms, Api method ([Method] section) and
	 * class([ApiClass] section are loaded from mappingApiconfig.ini using
	 * reflections
	 *
	 * Refund method basically used to do refunds
	 *
	 * @param refundData    It is a Map object coming from calling function which
	 *                      contains relevant data required to make refund call like
	 *                      refundData with key value pair map where keys can be
	 *                      relevant to the attributes.Please refer API
	 *                      documentation for fields related information {key,
	 *                      value} : {String, Object}
	 * 
	 * @param orderId       Object contains orderId/id's to get refund for
	 *                      particular order. Refer API document to check data types
	 * 
	 * @param attemptNumber Integer to be sent from calling function (of shop
	 *                      system) with constant value as 1.This attribute is used
	 *                      as counter to check number of attempts made for
	 *                      retrying.
	 *
	 * @return Response in form of java.util.map object. Calling function can use
	 *         the response map for further implementations.Please check API
	 *         documentation for response details coming from API {key, value} :
	 *         {String, Object}
	 * 
	 * @throws Exception Generic Exception which is parent class and can handle
	 *                   multiple exceptions all together, main exception coming
	 *                   from API is custom Exception named as APIException
	 */
	public Map<String, Object> refund(final Map<String, Object> refundData, final Object orderId, int attemptNumber)
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
					int maxRetry = Helper.getMaxRetryValue();
					if (attemptNumber <= maxRetry) {
						logger.info("Retrying again due to timeout exception");
						attemptNumber++;
						return refund(refundData, orderId, attemptNumber);
					} else {
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

	/**
	 * GetPSPConfig method make a call to api method in SDK layer. As this is a
	 * Generic method created to be used on multiple platforms, Api method ([Method]
	 * section) and class([ApiClass] section are loaded from mappingApiconfig.ini
	 * using reflections
	 *
	 * GetPSPConfig method basically used to set price limits for purchase
	 * 
	 * @param attemptNumber Integer to be sent from calling function (of shop
	 *                      system) with constant value as 1.This attribute is used
	 *                      as counter to check number of attempts made for
	 *                      retrying.
	 *
	 * @return Response in form of map object. Calling function can use the response
	 *         map for further implementations
	 * 
	 * @throws Exception Generic Exception which is parent class and can handle
	 *                   multiple exceptions all together, main exception coming
	 *                   from API is custom Exception named as APIException
	 */
	public Map<String, Object> getPSPConfig(int attemptNumber) throws Exception {
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
					int maxRetry = Helper.getMaxRetryValue();
					if (attemptNumber <= maxRetry) {
						logger.info("Retrying again due to timeout exception");
						attemptNumber++;
						return getPSPConfig(attemptNumber);
					} else {
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

	/**
	 * UpdateShopOrder method creates request from @param orderId and make a call to
	 * api method in SDK layer. As this is a Generic method created to be used on
	 * multiple platforms, Api method ([Method] section) and class([ApiClass]
	 * section are loaded from mappingApiconfig.ini using reflections
	 *
	 * UpdateShopOrder basically update particular order in third party system
	 * 
	 * @param orderId       Object contains orderId/id's to get refund for
	 *                      particular order.Refer API document to check data types
	 * @param attemptNumber Integer to be sent from calling function (of shop
	 *                      system) with constant value as 1.This attribute is used
	 *                      as counter to check number of attempts made for
	 *                      retrying.
	 *
	 * @return Response in form of map object. Calling function can use the response
	 *         map for further implementations
	 * 
	 * @throws Exception Generic Exception which is parent class and can handle
	 *                   multiple exceptions all together, main exception coming
	 *                   from API is custom Exception named as APIException
	 */
	public Map<String, Object> updateShopOrder(final Object orderId, int attemptNumber) throws Exception {
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
					int maxRetry = Helper.getMaxRetryValue();
					if (attemptNumber <= maxRetry) {
						logger.info("Retrying again due to timeout exception");
						attemptNumber++;
						return updateShopOrder(orderId, attemptNumber);
					} else {
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

	/**
	 * CapturePayment method creates request from @param orderId and make a call to
	 * api method in SDK layer. As this is a Generic method created to be used on
	 * multiple platforms, Api method ([Method] section) and class([ApiClass]
	 * section are loaded from mappingApiconfig.ini using reflections
	 *
	 * Capture payment basically confirms captures the payment in third party system
	 * 
	 * @param orderId       Object contains orderId/id's to get refund for
	 *                      particular order.Refer API document to check data types
	 * @param captureData    It is a Map object coming from calling function which
	 *                      contains relevant data required to make capture call like
	 *                      captureData with key value pair map where keys can be
	 *                      relevant to the attributes.Please refer API
	 *                      documentation for fields related information {key,
	 *                      value} : {String, Object}
	 * @param attemptNumber Integer to be sent from calling function (of shop
	 *                      system) with constant value as 1.This attribute is used
	 *                      as counter to check number of attempts made for
	 *                      retrying.
	 *
	 * @return Response in form of map object. Calling function can use the response
	 *         map for further implementations
	 * 
	 * @throws Exception Generic Exception which is parent class and can handle
	 *                   multiple exceptions all together, main exception coming
	 *                   from API is custom Exception named as APIException
	 */
	public Map<String, Object> capturePayment(final Object orderId, final Map<String, Object> captureData, int attemptNumber) throws Exception {
		Helper.isNotNull(captureData);
		Helper.isNotNull(orderId);
		logger.info("CAPTURING PAYMENT");
		try {
			final Map<String, Object> bodyMap = CapturePayment.createBody(captureData);
			if (bodyMap.isEmpty()) {
				return null;
			}
			final Object response = CapturePayment.capturePayment(sdkClass, bodyMap, orderId);
			if (response == null) {
				return null;
			}
			logger.info("Response during capture payment is" + response);
			return Util.convertObjectToMap(response);
		} catch (final Exception e) {
			if (e.getCause() instanceof ApiException) {
				if (((ApiException) e.getCause()).getMessage().contains("java.net.SocketTimeoutException")) {
					int maxRetry = Helper.getMaxRetryValue();
					if (attemptNumber <= maxRetry) {
						logger.info("Retrying again due to timeout exception");
						attemptNumber++;
						return capturePayment(orderId, captureData, attemptNumber);
					} else {
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

	/**
	 * This method is basically called in constructor method to set authentication
	 * parameters in header for each API call. UserName and password and version are fetch from
	 * merchantConfig.ini
	 * 
	 * sdkClass is global param which sets the value API class to be called from SDK
	 * layer from [ApiClass] section of mappingAPIConfig.ini
	 *
	 */
	public static void doAuthentication(String userName, String password, String endPointUrl, String version) {
		try {
			BasicConfigurator.configure();
			Configuration.getDefaultApiClient().setBasePath(endPointUrl);
			final ApiClient defaultClient = Configuration.getDefaultApiClient();
			defaultClient.addDefaultHeader(PlugintConstants.OPENPAY_VERSION, version);
			// Configure HTTP basic authorization: Basic auth
			final HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("Basic auth");
			basicAuth.setUsername(userName);
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
