package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.SundryApi;
import com.peiban.app.api.UserApi;
import com.peiban.app.control.RegisterControl;
import com.peiban.app.vo.RegisterVo;

public class RegisterActivity extends BaseRegisterActivity {
	private static final String TAG = RegisterActivity.class.getCanonicalName();
	@ViewInject(id = R.id.register_edit_username)
	private EditText editUsername;
	@ViewInject(id = R.id.register_edit_password)
	private EditText editPassword;
	@ViewInject(id = R.id.register_edit_repassword)
	private EditText editRepassword;
	@ViewInject(id = R.id.register_edit_nominate)
	private EditText editNominate;
	/** 提示信息 */
	@ViewInject(id = R.id.register_tips)
	private TextView textTips;
	
	private RegisterControl registerControl;
	
	private UserApi mUserApi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		this.baseInit();
		addListener();
	}

	protected void baseInit() {
		super.baseInit();
		new SundryApi().getTips(SundryApi.TIPS_REG, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.v("注册信息:", t);
				try {
					String data = ErrorCode.getData(t);
					if(!TextUtils.isEmpty(data)){
						String value = JSONObject.parseObject(data).getString("value");
						textTips.setText(value);
					}
				} catch (Exception e) {
				}
				
			}
			
		});
	}
	
	@Override
	protected void initTitle() {
		setTitleContent(R.string.register_title);
		setTitleRight(R.string.title_btn_next);
	}
	
	private void addListener(){
		registerControl = new RegisterControl(this);
	}

	
	public void nextActivity(final RegisterVo registerVo){
		if (mUserApi == null) {
			mUserApi = new UserApi();
		}
		
		if(!checkNetWork()){
			return;
		}
		
		mUserApi.checkPhone(registerVo.getUsername(), registerVo.getNominate(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				try {
					super.onSuccess(t);
					String data = ErrorCode.getData(getBaseContext(), t);
					if(data != null){
						if("0".equals(data)){
							showToast("该号码已经被注册!");
						}if("1".equals(data))
						{
							showToast("推荐人不存在！");
						} if("2".equals(data))
						{
							showToast("推荐人不为会员或者认证用户!");
						} if("3".equals(data))
						{
							Intent intent = new Intent(RegisterActivity.this, RegisterValidateActivity.class);
							intent.putExtra(Constants.ACTION_REGISTER_VO_EXTRA, registerVo);
							startActivity(intent);
						}
					}
				} catch (Exception e) {
					showToast("发生错误");
					Log.e(TAG, "注册错误;", e);
				}
				
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				showToast("服务器未响应!");
			}
		});
	}
	
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		registerControl.submit();
	}

	/**
	 * 用户名
	 */
	public EditText getEditUsername() {
		return editUsername;
	}

	/**
	 * 密码
	 */
	public EditText getEditPassword() {
		return editPassword;
	}

	/**
	 * 确认密码
	 */
	public EditText getEditRepassword() {
		return editRepassword;
	}

	/**
	 * 推荐号
	 */
	public EditText getEditNominate() {
		return editNominate;
	}

}
