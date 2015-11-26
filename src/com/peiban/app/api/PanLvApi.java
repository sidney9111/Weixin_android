package com.peiban.app.api;

import java.util.Map;

import android.text.TextUtils;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class PanLvApi {
	protected FinalHttp finalHttp = new FinalHttp();
	private String actionUrl;
	
	/** 是否开启DEBUG 模式 */
	protected boolean deBug = true;
	
	public PanLvApi(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	protected void postPanLv(AjaxParams params, AjaxCallBack<String> callBack) {
		postPanLv(actionUrl, params, callBack);
	}
	
	protected void postPanLv(String url, AjaxParams params, AjaxCallBack<String> callBack) {
		finalHttp.post(url, params, callBack);
	}
	
	protected void attribleEmpty(String attrible) {
		if (TextUtils.isEmpty(attrible)) {
			throw new NullPointerException("uid attribute cannot be empty!");
		}
	}

	protected void attribleEmpty(Object attrible) {
		if (null == attrible) {
			throw new NullPointerException("attribute cannot be empty!");
		}
	}


	protected AjaxParams getParams(String action) {
		AjaxParams params = new AjaxParams();
		params.put("action", action);
		return params;
	}
	
	protected AjaxParams getParams(String action, String uid) {
		attribleEmpty(uid);
		AjaxParams params = getParams(action);
		params.put("uid", uid);
		return params;
	}
	public FinalHttp getFinalHttp() {
		return finalHttp;
	}

	protected AjaxParams getParams(Map<String, String> maps, String action) {
		AjaxParams params = new AjaxParams(maps);
		params.put("action", action);
		return params;
	}

	public String getActionUrl() {
		return actionUrl;
	}
	
	
}
