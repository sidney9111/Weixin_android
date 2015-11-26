package com.peiban.app.api;

import java.util.Map;

import android.text.TextUtils;

import com.peiban.app.Constants;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class ProjectApi extends PanLvApi {

	public ProjectApi() {
		super(Constants.ApiUrl.LOGIN_REGISTER);
	
	}
	
	public void getList(String uid,int curPage,int pageSize,Map<String, String> param, AjaxCallBack<String> callBack){
		AjaxParams params = getParams(param, "project_list");
		params.put("uid", uid);
		
		params.put("page", String.valueOf(curPage));
		params.put("pagesize", String.valueOf(pageSize));
		
		postPanLv(params, callBack);
	}
	
	public void addProjectAlbum(String uid, String description, String albumCover,AjaxCallBack<String> callBack) {
		attribleEmpty(uid);
		AjaxParams params = getParams("add_project");
		params.put("uid", uid);
		params.put("description", description);
		params.put("location", "上海");
		params.put("buget", "1");
		if (!TextUtils.isEmpty(albumCover)) {
			params.put("albumCover", albumCover);
		}
		params.put("privacy", "1");
		postPanLv(params, callBack);
	}

}
