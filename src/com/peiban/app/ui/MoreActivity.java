package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.application.PeibanApplication;
import com.peiban.service.UpdateService;

public class MoreActivity extends BaseActivity {
	@ViewInject(id = R.id.more_layout_tickling)
	private View tickling; // 反馈意见
	@ViewInject(id = R.id.more_layout_protocol)
	private View protocol; // 协议
	@ViewInject(id = R.id.more_layout_update)
	private View update; // 更新
	@ViewInject(id = R.id.more_layout_contact_me)
	private View callMe;
	@ViewInject(id = R.id.ver_tag)
	private TextView appVersion;
	private PeibanApplication shangwupanlvApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		this.baseInit();
		shangwupanlvApplication = (PeibanApplication) getApplication();
	}

	protected void baseInit() {
		super.baseInit();
		MroeOnclickListener listener = new MroeOnclickListener();

		tickling.setOnClickListener(listener);
		protocol.setOnClickListener(listener);
		update.setOnClickListener(listener);
		callMe.setOnClickListener(listener);
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
			appVersion.setText("程序版本： " + info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			appVersion.setText("程序版本：未找到! ");
		}
		
		
	}

	@Override
	protected void initTitle() {
		setTitleContent(getResources().getString(R.string.more_title));
		setBtnBack();
	}

	// 联系我们
	private void contactMeAction() {
		Intent intent = new Intent(this, PanlvWebActivity.class);
		intent.putExtra("url", Constants.ApiUrl.CONTACT_OUR);
		intent.putExtra("titleName", "联系我们");
		startActivity(intent);
	}

	// 反馈信息
	private void ticklingAction() {
		Intent intent = new Intent(this, TalkingActivity.class);
		startActivity(intent);
	}
	
	// 提示当前为最新版本
	private void showCurrVresion()
	{
		getPromptDialog().setMessage("当前为最新版本!");
		getPromptDialog().addCannel();
		getPromptDialog().setCannelText("确定");
		getPromptDialog().removeConfirm();
		getPromptDialog().show();
	}
	
	// 提示版本需要更新 discription 新版本的描述
	// down_url 下载地址
	private void showUpdateVersion(String discription, final String down_url)
	{
		getPromptDialog().setMessage(discription);
		getPromptDialog().addCannel();
		getPromptDialog().setCannelText("不更新");
		getPromptDialog().setConfirmText("更新");
		getPromptDialog().addConfirm(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						getPromptDialog().cancel();
						Intent updateIntent = new Intent(
								MoreActivity.this,
								UpdateService.class);
						updateIntent.putExtra("url", down_url);
						updateIntent
								.putExtra(
										"app_name",
										getResources()
												.getString(
														R.string.app_name));
						startService(updateIntent);
					}
				});
		getPromptDialog().show();
	}
	
	// 更新
	private void updateAction() {
		int ver = shangwupanlvApplication.getLocalVersion();
		
		if(!checkNetWork()){
			return;
		}
		
		AjaxParams params = new AjaxParams();
		params.put("action", "version");
		params.put("version", ver + "");
		getFinalHttp().post(Constants.ApiUrl.BACKFEED, params,
				new AjaxCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						getWaitDialog().setMessage("正在检查更新...");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) 
					{
						super.onSuccess(t);
						getWaitDialog().cancel();
						try {
							String data = ErrorCode.getData(getBaseContext(), t);
							if (data != null) {
								if ("1".equals(data)) {
									showCurrVresion();
								} else {
									String version = JSONObject.parseObject(data)
									.getString("version");
									String discription = JSONObject.parseObject(data)
									.getString("discription");
								final String down_url=JSONObject.parseObject(data).getString("url");
									showUpdateVersion(discription, down_url);
								}
							}
						
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().cancel();
						showToast("服务器响应失败!");
					}
				});

	}

	// 用户协议
	private void protocolAction() {
		Intent intent = new Intent(this, ProtocolActivity.class);
		startActivity(intent);
	}

	class MroeOnclickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.more_layout_contact_me:
				contactMeAction();
				break;
			case R.id.more_layout_protocol:
				protocolAction();
				break;
			case R.id.more_layout_update:
				if (UpdateService.isServiceRunning(getApplicationContext(),
						"com.shangwupanlv.service.UpdateService")) {
					showToast("莫急哈！已经在更新了");
				} else {
					updateAction();
				}
				break;
			case R.id.more_layout_tickling:
				ticklingAction();
				break;
			default:
				break;
			}
		}

	}
}
