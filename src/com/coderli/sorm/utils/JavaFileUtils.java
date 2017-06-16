package com.coderli.sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.coderli.sorm.bean.ColumnInfo;
import com.coderli.sorm.bean.JavaFieldGetSet;
import com.coderli.sorm.bean.TableInfo;
import com.coderli.sorm.core.DBManager;
import com.coderli.sorm.core.MySqlTypeConvertor;
import com.coderli.sorm.core.TableContext;
import com.coderli.sorm.core.TypeConvertor;

/**
 * 封装了java文件操作
 * @author lishichun
 *
 */
public class JavaFileUtils {
	/**
	 * 根据字段信息生成java属性信息。如：varchar  username-->private String username;以及相应的set和get方法源码
	 * @param column 字段信息
	 * @param convertor 类型转换器
	 * @return java属性和set/get方法源码
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column,TypeConvertor convertor){
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		
		String javaFieldType  =convertor.databaseTypeToJavaType(column.getDataType());
		
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");
		//生成get方法源码
		StringBuilder getSrc=new StringBuilder();
		getSrc.append("\tpublic " +javaFieldType+" get"+StringUtils.firstCharToUpperCase(column.getName())+"(){\n");
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		//生成set方法源码
		StringBuilder setSrc=new StringBuilder();
		setSrc.append("\tpublic void set"+StringUtils.firstCharToUpperCase(column.getName())+"(");
		setSrc.append(javaFieldType+" "+column.getName()+"){\n");
		setSrc.append("\t\tthis."+column.getName()+"="+column.getName()+";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		
		
		return jfgs;
	}
	/**
	 * 根据表的信息生成java类的源码
	 * @param tableInfo 表信息	
	 * @param convertor 数据类型转换器
	 * @return java类的源代码
	 */
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFies = new ArrayList<>();
		
		for(ColumnInfo c : columns.values()){
			javaFies.add(createFieldGetSetSRC(c,convertor));
		}		
		
		StringBuilder src= new StringBuilder();
		
		//生成package语句
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//生成import语句
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		//生成类声明语句
		src.append("public class "+StringUtils.firstCharToUpperCase(tableInfo.getTname())+" {\n\n");
		
		//生成属性列表
		
		for(JavaFieldGetSet f:javaFies){
			src.append(f.getFieldInfo());
		}
		src.append("\n\n");
		//生成get方法列表
		for(JavaFieldGetSet f:javaFies){
			src.append(f.getGetInfo());
		}

		//生成set方法列表
		for(JavaFieldGetSet f:javaFies){
			src.append(f.getSetInfo());
		}
		//生成类结束
		src.append("}\n");
		return src.toString();
	}
	
	public static void createJavaPOFile(TableInfo tableInfo,TypeConvertor convertor){
		String src = createJavaSrc(tableInfo,convertor);
		
		String srcPath = DBManager.getConf().getSrcPath()+"\\";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
		File f =new File(srcPath+packagePath);
		//指定文件不存在
		if(!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()+"/"+StringUtils.firstCharToUpperCase(tableInfo.getTname())+".java"));
			bw.write(src);
			System.out.println("建立表"+tableInfo.getTname()+"对应的java类:"+StringUtils.firstCharToUpperCase(tableInfo.getTname()+".java"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
//		ColumnInfo ci = new ColumnInfo("username","varchar",0);
//		JavaFieldGetSet j = createFieldGetSetSRC(ci, new MySqlTypeConvertor());
//		System.out.println(j);
		Map<String,TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()){
			JavaFileUtils.createJavaPOFile(t,new MySqlTypeConvertor());
		}
		
	}
}
