package com.spring.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.ArrayList;

public class ProductBeanUtils {

	// 抓出欄位資料為空的欄位名稱，組合成一個字串陣列返回給BeanUtils去除外複製
	public static String[] getNullPropertiesNames(Object source) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds =  beanWrapper.getPropertyDescriptors();
		// 設置陣列集合接收資料晚點再轉成陣列數組(因為要先定義大小現在使用不方便)
		List<String> nullPropertyNames = new ArrayList<>();
		for (PropertyDescriptor pd : pds) {
			String propertyName = pd.getName(); 
			if (beanWrapper.getPropertyValue(propertyName) == null) {
				nullPropertyNames.add(propertyName);
			}
			if (propertyName.equals("price") || propertyName.equals("cid")) {
				if (Integer.parseInt(beanWrapper.getPropertyValue(propertyName).toString()) == 0) {
					nullPropertyNames.add(propertyName);
				}
			}
		}
		return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
	}
	
}
