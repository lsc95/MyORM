package com.coderli.sorm.core;

/**
 * MySql数据库类型和Java数据类型转换
 * 
 * @author lishichun
 *
 */
public class MySqlTypeConvertor implements TypeConvertor {

	@Override
	public String databaseTypeToJavaType(String columnType) {
		if ("varchar".equalsIgnoreCase(columnType) || "char".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("int".equalsIgnoreCase(columnType) || "tinyint".equalsIgnoreCase(columnType)
				|| "smallint".equalsIgnoreCase(columnType) || "integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		} else if ("double".equalsIgnoreCase(columnType) || "float".equalsIgnoreCase(columnType)) {
			return "Double";
		} else if ("clob".equalsIgnoreCase(columnType)) {
			return "java.sql.CLob";
		} else if ("blob".equalsIgnoreCase(columnType)) {
			return "java.sql.BLob";
		} else if ("date".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		} else if ("time".equalsIgnoreCase(columnType)) {
			return "java.sql.Time";
		} else if ("timestamp".equalsIgnoreCase(columnType)) {
			return "java.sql.Timestamp";
		}
		return null;
	}

	@Override
	public String javaTypeToDatabaseType(String javaDatabaseType) {

		return null;
	}

}
