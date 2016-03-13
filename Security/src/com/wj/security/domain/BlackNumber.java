package com.wj.security.domain;

public class BlackNumber {
	int id;
	String number;
	int mode;
	public BlackNumber(int id, String number, int mode) {
		this.id = id;
		this.number = number;
		this.mode = mode;
	}
	public BlackNumber(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	

}
