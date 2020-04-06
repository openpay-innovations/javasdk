package com.plugint.businessLayer.core;

/*
 * Load file from resources
 */
public class FileLoader {
	
	/*
	 * Load file from src/main/resources
	 * @param java.lang.String
	 * @return java.lang.String
	 */
	public String loadFileFromResources(String fileName)
	{
		Helper.isNotEmpty(fileName);
		return getClass().getClassLoader().getResource(fileName).getFile();
	}

}
