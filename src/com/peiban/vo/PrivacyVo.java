/**
 * @Title: AlbumVo.java 
 * @Package com.shangwupanlv.vo 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-28 上午10:38:47 
 * @version V1.0
 */
package com.peiban.vo;

import java.io.Serializable;

import android.text.TextUtils;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name = "privacy")
public class PrivacyVo implements Serializable{
	@Transient
	private static final long serialVersionUID = -5094078474942062095L;
	@Id
	private String uid; // 用户id
	private String serch;// 是否能搜索到我
	private String head;// 头像
	private String album;// 相册
	private String qq;// qq
	private String phone;// 手机
	public String getUid() {
		return uid;
	}
	public String getSerch() {
		return TextUtils.isEmpty(serch) ? "1" : serch;
	}
	public String getHead() {
		return TextUtils.isEmpty(head) ? "1" : head;
	}
	public String getAlbum() {
		return TextUtils.isEmpty(album) ? "1" : album;
	}
	public String getQq() {
		return TextUtils.isEmpty(qq) ? "2" : qq;
	}
	public String getPhone() {
		return TextUtils.isEmpty(phone) ? "2" : phone;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setSerch(String serch) {
		if(TextUtils.isEmpty(serch)){
			return;
		}
		this.serch = serch;
	}
	public void setHead(String head) {
		if(TextUtils.isEmpty(head)){
			return;
		}
		this.head = head;
	}
	public void setAlbum(String album) {
		if(TextUtils.isEmpty(album)){
			return;
		}
		this.album = album;
	}
	public void setQq(String qq) {
		if(TextUtils.isEmpty(qq)){
			return;
		}
		this.qq = qq;
	}
	public void setPhone(String phone) {
		if(TextUtils.isEmpty(phone)){
			return;
		}
		this.phone = phone;
	}
}
