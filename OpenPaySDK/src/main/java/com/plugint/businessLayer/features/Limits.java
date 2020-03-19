package com.plugint.businessLayer.features;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.PlugintSDK;

/*
 * Limits class to call getLimits API to configure min and max values
 */
public class Limits {
	
	private static final Logger logger = Logger.getLogger(Limits.class);
	
	/*
	 * Call getLimits function from SDK
	 * @param java.lang.Class<?>
	 * @return java.lang.Object
	 */
	public static Object getLimits(Class<?> sdkClass) throws Exception
	{
		String declaredMethod = ControllerIni.loadPropertyValue(ControllerIni.loadPropertyFile(PlugintConstants.CONFIG_FILE),
				PlugintConstants.LIMIT_METHOD, PlugintConstants.METHODS);
		Method limitMethod = sdkClass.getDeclaredMethod(declaredMethod);
		return limitMethod.invoke(sdkClass.newInstance());
	}

}
