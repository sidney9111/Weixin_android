package com.peiban.app.vo;

import java.util.Calendar;
import java.util.Date;

import android.provider.ContactsContract.Contacts.Data;

public class ProjectVo extends BaseVo {
	
	public ProjectVo(){
		setCreate_Time(1378358428);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type="";
	private String name="";
	private String[] images;
	private String desc="";
	private String uid;
	private int createTime;
	private String location;
	private String buget;
	private int hot_count;
	private int milestone_time;
	private int number;
	private int month;
	private int day;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String title) {
		this.name = title;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getDescription() {
		return desc;
	}
	public void setDescription(String desc) {
		this.desc = desc;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getCreate_Time() {
		return createTime;
	}
	public void setCreate_Time(int createTime) {
		Date date = new Date(createTime);
		
		//now = Calendar.getInstance();
		month = date.getMonth();
		day = date.getDay();
		this.createTime = createTime;
	}
	public int getCreateMonth(){
		return this.month;
	}
	public int getCreateDay(){
		return this.day;
	}
	public String getBuget() {
		return buget;
	}
	public void setBuget(String buget) {
		this.buget = buget;
	}
	public int getHot_count() {
		return hot_count;
	}
	public void setHot_count(int hot_count) {
		this.hot_count = hot_count;
	}
	public int getMilestone_time() {
		return milestone_time;
	}
	public void setMilestone_time(int milestone_time) {
		this.milestone_time = milestone_time;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	
}
