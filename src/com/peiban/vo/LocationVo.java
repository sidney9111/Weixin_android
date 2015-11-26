package com.peiban.vo;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
@Table(name="locationvos")
public class LocationVo implements Serializable{
	@Transient
	private static final long serialVersionUID = -7383366222224755653L;
	@Id
	private String uid;
	private String locations;   // 附近的人CustomerVos
	private String createTime;
	private String updateTime;
	@Transient
	private int state;		// 1 更新 0 创建
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUid() {
		return uid;
	}
	public String getLocations() {
		return locations;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
