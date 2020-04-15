package com.plugint.businessLayer.features;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.json.simple.parser.ParseException;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.RequestCreaterHelper;
import com.plugint.businessLayer.util.Util;

/**
 * Feature class Tokenisation is created to call API method for SDK layer , send
 * request data and get the response back. This class is being called by method
 * GetToken() of PlugintSDK.java to create Request body for API and calling SDK
 * layer to get new token by creating new orders
 * 
 */
public class Tokenisation {

	private static final Logger logger = Logger.getLogger(Tokenisation.class);

	/**
	 * This function is used to create request body for Tokenisation call. It makes
	 * call to RequestCreaterHelper.createRequestBodyMap() with required parameters
	 * recursively to create request body in form of Map.
	 * 
	 * @param jsonMap This map is request map coming from shop system with all
	 *                attributes required to create request for Tokenisation call
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
		Wini apiFile = Helper.loadApiConfigFile();
		Section tokenisationList = ControllerIni.loadProperties(apiFile, PlugintConstants.CREATE_ORDER_SECTION);
		Set<String> tokenisationSet = tokenisationList.keySet();
		Iterator<String> iterateTokenisationSet = tokenisationSet.iterator();
		LinkedHashMap<String, Object> bodyMap = new LinkedHashMap();
		Wini shopFile = ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_SHOP_FILE);
		while (iterateTokenisationSet.hasNext()) {
			String apiKey = iterateTokenisationSet.next();
			String apiValue = tokenisationList.get(apiKey);
			RequestCreaterHelper.createRequestBodyMap(apiValue,
					ControllerIni.loadPropertyValue(shopFile, apiKey, "createOrder"), bodyMap, jsonMap);
		}
		if (bodyMap.isEmpty()) {
			return null;
		}
		logger.info("Request map created for tokenisation is" + bodyMap);
		return bodyMap;
	}

	/**
	 * This function calls API method from SDK layer based on request data coming
	 * from calling function. It is using java Reflection feature to load method
	 * name and number of parameters from [Method] section and [ApiModels] section
	 * mappingApiConfig.xml.
	 * 
	 * @param sdkClass Global parameter set in PlugintSDK.java during
	 *                 authentication. Contains ClassName of API class present in
	 *                 SDK layer. Name of class is present in [ApiClass] section in
	 *                 mappingApiConfig.xml
	 * 
	 * @param bodyMap  Request Map created for Tokenisation call using
	 *                 Tokenisation.createBody() method is converted to
	 *                 RequestModelClass using Reflections where name of class is
	 *                 stored in mappingApiConfig.ini under [ApiModels] section
	 * 
	 * @return Response Object coming from SDK layer
	 * 
	 * @throws Exception Generic exception is thrown if some issue occurred while
	 *                   sending request to SDK or while invoking methods
	 */
	public static Object sendTokenisationRequest(Class<?> sdkClass, Map<String, Object> bodyMap) throws Exception {
		Helper.isNotNull(bodyMap);
		Wini configFile = Helper.loadApiConfigFile();
		String sdkClassString = Helper.loadPropertyValue(configFile, PlugintConstants.TOKEN_REQUEST_MODEL,
				PlugintConstants.API_MODELS);
		Class<?> requestBodyClass = Class.forName(sdkClassString);
		String declaredMethodString = Helper.loadPropertyValue(configFile, PlugintConstants.TOKENISATION_METHOD,
				PlugintConstants.METHODS);
		Method tokenMethod = sdkClass.getDeclaredMethod(declaredMethodString, requestBodyClass);
		if (bodyMap.isEmpty()) {
			return null;
		}
		return tokenMethod.invoke(sdkClass.newInstance(), Util.convertMapToClass(bodyMap, requestBodyClass));
	}

}
