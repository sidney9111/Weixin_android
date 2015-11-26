/**
 * @Title: GetBackActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.ui.common.RegisterUtil;
/**
 * 
 * 功能： 输入手机号页面 <br />
 * 日期：2013-5-21<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 */
public class GetBackPasswordActivity extends BaseRegisterActivity{
	@ViewInject(id = R.id.getback_edit_phone)
	private EditText editPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getback_password);
		baseInit();
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.getback_password);
		setBtnBack();
		setTitleRight(R.string.register_head_title_right_tag);
	}
	
	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		
		nextAction();
	}
	
	// 下一步
	void nextAction(){
		String phone = editPhone.getText().toString();
		
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(getBaseContext(), "输入手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!RegisterUtil.validatePhone(phone)){
			Toast.makeText(getBaseContext(), "输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = new Intent(this, GetBackPasswordValidateActivity.class);
		intent.putExtra("phone", phone);
		startActivity(intent);
	}

}
