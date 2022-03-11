package com.plugint.businessLayer.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugint.businessLayer.core.Helper;

/**
 * Utility class created to handle all jackson conversions realted functions
 * like converting map to object or map to class and so on.
 */
public class Util {

	private static final Logger logger = Logger.getLogger(Util.class);

	/**
	 * Function to convert Object to Map. Generic object is converted to Map Collection
	 * 
	 * @param objectToBeConverted Sent by calling function, to be converted to Map
	 * 
	 * @return Map Collection is returned to calling function
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> convertObjectToMap(Object objectToBeConverted) {
		try {
			Helper.isNotNull(objectToBeConverted);
			ObjectMapper oMapper = new ObjectMapper();
			return oMapper.convertValue(objectToBeConverted, LinkedHashMap.class);
		} catch (Exception e) {
			logger.error("Exception occured while converting ObjectToMap", e.getCause());
			logger.error("Exception occurred in convertObjectToMap - ", e);
		}
		return null;

	}

	/**
	 * Function to convert Map to Object.  Map is converted to Generic object
	 * 
	 * @param mapToBeConverted Sent by calling function, to be converted to Generic object
	 * 
	 * @return Object is returned to calling function
	 */
	public static Object convertMapToObject(Map<String, Object> mapToBeConverted) {
		try {
			Helper.isNotNull(mapToBeConverted);
			ObjectMapper oMapper = new ObjectMapper();
			if (mapToBeConverted.isEmpty()) {
				return null;
			}
			return oMapper.convertValue(mapToBeConverted, Object.class);
		} catch (Exception e) {
			logger.error("Exception occured while converting MapToObject", e.getCause());
			logger.error("Exception occurred in convertMapToObject - ", e);
		}
		return null;

	}

	/**
	 * Function to convert Map to Json String.  Map is converted to Json String
	 * 
	 * @param mapToBeConverted Sent by calling function, to be converted to Json String
	 * 
	 * @return String in form of Json is returned to calling function 
	 * 
	 * @throws JsonProcessingException If Map data is not sent correctly, won't be converted to json string
	 */
	public static String convertMapToJsonString(Map<String, Object> mapToBeConverted) throws JsonProcessingException {
		Helper.isNotNull(mapToBeConverted);
		ObjectMapper oMapper = new ObjectMapper();
		if (mapToBeConverted.isEmpty()) {
			return null;
		}
		return oMapper.writeValueAsString(mapToBeConverted);

	}

	/**
	 * Function to convert String to Map.  String is converted to Linked Hash Map
	 * 
	 * @param stringToBeConverted Sent by calling function, to be converted to Linked Hash Map
	 * 
	 * @return LinkedHashMap is returned to calling function
	 * 
	 * @throws JsonParseException
	 * 
	 *			JsonMappingException
	 *	
	 *			IOException
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> convertStringToMap(String stringToBeConverted)
			throws JsonParseException, JsonMappingException, IOException {
		Helper.isNotEmpty(stringToBeConverted);
		ObjectMapper objectMapper = new ObjectMapper();
		if (stringToBeConverted.isEmpty()) {
			return null;
		}
		return (LinkedHashMap<String, Object>) objectMapper.readValue(stringToBeConverted, Map.class);
	}

	/**
	 * Function to convert Map to Class.  Map is converted to Class
	 * 
	 * @param mapToBeConverted Sent by calling function, to be converted to Class
	 * 
	 * @return Class is returned to calling function
	 */
	public static Object convertMapToClass(Map<String, Object> mapToBeConverted, Class<?> finalClass) {
		try {
			Helper.isNotNull(mapToBeConverted);
			ObjectMapper objectMapper = new ObjectMapper();
			if (mapToBeConverted.isEmpty()) {
				return null;
			}
			return objectMapper.convertValue(mapToBeConverted, finalClass);
		} catch (Exception e) {
			logger.error("Exception occured while converting MapToClass", e.getCause());
			logger.error("Exception occurred in convertMapToClass - ", e);
		}
		return null;
	}

	/**
	 * Function to convert Map to Object[].  Map is converted to Object[]
	 * 
	 * @param mapToBeConverted Sent by calling function, to be converted to Object[]
	 * 
	 * @return Object[] is returned to calling function
	 */
	public static Object[] convertMapToObjectArray(Map<String, Object> mapToBeConverted) {
		try {
			Helper.isNotNull(mapToBeConverted);
			Object objectArray[] = new Object[mapToBeConverted.size()];
			int i = 0;
			for (Map.Entry<String, Object> entry : mapToBeConverted.entrySet()) {
				objectArray[i] = entry.getValue();
				i++;
			}
			return objectArray;
		} catch (Exception e) {
			logger.error("Exception occured while converting MapToObjectArray", e.getCause());
			logger.error("Exception occurred in convertMapToObjectArray - ", e);
		}
		return null;

	}
}
