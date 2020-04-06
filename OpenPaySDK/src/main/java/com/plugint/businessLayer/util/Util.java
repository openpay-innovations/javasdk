package com.plugint.businessLayer.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.features.CapturePayment;

/*
 * Utility class 
 */
public class Util {
	
	private static final Logger logger = Logger.getLogger(Util.class);
	/*
	 * function to convert object to map
	 * 
	 * @param java.lang.Object
	 * 
	 * @return java.util.Map
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> convertObjectToMap(Object objectToBeConverted) {
		try {
		Helper.isNotNull(objectToBeConverted);
		ObjectMapper oMapper = new ObjectMapper();
		return oMapper.convertValue(objectToBeConverted, LinkedHashMap.class);
		}catch(Exception e)
		{
			logger.error("Exception occured while converting ObjectToMap", e.getCause());
			logger.error("Exception occurred in convertObjectToMap - ", e);
		}
		return null;

	}

	/*
	 * Convert Map to Object
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.lang.Object
	 */
	public static Object convertMapToObject(Map<String, Object> mapToBeConverted) {
		try {
		Helper.isNotNull(mapToBeConverted);
		ObjectMapper oMapper = new ObjectMapper();
		if (mapToBeConverted.isEmpty()) {
			return null;
		}
		return oMapper.convertValue(mapToBeConverted, Object.class);
		}catch(Exception e)
		{
			logger.error("Exception occured while converting MapToObject", e.getCause());
			logger.error("Exception occurred in convertMapToObject - ", e);
		}
		return null;

	}

	/*
	 * Convert Map to JsonString
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.lang.String
	 */
	public static String convertMapToJsonString(Map<String, Object> mapToBeConverted) throws JsonProcessingException {
		Helper.isNotNull(mapToBeConverted);
		ObjectMapper oMapper = new ObjectMapper();
		if (mapToBeConverted.isEmpty()) {
			return null;
		}
		return oMapper.writeValueAsString(mapToBeConverted);

	}

	/*
	 * Convert String to map
	 * 
	 * @param String
	 * 
	 * @return java.util.Map
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

	/*
	 * Convert map to class
	 * 
	 * @param java.util.Map, java.lang.Class<?>
	 * 
	 * @return java.lang.Object
	 */
	public static Object convertMapToClass(Map<String, Object> mapToBeConverted, Class<?> finalClass) {
		try {
		Helper.isNotNull(mapToBeConverted);
		ObjectMapper objectMapper = new ObjectMapper();
		if (mapToBeConverted.isEmpty()) {
			return null;
		}
		return objectMapper.convertValue(mapToBeConverted, finalClass);
		}catch(Exception e)
		{
			logger.error("Exception occured while converting MapToClass", e.getCause());
			logger.error("Exception occurred in convertMapToClass - ", e);
		}
		return null;
	}

	/*
	 * Convert map to class
	 * 
	 * @param java.util.Map, java.lang.Class<?>
	 * 
	 * @return java.lang.Object
	 */
	public static Object convertJsonStringToClass(String stringToBeConverted, Class<?> finalClass)
			throws JsonParseException, JsonMappingException, IOException {
		Helper.isNotEmpty(stringToBeConverted);
		ObjectMapper objectMapper = new ObjectMapper();
		if (stringToBeConverted.isEmpty()) {
			return null;
		}
		return objectMapper.readValue(stringToBeConverted, finalClass);
	}

	/*
	 * Convert map to ObjectArray
	 * 
	 * @param java.util.Map
	 * 
	 * @return java.lang.Object[]
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
		}catch(Exception e)
		{
			logger.error("Exception occured while converting MapToObjectArray", e.getCause());
			logger.error("Exception occurred in convertMapToObjectArray - ", e);
		}
		return null;

	}

	/*
	 * Convert List to JsonString
	 * 
	 * @param java.util.List
	 * 
	 * @return String
	 */
	public static String convertListToString(List<String> listToBeConverted) throws IOException {
		Helper.isNotNull(listToBeConverted);
		ObjectMapper objectMapper = new ObjectMapper();
		if (listToBeConverted.isEmpty()) {
			return null;
		}
		return objectMapper.writeValueAsString(listToBeConverted);

	}

	/*
	 * Convert String to JSONObject
	 * 
	 * @param String
	 * 
	 * @return JSONObject
	 */
	public static JSONArray convertStringToJsonObject(String stringToBeConverted) throws IOException, ParseException {
		Helper.isNotEmpty(stringToBeConverted);
		return new JSONArray(stringToBeConverted);

	}
}
