package com.peiban.app.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;

import com.peiban.app.Constants;
import com.peiban.app.action.ImageInfoAction;

/**
 * 
 * 功能： 相册功能. <br />
 * 日期：2013-5-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AlbumApi extends PanLvApi {
	public AlbumApi() {
		super(Constants.ApiUrl.ALBUM_ACTION);
	}

	/**
	 * 添加相册
	 * 
	 * @param uid
	 *            用户id
	 * @param albumName
	 *            相册名
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void addAlbum(String uid, String albumName, String albumCover,
			String privacy, AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		AjaxParams params = getParams("add_s_album");
		params.put("uid", uid);
		params.put("albumName", albumName);

		if (!TextUtils.isEmpty(albumCover)) {
			params.put("albumCover", albumCover);
		}

		if (!TextUtils.isEmpty(privacy)) {
			params.put("privacy", privacy);
		}

		postPanLv(params, callBack);
	}

	/**
	 * 相片认证检测
	 * */
	public void authPhotoCheck(String uid, String pid, String t,
			AjaxCallBack<String> callBack) {
		AjaxParams params = getParams("check_auth");
		params.put("uid", uid);
		params.put("pid", pid);
		params.put("t", t);
		postPanLv(params, callBack);

	}
	/**
	 * 相片认证
	 * */
	public void authPhoto(String uid, String pid, String photoUrl,
			AjaxCallBack<String> callBack) {
		AjaxParams params = getParams("auth_photo");
		params.put("uid", uid);
		params.put("pid", pid);
		params.put("authphotoUrl", photoUrl);
		postPanLv(params, callBack);

	}
	/**
	 * 相册平行相片认证
	 * */
	public void authAlbumPhoto(String uid, String aid, String photoUrl,
			AjaxCallBack<String> callBack) {
		AjaxParams params = getParams("aid_auth_photo");
		params.put("uid", uid);
		params.put("aid", aid);
		params.put("authphotoUrl", photoUrl);
		postPanLv(params, callBack);

	}
	/**
	 * 相册平行入口添加图片
	 * 
	 * @param uid
	 *            用户id
	 * @param albumName
	 *            相册名
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void addAlbumPhoto(String uid, String albumName, String albumCover,
			AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		AjaxParams params = getParams("add_album_photo");
		params.put("uid", uid);
		params.put("albumName", albumName);

		if (!TextUtils.isEmpty(albumCover)) {
			params.put("albumCover", albumCover);
		}
		params.put("privacy", "1");
		postPanLv(params, callBack);
	}

	/**
	 * 获取相册
	 * 
	 * @param uid
	 *            用户uid
	 * @param touid
	 *            获取对方uid的信息. 作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void getAlbum(String uid, String touid, AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(touid);

		AjaxParams params = getParams("get_s_album");
		params.put("uid", uid);
		params.put("toUid", touid);
		postPanLv(params, callBack);
	}

	/**
	 * 修改相册
	 * 
	 * @param uid
	 *            用户uid
	 * @param albumName
	 *            相册名 可选
	 * @param ablumCover
	 *            相册封面 可选
	 * @param privacy
	 *            相册权限 可选
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void editAlbum(String uid, String aid, String albumName,
			String ablumCover, String privacy, AjaxCallBack<String> callBack) {
		attribleEmpty(uid);

		AjaxParams params = getParams("edit_s_album");
		params.put("uid", uid);
		params.put("aid", aid);
		if (!TextUtils.isEmpty(albumName))
			params.put("albumName", albumName);
		if (!TextUtils.isEmpty(ablumCover))
			params.put("ablumCover", ablumCover);
		if (!TextUtils.isEmpty(privacy))
			params.put("privacy", privacy);

		postPanLv(params, callBack);
	}

	/**
	 * 删除相册
	 * 
	 * @param aid
	 *            相册对应id
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void delAlbum(String uid, String aid, AjaxCallBack<String> callBack) {
		attribleEmpty(aid);

		AjaxParams params = getParams("del_s_album");
		params.put("uid", uid);
		params.put("aid", aid);

		postPanLv(params, callBack);
	}

	/**
	 * 上传图片
	 * 
	 * @param type
	 *            图片的类型 1: 头像， 2：图片
	 * @param fileName
	 *            文件 类型 (jpeg, png)
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 * @throws FileNotFoundException
	 *             文件没找到!
	 */
	public void upload(String uid, String type, File fileName,
			AjaxCallBack<String> callBack) throws FileNotFoundException {
		attribleEmpty(type);
		attribleEmpty(fileName);

		AjaxParams params = getUploadParms();

		params.put("uid", uid);
		params.put("type", type);
		params.put("f_upload", fileName);

		postPanLv(Constants.ApiUrl.UPLOAD_ACTION, params, callBack);
	}

	/**
	 * 上传图片
	 * 
	 * @param type
	 *            图片的类型 1: 头像， 2：图片
	 * @param bitmap
	 *            Bitmap文件.
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void upload(String uid, String type, Bitmap bitmap,
			CompressFormat format, AjaxCallBack<String> callBack) {
		attribleEmpty(type);
		attribleEmpty(bitmap);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, 73, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		upload(uid, type, bais, callBack);
	}

	/**
	 * 上传图片
	 * 
	 * @param type
	 *            图片的类型 1: 头像， 2：图片
	 * @param bitmap
	 *            Bitmap文件.
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void upload(String uid, String type, Bitmap bitmap,
			AjaxCallBack<String> callBack) {
		attribleEmpty(type);
		attribleEmpty(bitmap);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 73, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		upload(uid, type, bais, callBack);
	}

	/**
	 * 上传图片
	 * 
	 * @param type
	 *            图片的类型 1: 头像， 2：图片
	 * @param fileName
	 *            图片文件流
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void upload(String uid, String type, InputStream fileName,
			AjaxCallBack<String> callBack) {
		attribleEmpty(type);
		attribleEmpty(fileName);

		AjaxParams params = getUploadParms();

		params.put("uid", uid);
		params.put("type", type);
		params.put("f_upload", fileName, "upload.jpg");

		postPanLv(Constants.ApiUrl.UPLOAD_ACTION, params, callBack);
	}

	/**
	 * 往相册添加图片
	 * 
	 * @param uid
	 *            用户uid
	 * @param aid
	 *            相册id
	 * @param photoUrl
	 *            图片URL
	 * @param photoName
	 *            图片描述 可选
	 * @param prvivacy
	 *            权限 可选
	 * @param auth
	 *            认证 可选
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void addPhoto(String uid, String aid, String photoUrl,
			String photoName, String prvivacy, String auth,
			AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(aid);
		attribleEmpty(photoUrl);

		AjaxParams params = getParams("add_photo");
		params.put("uid", uid);
		params.put("aid", aid);
		params.put("photoUrl", photoUrl);

		if (TextUtils.isEmpty(photoName)) {
			params.put("photoName", ImageInfoAction.getPhotoFileName());
		} else {
			params.put("photoName", photoName);
		}

		if (!TextUtils.isEmpty(prvivacy)) {
			params.put("prvivacy", prvivacy);
		}
		if (!TextUtils.isEmpty(auth)) {
			params.put("auth", auth);
		}

		postPanLv(params, callBack);
	}

	/**
	 * 添加认证头像
	 * 
	 * @param uid
	 *            用户uid
	 * @param photoUrl
	 *            图片URL
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void addAuthPhoto(String uid, String photoUrl,
			AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(photoUrl);

		AjaxParams params = getParams("add_photo");
		params.put("uid", uid);
		params.put("photoUrl", photoUrl);

		params.put("auth", "2");

		postPanLv(params, callBack);
	}

	/**
	 * 获取相册中图片列表
	 * 
	 * @param aid
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void getAlbumPhoto(String uid, String aid,
			AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(aid);

		AjaxParams params = getParams("get_a_photo");

		params.put("uid", uid);
		params.put("aid", aid);

		postPanLv(params, callBack);
	}

	/**
	 * 删除相片
	 * 
	 * @param pid
	 *            相片id
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void delPhoto(String uid, String pid, AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(pid);

		AjaxParams params = getParams("del_photo");

		params.put("uid", uid);
		params.put("pid", pid);

		postPanLv(params, callBack);

	}

	/**
	 * 修改相片信息
	 * 
	 * @param pid
	 *            相片id
	 * @param photoName
	 *            相片描述 可选
	 * @param prvivacy
	 *            权限 可选
	 * @param auth
	 *            认证(待定) 可选
	 * @param callBack
	 *            作者:fighter <br />
	 *            创建时间:2013-5-23<br />
	 *            修改时间:<br />
	 */
	public void editPhoto(String uid, String pid, String photoName,
			String prvivacy, String auth, AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		attribleEmpty(pid);

		AjaxParams params = getParams("edit_photo");
		params.put("pid", pid);
		params.put("uid", uid);

		if (!TextUtils.isEmpty(photoName)) {
			params.put("photoName", photoName);
		}

		if (!TextUtils.isEmpty(prvivacy)) {
			params.put("prvivacy", prvivacy);
		}

		if (!TextUtils.isEmpty(auth)) {
			params.put("auth", auth);
		}

		postPanLv(params, callBack);
	}

	private AjaxParams getUploadParms() {
		return getParams("upload");
	}
}
