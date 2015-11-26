package com.peiban.service;

import android.util.Log;

import com.peiban.app.api.UserInfoApi;
import com.peiban.service.type.XmppTypeManager;
import com.peiban.vo.UserInfoVo;


public class PhpServiceThread implements Runnable{
	private static final String TAG = "php_content_thread";
	private XmppTypeManager manager;
	private UserInfoVo userInfoVo;
	private UserInfoApi userInfoApi;
	public static final long TIME = 300000;
	
	public boolean runState = true;
	
	public PhpServiceThread(XmppTypeManager manager, UserInfoVo userInfoVo) {
		super();
		this.manager = manager;
		this.userInfoVo = userInfoVo;
		userInfoApi = new UserInfoApi();
	}

	@Override
	public void run() {
		while(runState){
			connect();
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void connect(){
		Log.d(TAG, "connect()");
		try {
			String result = userInfoApi.online(userInfoVo.getUid());
			Log.d(TAG, "connect:online:" + result);
		} catch (Exception e) {
			Log.e(TAG, "connect()", e);
		}
	}

}
