package com.peiban.vo;

import java.io.Serializable;
/**
 * 
 * 功能：用户信息登录信息!<br />
 * 日期：2013-5-21<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class UserInfoVo implements Serializable{
	private static final long serialVersionUID = 3915914401478077952L;
	
	private String uid;
	private String phone;
	private String password;
	public String getUid() {
		return uid;
	}
	public String getPhone() {
		return phone == null ? "" : phone;
	}
	public String getPassword() {
		return password == null ? "" : password;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
