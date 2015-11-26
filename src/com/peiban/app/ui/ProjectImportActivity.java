package com.peiban.app.ui;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.peiban.R;
import com.peiban.app.Constants;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProjectImportActivity extends BaseActivity implements OnClickListener{
	@ViewInject(id=R.id.txt)
	private EditText txtNumber;
	@ViewInject(id=R.id.btnImport)
	private Button btnImport;
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.projectimport);
		super.onCreate(savedInstanceState);
		
		btnImport.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		//这里直接发请求吧，不用绕肠子
		String actionUrl = Constants.ApiUrl.LOGIN_REGISTER;
		AjaxParams params = new AjaxParams();
		params.put("action", "import_project");
		params.put("uid", getUserInfoVo().getUid());
		params.put("number", txtNumber.getEditableText().toString());
		
		//postPanLv(params, callBack);
		FinalHttp finalHttp = new FinalHttp();
		finalHttp.post(actionUrl, params, new PostProjectInfo());
	}
	class PostProjectInfo extends AjaxCallBack<String> {
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			
			
		}
		@Override
		public void onFailure(Throwable t, String strMsg) {
			super.onFailure(t, strMsg);
			showToast("发生错误!");
		}
	}


}
