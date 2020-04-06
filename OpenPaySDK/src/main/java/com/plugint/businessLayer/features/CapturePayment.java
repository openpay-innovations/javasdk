package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

public class CapturePayment {
	
	private static final Logger logger = Logger.getLogger(CapturePayment.class);

	/*
	 * Call getorders function from SDK
	 * @param java.lang.Class<?>,java.lang.Object
	 * @return java.lang.Object
	 */
	public static Object capturePayment(Class<?> sdkClass, Object orderId) throws Exception
	{
		Helper.isNotNull(orderId);
		Wini configFile = Helper.loadApiConfigFile();
		String sdkClassString = ControllerIni.loadPropertyValue(configFile,
				PlugintConstants.CAPTURE_PAYMENT_REQUEST_MODEL, PlugintConstants.API_MODELS);
		Class<?> requestBodyClass = Class
				.forName(sdkClassString);
		String capturePaymentDeclaredMethod = ControllerIni.loadPropertyValue(configFile,
				PlugintConstants.CAPTURE_PAYMENT_METHOD, PlugintConstants.METHODS);
		Method capturePaymentMethod = sdkClass.getDeclaredMethod(capturePaymentDeclaredMethod,requestBodyClass);
		return capturePaymentMethod.invoke(sdkClass.newInstance(),orderId);
	}

}
