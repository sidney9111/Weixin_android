package com.peiban.app.api;

import java.util.Map;
import java.util.Set;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.text.TextUtils;

import com.peiban.app.Constants;
/**
 * 
 * 功能： 附近获取好友 <br />
 * 获取附近的人接口。
         使用获取附近的接口时，需要上传自己的位置信息.
 * 日期：2013-5-30<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class LocationApi extends PanLvApi{

	public LocationApi() {
		super(Constants.ApiUrl.USER_INFO_ACTION);
	}
	
	public void getDistanceList(String uid, String lat, String lng, String page, String pageSize, Map<String, String> param, AjaxCallBack<String> callBack){
		getLocationList(uid, lat, lng, page, pageSize, param, callBack);
	}
	
	public void getScoreList(String uid, String lat, String lng, String page, String pageSize, Map<String, String> param, AjaxCallBack<String> callBack){
		getLocationList(uid, lat, lng, page, pageSize, param, callBack);
	}
	
	/**
	 * action	serch 请求方法
		uid	
		sex	性别 0：女；1：男
		occasions	服务场合：
		1,独自
		2,社交
		3,不限
		onlinetime	在线时间,换算到秒
		sort	排序方式：
		asc:升序
		desc：降序
		sortName	排序字段：
		默认：distance
		score：综合评分
		ascore：形象评分
		sscore：服务评分
		usercp：荣誉值
		radius	半径：单位为米
		sradius	半径下限：默认为0
		headattest	头像认证 
		0:未认证；1：认证
		vip	vip，0：不是，1：是
		age	年龄：范围表示，逗号分隔（如20,25）
		lat	纬度 精确到小数点后6位
		lng 	经度 精确到小数点后6位
		page	页数，默认为1
		page_size	每页条数，默认为10
		usercp	荣誉值/魅力值,范围，逗号分隔
		height	身高，范围，逗号分隔
		weight	体重，范围，逗号分隔
		agent	经济人：
		1，是
		0，否
		error	101,uid为空
		103,action请求错误
		122,lat为空
		123,lng为空
		125,请求页数超出范围
		data	同个人信息获取
	 * @param param
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-6-9<br />
	 * 修改时间:<br />
	 */
	public void getLocationList(String uid, String lat, String lng,String page, String pageSize, Map<String, String> param, AjaxCallBack<String> callBack){
		AjaxParams params = null;
		if(param  == null){
			params = new AjaxParams();
		}else{
			params = new AjaxParams(param);
		}
		
//		System.out.println("#####################根据条件获取附近列表信息#####################");
//		System.out.println("############参数列表：");
//		System.out.println("uid:" + uid);
//		System.out.println("lat:" + lat);
//		System.out.println("lng:" + lng);
//		
//		if(param != null){
//			Set<String> keys = param.keySet();
//			
//			for (String key : keys) {
//				System.out.println(key + ":" + param.get(key));
//			}
//		}
		
		params.put("uid", uid);
		params.put("action", "serch");
		params.put("page", page);
		params.put("page_size", pageSize);
		
		params.put("lat", lat);
		params.put("lng", lng);
		
		postPanLv(params, callBack);
		locationUplod(uid, lat, lng, new LocationUplodCallBack());
	}
	
	
	/**
	 * 按条件拉取用户
	 * @param uid
	 * @param sex性别 0：女；1：男
	 * @param occasions  服务场合：
				1,独自
				2,社交
				3,不限
	 * @param onlinetime 在线时间,换算到秒ex. 
	 * @param headattest
	 * 头像认证 
			0:未认证；1：认证
	 * @param vip  0：不是，1：是
	 * @param age  年龄：范围表示，逗号分隔（如20,25）
	 * @param lat 纬度 精确到小数点后6位
	 * @param lng 经度 精确到小数点后6位
	 * @param page 页数，默认为1
	 * @param page_size 每页条数，默认为10
	 * @param usercp 荣誉值/魅力值,范围，逗号分隔
	 * @param height 身高，范围，逗号分隔
	 * @param weight 体重，范围，逗号分隔
	 * @param agent 经济人：
					1，是
					0，否
	 * 作者:fighter <br />
	 * 创建时间:2013-6-7<br />
	 * 修改时间:<br />
	 */
	public void getLocationWhere(String uid, String sex, String occasions
			, String onlinetime, String headattest, String vip, 
			String age, String lat, String lng, String page, 
			String page_size, String usercp,
			String height, String weight, String agent, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("serch_by_condition", uid);
		if (!TextUtils.isEmpty(sex))
			params.put("sex", sex);
		if (!TextUtils.isEmpty(occasions))
			params.put("occasions", occasions);
		if (!TextUtils.isEmpty(onlinetime))
			params.put("onlinetime", onlinetime);
		if (!TextUtils.isEmpty(headattest))
			params.put("headattest", headattest);
		if (!TextUtils.isEmpty(vip))
			params.put("vip", vip);
		if (!TextUtils.isEmpty(age))
			params.put("age", age);
		if (!TextUtils.isEmpty(lat))
			params.put("lat", lat);
		if (!TextUtils.isEmpty(lng))
			params.put("lng", lng);
		if (!TextUtils.isEmpty(page))
			params.put("page", page);
		if (!TextUtils.isEmpty(page_size))
			params.put("page_size", page_size);
		if (!TextUtils.isEmpty(usercp))
			params.put("usercp", usercp);
		if (!TextUtils.isEmpty(height))
			params.put("height", height);
		if (!TextUtils.isEmpty(weight))
			params.put("weight", weight);
		if (!TextUtils.isEmpty(agent))
			params.put("agent", agent);
		
		postPanLv(params, callBack);
		locationUplod(uid, lat, lng, new LocationUplodCallBack());
		
	}
	
	/**
	 * 
	 * @param uid
	 * @param lat 经度
	 * @param lng 纬度
	 * @param radius 起点
	 * @param sradius 终点
	 * @param page 当前页
	 * @param page_size 每页显示条数(默认 10 条)
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public void getLocationList(String uid, String lat, String lng, String radius,
			String sradius, String page, String page_size, String sort, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("serch_by_radius");
		
		params.put("uid", uid);
		params.put("lat", lat);
		params.put("lng", lng);
		params.put("radius", sradius);
		params.put("sradius", radius);
		if(!TextUtils.isEmpty(page)){
			params.put("page", page);
		}
		if(!TextUtils.isEmpty(page_size)){
			params.put("page_size", page_size);
		}
		if(!TextUtils.isEmpty(page_size)){
			params.put("sort", sort);
		}
		
		postPanLv(params, callBack);
		locationUplod(uid, lat, lng, new LocationUplodCallBack());
	}
	
	/**
	 * 上传位置信息
	 * @param uid  
	 * @param lat
	 * @param lng
	 * @param callBack
	 * 作者:fighter <br />
	 * 创建时间:2013-6-7<br />
	 * 修改时间:<br />
	 */
	public void locationUplod(String uid, String lat, String lng, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("up_add", uid);
		params.put("lat", lat);
		params.put("lng", lng);
		postPanLv(params, callBack);
	}
	
	
	class LocationUplodCallBack extends AjaxCallBack<String>{

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
		}
		
	}
}
