package com.coderli.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.coderli.sorm.bean.ColumnInfo;
import com.coderli.sorm.bean.TableInfo;
import com.coderli.sorm.utils.JavaFileUtils;
import com.coderli.sorm.utils.StringUtils;

/**
 * 负责管理数据库所有表结构和类结构的关系，并可以根据表的结构生成类结构
 * 
 * @author lishichun
 *
 */
public class TableContext {
	/**
	 * 表名为key，表信息为value
	 */
	public static Map<String, TableInfo> tables = new HashMap<>();
	/**
	 * 将po的class的对象和表信息关联起来，便于重用
	 */
	public static Map<Class, TableInfo> poClassTableMap = new HashMap<>();

	private TableContext() {
	}

	static {
		try {
			// 初始化获得表信息
			Connection con = DBManager.getConnection();
			// 获得表的源信息
			DatabaseMetaData dbmd = con.getMetaData();

			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });

			while (tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");

				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
				tables.put(tableName, ti);

				// 查询表中所有的字段
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");
				while (set.next()) {
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}

				// 查询表中主键
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);
				while (set2.next()) {
					ColumnInfo ci2 = ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					// 设置为主键类型
					ci2.setKeyType(1);
					ti.getPriKeys().add(ci2);
				}

				// 取唯一主键，方便使用。如果是联合主键，则为空
				if (ti.getPriKeys().size() > 0) {
					ti.setOnlyPrikey(ti.getPriKeys().get(0));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新类结构
		updatePOFile();
		//加载po包下的所有类，便于重用，提高效率！
		loadPOTables();
	}
	/**
	 * 根据表结构，更新配置的po包下面的java类
	 * 实现了从表结构转换到类结构
	 */
	public static void updatePOFile(){
		Map<String,TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()){
			JavaFileUtils.createJavaPOFile(t,new MySqlTypeConvertor());
		}
	}
	
	/**
	 * 加载PO包下的类
	 */
	public static void loadPOTables(){
		for(TableInfo tableInfo:tables.values()){
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage()+"."+StringUtils.firstCharToUpperCase(tableInfo.getTname()));
				
				
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Map<String , TableInfo> tables = TableContext.tables;
		System.out.println(tables);
	}

}
