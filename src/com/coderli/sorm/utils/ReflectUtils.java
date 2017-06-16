package com.coderli.sorm.utils;

import java.lang.reflect.Method;

/**
 * 封装反射常用的操作
 * 
 * @author lishichun
 *
 */
public class ReflectUtils {
	/**
	 * 调用obj对象对应属性的filedName的get方法
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static Object invokeGet(String fieldName,Object obj) {
		try {
			Class c = obj.getClass();
			Method m = c.getMethod("get" + StringUtils.firstCharToUpperCase(fieldName), null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 调用obj对象对应属性的filedName的set方法
	 * @param columnName 对应属性的名称
	 * @param columnValue 对应属性的值
	 * @param obj
	 * @return
	 */
	public static void invokeSet(Object obj,String columnName,Object columnValue) {
		try {
			Method m = obj.getClass().getDeclaredMethod("set" + StringUtils.firstCharToUpperCase(columnName), columnValue.getClass());
			m.invoke(obj, columnValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
