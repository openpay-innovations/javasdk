package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

/**
 * Feature class Orders is created to call API method for SDK layer ,
 * send request data and get the response back. This class is being called by
 * method updateShopOrder() of PlugintSDK.java
 * 
 */
public class Orders {
	
private static final Logger logger = Logger.getLogger(Orders.class);
	
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
 * @param orderId  Object sent from calling function is request param to be sent
 *                 to API method to check order status for particular order id
 *                 coming from shop system.
 * 
 * @return Response Object coming from SDK layer
 * 
 * @throws Exception Generic exception is thrown if some issue occurred while
 *                   sending request to SDK or while invoking methods
 */
	public static Object getOrders(Class<?> sdkClass, Object orderId) throws Exception
	{
		Helper.isNotNull(orderId);
		Wini configFile = Helper.loadApiConfigFile();
		String sdkClassString = Helper.loadPropertyValue(configFile,
				PlugintConstants.GET_ORDERS_REQUEST_MODEL, PlugintConstants.API_MODELS);
		Class<?> requestBodyClass = Class
				.forName(sdkClassString);
		Method orderMethod = sdkClass.getDeclaredMethod(
				Helper.loadPropertyValue(configFile,
						PlugintConstants.ORDERS_METHOD, PlugintConstants.METHODS),requestBodyClass);
		return orderMethod.invoke(sdkClass.newInstance(),orderId);
	}

}
