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

@Table(name = "album")
public class AlbumVo implements Serializable {
	@Transient
	private static final long serialVersionUID = -5094078474942062094L;
	@Id
	private String aid; // 相册id
	private String uid;// 用户id
	private String albumCover;// 相册封面
	private String albumName;// 相册名
	private String privacy;// 隐私
	private String photoCount;// 相片数量
	private String tag;// 相片标记
	private String createDate;// 创建时间
	private String auth;//认证

	public String getAid() {
		return aid == null ? "" : aid;
	}

	public String getUid() {
		return uid;
	}

	public String getAlbumCover() {
		return albumCover;
	}

	public String getAlbumName() {
		return albumName;
	}

	public String getPrivacy() {
		return privacy;
	}

	public String getPhotoCount() {
		return photoCount;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTag() {
		return tag == null ? "" : tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

}
