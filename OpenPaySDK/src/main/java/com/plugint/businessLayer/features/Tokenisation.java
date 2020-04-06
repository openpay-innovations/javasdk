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

/*
 * Feature class 
 */
public class Tokenisation {

	private static final Logger logger = Logger.getLogger(Tokenisation.class);

	/*
	 * Create body function to create body for tokenisations
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.util.Map
	 */
	public static Map<String, Object> createBody(Map<String, Object> jsonMap) throws IOException, ParseException {
		Helper.isNotNull(jsonMap);
		Wini apiFile = Helper.loadApiConfigFile();
		Section tokenisationList = ControllerIni.loadProperties(apiFile, "createOrder");
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

	/*
	 * Function to send getToken request to API
	 * 
	 * @param java.lang.Class<?>, java.Util.Map
	 * 
	 * @return java.lang.Object
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
