package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class TalkingActivity extends BaseActivity{
	@ViewInject(id = R.id.edit_talk_content)
	private EditText editTextContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.talking);
		this.baseInit();
	}
	
	protected void baseInit() {
		super.baseInit();
		
	}

	@Override
	protected void initTitle() {
		setBtnBack();
		setTitleContent("反馈信息");
		setTitleRight("提交");
	}
	
	protected void submitSuccess() {
		getPromptDialog().addCannel(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getPromptDialog().setCannelText("确定");
		getPromptDialog().setMessage("提交成功");
		
		getPromptDialog().show();
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		
		String content = editTextContent.getText().toString();
		if(TextUtils.isEmpty(content)){
			showToast("内容不能为空!");
			return;
		}else if(content.length() < 6){
			showToast("长度少于六位!");
			return;
		}
		
		hideSoftKeyboard();
		AjaxParams params = new AjaxParams();
		params.put("uid", getUserInfoVo().getUid());
		params.put("action", "back_feed");
		
		params.put("content", content);
		getFinalHttp().post(Constants.ApiUrl.BACKFEED, params, new AjaxCallBack<String>() {
			
			@Override
			public void onStart() {
				super.onStart();
				getWaitDialog().setMessage("提交...");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				getWaitDialog().cancel();
				String data = ErrorCode.getData(getBaseContext(), t);
				if(data != null){
					if("1".equals(data)){
						submitSuccess();
					}else{
						showToast("提交重复!");
					}
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
	
}
