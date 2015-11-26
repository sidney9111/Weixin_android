package com.peiban.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.cache.UserInfoCache;
import com.peiban.app.receiver.RegisterReceiver;
import com.peiban.app.vo.RegisterVo;
import com.peiban.application.PeibanApplication;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.UserInfoVo;
/*
 * 短信验证码界面
 */
public class RegisterValidateActivity extends BaseRegisterActivity {
	private static final String GETVALIDATE = "获取验证"; // 获取验证
	private static final String REGETVALIDATE = "重新获取"; // 获取验证
	private static final String VALIDATEING = "剩余"; // 获取验证
	private static final int VALIDATEPERIOD = 180;

	private int validateTime = VALIDATEPERIOD; // 验证周期 180 秒

	private RegisterVo registerVo;
	private Button btnGetValidateCode; // 获取验证信息按钮
	private TextView txtValidateCode;

	private Timer timer;
	
	private PeibanApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initIntent();
		
		application = (PeibanApplication) getApplication();
	}

	private void initIntent() {
		Intent intent = getIntent();
		RegisterVo registerVo = (RegisterVo) intent
				.getSerializableExtra(Constants.ACTION_REGISTER_VO_EXTRA);

		if (registerVo == null) {
			localError();
			return;
		}

		this.registerVo = registerVo;
		localSuccess();
	}

	// 本地验证成功..
	private void localSuccess() {
		setContentView(R.layout.register_validate);
		super.baseInit();

		this.init();
	}

	private void init() {
		btnGetValidateCode = (Button) this
				.findViewById(R.id.register_validate_edit_submit);
		btnGetValidateCode.setTag(GETVALIDATE);
		btnGetValidateCode.setText(GETVALIDATE);

		btnGetValidateCode.setOnClickListener(new BtnValidateOnClick());

		txtValidateCode = (TextView) this
				.findViewById(R.id.register_validate_edit_code);

	}

	// 本地效验失败..
	private void localError() {
		setContentView(R.layout.error_page);
	}

	// 获取效验码
	private void getValidateCode() {
		if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			NetworkUtils.notWorkToast(getBaseContext());
			return;
		}
		;

		startTimer();
		//20151119  验证需要sms，暂时改为check_code
		String action = "cr_code";
		String phone = registerVo.getUsername();

		AjaxParams params = new AjaxParams();
		params.put("action", action);
		params.put("phone", phone);

		getFinalHttp().post(Constants.ApiUrl.LOGIN_REGISTER, params,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
					
						String data = ErrorCode.getData(getBaseContext(), t);
						if (data != null) {
						}
					}
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						showToast(strMsg);
						cannelTimer();
					}
					
				});
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.register_title);
		setTitleRight(R.string.txt_comfirm);
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		cannelTimer();
		String code = txtValidateCode.getText().toString();
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(getBaseContext(), "请填入验证码!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		registerVo.setValidateCode(code);

		sumitRegister();
	}
	
	// 提交注册信息
	void sumitRegister() {
		if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			NetworkUtils.notWorkToast(getBaseContext());
			return;
		}
		;

		AjaxParams params = new AjaxParams();
		params.put("action", "reg");
		//params.put("phone", registerVo.getUsername());
		params.put("username", registerVo.getUsername());
		params.put("password", registerVo.getPassword());
		params.put("referee", registerVo.getNominate());
		params.put("code", registerVo.getValidateCode());

		getFinalHttp().post(Constants.ApiUrl.LOGIN_REGISTER, params,
				new AjaxCallBack<String>() {

					ProgressDialog progressDialog = new ProgressDialog(
							RegisterValidateActivity.this);

					@Override
					public void onStart() {
						super.onStart();
						progressDialog.setMessage("提交信息...");
						progressDialog.show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						progressDialog.cancel();
						String data = ErrorCode.getData(getBaseContext(), t);

						if (data != null) {
							String uid = JSON.parseObject(data)
									.getString("uid");
							registerVo.setUid(uid);

							registerSuccess();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						progressDialog.cancel();
					}

				});
	}

	// 注册成功
	void registerSuccess() {
		UserInfoVo userInfo = new UserInfoVo();
		userInfo.setPassword(registerVo.getPassword());
		userInfo.setPhone(registerVo.getUsername());
		userInfo.setUid(registerVo.getUid());

		new UserInfoCache(getBaseContext()).cacheUserInfo(userInfo);

		application.setUserInfoVo(userInfo);
		
		getPromptDialog().setMessage(R.string.dialog_register_txt_title_tag);
		getPromptDialog().addConfirm(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPromptDialog().cancel();
				Intent intent = new Intent(RegisterValidateActivity.this,
						RegisterGrzlActivity.class);
				intent.putExtra("phone", registerVo.getUsername());
				startActivity(intent);
				
				// 关闭注册填写内容页面
				sendBroadcast(new Intent(RegisterReceiver.ACIONT_INTENT));
			}
		});
		
		getPromptDialog().show();
	}

	// 验证成功
	void confirm() {
		Intent intent = new Intent(this, RegisterGrzlActivity.class);
		startActivity(intent);
	}

	private void startTimer() {
		timer = new Timer();
		timer.schedule(new MyTimeTask(), 0, 1000);
	}

	private void cannelTimer() {
		btnGetValidateCode.setTag(REGETVALIDATE);
		btnGetValidateCode.setText(REGETVALIDATE);
		try {
			if(timer != null){
				timer.cancel();
			}
		} catch (Exception e) {
		}
		
	}

	protected void onDestroy() {
		super.onDestroy();
		
			cannelTimer();
		
	}
	

	class BtnValidateOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			String action = (String) v.getTag();

			if (GETVALIDATE.equals(action)) {
				getValidateCode();
			} else if (REGETVALIDATE.equals(action)) {
				getValidateCode();
			}
		}

	}

	class MyTimeTask extends TimerTask {

		@Override
		public void run() {
			validateTime = validateTime - 1;
			btnGetValidateCode.post(new Runnable() {

				@Override
				public void run() {
					if (validateTime <= 0) {
						btnGetValidateCode.setText(REGETVALIDATE);
						btnGetValidateCode.setTag(REGETVALIDATE);
						cannelTimer();

					} else {
						btnGetValidateCode.setText(VALIDATEING + " "
								+ validateTime + " 秒");
						btnGetValidateCode.setTag(VALIDATEING);
					}
				}
			});
		}

	}
}
