package com.yaponit.entity;

public class HttpResult {
	boolean success;
	String result;
	public HttpResult(boolean s,String r){
		this.success = s;
		this.result = r;
	}
	public boolean getSuccess(){
		return this.success;
	}
	public String getResult(){
		return this.result;
	}

}
