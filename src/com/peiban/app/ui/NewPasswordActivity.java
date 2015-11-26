package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.receiver.RegisterReceiver;
import com.peiban.app.ui.common.RegisterUtil;
import com.peiban.command.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class NewPasswordActivity extends BaseRegisterActivity{
	@ViewInject(id = R.id.new_password_edit)
	private EditText editNewPassword;
	@ViewInject(id = R.id.new_repassword_edit)
	private EditText editRepassword;
	
	private String phone;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_password);
		baseInit();
		
		initIntent();
	}

	private void initIntent() {
		// TODO Auto-generated method stub
		phone = getIntent().getStringExtra("phone");
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setBtnBack();
		setTitleContent(R.string.new_repassword_title);
		setTitleRight(R.string.txt_comfirm);
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		submitNewpassword();
	}
	/** 提交新密码 */
	private void submitNewpassword() {
		// TODO Auto-generated method stub
		String password = editNewPassword.getText().toString();
		String repassword = editRepassword.getText().toString();
		
		if(TextUtils.isEmpty(password)){
			Toast.makeText(getBaseContext(), "新密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!RegisterUtil.validatePassword(password)){
			Toast.makeText(getBaseContext(), "密码长度为 6 ~ 12 位", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!RegisterUtil.validate(password, repassword)){
			Toast.makeText(getBaseContext(), "两次密码不相同", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!NetworkUtils.isnetWorkAvilable(getBaseContext())){
			Toast.makeText(getBaseContext(), "没有可用网络", Toast.LENGTH_SHORT).show();
			return;
		}
		
		AjaxParams params = new AjaxParams();
		this.password = password;
		params.put("action", "edit_f_password");
		params.put("phone", phone);
		params.put("password", password);
		
		getFinalHttp().post(Constants.ApiUrl.LOGIN_REGISTER, params, new NewPasswordCallBack());
	}
	
	// 修改成功!
	void submitSuccess(){
		Toast.makeText(getBaseContext(), "修改成功!", Toast.LENGTH_SHORT).show();
		
		// 开始登录.
		sendBroadcast(new Intent(RegisterReceiver.ACIONT_INTENT));
		
		Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
		intent.putExtra("action", true);
		intent.putExtra("username", phone);
		intent.putExtra("password", password);
		startActivity(intent);
		
	}
	
	// 修改失败!
	void submitError(String data){
		Toast.makeText(getBaseContext(), "修改失败!", Toast.LENGTH_SHORT).show();
	}

	
	class NewPasswordCallBack extends AjaxCallBack<String>{
		private ProgressDialog dialog = new ProgressDialog(NewPasswordActivity.this);

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			dialog.setMessage("修改中...");
			dialog.show();
		}

		@Override
		public void onSuccess(String t) {
			// TODO Auto-generated method stub
			super.onSuccess(t);
			dialog.cancel();
			String data = ErrorCode.getData(getBaseContext(), t);
			if(data != null){
				if("1".equals(data.trim())){
					submitSuccess();
				}else{
					submitError(data);
				}
			}
		}

		@Override
		public void onFailure(Throwable t, String strMsg) {
			// TODO Auto-generated method stub
			super.onFailure(t, strMsg);
			dialog.cancel();
		}
	}
}
