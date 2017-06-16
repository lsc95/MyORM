package com.coderli.sorm.bean;

/**
 * 管理配置信息
 * 
 * @author lishichun
 *
 */
public class Configuration {
	/**
	 * 驱动类
	 */
	private String driver;
	/**
	 * JDBC的Url
	 */
	private String url;
	/**
	 * 数据库用户名
	 */
	private String user;
	/**
	 * 数据库密码
	 */
	private String pwd;
	/**
	 * 正在使用的数据库的类型
	 */
	private String usingDb;
	/**
	 * 项目的源码路径
	 */
	private String srcPath;
	/**
	 * 扫描生成java类的包
	 */
	private String poPackage;
	/**
	 * 项目使用查询类的路劲
	 */
	private String queryClass;
	/**
	 * 连接池中最小连接数
	 */
	private int poolMinSize;
	/**
	 * 连接池中最大连接数
	 */
	private int poolMaxSize;

	public Configuration() {
		// TODO Auto-generated constructor stub
	}

	public Configuration(String driver, String url, String user, String pwd, String usingDb, String srcPath,
			String poPackage) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingDb = usingDb;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
	}

	public int getPoolMinSize() {
		return poolMinSize;
	}

	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}

	public int getPoolMaxSize() {
		return poolMaxSize;
	}

	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}

	public String getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsingDb() {
		return usingDb;
	}

	public void setUsingDb(String usingDb) {
		this.usingDb = usingDb;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getPoPackage() {
		return poPackage;
	}

	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}

}
