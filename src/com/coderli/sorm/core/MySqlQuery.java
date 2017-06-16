package com.coderli.sorm.core;



/**
 * 负责针对MySql数据库的查询(对外提供服务的核心类)
 * @author lishichun
 *
 */
public class MySqlQuery extends Query {

	@Override
	public Object queryPagenate(int pageNum, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		Object obj = new MySqlQuery().queryValue("select count(*) from emp", null);
		System.out.println(obj);
	}
}
