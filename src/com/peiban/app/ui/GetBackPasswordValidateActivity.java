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
import com.peiban.command.NetworkUtils;

/**
 * 
 * 功能：找回密码获取验证<br />
 * 日期：2013-5-21<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class GetBackPasswordValidateActivity extends BaseRegisterActivity {
	private static final String GETVALIDATE = "获取验证"; // 获取验证
	private static final String REGETVALIDATE = "重新获取"; // 获取验证
	private static final String VALIDATEING = "剩余"; // 获取验证
	private static final int VALIDATEPERIOD = 180;

	private int validateTime = VALIDATEPERIOD; // 验证周期 180 秒

	private Button btnGetValidateCode; // 获取验证信息按钮
	private TextView txtValidateCode;

	private Timer timer;

	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_validate);
		super.baseInit();

		this.init();
		initIntent();

	}

	private void initIntent() {
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");
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

	// 获取效验码
	private void getValidateCode() {
		if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			Toast.makeText(getBaseContext(), "没有可用网络!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		;

		startTimer();
		String action = "cr_code";
		String phone = this.phone;

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
//							JSONObject dataObject = JSON
//									.parseObject(data);
//							final String code = dataObject.getString("code");
//
//							txtValidateCode.post(new Runnable() {
//
//								@Override
//								public void run() {
//									txtValidateCode.setText(code + "");
//								}
//							});
						}
					}
				});
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.register_title);
		setTitleRight(R.string.txt_comfirm);
		setBtnBack();
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


		sumitValidate();
	}

	// 提交验证的信息
	void sumitValidate() {
		if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			Toast.makeText(getBaseContext(), "没有可用网络!", Toast.LENGTH_SHORT)
					.show();
			return;
		};

		String code = txtValidateCode.getText().toString();
		
		AjaxParams params = new AjaxParams();
		params.put("action", "edit_c_code");
		params.put("phone", phone);
		params.put("code", code);

		getFinalHttp().post(Constants.ApiUrl.LOGIN_REGISTER, params,
				new AjaxCallBack<String>() {

					ProgressDialog progressDialog = new ProgressDialog(
							GetBackPasswordValidateActivity.this);

					@Override
					public void onStart() {
						super.onStart();
						progressDialog.setMessage("验证信息...");
						progressDialog.show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						progressDialog.cancel();
						String data = ErrorCode.getData(getBaseContext(), t);

						if (data != null) {
							if("1".equals(data.trim())){
								validateSuccess();
							}else{
								validateError();
							}
							
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						progressDialog.cancel();
					}

				});
	}

	// 验证成功!
	void validateSuccess() {
		Intent intent = new Intent(this, NewPasswordActivity.class);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}
	
	// 验证失败!
	void validateError(){
		Toast.makeText(getBaseContext(), "验证失败!", Toast.LENGTH_SHORT).show();
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

		timer.cancel();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (timer != null) {
				cannelTimer();
			}
		} catch (Exception e) {
		}

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
