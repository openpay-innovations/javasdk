package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

public class Orders {
	
private static final Logger logger = Logger.getLogger(Orders.class);
	
	/*
	 * Call getorders function from SDK
	 * @param java.lang.Class<?>,java.lang.Object
	 * @return java.lang.Object
	 */
	public static Object getOrders(Class<?> sdkClass, Object orderId) throws Exception
	{
		Helper.isNotNull(orderId);
		String sdkClassString = ControllerIni.loadPropertyValue(ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE),
				PlugintConstants.GET_ORDERS_REQUEST_MODEL, PlugintConstants.API_MODELS);
		Class<?> requestBodyClass = Class
				.forName(sdkClassString);
		Method orderMethod = sdkClass.getDeclaredMethod(
				ControllerIni.loadPropertyValue(ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE),
						PlugintConstants.ORDERS_METHOD, PlugintConstants.METHODS),requestBodyClass);
		return orderMethod.invoke(sdkClass.newInstance(),orderId);
	}

}
