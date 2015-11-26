/**
 * @Title: DialogPrivacy.java 
 * @Package com.shangwupanlv.app.control 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-7 上午9:24:48 
 * @version V1.0
 */
package com.peiban.app.control;

import com.peiban.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogPrivacy {
	private Context context;
	private Dialog privacyDialog;
	private TextView privacyTxt;
	private TextView privacyTitle;
	private Button btnAll;
	private Button btnVip;
	private Button btnFriend;
	private String tag;//根据tag判断点击的项
	private String title;//title显示项
	public DialogPrivacy(Context context, TextView privacyTxt, String tag,String title) {
		this.context = context;
		this.privacyTxt = privacyTxt;
		this.tag = tag;
		this.title = title;
		initWight();
		OnClick();
		
	}
	/**
	 * dialog点击事件
	 * */
	private void OnClick() {
		// TODO Auto-generated method stub
        DialogClickListener l=new DialogClickListener();
        btnAll.setOnClickListener(l);
        btnVip.setOnClickListener(l);
        btnFriend.setOnClickListener(l);
	}

	/**
	 * 初始化dialog布局
	 * */
	private void initWight() {
		// TODO Auto-generated method stub
		privacyDialog = new Dialog(context, R.style.MyDialog);
		privacyDialog.setContentView(R.layout.dialog_privacy);
		privacyTitle = (TextView) privacyDialog
				.findViewById(R.id.privacy_dialog_title);
		privacyTitle.setText(title);
		btnAll = (Button) privacyDialog
				.findViewById(R.id.privacy_dialog_btn_all);
		
		if("QQ".equals(title) || "手机".equals(title)){
			btnAll.setVisibility(View.GONE);
		}else{
			btnAll.setVisibility(View.VISIBLE);
		}
		btnVip = (Button) privacyDialog
				.findViewById(R.id.privacy_dialog_btn_vip);
		btnFriend = (Button) privacyDialog
				.findViewById(R.id.privacy_dialog_btn_friend);
	}
     class DialogClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.privacy_dialog_btn_all:
				privacyTxt.setText(context.getResources().getString(R.string.privacy_all));
				privacyDialogCancel();
				break;
			case R.id.privacy_dialog_btn_vip:
				privacyTxt.setText(context.getResources().getString(R.string.privacy_vip));
				privacyDialogCancel();
				break;
			case R.id.privacy_dialog_btn_friend:
				privacyTxt.setText(context.getResources().getString(R.string.privacy_friend));
				privacyDialogCancel();
				break;
			}
		}
    	 
     }
     public void privacyDialogShow(){
    	 privacyDialog.show();
     }
     public void privacyDialogCancel(){
    	 privacyDialog.cancel();
     }
}
