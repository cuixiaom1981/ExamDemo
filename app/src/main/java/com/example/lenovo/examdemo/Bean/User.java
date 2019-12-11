package com.example.lenovo.examdemo.Bean;


public class User {

	public User(String phone, String password, String stuId) {
		super();
		
		this.phone = phone;
		this.password = password;
		this.stuId = stuId;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	
	private String phone;
	private String password;
	private String stuId;
	
}
