package com.peiban.app.api;

import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.baidu.location.BDLocation.a;
import com.peiban.app.Constants;
import com.peiban.app.vo.AccountVo;
import com.peiban.command.TextdescTool;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 
 * 功能：个人信息<br />
 * 日期：2013-5-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class UserInfoApi extends PanLvApi{
	public UserInfoApi() {
		super(Constants.ApiUrl.USER_INFO_ACTION);
	}
	/**
	 * 修改个人资料
	 * @param uid
	 * @param infos  根据个人信息的模型
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void editInfo(String uid, Map<String, String> infos, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
		attribleEmpty(infos);
		
		AjaxParams params = getParams(infos, "edit_infor");
		params.put("uid", uid);
		
		params.remove("headattest");
		
		postPanLv(params, callBack);
	}
	
	/**
	 * 获取用户信息
	 * @param toUid 目标UID
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void getInfo(String uid, String toUid, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
		attribleEmpty(toUid);
		
		AjaxParams params = getParams("get_infor");
		params.put("uid", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	public String getInfo(String uid, String toUid){
		attribleEmpty(uid);
		attribleEmpty(toUid);
		
		AjaxParams params = getParams("get_infor");
		params.put("uid", uid);
		params.put("toUid", toUid);
		
		return (String) getFinalHttp().postSync(getActionUrl(), params);
	}
	
	/**
	 * 添加个人信息.
	 * @param uid
	 * @param maps
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void addInfo(String uid, Map<String, String> maps, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
		attribleEmpty(maps);
		
		AjaxParams params = getParams(maps, "add_infor");
		
		params.put("uid", uid);
		
		postPanLv(params, callBack);
	}
	
	/**
	 * 获取积分
	 * @param uid 用户id
	 * @param toUid 获取对象id的积分
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void getScore(String uid, String toUid, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
//		attribleEmpty(toUid);
		AjaxParams params = getParams("get_score");
		params.put("uid", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	/**
	 * 评分
	 * @param uid 用户id
	 * @param toUid 评价对象
	 * @param asorce 外形评分
	 * @param ssorce 服务评分
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void editScore(String uid, String toUid, String asorce, String ssorce, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
		attribleEmpty(toUid);
		
		AjaxParams params = getParams("edit_score");
		
		params.put("uid", uid);
		params.put("toUid", toUid);
		params.put("ascore", asorce + "");
		params.put("sscore", ssorce + "");
		
		postPanLv(params, callBack);
	}
	
	/**
	 * 位置上传
	 * @param uid
	 * @param lat 经度
	 * @param lng 纬度
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-5-23<br />
	 * 修改时间:<br />
	 */
	public void locationUplod(String uid, String lat, String lng, AjaxCallBack<String> callBack){
		attribleEmpty(uid);
		attribleEmpty(lat);
		attribleEmpty(lng);
		
		AjaxParams params = getParams("up_add");
		
		params.put("uid", uid);
		params.put("lat", lat);
		params.put("lng", lng);
		
		postPanLv(params, callBack);
	}
	
	public String online(String uid){
		AjaxParams params = getParams("user_online", uid);
		return (String) getFinalHttp().postSync(getActionUrl(), params);
	}
	/**
	 * 申请查看好友信息。
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-6-14<br />
	 * 修改时间:<br />
	 */
	public void applyAuth(String uid, String toUid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("app_auth", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	public void getScoreList(String uid, String toUid, String page, String pageSize, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("get_l_score", uid);
		params.put("toUid", toUid);
		params.put("page", page);
		params.put("page_size", pageSize + "");
		if(deBug){
			StringBuffer buffer = new StringBuffer();
			buffer.append("POST  ").append("page:").append(page)
			.append("page_size:").append(pageSize).
			append("uid:").append(uid).append("toUid").append(toUid);
			Log.v("积分列表", buffer.toString());
		}
		postPanLv(params, callBack);
		
	}
	
	public void agAuth(String uid, String toUid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("ag_auth", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	
	public void authUser(String uid, String page, String pageSize, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("auth_user", uid);
		params.put("page", page);
		params.put("page_size", pageSize);
		if (deBug) {
			String tag = "authUser";
			Log.d(tag, "action:auth_user");
			Log.d(tag, "uid:" + uid);
			Log.d(tag, "page:" + page);
			Log.d(tag, "page_size:" + pageSize);
		}
		
		postPanLv(params, callBack);
	}
	
	public void delAuthUser(String uid, String toUid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("del_auth", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	public void refApp(String uid, String toUid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("ref_app", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	public void checkAuth(String uid, String toUid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("check_auth", uid);
		params.put("toUid", toUid);
		
		postPanLv(params, callBack);
	}
	
	/**
	 * 申请提现
	 * */
	public void account(String uid, AccountVo accountVo, AjaxCallBack<String> callBack)
	{
		Map<String, String> params1 = TextdescTool.objectToMap(accountVo);
		params1.put("uid", uid);
		AjaxParams params = getParams(params1, "account"); 
		
		if(deBug){
			Set<String> keys = params1.keySet();
			for (String key : keys) {
				System.out.println(key + " ==>> " + params1.get(key));
			}
		}
		
		postPanLv(params, callBack);
	}

	/**
	 * 获取提现的信息
	 * */
	public void getAccount(String uid, AjaxCallBack<String> callBack)
	{
		AjaxParams params = getParams("get_account"); 
		params.put("uid", uid);
		
		postPanLv(params, callBack);
	};
}
