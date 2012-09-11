package com.xinyin.android.entity;

import java.io.Serializable;

public class IdentityCard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629524248511683295L;
	private String code;
	private String location;
	private String birthday;
	private String gender;
	public IdentityCard() {
		super();
	}
	public IdentityCard(String code, String location, String birthday,
			String gender) {
		super();
		this.code = code;
		this.location = location;
		this.birthday = birthday;
		this.gender = gender;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
