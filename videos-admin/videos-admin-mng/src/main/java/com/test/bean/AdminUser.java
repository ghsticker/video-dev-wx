package com.test.bean;
/**
 * @Description
 * @author ghsticker
 * 2019年3月12日
 */
public class AdminUser {
	private String usertoken;
	private String username;
	private String password;
	
	
	public AdminUser() {
		super();
	}
	
	
	public AdminUser(String usertoken, String username, String password) {
		super();
		this.usertoken = usertoken;
		this.username = username;
		this.password = password;
	}


	public String getUsertoken() {
		return usertoken;
	}
	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}

