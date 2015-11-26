package com.peiban.app.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.app.Constants;

public class UserApi extends PanLvApi{

	public UserApi() {
		super(Constants.ApiUrl.LOGIN_REGISTER);
	}
	
	/**
	 * 效验电话号是否存在
	 * */
	public void checkPhone(String phone, String referee, AjaxCallBack<String> callBack){
		AjaxParams params = getParams("check_phone");
		params.put("phone", phone);
		if (referee==null){
			params.put("referee", "");
		}
		else
		{
			params.put("referee", referee);
		}
		
		postPanLv(params, callBack);
	}
}
