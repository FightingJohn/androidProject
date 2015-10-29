package com.lyl.bean;
/**
 * 电话联系人信息
 * @author 木木
 *
 */
public class Contact {

	private String name;		//姓名
	private String phoneNum;	//电话号码
	private String email;		//邮箱
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(String name, String phoneNum, String email) {
		super();
		this.name = name;
		this.phoneNum = phoneNum;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", phoneNum=" + phoneNum + ", email="
				+ email + "]";
	}
	
	
}
