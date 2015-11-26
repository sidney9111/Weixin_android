package com.peiban.app;

import com.peiban.SharedStorage;
import com.peiban.app.constants.LoginType;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * 功能： 本地认证. <br />
 * 日期：2013-5-30<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class LocalAuth {
	/** 登录的状态 */
	private static final String LOGIN_STATE = "mlstate";
	/** 聊天服务的状态 */
	private static final String CHATTING_STATE = "mcstate";
	
	private SharedPreferences sharedPreferences;
	
	public LocalAuth(Context context){
		sharedPreferences = SharedStorage.getConfigSharedPreferences(context);
	}
	
	/**
	 * 登录成功
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public void loginSuccess(){
		Editor editor = sharedPreferences.edit();
		editor.putString(LOGIN_STATE, LoginType.LOGINING);
		editor.commit();
	}
	/**
	 * 注销
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public void logined(){
		Editor editor = sharedPreferences.edit();
		editor.putString(LOGIN_STATE, LoginType.LOGINED);
		editor.commit();
	}
	/**
	 * 获取登录的状态
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public String getLoginType(){
		return sharedPreferences.getString(LOGIN_STATE, LoginType.NOTLOGIN);
	}
}
