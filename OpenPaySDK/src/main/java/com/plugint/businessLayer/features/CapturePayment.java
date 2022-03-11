package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

/**
 * Feature class Capture Payment is created to call API method for SDK layer ,
 * send request data and get the response back. This class is being called by
 * method capturePayment() of PlugintSDK.java
 * 
 */
public class CapturePayment {

	private static final Logger logger = Logger.getLogger(CapturePayment.class);

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
	 *                 to API method to capture payment for particular order id
	 *                 coming from shop system.
	 * 
	 * @return Response Object coming from SDK layer
	 * 
	 * @throws Exception Generic exception is thrown if some issue occurred while
	 *                   sending request to SDK or while invoking methods
	 */
	public static Object capturePayment(Class<?> sdkClass, Object orderId) throws Exception {
		Helper.isNotNull(orderId);
		Wini configFile = Helper.loadApiConfigFile();
		String sdkClassString = ControllerIni.loadPropertyValue(configFile,
				PlugintConstants.CAPTURE_PAYMENT_REQUEST_MODEL, PlugintConstants.API_MODELS);
		Class<?> requestBodyClass = Class.forName(sdkClassString);
		String capturePaymentDeclaredMethod = ControllerIni.loadPropertyValue(configFile,
				PlugintConstants.CAPTURE_PAYMENT_METHOD, PlugintConstants.METHODS);
		Method capturePaymentMethod = sdkClass.getDeclaredMethod(capturePaymentDeclaredMethod, requestBodyClass);
		return capturePaymentMethod.invoke(sdkClass.newInstance(), orderId);
	}

}
