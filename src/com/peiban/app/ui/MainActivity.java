package com.peiban.app.ui;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.LocalAuth;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.cache.UserInfoCache;
import com.peiban.app.constants.LoginType;
import com.peiban.application.PeibanApplication;
import com.peiban.service.SnsService;
import com.peiban.service.UpdateService;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.UserInfoVo;
 
public class MainActivity extends BaseActivity {
	private long waitTime = 2000;
	private LocalAuth localAuth;
	private PeibanApplication shangwupanlvApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.welcome);
		shangwupanlvApplication = (PeibanApplication) getApplication();
		if (UpdateService.isServiceRunning(getApplicationContext(),
				UpdateService.class.getCanonicalName())) {
			init();
		} else if(!checkNetWork()){
//			showToast(getResources().getString(R.string.toast_network));
			init();
		}
		else {
			checkVersion();
		}
	}

	/***
	 * 检查是否更新版本
	 */
	public void checkVersion() {
		int ver = shangwupanlvApplication.getLocalVersion();
		if(ver != -1)
		{
			if(!checkNetWorkOrSdcard())
			{
				init();
				return;
			}
			
			checkUpdate(ver);
			
		}else{
			init();
		}
		
	}
	
	private void checkUpdate(int ver)
	{
		AjaxParams params = new AjaxParams();
		params.put("action", "version");
		params.put("version", ver + "");
		getFinalHttp().post(Constants.ApiUrl.BACKFEED, params,
				new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(String t) 
					{
						try {

							super.onSuccess(t);
							
							String data = ErrorCode.getData(getBaseContext(), t);
							System.out.println("data:" + data);
							if (!"1".equals(data)) {
								String version = JSONObject.parseObject(data)
										.getString("version");
								String discription = JSONObject.parseObject(data)
										.getString("discription");
								final String down_url = JSONObject
										.parseObject(data).getString("url");
								getPromptDialog().setMessage("检查到有新版本!");
								getPromptDialog().setMessage(discription);
								getPromptDialog().addCannel();
								getPromptDialog().setCannelText("不更新");
								getPromptDialog().addCannel(
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												getPromptDialog().cancel();
												waitTime = 1000;
												init();
											}
										});
								getPromptDialog().setConfirmText("更新");
								getPromptDialog().addConfirm(
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												getPromptDialog().cancel();
												Intent updateIntent = new Intent(
														MainActivity.this,
														UpdateService.class);
												updateIntent.putExtra("url",
														down_url);
												updateIntent.putExtra(
														"app_name",
														getResources().getString(
																R.string.app_name));
												startService(updateIntent);
												waitTime = 1000;
												init();
											}
										});
								getPromptDialog().show();
							} else {
//								showToast("获取服务器版本数据失败");
								waitTime = 1000;
								init();
							}
						
						} catch (Exception e) {
							waitTime = 1000;
							init();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().cancel();
						waitTime = 1000;
						init();
//						showToast("服务器响应失败!");
					}
				});
	}

	// 开始初始化
	private void init() {
		localAuth = new LocalAuth(this);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				startAuth();
				return null;
			}
		}.execute();
	}

	// 开始进行验证.
	void startAuth() {
		String loginType = localAuth.getLoginType();
		if (LoginType.LOGINING.equals(loginType)) {
			initUserInfo();
		} else {
			trancLoginActivity();
		}
	}

	// 初始化用户信息.
	private void initUserInfo() {
		UserInfoVo userInfoVo = new UserInfoCache(this).getCacheUserInfo();
		if (userInfoVo == null) {
			trancLoginActivity();
			return;
		}

		CustomerVo customerVo = FinalFactory.createFinalDb(getBaseContext(),
				userInfoVo).findById(userInfoVo.getUid(), CustomerVo.class);

		if (customerVo == null) {
			trancLoginActivity();
			return;
		}

		shangwupanlvApplication.setUserInfoVo(userInfoVo);
		shangwupanlvApplication.setCustomerVo(customerVo);

		trancIndexActivity();
	}

	// 跳转到主页
	private void trancIndexActivity() {
		startService(new Intent(getBaseContext(), SnsService.class));
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this, IndexTabActivity.class);
		startActivity(intent);
		finish();
	}

	// 跳转到登录
	private void trancLoginActivity() {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub

	}
}
