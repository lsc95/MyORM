package com.coderli.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.coderli.sorm.bean.Configuration;

/**
 * 根据配置文件的信息，维持连接对象的管理(增加连接池功能)
 * 
 * @author lishichun
 *
 */
public class DBManager {
	private static Configuration conf;

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
	}

	public static Connection getConnection() {
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
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void close(Statement ps, Connection con) {

		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void close(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 返回Configuration对象
	 * @return
	 */
	public static Configuration getConf(){
		return conf;
	}
}
