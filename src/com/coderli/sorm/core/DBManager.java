package com.coderli.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.coderli.sorm.bean.Configuration;
import com.coderli.sorm.pool.DBConnPool;

/**
 * 根据配置文件的信息，维持连接对象的管理(增加连接池功能)
 * 
 * @author lishichun
 *
 */
public class DBManager {
	/**
	 * 配置信息
	 */
	private static Configuration conf;
	/**
	 * 连接池对象
	 */
	private static  DBConnPool pool =null;
	static {
		Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setDriver(props.getProperty("driver"));
		conf.setPoPackage(props.getProperty("poPackage"));
		conf.setPwd(props.getProperty("pwd"));
		conf.setSrcPath(props.getProperty("srcPath"));
		conf.setUrl(props.getProperty("url"));
		conf.setUser(props.getProperty("user"));
		conf.setUsingDb(props.getProperty("usingDb"));
		conf.setQueryClass(props.getProperty("queryClass"));
		conf.setPoolMinSize(Integer.parseInt(props.getProperty("poolMinSize")));
		conf.setPoolMaxSize(Integer.parseInt(props.getProperty("poolMaxSize")));
		/**
		 * 加载TableContext类
		 */
		System.out.println(TableContext.class);
	}
	/**
	 * 获得新的Connection对象
	 * @return 返回生成好的connection对象
	 */
	public static Connection getConnection() {
		if(pool==null){
			pool=new DBConnPool();
		}
		return pool.getConnection();
	}
	/**
	 * 创建新的Connection对象
	 * @return 返回生成好的connection对象
	 */
	public static Connection createConnection() {
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());//直接连接，后期增加连接池处理，提高效率
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 关闭传入的ResultSet，Statement，Connection
	 * @param rs
	 * @param ps
	 * @param con
	 */
	public static void close(ResultSet rs, Statement ps, Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pool.close(con);

	}
	/**
	 * 关闭传入的Statement，Connection
	 * @param ps
	 * @param con
	 */
	public static void close(Statement ps, Connection con) {

		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pool.close(con);

	}

	public static void close(Connection con) {
		pool.close(con);
	}
	/**
	 * 返回Configuration对象
	 * @return
	 */
	public static Configuration getConf(){
		return conf;
	}
}
