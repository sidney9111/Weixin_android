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

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name = "photo")
public class PhotoVo implements Serializable{
	@Transient
	private static final long serialVersionUID = -5094078474942062095L;
	@Id
	private String pid; // 相片id
	private String aid;
	private String photoName;//照片名
	private String photoUrl;// 相片路径
	private String auth;// 相片认证

	public String getPid() {
		return pid == null ? "" : pid;
	}

	public String getAid() {
		return aid == null ? "" : aid;
	}

	public String getPhotoUrl() {
		return photoUrl == null ? "" : photoUrl;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getAuth() {
		return auth == null ? "" : auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhotoVo other = (PhotoVo) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		return true;
	}

	

}
