package com.coderli.test;

import com.coderli.sorm.core.Query;
import com.coderli.sorm.core.QueryFactory;

public class Test {
	public static void main(String[] args) {
		Query q = QueryFactory.createQuery();
		Object obj = q.queryValue("select count(*) from emp", null);
		System.out.println(obj);
	}
}
