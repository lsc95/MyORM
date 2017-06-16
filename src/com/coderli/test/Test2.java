package com.coderli.test;

import com.coderli.sorm.core.Query;
import com.coderli.sorm.core.QueryFactory;

public class Test2 {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int i=0;i<1000;i++){
			test1();
		}
		long cost = System.currentTimeMillis()-start;
		System.out.println("花费时间:"+cost);
	}
	public static void test1(){
		Query q = QueryFactory.createQuery();
		Object obj = q.queryValue("select count(*) from emp", null);
		System.out.println(obj);
	}
}
