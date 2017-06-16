package com.coderli.sorm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储表结构的信息
 * 
 * @author lishichun
 *
 */
public class TableInfo {
	/**
	 * 表明
	 */
	private String tname;
	/**
	 * 所有字段信息
	 */
	private Map<String, ColumnInfo> columns;

	/**
	 * 表中唯一的主键
	 */
	private ColumnInfo onlyPrikey;
	/**
	 * 如果使用了联合主键，存在这里
	 */
	private List<ColumnInfo> priKeys;

	public TableInfo() {
		// TODO Auto-generated constructor stub
	}

	public TableInfo(String tname, List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.priKeys = priKeys;
	}



	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public ColumnInfo getOnlyPrikey() {
		return onlyPrikey;
	}

	public void setOnlyPrikey(ColumnInfo onlyPrikey) {
		this.onlyPrikey = onlyPrikey;
	}

}
