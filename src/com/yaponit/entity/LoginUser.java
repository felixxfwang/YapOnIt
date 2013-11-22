package com.yaponit.entity;

public class LoginUser {
	private String token;
	private String firstName;
	
	public LoginUser(String token,String firstName){
		this.token = token;
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstNam3e) {
		this.firstName = firstNam3e;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
