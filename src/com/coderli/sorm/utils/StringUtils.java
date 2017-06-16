package com.coderli.sorm.utils;

/**
 * 封装字符串的常用操作
 * 
 * @author lishichun
 *
 */
public class StringUtils {
	/**
	 * 将目标字符串首字母变成大写
	 * @param str 目标字符串
	 * @return 首字母变为大写的字符串
	 */
	public static String firstCharToUpperCase(String str) {
		return str.toUpperCase().substring(0,1)+str.substring(1);
	}
}
