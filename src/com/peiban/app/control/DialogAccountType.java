package com.peiban.app.control;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peiban.R;
import com.peiban.app.Constants;

/**
 * 
 * 功能：账户类型.<br />
 * 日期：2013-5-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class DialogAccountType extends DialogEditDataModel{
	private TextView txtAccountType;
	
	private int[] btnId = new int[]{
			R.id.dialog_account_type_btn_chatting,
			R.id.dialog_account_type_btn_withchat
	};
	
	private int[] btnStr = new int[]{
			R.string.accout_chatiing,
			R.string.accout_withchat
	};
	
	private View.OnClickListener l;
	
	public DialogAccountType(Context context, TextView txtAccountType) {
		super(context);
		
		this.txtAccountType = txtAccountType;
		l = new DialogAccountListener();
		this.initWeightw();
		this.setTitle(R.string.register_grzl_accout_type);
	}


	private void initWeightw() {
		makeButton(btnId, btnStr, l);
	}
	

	void chattingAction(View v){
		this.txtAccountType.setText(((Button)v).getText());
		this.txtAccountType.setTag(Constants.CustomerType.CHATTING);
		cancel();
	}
	
	void withchat(View v){
		this.txtAccountType.setText(((Button)v).getText());
		this.txtAccountType.setTag(Constants.CustomerType.WITHCHAT);
		cancel();
	}
	
	class DialogAccountListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.dialog_account_type_btn_chatting:
				chattingAction(v);
				break;
			case R.id.dialog_account_type_btn_withchat:
				withchat(v);
				break;

			default:
				break;
			}
		}
		
	}
}
