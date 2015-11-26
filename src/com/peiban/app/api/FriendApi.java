package com.peiban.app.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.app.Constants;

public class FriendApi extends PanLvApi{

	public FriendApi() {
		super(Constants.ApiUrl.FRIEND_ACTION);
	}
	/**
	 * 获取好友列表
	 * @param uid
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public void getFriendList(String uid, AjaxCallBack<String> callBack){
		postPanLv(getParams("get_f_list", uid), callBack);
	}
	
	/**
	 * 删除好友
	 * @param uid
	 * @param fuid 好友uid
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public void delFriend(String uid, String fuid, AjaxCallBack<String> callBack){
		attribleEmpty(fuid);
		AjaxParams params = getParams("del_friend", uid);
		params.put("fuid", fuid);
		postPanLv(params, callBack);
		
	}
	
	/**
	 * 同意添加好友.
	 * @param uid
	 * @param fuid
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public void beFriend(String uid, String fuid, AjaxCallBack<String> callBack){
		attribleEmpty(fuid);
		AjaxParams params = getParams("be_friend", uid);
		params.put("fuid", fuid);
		postPanLv(params, callBack);
	}
	
	/**
	 * 取消添加好友
	 * @param uid
	 * @param fuid
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-6-8<br />
	 * 修改时间:<br />
	 */
	public void refuseFriend(String uid, String fuid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("refuse_f", uid);
		params.put("toUid", fuid);
		postPanLv(params, callBack);
	}
	
	/**
	 * 申请好友
	 * @param uid
	 * @param fuid
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public void toFriend(String uid, String fuid, AjaxCallBack<String> callBack){
		attribleEmpty(fuid);
		AjaxParams params = getParams("to_friend", uid);
		params.put("fuid", fuid);
		postPanLv(params, callBack);
	}
	
	/**
	 * 备注名
	 * @param uid
	 * @param fuid
	 * @param markName 备注名
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public void markFriend(String uid, String fuid, String markName, AjaxCallBack<String> callBack){
		attribleEmpty(fuid);
		AjaxParams params = getParams("friend_mark", uid);
		params.put("fuid", fuid);
		params.put("markName", markName);
		postPanLv(params, callBack);
	}
}
