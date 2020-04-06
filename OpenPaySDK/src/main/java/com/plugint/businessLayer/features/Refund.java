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

/*
 * Refund feature class to create request
 */
public class Refund {

	private static final Logger logger = Logger.getLogger(Refund.class);

	/*
	 * Create body function to create body for tokenisations
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.util.Map
	 */
	public static Map<String, Object> createBody(Map<String, Object> jsonMap) throws IOException, ParseException {
		Helper.isNotNull(jsonMap);
		Section refundList = ControllerIni
				.loadProperties(Helper.loadApiConfigFile(), "Refund");
		Helper.isNotNull(refundList);
		Set<String> refundSet = refundList.keySet();
		Helper.isNotNull(refundSet);
		Iterator<String> refundSetItr = refundSet.iterator();
		LinkedHashMap<String, Object> bodyMap = new LinkedHashMap();
		Object bodymapObject = new Object();
		while (refundSetItr.hasNext()) {
			String refundKey = refundSetItr.next();
			String refundValue = refundList.get(refundKey);
			bodymapObject = RequestCreaterHelper.createRequestBodyMap(refundValue,
					ControllerIni.loadPropertyValue(ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_SHOP_FILE),
							refundValue, "Refund"),
					bodyMap, jsonMap);
		}
		bodyMap = Util.convertObjectToMap(bodymapObject);
		if (bodyMap.isEmpty()) {
			return null;
		}
		logger.info("request map created for refund is" + bodyMap.toString());
		return bodyMap;
	}

	/*
	 * function to call getRefund()
	 * 
	 * @param java.lang.Class<?>, java.util.Map, java.lang.Object
	 * 
	 * @return java.lang.Object
	 */
	public static Object sendRefundRequest(Class<?> sdkClass, Map<String, Object> bodyMap, Object orderId)
			throws Exception {
		Helper.isNotNull(bodyMap);
		Helper.isNotNull(orderId);
		Wini configFile = Helper.loadApiConfigFile();
		String requestParameterName = Helper.loadPropertyValue(
				configFile, PlugintConstants.REFUND_PARAMETER,
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
		Method refundMethod = sdkClass.getDeclaredMethod(
				Helper.loadPropertyValue(configFile,
						PlugintConstants.REFUND_METHOD, PlugintConstants.METHODS),
				params);
		Object requestBodyObject = Util.convertMapToClass(bodyMap,
				Class.forName(
						Helper.loadPropertyValue(configFile,
								PlugintConstants.REFUND_REQUEST_MODEL, PlugintConstants.API_MODELS)));
		Map<String, Object> paramsForInvocationMap = new HashMap();
		paramsForInvocationMap.put("orderId", orderId);
		paramsForInvocationMap.put("body", requestBodyObject);
		return refundMethod.invoke(sdkClass.newInstance(), Util.convertMapToObjectArray(paramsForInvocationMap));
	}
}
