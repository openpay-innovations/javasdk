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
import com.plugint.businessLayer.customLayer.Customizations;
import com.plugint.businessLayer.util.Util;

/**
 * RequestCreaterHelper class contains methods which create Request body for
 * methods of SDK layer based on properties of mappingShopConfig.ini and
 * mappingApiConfig.ini
 */
public class RequestCreaterHelper {

	private static final Logger logger = Logger.getLogger(RequestCreaterHelper.class);

	/**
	 * Method created a request body key value pair map where key is attribute name
	 * of particular Request Model of API method and value is the value to be sent
	 * to API from shop.Its a recursive function which add each API key and value to
	 * the map.
	 * 
	 * @param apiValue             String coming from calling function which is the
	 *                             value of particular key from
	 *                             mappingApiConfig.ini. Example firstName =
	 *                             customerJourney.online.customerDetails.firstName
	 * 
	 * @param shopMappingValue     String coming from calling function which is the
	 *                             value of same key of apiValue from
	 *                             mappingShopConfig.ini. Example firstName =
	 *                             deliveryAddress.firstName
	 * 
	 * @param bodyMap              New Map object is sent to function which is set
	 *                             recursively with API value as key and value from
	 *                             reqJsonToMap
	 * 
	 * @param shopSystemRequestMap This Map is iterated and value is fetched for
	 *                             each shopMappingValue and set as value to @param
	 *                             bodyMap for particular key @param apiValue
	 * 
	 * @return BodyMap Object with key value pair of keys as attributes to
	 *         requestModel and value as value from shopData
	 * 
	 * @throws IOException    if property file is not loaded successfully
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Object createRequestBodyMap(String apiValue, String shopMappingValue,
			LinkedHashMap<String, Object> bodyMap, Map<String, Object> shopSystemRequestMap)
			throws IOException, ParseException {
		Helper.isNotEmpty(apiValue);
		Helper.isNotNull(shopSystemRequestMap);
		String[] apiValueArrayOfString = apiValue.split("\\.");
		if (apiValueArrayOfString.length == 1) {
			if (bodyMap.containsKey(apiValueArrayOfString[0])) {
				bodyMap = new LinkedHashMap();
			}
			bodyMap.put(apiValueArrayOfString[0], extractDataFromShop(shopSystemRequestMap, shopMappingValue));
			return bodyMap;
		}
		String firstValFromApiValueArrayOfString = apiValueArrayOfString[0];
		ObjectMapper mapper = new ObjectMapper();
		if (firstValFromApiValueArrayOfString.equals(PlugintConstants.ARRAY_STRING)) {
			Object shopArrayObj = extractArrayDataFromShop(shopSystemRequestMap, shopMappingValue);
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
						(LinkedHashMap<String, Object>) Util.convertObjectToMap(firstValObject), shopSystemRequestMap);
				bodyMap.replace(firstValFromApiValueArrayOfString, nestedbodyObject);
			} else {
				ArrayList firstValObject = (ArrayList) bodyMap.get(firstValFromApiValueArrayOfString);
				List<Object> objList = new ArrayList();
				Object shopArrayObj = extractArrayDataFromShop(shopSystemRequestMap, shopMappingValue);
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
					new LinkedHashMap<String, Object>(), shopSystemRequestMap);
			bodyMap.put(firstValFromApiValueArrayOfString, nestedbodyObject);
		}
		return bodyMap;
	}

	/**
	 * Method iterate @param reqJsonToMap map and get the value of particular
	 * key @param shopMappingValue
	 * 
	 * @param shopSystemRequestMap Map coming from shop system which contains
	 *                             relevant data used to create request body for API
	 *                             method
	 * 
	 * @param shopMappingValue     key coming from calling function which is sent
	 *                             to @param reqJsonToMap to get value
	 * 
	 * @return Generic Object from Map for particular key.
	 */
	public static Object extractDataFromShop(Map<String, Object> shopSystemRequestMap, String shopMappingValue) {
		try {
			if (shopMappingValue == null || shopMappingValue.isEmpty()) {
				return null;
			}
			String[] arrOfShop = shopMappingValue.split("\\.");
			for (int index = 0; index < arrOfShop.length; index++) {
				Object shopSystemRequestMapValue = shopSystemRequestMap.get(arrOfShop[index]);
				if (index + 1 == arrOfShop.length) {
					if (shopSystemRequestMapValue != null) {
						shopSystemRequestMapValue = Customizations.requestCustomization(shopSystemRequestMapValue,
								shopMappingValue);
						return shopSystemRequestMapValue;
					} else {
						return null;
					}
				} else {
					shopSystemRequestMap = Util.convertObjectToMap(shopSystemRequestMapValue);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while extracting data from shop", e.getCause());
			logger.error("Exception occurred in extractDataFromShop - ", e);

		}
		return null;
	}

	/**
	 * Method iterate @param reqJsonToMap map and get the value of particular
	 * key @param shopMappingValue by iterating array data present in map (if
	 * present). Example multiple product data in cart
	 * 
	 * @param shopSystemRequestMap Map coming from shop system which contains
	 *                             relevant data used to create request body for API
	 *                             method
	 * 
	 * @param shopMappingValue     key coming from calling function which is sent
	 *                             to @param reqJsonToMap to get value
	 * 
	 * @return Generic Object from Map for particular key.
	 */
	public static Object extractArrayDataFromShop(Map<String, Object> shopSystemRequestMap, String shopMappingValue) {
		try {
			if (shopMappingValue == null || shopMappingValue.isEmpty()) {
				return null;
			}
			String[] shoppingMapKeyArray = shopMappingValue.split("\\.");

			for (int index = 0; index < shoppingMapKeyArray.length; index++) {
				Object shoppingMapKeyObject = shopSystemRequestMap.get(shoppingMapKeyArray[index]);
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
					shopSystemRequestMap = Util.convertObjectToMap(shoppingMapKeyObject);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured while extracting array data from shop", e.getCause());
			logger.error("Exception occurred in extractArrayDataFromShop - ", e);
		}

		return null;
	}

	/**
	 * Function to extract Substring from @shopMappingValue after "array"
	 * 
	 * @param shopMappingValue String coming from calling function is the value of
	 *                         particular key from mappingShopConfig.ini
	 * 
	 *                         example for entries.array.product.name return
	 *                         product.name
	 * 
	 * @return Substring beinge extracted from @param shopMappingValue
	 * 
	 * 
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
