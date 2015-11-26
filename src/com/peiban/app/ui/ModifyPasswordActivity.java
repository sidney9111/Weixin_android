package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.ui.common.RegisterUtil;
import com.peiban.application.PeibanApplication;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.UserInfoVo;

public class ModifyPasswordActivity extends BaseActivity{
	@ViewInject(id = R.id.modify_password_edit_password)
	private EditText editPassword;
	@ViewInject(id = R.id.modify_password_edit_newpassword)
	private EditText editNewpassword;
	@ViewInject(id = R.id.modify_password_edit_renewpassword)
	private EditText editRenewpassword;
	
	private UserInfoVo userInfoCokie;
	private PeibanApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_password);
		baseInit();
		application = (PeibanApplication) getApplication();
		userInfoCokie = application.getUserInfoVo();
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setBtnBack();
		setTitleRight(R.string.txt_comfirm);
		setTitleContent(R.string.modify_password_title);
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		if(!NetworkUtils.isConnected(getBaseContext())){
			Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_network), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String password = editPassword.getText().toString();
		String newpassword = editNewpassword.getText().toString();
		String reNewpassword = editRenewpassword.getText().toString();
		
		if(TextUtils.isEmpty(password)){
			Toast.makeText(getBaseContext(), "请输入旧密码", Toast.LENGTH_SHORT).show();
			return;
		}else if(TextUtils.isEmpty(newpassword)){
			Toast.makeText(getBaseContext(), "新密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(!RegisterUtil.validatePassword(newpassword)){
			Toast.makeText(getBaseContext(), "密码长度为6-12位", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(!RegisterUtil.validate(newpassword, reNewpassword)){
			Toast.makeText(getBaseContext(), "两次密码输入不同", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(RegisterUtil.validate(password, reNewpassword)){
			Toast.makeText(getBaseContext(), "新密码与旧密码相同", Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			// 提交修改密码信息.
			submitModifyPasswordAction(userInfoCokie.getUid(), password, newpassword);
		}
	}

	void submitModifyPasswordAction(String uid, String oldPassword, String newPassword){
		if(!NetworkUtils.isnetWorkAvilable(getBaseContext())){
			NetworkUtils.notWorkToast(getBaseContext());
			return;
		}
		
		AjaxParams params = new AjaxParams();
		params.put("action", "edit_password");
		params.put("uid", uid);
		params.put("oldpassword", oldPassword);
		params.put("newpassword", newPassword);
		
		getFinalHttp().post(Constants.ApiUrl.LOGIN_REGISTER, params, new ModifyPasswordCallBack());
	}
	
	class ModifyPasswordCallBack extends AjaxCallBack<String>{
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			getWaitDialog().setMessage("修改中...");
			getWaitDialog().show();
		}

		@Override
		public void onSuccess(String t) {
			// TODO Auto-generated method stub
			super.onSuccess(t);
			getWaitDialog().cancel();
			String data = ErrorCode.getData(getBaseContext(), t);
			if(data != null){
				if("1".equals(data.trim())){
					getPromptDialog().setMessage("修改密码成功!");
					getPromptDialog().removeCannel();
					getPromptDialog().addConfirm(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							getPromptDialog().cancel();
							ModifyPasswordActivity.this.finish();
						}
					});
				}else{
					getPromptDialog().setMessage("修改密码失败");
					getPromptDialog().removeConfirm();
					getPromptDialog().addCannel();
				}
				
				getPromptDialog().show();
			}
		}

		@Override
		public void onFailure(Throwable t, String strMsg) {
			// TODO Auto-generated method stub
			super.onFailure(t, strMsg);
			getWaitDialog().cancel();
		}
		
	}
}
