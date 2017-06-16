package com.coderli.sorm.core;

import java.util.List;

/**
 * 负责查询，对外提供服务的核心类
 * @author lishichun
 *
 */
@SuppressWarnings("all")
public interface Query {
	/**
	 * 直接执行一个DML语句
	 * @param sql sql语句
	 * @param params 参数
	 * @return 这个语句执行sql以后影响记录的行数
	 */
	public int executeDML(String sql,Object[] params);
	/**
	 * 把一个对象存储到数据库中
	 * 把对象中不为null的属性往数据库中存储
	 * @param obj 要存储的对象
	 */
	public void insert(Object obj);
	/**
	 * 删除clazz表示类对应表中的记录（指定主键id的值）
	 * @param clazz 跟表对应的类的Clazz对象
	 * @param id 主键的值
	 */
	public void delete(Class clazz,Object id);
	/**
	 * 删除对象在数据库中对应的记录(对象所在类对应到表，对象主键值对应到记录)
	 * @param obj 需要删除的记录对应的对象
	 */
	public void delete(Object obj);
	
	/**
	 * 更新对象对应的记录，并且只更新指定的字段的值
	 * @param obj 需要跟新的对象
	 * @param fieldName 更新的属性列表
	 * @return
	 */
	public int update(Object obj,String[] fieldName);
	
	
	/**
	 * 查询返回多行记录，并将每行记录封装到clazz对应类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的javabean对应的Class对象
	 * @param prams sql的参数
	 * @return 查询到的结果
	 */
	public List queryRows(String sql,Class clazz,Object[] prams);
	

	/**
	 * 查询返回一行记录，并将记录封装到clazz对应类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的javabean对应的Class对象
	 * @param prams sql的参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRows(String sql,Class clazz,Object[] prams);

	/**
	 * 查询返回一个值（一行一列），并将该值返回
	 * @param sql 查询sql
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql,Object[] params);
	
	/**
	 * 查询返回一个数字（一行一列），并将该值返回
	 * @param sql 查询sql
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Number queryNumber(String sql,Object[] params);
}
