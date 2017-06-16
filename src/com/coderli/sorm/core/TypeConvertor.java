package com.coderli.sorm.core;

/**
 * 负责java类型和数据类型互相转换
 * @author lishichun
 *
 */
public interface TypeConvertor {
	/**
	 * 将数据库数据类型转换为java数据类型
	 * @param columnType 数据库字段的数据类型
	 * @return java的数据类型
	 */
	public String databaseTypeToJavaType(String columnType);
	/**
	 * 将java数据类型转换为对应的数据库类型
	 * @param javaDatabaseType java数据库类型
	 * @return 数据库数据类型
	 */
	public String javaTypeToDatabaseType(String javaDatabaseType);
}
