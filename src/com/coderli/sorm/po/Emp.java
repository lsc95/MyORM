package com.coderli.sorm.po;


public class Emp {

	private String empname;
	private java.sql.Date birthday;
	private Double bonus;
	private Integer deptid;
	private Integer id;
	private Double salary;
	private Integer age;


	public String getEmpname(){
		return empname;
	}
	public java.sql.Date getBirthday(){
		return birthday;
	}
	public Double getBonus(){
		return bonus;
	}
	public Integer getDeptid(){
		return deptid;
	}
	public Integer getId(){
		return id;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
	public void setEmpname(String empname){
		this.empname=empname;
	}
	public void setBirthday(java.sql.Date birthday){
		this.birthday=birthday;
	}
	public void setBonus(Double bonus){
		this.bonus=bonus;
	}
	public void setDeptid(Integer deptid){
		this.deptid=deptid;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setSalary(Double salary){
		this.salary=salary;
	}
	public void setAge(Integer age){
		this.age=age;
	}
	@Override
	public String toString() {
		return "Emp [empname=" + empname + ", birthday=" + birthday + ", bonus=" + bonus + ", deptid=" + deptid
				+ ", id=" + id + ", salary=" + salary + ", age=" + age + "]\n";
	}
	
}
