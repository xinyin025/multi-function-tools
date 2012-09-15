package com.xinyin.android.entity;

import java.io.Serializable;

public class KuaidiEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8993599793938910572L;
	
	private String time;
	private String context;
	public KuaidiEntity() {
		super();
	}
	public KuaidiEntity(String time, String context) {
		super();
		this.time = time;
		this.context = context;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	

}
