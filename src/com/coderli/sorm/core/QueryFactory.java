package com.coderli.sorm.core;

/**
 * 创建Query对象的工厂类
 * 
 * @author lishichun
 *
 */
public class QueryFactory {

	private static QueryFactory factory = new QueryFactory();
	/**
	 * 原型对象
	 */
	private static Query prototypeObj;
	static {
		
		
		// 加类指定的query类
		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass());
			prototypeObj = (Query) c.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 私有构造器
	 */
	private QueryFactory() {
	}

	
	public static Query createQuery() {
		try {
			return (Query) prototypeObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
