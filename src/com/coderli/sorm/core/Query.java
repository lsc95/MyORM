package com.coderli.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coderli.sorm.bean.ColumnInfo;
import com.coderli.sorm.bean.TableInfo;
import com.coderli.sorm.utils.JDBCUtil;
import com.coderli.sorm.utils.ReflectUtils;

/**
 * 负责查询，对外提供服务的核心类
 * @author lishichun
 *
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	/**
	 * 采用摸板方法模式将JDBC操作封装成摸板，便于重用
	 * @param sql sql语句
	 * @param params sql参数
	 * @param clazz 记录要封装到java类
	 * @param back  回调方法
	 * @return 结果
	 */
	public Object executeQueryTemplate(String sql,Object[] params,Class clazz,CallBack back){
		Connection conn = DBManager.getConnection();
		//存放查询结果的容器
		List list = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps=conn.prepareStatement(sql);
			//给sql设置参数
			JDBCUtil.handleParams(ps, params);
			rs=ps.executeQuery();
			
			return back.doExcute(conn, ps, rs);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			DBManager.close(rs,ps,conn);
		}
	}
	
	/**
	 * 直接执行一个DML语句
	 * @param sql sql语句
	 * @param params 参数
	 * @return 这个语句执行sql以后影响记录的行数
	 */
	public int executeDML(String sql,Object[] params){
		Connection conn = DBManager.getConnection();
		int count = 0;
		PreparedStatement ps = null;
		try {
			ps=conn.prepareStatement(sql);
			//给sql设置参数
			JDBCUtil.handleParams(ps, params);
			
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(ps, conn);
		}
		
		return count;
	}
	/**
	 * 把一个对象存储到数据库中
	 * 把对象中不为null的属性往数据库中存储
	 * @param obj 要存储的对象
	 */
	public void insert(Object obj){
		Class c = obj.getClass();
		//存储sql的参数对象
		List<Object> params = new ArrayList<>();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");
		//计算不为空的属性值
		int countNotNullField = 0;
		Field[] fs = c.getDeclaredFields();
		for(Field f:fs){
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			if(fieldValue!=null){
				countNotNullField ++;
				sql.append(fieldName+",");
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values (");
		for(int i=0;i<countNotNullField;i++){
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1, ')');
		
		executeDML(sql.toString(), params.toArray());
	}
	/**
	 * 删除clazz表示类对应表中的记录（指定主键id的值）
	 * @param clazz 跟表对应的类的Clazz对象
	 * @param id 主键的值
	 */
	public void delete(Class clazz,Object id){
		//通过Class找到TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		//获得主键
		ColumnInfo onlyProKy = tableInfo.getOnlyPrikey();
		String sql = "delete from "+tableInfo.getTname()+" where "+onlyProKy.getName()+"=?";
		executeDML(sql, new Object[]{id});
	}
	/**
	 * 删除对象在数据库中对应的记录(对象所在类对应到表，对象主键值对应到记录)
	 * @param obj 需要删除的记录对应的对象
	 */
	public void delete(Object obj){
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		//获得主键
		ColumnInfo onlyProKy = tableInfo.getOnlyPrikey();
		
		//通过反射机制调用对应get方法或者set方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyProKy.getName(), obj);
		delete( c,  priKeyValue);
	}
	
	/**
	 * 更新对象对应的记录，并且只更新指定的字段的值
	 * @param obj 需要跟新的对象
	 * @param fieldName 更新的属性列表
	 * @return
	 */
	public int update(Object obj,String[] fieldName){
		Class c = obj.getClass();
		//存储sql的参数对象
		List<Object> params = new ArrayList<>();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		//获得唯一的主键
		ColumnInfo priKey = tableInfo.getOnlyPrikey();
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
		for(String fname:fieldName){
			Object fvalue= ReflectUtils.invokeGet(fname, obj);
			params.add(fvalue);
			sql.append(fname+"=?,");
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append(" where ");
		sql.append(priKey.getName()+"=? ");
		//主键的值
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));
		return executeDML(sql.toString(), params.toArray());
	}
	
	
	/**
	 * 查询返回多行记录，并将每行记录封装到clazz对应类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的javabean对应的Class对象
	 * @param prams sql的参数
	 * @return 查询到的结果
	 */
	public List queryRows(final String sql, final Class clazz,final Object[] params) {
		

		return (List)executeQueryTemplate(sql, params, clazz, new CallBack() {

			@Override
			public Object doExcute(Connection conn, PreparedStatement ps, ResultSet rs) {
				// 存放查询结果的容器
				List list = null;
				try {
					ResultSetMetaData metaData = rs.getMetaData();
					// 多行
					while (rs.next()) {
						if(list==null){
							list = new ArrayList<>();
						}
						// 调用javabean的无参构造器
						Object rowObject = clazz.newInstance();
						// 多列
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							String columnName = metaData.getColumnLabel(i + 1);
							Object columnValue = rs.getObject(i + 1);
							// 调用rowObj对象的setUserName方法，将columnValue的值设置进去
							ReflectUtils.invokeSet(rowObject, columnName, columnValue);
						}
						list.add(rowObject);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return list;
			}
		});
	}
	

	/**
	 * 查询返回一行记录，并将记录封装到clazz对应类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的javabean对应的Class对象
	 * @param prams sql的参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRows(String sql,Class clazz,Object[] params){
		List list = queryRows(sql, clazz, params);
		return (list==null&&list.size()>=0)?null:list.get(0);
	}

	/**
	 * 查询返回一个值（一行一列），并将该值返回
	 * @param sql 查询sql
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql, Object[] params) {
		return executeQueryTemplate(sql, params, null, new CallBack() {

			@Override
			public Object doExcute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while (rs.next()) {
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return value;
			}
		});
	}
	
	/**
	 * 查询返回一个数字（一行一列），并将该值返回
	 * @param sql 查询sql
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Number queryNumber(String sql,Object[] params){
		return (Number)queryValue(sql, params);
	}
	/**
	 * 分页查询
	 * @param pageNum 第几页数据
	 * @param size 每页显示多少记录
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum,int size);
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
