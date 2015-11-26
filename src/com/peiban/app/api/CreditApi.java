/**
 * @Title: CreditApi.java 
 * @Package com.shangwupanlv.app.api 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-19 下午2:45:09 
 * @version V1.0
 */
package com.peiban.app.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.app.Constants;

public class CreditApi extends PanLvApi {

	public CreditApi() {
		super(Constants.ApiUrl.CREDIT_URL);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获取积分信息
	 * */
	public void getCredit(String uid, AjaxCallBack<String> callBack) {
		AjaxParams params = getParams("get_user_credit");
		params.put("uid", uid);
		postPanLv(params, callBack);
	}
	
	/**
	 * 购买会员配置获取
	 * @param uid
	 * @param callBack
	 * @author fighter <br />
	 * 创建时间:2013-7-9<br />
	 * 修改时间:<br />
	 */
	public void getBuyConfig(String uid, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("get_buy_config");
		params.put("uid", uid);
		postPanLv(params, callBack);
	}
	
	/**
	 * 购买vip
	 * @param uid
	 * @param credit  积分
	 * @param value   购买的时间
	 * @param callBack
	 * @author fighter <br />
	 * 创建时间:2013-7-9<br />
	 * 修改时间:<br />
	 */
	public void buyVip(String uid, String credit, String value, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("buy_vip");
		params.put("uid", uid);
		params.put("credit", credit);
		params.put("value", value);
		postPanLv(params, callBack);
	}
}
