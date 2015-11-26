/**
 * @Title: GetBackActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
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
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.PointPromptDialog;
import com.peiban.app.api.CreditApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.SundryApi;
import com.peiban.command.TextdescTool;

public class MyPointActivity extends BaseActivity {
	private static final int REQUEST_CODE = 2057;
	
	@ViewInject(id = R.id.point_txt_title)
	private TextView txtName;// 用户名
	@ViewInject(id = R.id.list_item_img_head)
	private ImageView imgHead;// 头像
	@ViewInject(id = R.id.list_item_img_subscript)
	private ImageView imgVip;// 头像VIP图标
	@ViewInject(id = R.id.list_img_sex)
	private ImageView imgSex;// 性别图片
	@ViewInject(id = R.id.point_txt_age)
	private TextView txtAge;// 年龄
	@ViewInject(id = R.id.point_txt)
	private TextView txtPoint;// 积分
	@ViewInject(id = R.id.point_img_vip)
	private ImageView imgPointVip;// VIP图标
	@ViewInject(id = R.id.tocash_point_txt)
	private TextView txtTocashPoint;// 可提现积分
	@ViewInject(id = R.id.vip_time_txt)
	private TextView txtVipTime;// 头像
	@ViewInject(id = R.id.point_btn_buy)
	private Button btnPointBuy;// 购买按钮
	@ViewInject(id = R.id.point_btn_recharge)
	private Button btnPointRecharge;// 充值按钮
	@ViewInject(id = R.id.mypoint_eg)
	private TextView txtEg;// 注意文本
	private String credit;
	private String kiting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mypoint_layout);
		super.onCreate(savedInstanceState);
		initCustomerInfo();
		baseInit();
		btnOnClick();
	}

	protected void baseInit() {
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
	}
	
	/**
	 * 点击事件
	 * */
	public void btnOnClick() {
		btnClick l = new btnClick();
		btnPointRecharge.setOnClickListener(l);
		btnPointBuy.setOnClickListener(l);
	}

	/**
	 * 初始化用户信息
	 * */
	private void initCustomerInfo() {
		// TODO Auto-generated method stub
		FinalBitmap finalBitmap = FinalFactory
				.createFinalBitmap(MyPointActivity.this);
		finalBitmap.display(imgHead, getMyCustomerVo().getHead());
		txtName.setText(getMyCustomerVo().getName());
		if ("1".equals(getMyCustomerVo().getSex())) {
			imgSex.setBackgroundResource(R.drawable.sex_man);
		} else {
			imgSex.setBackgroundResource(R.drawable.sex_woman);
		}
		if ("1".equals(getMyCustomerVo().getVip())) {
			imgVip.setVisibility(View.VISIBLE);
			imgPointVip.setVisibility(View.VISIBLE);
		} else {
			imgVip.setVisibility(View.GONE);
			imgPointVip.setVisibility(View.GONE);
		}

		txtAge.setText(TextdescTool.dateToAge(getMyCustomerVo().getBirthday()) + " 岁");

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
							String sTime = json.getString("stime");
							String eTime = json.getString("etime");
							if (!TextUtils.isEmpty(sTime)
									&& !TextUtils.isEmpty(eTime)) {
								txtVipTime.setText(TextdescTool.getDate(sTime
										+ "000")
										+ "-"
										+ TextdescTool.getDate(eTime + "000"));
							} else {
								txtVipTime.setText("未开通");
							}

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && Activity.RESULT_OK == resultCode){
			initCustomerInfo();
		}
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		final PointPromptDialog pointPromptDialog = new PointPromptDialog(this);
		pointPromptDialog.show();
		pointPromptDialog.getBtnApply().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyPointActivity.this, ToCashActivity.class);
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
			case R.id.point_btn_recharge:
				rechargeAction();
				break;
			case R.id.point_btn_buy:
				buyVipAction();
				break;

			default:
				break;
			}
		}

	}

	public void rechargeAction() {
		Intent intent = new Intent(this, PanlvWebActivity.class);
		intent.putExtra("url", Constants.WebviewUrl.RECHARGE);
		intent.putExtra("titleName", "充值");
		startActivity(intent);
	}
	
	public void buyVipAction(){
		Intent intent = new Intent(this, BuyVipActivity.class);
//		startActivity(intent);
		startActivityForResult(intent, REQUEST_CODE);
	}
}
