package com.coderli.sorm.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coderli.sorm.bean.ColumnInfo;
import com.coderli.sorm.bean.TableInfo;
import com.coderli.sorm.po.Emp;
import com.coderli.sorm.utils.JDBCUtil;
import com.coderli.sorm.utils.ReflectUtils;
import com.coderli.sorm.utils.StringUtils;
import com.coderli.sorm.vo.EmpVO;


/**
 * 负责针对MySql数据库的查询(对外提供服务的核心类)
 * @author lishichun
 *
 */
public class MySqlQuery implements Query {

	@Override
	public int executeDML(String sql, Object[] params) {
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

	@Override
	public void insert(Object obj) {
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

	@Override
	public void delete(Class clazz, Object id) {
		//通过Class找到TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		//获得主键
		ColumnInfo onlyProKy = tableInfo.getOnlyPrikey();
		
		String sql = "delete from "+tableInfo.getTname()+" where "+onlyProKy.getName()+"=?";
		executeDML(sql, new Object[]{id});
	}

	@Override
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		//获得主键
		ColumnInfo onlyProKy = tableInfo.getOnlyPrikey();
		
		//通过反射机制调用对应get方法或者set方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyProKy.getName(), obj);
		delete( c,  priKeyValue);
	}

	@Override
	public int update(Object obj, String[] fieldName) {
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

	@Override
	public List queryRows(String sql, Class clazz, Object[] params) {
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
			
			ResultSetMetaData metaData = rs.getMetaData();
			//多行
			while(rs.next()){
				if(list==null){
					list= new ArrayList<>();
				}
				//调用javabean的无参构造器
				Object rowObject = clazz.newInstance();
				//多列
				for(int i=0;i<metaData.getColumnCount();i++){
					String columnName= metaData.getColumnLabel(i+1);
					Object columnValue = rs.getObject(i+1);
					//调用rowObj对象的setUserName方法，将columnValue的值设置进去
					ReflectUtils.invokeSet(rowObject, columnName, columnValue);
				}
				list.add(rowObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBManager.close(rs,ps,conn);
		}
		
		return list;
	}

	@Override
	public Object queryUniqueRows(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list==null&&list.size()>=0)?null:list.get(0);
	}

	@Override
	public Object queryValue(String sql, Object[] params) {
		Connection conn = DBManager.getConnection();
		//存放查询结果的对象
		Object value = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps=conn.prepareStatement(sql);
			//给sql设置参数
			JDBCUtil.handleParams(ps, params);
			rs=ps.executeQuery();
			while(rs.next()){
				value = rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBManager.close(rs,ps,conn);
		}
		
		return value;
	}

	@Override
	public Number queryNumber(String sql, Object[] params) {
		return (Number)queryValue(sql, params);
	}
	
	public static void main(String[] args) {
//		Emp e = new Emp();
//		e.setEmpname("tom");
//		e.setId(1);
//		e.setAge(30);
//		e.setBirthday(new Date(System.currentTimeMillis()));
//		new MySqlQuery().update(e,new String[]{"empname","age"});
//		List<Emp> lists = new MySqlQuery().queryRows("select id,empname,age from emp ", Emp.class, null);
//		System.out.println(lists);
//		System.out.println("========================");
//		String sql="select e.id,e.empname,e.age,e.salary+e.bonus 'xinshui',d.dname 'deptName',d.address 'deptAddr' from emp e left join dept d on d.id=e.deptid;";
//		List<EmpVO> lists2 = new MySqlQuery().queryRows(sql, EmpVO.class, null);
//		System.out.println(lists2);
		Object obj = new MySqlQuery().queryValue("select count(*) from emp where salary>?" , new Object[]{100});
		System.out.println(obj);
	}
}
