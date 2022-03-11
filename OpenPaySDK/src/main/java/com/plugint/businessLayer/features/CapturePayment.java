package com.plugint.businessLayer.features;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.ini4j.Profile.Section;
import org.json.simple.parser.ParseException;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.RequestCreaterHelper;
import com.plugint.businessLayer.util.Util;

/**
 * Feature class Capture Payment is created to call API method for SDK layer ,
 * send request data and get the response back. This class is being called by
 * method capturePayment() of PlugintSDK.java
 * 
 */
public class CapturePayment {

	private static final Logger logger = Logger.getLogger(CapturePayment.class);
	
	/**
	 * This function is used to create request body for CapturePayment call. It makes call
	 * to RequestCreaterHelper.createRequestBodyMap() with required parameters
	 * recursively to create request body in form of Map.
	 * 
	 * @param jsonMap This map is request map coming from shop system with all
	 *                attributes required to create request for CapturePayment call
	 * 
	 * @return RequestBodyMap is created that is to be sent to API method in SDK
	 *         layer.
	 * 
	 * @throws IOException    Throws this exception if Property file is not loaded
	 *                        successfully
	 * @throws ParseException
	 */
	public static Map<String, Object> createBody(Map<String, Object> jsonMap) throws IOException, ParseException {
		Helper.isNotNull(jsonMap);
		Section CapturePaymentList = ControllerIni.loadProperties(Helper.loadApiConfigFile(), "Capture");
		Helper.isNotNull(CapturePaymentList);
		Set<String> capturePaymentSet = CapturePaymentList.keySet();
		Helper.isNotNull(capturePaymentSet);
		Iterator<String> capturePaymentSetItr = capturePaymentSet.iterator();
		LinkedHashMap<String, Object> bodyMap = new LinkedHashMap();
		Object bodymapObject = new Object();
		while (capturePaymentSetItr.hasNext()) {
			String capturePaymentKey = capturePaymentSetItr.next();
			String capturePaymentValue = CapturePaymentList.get(capturePaymentKey);
			bodymapObject = RequestCreaterHelper.createRequestBodyMap(capturePaymentValue,
					ControllerIni.loadPropertyValue(ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_SHOP_FILE),
							capturePaymentValue, "Capture"),
					bodyMap, jsonMap);
		}
		bodyMap = Util.convertObjectToMap(bodymapObject);
		if (bodyMap.isEmpty()) {
			return null;
		}
		logger.info("request map created for capture is" + bodyMap.toString());
		return bodyMap;
	}


	/**
	 * This function calls API method from SDK layer based on request data coming
	 * from calling function. It is using java Reflection feature to load method
	 * name and number of parameters from [Method] section and [ApiModels] section
	 * mappingApiConfig.xml
	 * 
	 * @param sdkClass Global parameter set in PlugintSDK.java during
	 *                 authentication. Contains ClassName of API class present in
	 *                 SDK layer. Name of class is present in [ApiClass] section in
	 *                 mappingApiConfig.xml
	 *              
	 * @param bodyMap  Request Map created for Refund API call using
	 *                 CapturePayment.createBody() method is converted to RequestModelClass
	 *                 using Reflections where name of class is stored in
	 *                 mappingApiConfig.ini under [ApiModels] section
	 * 
	 * @param orderId  Object sent from calling function is request param to be sent
	 *                 to API method to capture payment for particular order id
	 *                 coming from shop system.
	 * 
	 * @return Response Object coming from SDK layer
	 * 
	 * @throws Exception Generic exception is thrown if some issue occurred while
	 *                   sending request to SDK or while invoking methods
	 */
	public static Object capturePayment(Class<?> sdkClass, Map<String, Object> bodyMap, Object orderId) throws Exception {
		Helper.isNotNull(bodyMap);
		Helper.isNotNull(orderId);
		Wini configFile = Helper.loadApiConfigFile();
		String requestParameterName = Helper.loadPropertyValue(configFile, PlugintConstants.CAPTURE_PAYMENT_PARAMETER,
				PlugintConstants.API_MODELS);
		String[] arrayOfRequestParameterName = requestParameterName.split(",");
		Class<?>[] params = new Class[arrayOfRequestParameterName.length];
		if (arrayOfRequestParameterName.length == 1) {
			params[0] = Class.forName(arrayOfRequestParameterName[0]);
		} else {
			for (int i = 0; i < arrayOfRequestParameterName.length; i++) {
				params[i] = Class.forName(arrayOfRequestParameterName[i]);
			}
		}
		Method captureMethod = sdkClass.getDeclaredMethod(
				Helper.loadPropertyValue(configFile, PlugintConstants.CAPTURE_PAYMENT_METHOD, PlugintConstants.METHODS), params);
		Object requestBodyObject = Util.convertMapToClass(bodyMap, Class.forName(Helper.loadPropertyValue(configFile,
				PlugintConstants.CAPTURE_PAYMENT_REQUEST_MODEL, PlugintConstants.API_MODELS)));
		Map<String, Object> paramsForInvocationMap = new HashMap();
		paramsForInvocationMap.put("orderId", orderId);
		paramsForInvocationMap.put("body", requestBodyObject);
		return captureMethod.invoke(sdkClass.newInstance(), Util.convertMapToObjectArray(paramsForInvocationMap));
	}

}
