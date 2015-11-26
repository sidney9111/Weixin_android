/**
 * @Title: GetBackActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2012-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.peiban.R;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.PrivateApi;
import com.peiban.app.control.DialogPrivacy;
import com.peiban.command.TextdescTool;
import com.peiban.vo.PrivacyVo;

public class PrivacyActivity extends BaseActivity {
	@ViewInject(id = R.id.searchButton)
	private ToggleButton searchButton;
	@ViewInject(id = R.id.head_txt)
	private TextView headTxt;
	@ViewInject(id = R.id.album_txt)
	private TextView albumTxt;
	@ViewInject(id = R.id.qq_txt)
	private TextView qqTxt;
	@ViewInject(id = R.id.phone_txt)
	private TextView phoneTxt;
	@ViewInject(id = R.id.privacy_layout_head)
	private RelativeLayout btnHead;
	@ViewInject(id = R.id.privacy_layout_album)
	private RelativeLayout btnAlbum;
	@ViewInject(id = R.id.privacy_layout_qq)
	private RelativeLayout btnQQ;
	@ViewInject(id = R.id.privacy_layout_phone)
	private RelativeLayout btnPhone;
	
	private PrivacyVo mPrivacyVo;		// 我的隐私设置。
	
	private PrivateApi mPrivateApi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.privacy_layout);
		super.onCreate(savedInstanceState);
		baseInit();
	}
	
	protected void baseInit() {
		super.baseInit();
		mPrivateApi = new PrivateApi();
		setTag();
		initprivacy();
		onClick();
	}

	/**
	 * 点击事件
	 * */
	private void onClick() {
		PrivacyClickListener l = new PrivacyClickListener();
		btnHead.setOnClickListener(l);
		btnAlbum.setOnClickListener(l);
		btnQQ.setOnClickListener(l);
		btnPhone.setOnClickListener(l);
	}

	/**
	 * 添加标签
	 **/
	private void setTag() {
		btnHead.setTag("head");
		btnAlbum.setTag("album");
		btnQQ.setTag("qq");
		btnPhone.setTag("phone");
	}

	/**
	 * 初始化隐私设置 eg: 搜索栏：0-off,1-on 其它项:0-所有人可见,1-vip可见,2-认证用户可见,2-好友可见
	 * */
	private void initprivacy() {
		mPrivacyVo = getFinalDb().findById(getUserInfoVo().getUid(), PrivacyVo.class);
		if(mPrivacyVo == null){
			mPrivacyVo = new PrivacyVo();
		}
		
		setPrivatyView(mPrivacyVo);
		
	}
	
	// 显示我的配置信息
	private void setPrivatyView(PrivacyVo privacyVo){
		if ("1".equals(privacyVo.getSerch())) {
			searchButton.setChecked(false);
		} else {
			searchButton.setChecked(true);
		}
		if ("1".equals(privacyVo.getHead())) {
			headTxt.setText(getResources().getString(
					R.string.privacy_all));
		} else 
			if ("2".equals(privacyVo.getHead())) {
			headTxt.setText(getResources().getString(
					R.string.privacy_vip));
		} else if ("3".equals(privacyVo.getHead())) {
			headTxt.setText(getResources().getString(
					R.string.privacy_friend));
		}
		if ("1".equals(privacyVo.getAlbum())) {
			albumTxt.setText(getResources().getString(
					R.string.privacy_all));
		} else 
			
			if ("2".equals(privacyVo.getAlbum())) {
			albumTxt.setText(getResources().getString(
					R.string.privacy_vip));
		} else if ("3".equals(privacyVo.getAlbum())) {
			albumTxt.setText(getResources().getString(
					R.string.privacy_friend));
		}
//		if ("1".equals(privacyVo.getQq())) {
//			qqTxt.setText(getResources()
//					.getString(R.string.privacy_all));
//		} else 
			
			if ("2".equals(privacyVo.getQq())) {
			qqTxt.setText(getResources()
					.getString(R.string.privacy_vip));
		} else if ("3".equals(privacyVo.getQq())) {
			qqTxt.setText(getResources().getString(
					R.string.privacy_friend));
		}
//		if ("1".equals(privacyVo.getPhone())) {
//			phoneTxt.setText(getResources().getString(
//					R.string.privacy_all));
//		} else 
			
			if ("2".equals(privacyVo.getPhone())) {
			phoneTxt.setText(getResources().getString(
					R.string.privacy_vip));
		} else if ("3".equals(privacyVo.getPhone())) {
			phoneTxt.setText(getResources().getString(
					R.string.privacy_friend));
		}
	}

	class PrivacyClickListener implements OnClickListener {
		DialogPrivacy dialogPrivacy;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.privacy_layout_head:
				dialogPrivacy = new DialogPrivacy(PrivacyActivity.this,
						headTxt, v.getTag().toString(), "头像");
				dialogPrivacy.privacyDialogShow();
				break;
			case R.id.privacy_layout_album:
				dialogPrivacy = new DialogPrivacy(PrivacyActivity.this,
						albumTxt, v.getTag().toString(), "相册");
				dialogPrivacy.privacyDialogShow();
				break;
			case R.id.privacy_layout_qq:
				dialogPrivacy = new DialogPrivacy(PrivacyActivity.this, qqTxt,
						v.getTag().toString(), "QQ");
				dialogPrivacy.privacyDialogShow();
				break;
			case R.id.privacy_layout_phone:
				dialogPrivacy = new DialogPrivacy(PrivacyActivity.this,
						phoneTxt, v.getTag().toString(), "手机");
				dialogPrivacy.privacyDialogShow();
				break;
			}
		}

	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.privacy_setting);
		setBtnBack();
		setTitleRight(R.string.authed_customer);
	}

	@Override
	protected void titleBtnBack() {
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		
		Intent intent = new Intent(PrivacyActivity.this, AuthorizeActivity.class);
		startActivity(intent);
	}

	/**
	 * 获取设置的值
	 * */
	private void getPrivacyValue() {
		if(mPrivacyVo == null){
			mPrivacyVo = new PrivacyVo();
		}
		
		String serch = !searchButton.isChecked() ? "1" : "0";
		mPrivacyVo.setUid(getUserInfoVo().getUid());
		mPrivacyVo.setSerch(serch);
		if (getResources().getString(R.string.privacy_all).equals(
				headTxt.getText().toString())) {
			mPrivacyVo.setHead("1");
		} else 
			
			if (getResources().getString(R.string.privacy_vip).equals(
				headTxt.getText().toString())) {
			mPrivacyVo.setHead("2");
		} else if (getResources().getString(R.string.privacy_friend).equals(
				headTxt.getText().toString())) {
			mPrivacyVo.setHead("3");
		}
		if (getResources().getString(R.string.privacy_all).equals(
				albumTxt.getText().toString())) {
			mPrivacyVo.setAlbum("1");
		} else 
			
			if (getResources().getString(R.string.privacy_vip).equals(
				albumTxt.getText().toString())) {
			mPrivacyVo.setAlbum("2");
		} else if (getResources().getString(R.string.privacy_friend).equals(
				albumTxt.getText().toString())) {
			mPrivacyVo.setAlbum("3");
		}
//		if (getResources().getString(R.string.privacy_all).equals(
//				qqTxt.getText().toString())) {
//			mPrivacyVo.setQq("1");
//		} else 
			
			if (getResources().getString(R.string.privacy_vip).equals(
				qqTxt.getText().toString())) {
			mPrivacyVo.setQq("2");
		}  else if (getResources().getString(R.string.privacy_friend).equals(
				qqTxt.getText().toString())) {
			mPrivacyVo.setQq("3");
		}
//		if (getResources().getString(R.string.privacy_all).equals(
//				phoneTxt.getText().toString())) {
//			mPrivacyVo.setPhone("1");
//		} else 
			if (getResources().getString(R.string.privacy_vip).equals(
				phoneTxt.getText().toString())) {
			mPrivacyVo.setPhone("2");
		} else if (getResources().getString(R.string.privacy_friend).equals(
				phoneTxt.getText().toString())) {
			mPrivacyVo.setPhone("3");
		}
		
		Map<String, String> maps = TextdescTool.objectToMap(mPrivacyVo);
		if(maps != null){
			if(checkNetWork()){
				mPrivateApi.privacy(getUserInfoVo().getUid(), maps, new SavePrivatyCallBack());
			}
		}
	}
	
	protected void onDestroy(){
		super.onDestroy();
		getPrivacyValue();
	}
	
	class SavePrivatyCallBack extends AjaxCallBack<String>{

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			String data = ErrorCode.getData(t);
			if(data != null){
				if(mPrivacyVo != null){
					try {
						getFinalDb().delete(mPrivacyVo);
					} catch (Exception e) {
					}
					
					try {
						getFinalDb().save(mPrivacyVo);
					} catch (Exception e) {
						Toast.makeText(getBaseContext(), "隐私保存失败!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		
	}
}
