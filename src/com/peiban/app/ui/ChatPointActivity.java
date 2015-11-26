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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.PointPromptDialog;
import com.peiban.app.api.CreditApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.SundryApi;
import com.peiban.app.ui.common.FinalOnloadBitmap;
import com.peiban.command.TextdescTool;

public class ChatPointActivity extends BaseActivity {
	@ViewInject(id = R.id.point_txt_title)
	private TextView txtName;// 用户名
	@ViewInject(id = R.id.list_item_img_head)
	private ImageView imgHead;// 头像
	@ViewInject(id = R.id.list_item_img_auth)
	private ImageView imgAuth;// 头像认证图标
	@ViewInject(id = R.id.list_img_sex)
	private ImageView imgSex;// 性别图片
	@ViewInject(id = R.id.point_txt_age)
	private TextView txtAge;// 年龄
	@ViewInject(id = R.id.point_txt)
	private TextView txtPoint;// 积分
	@ViewInject(id = R.id.tocash_point_txt)
	private TextView txtTocashPoint;// 可提现积分
	@ViewInject(id = R.id.mypoint_eg)
	private TextView txtEg;// 注意文本
	@ViewInject(id = R.id.point_btn_auth)
	private Button btnAuth;// 我要认证按钮
	private String credit;
	@ViewInject(id = R.id.point_auth)
	private TextView txtAuth;
	
	private String kiting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mypoint_chat_layout);
		super.onCreate(savedInstanceState);
		baseInit();
	}
	
	protected void baseInit(){
		super.baseInit();
		new SundryApi().getTips(SundryApi.TIPS_ACREDIT, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.v("我的积分信息:", t);
				try {
					String data = ErrorCode.getData(t);
					if(!TextUtils.isEmpty(data)){
						String value = JSONObject.parseObject(data).getString("value");
						txtEg.setText(value);
					}
				} catch (Exception e) {
				}
			}
		});
		
		initCustomerInfo();
		btnOnClick();
	}

	/**
	 * 点击事件
	 * */
	public void btnOnClick() {
		btnClick l = new btnClick();
		btnAuth.setOnClickListener(l);
	}

	/**
	 * 初始化用户信息
	 * */
	private void initCustomerInfo() {
		FinalOnloadBitmap.finalDisplay(getBaseContext(), getMyCustomerVo(), imgHead, getHeadBitmap());
		txtName.setText(getMyCustomerVo().getName());
		// 判断性别
		if ("1".equals(getMyCustomerVo().getSex())) {
			imgSex.setBackgroundResource(R.drawable.sex_man);
		} else {
			imgSex.setBackgroundResource(R.drawable.sex_woman);
		}
		
		// 是否认证
		if("1".equals(getMyCustomerVo().getHeadattest())){
			imgAuth.setVisibility(View.VISIBLE);
			imgAuth.setImageResource(R.drawable.subscript_auth);
			btnAuth.setVisibility(View.GONE);
			
			txtAuth.setText("认证");
			txtAuth.setTextColor(Color.GREEN);
		} else {
			imgAuth.setVisibility(View.GONE);
			btnAuth.setVisibility(View.VISIBLE);
			txtAuth.setText("未认证");
			txtAuth.setTextColor(Color.BLACK);
		}
		
		// 年龄
		txtAge.setText(TextdescTool.dateToAge(getMyCustomerVo().getAge()) + " 岁");


		getPointInfo();
		
	}
	
	/**
	 * 获取积分相关信息
	 * */
	private void getPointInfo() {
		// TODO Auto-generated method stub
		String uid = getUserInfoVo().getUid();
		if (checkNetWork()) {
			CreditApi creditApi = new CreditApi();
			creditApi.getCredit(uid, new AjaxCallBack<String>() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					getWaitDialog().setMessage("正在获取积分信息");
					getWaitDialog().show();
				}

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					getWaitDialog().setMessage("获取成功");
					getWaitDialog().dismiss();
					try {
						String data = ErrorCode.getData(getBaseContext(), t);
						if (!"".equals(data)) {
							JSONObject json = JSONObject.parseObject(data);
							credit = json.getString("credit");
							kiting = json.getString("kiting");
							if (TextUtils.isEmpty(credit)) {
								credit = "0";
							}
							if (TextUtils.isEmpty(kiting)) {
								kiting = "0";
							}
							txtPoint.setText(credit + "分");
							txtTocashPoint.setText(kiting + "分");
						} else {
							txtPoint.setText("0分");
							txtTocashPoint.setText("0分");
						}
					} catch (Exception e) {
						txtPoint.setText("0分");
						txtTocashPoint.setText("0分");
					}

				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					getWaitDialog().setMessage("获取失败");
					getWaitDialog().dismiss();
				}

			});

		} else {
			showToast(getResources().getString(R.string.toast_network));
		}

	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.title_mypoint);
		setBtnBack();
		setTitleRight(R.string.title_more);
	}

	@Override
	protected void titleBtnBack() {
		// TODO Auto-generated method stub
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		final PointPromptDialog pointPromptDialog = new PointPromptDialog(this);
		pointPromptDialog.show();
		pointPromptDialog.getBtnApply().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ChatPointActivity.this,
								ToCashActivity.class);
						intent.putExtra("credit", kiting);
						pointPromptDialog.cancel();
						startActivity(intent);
					}
				});
	}

	class btnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.point_btn_auth:
				autnAction();
				break;
			default:
				break;
			}
		}

	}

	public void autnAction() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, HeadAuthActivity.class);
		startActivity(intent);
	}
}
