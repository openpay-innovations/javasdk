package com.plugint.businessLayer.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.util.Util;

/*
 * Core functionality class
 */
public class RequestCreaterHelper {

	private static final Logger logger = Logger.getLogger(RequestCreaterHelper.class);

	/*
	 * Create a request body map based on each configuration file value
	 * 
	 * @param String, String, java.util.Map, java.util.Map
	 * 
	 * @return java.util.Map
	 */
	@SuppressWarnings("unchecked")
	public static Object createRequestBodyMap(String apiValue, String shopMappingValue,
			LinkedHashMap<String, Object> bodyMap, Map<String, Object> reqJsonToMap)
			throws IOException, ParseException {
		Helper.isNotEmpty(apiValue);
		Helper.isNotNull(reqJsonToMap);
		String[] apiValueArrayOfString = apiValue.split("\\.");
		// End condition for recursion.(to set value in map use key for property)
		if (apiValueArrayOfString.length == 1) {
			if (bodyMap.containsKey(apiValueArrayOfString[0])) {
				bodyMap = new LinkedHashMap();
			}
			bodyMap.put(apiValueArrayOfString[0], extractDataFromShop(reqJsonToMap, shopMappingValue));
			return bodyMap;
		}
		String firstValFromApiValueArrayOfString = apiValueArrayOfString[0];
		ObjectMapper mapper = new ObjectMapper();
		if (firstValFromApiValueArrayOfString.equals(PlugintConstants.ARRAY_STRING)) {
			Object shopArrayObj = extractArrayDataFromShop(reqJsonToMap, shopMappingValue);
			String shopArrayObjString = mapper.writeValueAsString(shopArrayObj);
			JSONArray jsonarr = new JSONArray(shopArrayObjString);
			String afterArrayShopMappingValue = extractAfterArrayString(shopMappingValue);
			String afterArrayValue = extractAfterArrayString(apiValue);
			// Fetch items from body map
			LinkedHashMap<String, Object> arrayBodyMap = new LinkedHashMap();
			List<String> bodyMapList = new ArrayList();
			if (!bodyMap.isEmpty()) {
				String bodyMapStr = bodyMap.toString();
				bodyMapList = Arrays.asList(bodyMapStr);
			}
			ListIterator<String> bodyMapListIterator = bodyMapList.listIterator();
			List<Object> listOfBodyMap = new ArrayList();
			for (int i = 0; i < jsonarr.length(); i++) {
				String nestedMapString = jsonarr.get(i).toString();
				String bodyMapStr;
				if (bodyMapListIterator.hasNext()) {
					bodyMapStr = bodyMapListIterator.next();
					arrayBodyMap = Util.convertStringToMap(bodyMapStr);
				}
				Object bodyMapObject = createRequestBodyMap(afterArrayValue, afterArrayShopMappingValue, arrayBodyMap,
						Util.convertStringToMap(nestedMapString));
				listOfBodyMap.add(bodyMapObject);
			}
			return listOfBodyMap;
		}
		int apiValueIndex = apiValue.indexOf('.');
		String nestedString = apiValue.substring(apiValueIndex + 1);
		if (bodyMap.containsKey(firstValFromApiValueArrayOfString)) {
			Map<String, Object> arrayBodyMap = new HashMap();
			if (!nestedString.split("\\.")[0].equals(PlugintConstants.ARRAY_STRING)
					&& !(bodyMap.get(firstValFromApiValueArrayOfString) instanceof java.util.ArrayList)) {
				Object firstValObject = bodyMap.get(firstValFromApiValueArrayOfString);
				Object nestedbodyObject = createRequestBodyMap(nestedString, shopMappingValue,
						(LinkedHashMap<String, Object>) Util.convertObjectToMap(firstValObject), reqJsonToMap);
				bodyMap.replace(firstValFromApiValueArrayOfString, nestedbodyObject);
			} else {
				ArrayList firstValObject = (ArrayList) bodyMap.get(firstValFromApiValueArrayOfString);
				List<Object> objList = new ArrayList();
				Object shopArrayObj = extractArrayDataFromShop(reqJsonToMap, shopMappingValue);
				String shopArrayObjString = mapper.writeValueAsString(shopArrayObj);
				JSONArray jsonarr = new JSONArray(shopArrayObjString);
				String afterArrayShopMappingValue = extractAfterArrayString(shopMappingValue);
				String afterArrayValue = extractAfterArrayString(apiValue);
				Object bodyMapObject;
				for (int i = 0; i < jsonarr.length(); i++) {
					arrayBodyMap = (Map<String, Object>) firstValObject.get(i);
					String nestedMapString = jsonarr.get(i).toString();
					bodyMapObject = createRequestBodyMap(afterArrayValue, afterArrayShopMappingValue,
							(LinkedHashMap<String, Object>) arrayBodyMap, Util.convertStringToMap(nestedMapString));
					objList.add(bodyMapObject);
				}
				bodyMap.replace(firstValFromApiValueArrayOfString, objList);
			}

		} else {
			Object nestedbodyObject = createRequestBodyMap(nestedString, shopMappingValue,
					new LinkedHashMap<String, Object>(), reqJsonToMap);
			bodyMap.put(firstValFromApiValueArrayOfString, nestedbodyObject);
		}
		return bodyMap;
	}

	/*
	 * extracting values from shop object
	 * 
	 * @param java.util.Map, String
	 * 
	 * @return java.lang.Object
	 */
	public static Object extractDataFromShop(Map<String, Object> reqJsonToMap, String shopMappingValue) {
		try {
			if (shopMappingValue == null || shopMappingValue.isEmpty()) {
				return null;
			}
			String[] arrOfShop = shopMappingValue.split("\\.");
			for (int index = 0; index < arrOfShop.length; index++) {
				Object reqJsonToMapValue = reqJsonToMap.get(arrOfShop[index]);
				if (index + 1 == arrOfShop.length) {
					if (reqJsonToMapValue != null) {
						return reqJsonToMapValue;
					} else {
						return null;
					}
				} else {
					reqJsonToMap = Util.convertObjectToMap(reqJsonToMapValue);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while extracting data from shop", e.getCause());
			logger.error("Exception occurred in extractDataFromShop - ", e);

		}
		return null;
	}

	/*
	 * Extract array data from shop platform
	 * 
	 * @param java.util.Map,String
	 * 
	 * @return java.lang.Object
	 * 
	 */
	public static Object extractArrayDataFromShop(Map<String, Object> reqJsonToMap, String shopMappingValue) {
		try {
			if (shopMappingValue == null || shopMappingValue.isEmpty()) {
				return null;
			}
			String[] shoppingMapKeyArray = shopMappingValue.split("\\.");

			for (int index = 0; index < shoppingMapKeyArray.length; index++) {
				Object shoppingMapKeyObject = reqJsonToMap.get(shoppingMapKeyArray[index]);
				if (shoppingMapKeyArray[index + 1].equals(PlugintConstants.ARRAY_STRING)) {
					return shoppingMapKeyObject;
				}
				if (index + 1 == shoppingMapKeyArray.length) {
					if (shoppingMapKeyObject != null) {
						return shoppingMapKeyObject;
					} else {
						return null;
					}
				} else {
					reqJsonToMap = Util.convertObjectToMap(shoppingMapKeyObject);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while extracting array data from shop", e.getCause());
			logger.error("Exception occurred in extractArrayDataFromShop - ", e);
		}

		return null;
	}

	/*
	 * extractAfterArrayString
	 * 
	 * @param java.lang.String
	 * 
	 * @return java.lang.String
	 */
	public static String extractAfterArrayString(String shopMappingValue) {
		try {
			Helper.isNotEmpty(shopMappingValue);
			String arrayString = PlugintConstants.ARRAY_STRING;
			return shopMappingValue
					.substring(shopMappingValue.indexOf(PlugintConstants.ARRAY_STRING) + arrayString.length() + 1);
		} catch (Exception e) {
			logger.error("Exception occured while extract After Array String", e.getCause());
			logger.error("Exception occurred in extractAfterArrayString - ", e);
		}
		return null;
	}

}
