/**
 * @Title: PrivacyShared.java 
 * @Package com.shangwupanlv.app 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-6 下午5:25:51 
 * @version V1.0
 */
package com.peiban.app;

import com.peiban.SharedStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class PrivacyShared {
	private SharedPreferences privacySharedPreferences;
	private Editor privacyEditor;
	/**
	 * sharedprefence文件key名 
	 * */
	public static final String PRIVACY_SEARCH = "privacy_search";
	public static final String PRIVACY_HEAD = "privacy_head";
	public static final String PRIVACY_ALBUM = "privacy_album";
	public static final String PRIVACY_QQ = "privacy_qq";
	public static final String PRIVACY_PHONE = "privacy_phone";
	
	private String search;
	private String head;
	private String album;
	private String qq;
	private String phone;
	public PrivacyShared(Context context) {
	privacySharedPreferences = context.getSharedPreferences(
			SharedStorage.SHARED_APP_NAME, Context.MODE_PRIVATE);
	privacyEditor=privacySharedPreferences.edit();
	}
	public String getSearch() {
		if (TextUtils.isEmpty(search)) {
			this.search = privacySharedPreferences.getString(PRIVACY_SEARCH,
					"");
		}
		return search;
	}
	public String getHead() {
		if (TextUtils.isEmpty(head)) {
			this.head = privacySharedPreferences.getString(PRIVACY_HEAD,
					"");
		}
		return head;
	}
	public String getAlbum() {
		if (TextUtils.isEmpty(album)) {
			this.album = privacySharedPreferences.getString(PRIVACY_ALBUM,
					"");
		}
		return album;
	}
	public String getQq() {
		if (TextUtils.isEmpty(qq)) {
			this.qq = privacySharedPreferences.getString(PRIVACY_QQ,
					"");
		}
		return qq;
	}
	public String getPhone() {
		if (TextUtils.isEmpty(phone)) {
			this.phone = privacySharedPreferences.getString(PRIVACY_PHONE,
					"");
		}
		return phone;
	}
	public void setSearch(String search) {
		if (!TextUtils.isEmpty(search)) {
			privacyEditor.putString(PRIVACY_SEARCH, search);
		}
		this.search = search;
	}
	public void setHead(String head) {
		if (!TextUtils.isEmpty(head)) {
			privacyEditor.putString(PRIVACY_HEAD, head);
		}
		this.head = head;
	}
	public void setAlbum(String album) {
		if (!TextUtils.isEmpty(album)) {
			privacyEditor.putString(PRIVACY_ALBUM, album);
		}
		this.album = album;
	}
	public void setQq(String qq) {
		if (!TextUtils.isEmpty(qq)) {
			privacyEditor.putString(PRIVACY_QQ, qq);
		}
		this.qq = qq;
	}
	public void setPhone(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			privacyEditor.putString(PRIVACY_PHONE, phone);
		}
		this.phone = phone;
	}
	
}
