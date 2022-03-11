package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;
import com.plugint.businessLayer.core.PlugintSDK;

/**
 * Feature class Limits is created to call API method for SDK layer ,
 * and get the response back. This class is being called by
 * method getPSPConfig() of PlugintSDK.java
 * 
 */
public class Limits {
	
	private static final Logger logger = Logger.getLogger(Limits.class);
	
	/**
	 * This function calls API method from SDK layer. It is using java Reflection feature to load method
	 * name and number of parameters from [Method] section and [ApiModels] section
	 * mappingApiConfig.xml
	 * 
	 * @param sdkClass Global parameter set in PlugintSDK.java during
	 *                 authentication. Contains ClassName of API class present in
	 *                 SDK layer. Name of class is present in [ApiClass] section in
	 *                 mappingApiConfig.xml
	 * 
	 * @return Response Object coming from SDK layer
	 * 
	 * @throws Exception Generic exception is thrown if some issue occurred while
	 *                   sending request to SDK or while invoking methods
	 */
	public static Object getLimits(Class<?> sdkClass) throws Exception
	{
		String declaredMethod = Helper.loadPropertyValue(Helper.loadApiConfigFile(),
				PlugintConstants.LIMIT_METHOD, PlugintConstants.METHODS);
		Method limitMethod = sdkClass.getDeclaredMethod(declaredMethod);
		return limitMethod.invoke(sdkClass.newInstance());
	}

}
