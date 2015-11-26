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
import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.ui.common.RegisterUtil;
import com.peiban.app.vo.AccountVo;

public class ToCashActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.tocash_btn)
	private Button btnTocash;
	@ViewInject(id = R.id.register_grzl_edit_bankname)
	private TextView bankName;// 开户银行全名
	@ViewInject(id = R.id.register_grzl_edit_personname)
	private TextView personName;// 开户名
	@ViewInject(id = R.id.register_grzl_edit_account)
	private TextView account;// 帐号
	@ViewInject(id = R.id.register_grzl_edit_phone)
	private TextView phone;// 手机号
	@ViewInject(id = R.id.tocash_point_txt)
	private TextView pointApply;// 可提现积分
	
	private UserInfoApi userInfoApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tocash_layout);
		super.onCreate(savedInstanceState);
		baseInit();
		onClick();
	}

	protected void baseInit() {
		super.baseInit();
		userInfoApi = new UserInfoApi();
		try {
			String creditpoint = getIntent().getExtras().getString("credit");
			pointApply.setText(creditpoint);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		userInfoApi.getAccount(getUserInfoVo().getUid(), new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				getWaitDialog().setMessage("获取记录!");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				getWaitDialog().cancel();
				Log.v("申请:", t);
				
				try {
					String data = ErrorCode.getData(t);
					if(!TextUtils.isEmpty(data)){
						AccountVo accountVo = JSONObject.toJavaObject(JSONObject.parseObject(data), AccountVo.class);
						buildView(accountVo);
					}
				} catch (Exception e) {
					Log.d("申请:", "---", e);
				}
				
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				getWaitDialog().cancel();
			}
		
			
		});
	}
	
	
	private void buildView(AccountVo accountVo){
		bankName.setText(accountVo.getBank());
		personName.setText(accountVo.getAccountName());
		account.setText(accountVo.getAccount());
		phone.setText(accountVo.getPhone());
	}
	
	private void onClick() {
		// TODO Auto-generated method stub
		btnTocash.setOnClickListener(this);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.tocash);
		setBtnBack();
	}

	@Override
	protected void titleBtnBack() {
		// TODO Auto-generated method stub
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tocash_btn:
			tocashAction();
			break;

		}
	}

	private void tocashAction() {
		// TODO Auto-generated method stub
		String bankname = bankName.getText().toString();
		String personname = personName.getText().toString();
		String accountstr = account.getText().toString();
		String phonestr = phone.getText().toString();
		if (TextUtils.isEmpty(bankname)) {
			showToast(getResources().getString(R.string.nobankname));
			return;
		} else if (TextUtils.isEmpty(personname)) {
			showToast(getResources().getString(R.string.noopenname));
			return;
		} else if (TextUtils.isEmpty(accountstr)) {
			showToast(getResources().getString(R.string.noaccount));
			return;
		} else if (TextUtils.isEmpty(phonestr)) {
			showToast(getResources().getString(R.string.nophone));
			return;
		} else if (!validatePhone(phonestr)) {
			showToast(getResources().getString(R.string.phoneworng));
			return;
		} else {
			AccountVo accountVo = new AccountVo(bankname, personname, accountstr, phonestr);
			userInfoApi.account(getUserInfoVo().getUid(), accountVo, new AjaxCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					getWaitDialog().setMessage("提交");
					getWaitDialog().show();
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					getWaitDialog().cancel();
					Log.v("申请:", t);
					try {
						String data = ErrorCode.getData(getBaseContext(), t);
						if(!TextUtils.isEmpty(data)){
							if("1".equals(data)){
								show("申请成功!");
							}else{
								show("申请失败!");
							}
						}else{
							show("申请错误!");
						}
					} catch (Exception e) {
						show("申请错误!");
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					super.onFailure(t, strMsg);
					getWaitDialog().cancel();
					show("申请错误!");
				}
				
				
				private void show(String str){
					getPromptDialog().setMessage(str);
					getPromptDialog().addCannel();
					getPromptDialog().setCannelText("确定");
					getPromptDialog().removeConfirm();
					getPromptDialog().show();
				}
				
			});
		}
	}

	private boolean validatePhone(String str) {
		return RegisterUtil.validatePhone(str);
	}
}
