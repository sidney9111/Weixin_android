package com.peiban.vo;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
/**
 * 
 * 功能： 好友列表信息 <br />
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
@Table(name="friendlist")
public class FriendListVo implements Serializable{
	@Transient
	private static final long serialVersionUID = -7869646970345728524L;
	@Id(column="id")
	private String uid;
	private String friends;  // 好友信息
	private String updateTime;  // 更新时间
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @return the friends
	 */
	public String getFriends() {
		return friends;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @param friends the friends to set
	 */
	public void setFriends(String friends) {
		this.friends = friends;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
