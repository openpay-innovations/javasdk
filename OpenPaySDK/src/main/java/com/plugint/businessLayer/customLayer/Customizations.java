package com.plugint.businessLayer.customLayer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import com.plugint.businessLayer.constant.PlugintConstants;
import com.plugint.businessLayer.core.ControllerIni;
import com.plugint.businessLayer.core.Helper;

/**
 * 
 * Customization class is used to handle all custom logics to be done while
 * creating request like price formatting
 *
 */
public class Customizations {

	/**
	 * Function to customize Price related data present in Shop system object.This
	 * function will fetch key priceFormattingFields from mappingShopConfig.ini
	 * which contains all price related keys with comma separator and compare with
	 * shopMappingValue, if result is true updatePrice function is called
	 * 
	 * @param shopSystemRequestMapValue : It represents value of each
	 *                                  shopMappingValue key from java.util.Map
	 *                                  Object coming from shop system
	 * @param shopMappingValue          : Represents value of each row of
	 *                                  mappingShopConfig.ini file
	 * @return java.lang.Object: It returns formatted price object OR it returns
	 *         input object in case of Price related data is not present.
	 * @throws IOException if config file is not loaded successfully
	 */
	public static Object requestCustomization(Object shopSystemRequestMapValue, String shopMappingValue)
			throws IOException {
		Wini configShopFile = ControllerIni.loadPropertyFile(PlugintConstants.MAPPING_SHOP_FILE);
		Helper.isNotNull(configShopFile);
		Helper.isNotNull(shopSystemRequestMapValue);
		Helper.isNotEmpty(shopMappingValue);
		String priceFormattingKeys = Helper.loadPropertyValue(configShopFile, "priceFormattingFields",
				"PriceFormattingFields");
		Helper.isNotEmpty(priceFormattingKeys);
		String[] priceFormattingKeysArrays = priceFormattingKeys.split("\\,");
		Helper.isNotNull(priceFormattingKeysArrays);
		List<String> priceFormattingKeysArrayList = Arrays.asList(priceFormattingKeysArrays);
		Helper.isNotNull(priceFormattingKeysArrayList);
		if (priceFormattingKeysArrayList.contains(shopMappingValue)) {
			return updatePrice(shopSystemRequestMapValue, configShopFile);
		}
		return shopSystemRequestMapValue;
	}

	/**
	 * Function to update price data by multiple it with denomination value present
	 * in mappingShopConfig.ini
	 * 
	 * @param shopSystemRequestMapValue: It represents value of each
	 *                                   shopMappingValue key from java.util.Map
	 *                                   Object coming from shop system
	 * @param configShopFile             : It represent mappingShopConfig.ini file
	 *                                   Object
	 * @return updated price value
	 */
	public static Object updatePrice(Object shopSystemRequestMapValue, Wini configShopFile) {
		Helper.isNotNull(configShopFile);
		Helper.isNotNull(shopSystemRequestMapValue);
		String priceDenominationValue = Helper.loadPropertyValue(configShopFile, "denominationValue",
				"PriceFormattingFields");
		Helper.isNotEmpty(priceDenominationValue);
		float priceToBeUpdated = Float.parseFloat(shopSystemRequestMapValue.toString()) * Integer.parseInt(priceDenominationValue);
		Helper.isNotNull(priceToBeUpdated);
		return  priceToBeUpdated;
	}

}
