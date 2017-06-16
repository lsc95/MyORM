package test;

import java.util.ArrayList;
import java.util.List;

import com.coderli.sorm.core.Query;
import com.coderli.sorm.core.QueryFactory;
import com.coderli.sorm.core.TableContext;
import com.coderli.sorm.po.Emp;

public class Test {
	public static void main(String[] args) {
		// 通过这个方法生成po类
		TableContext.updatePOFile();
		TableContext.loadPOTables();
		// add();
		// delete();
		// update();
		getAll();
	}

	public static void add() {
		Emp emp = new Emp();
		emp.setAge(18);
		emp.setId(10);
		emp.setEmpname("小刚");
		emp.setSalary(2000.0);
		Query q = QueryFactory.createQuery();
		q.insert(emp);
	}

	public static void delete() {
		Emp emp = new Emp();
		emp.setId(10);
		Query q = QueryFactory.createQuery();
		q.delete(emp);
	}

	public static void update() {
		Emp emp = new Emp();
		emp.setId(10);
		emp.setAge(30);
		emp.setSalary(3000.0);
		Query q = QueryFactory.createQuery();
		q.update(emp, new String[] { "age", "salary" });
	}

	public static void getAll() {
		Query q = QueryFactory.createQuery();
		 List<Emp> lists = q.queryRows("select * from emp", Emp.class, null);
		 for (Emp emp : lists) {
		 System.out.println(emp.getEmpname()+'-'+emp.getAge());
		 }
	
	}
}
