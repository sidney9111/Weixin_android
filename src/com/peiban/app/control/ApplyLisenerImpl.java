package com.peiban.app.control;

import com.peiban.R;

import android.app.Activity;
import android.view.View;
/**
 * 
 * 功能： 申请 布局，申请查看和我要评价按钮功能
 * 添加好友, 打招呼 <br />
 * 日期：2013-5-28<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ApplyLisenerImpl implements View.OnClickListener{
	private Activity activity;
	
	public ApplyLisenerImpl(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.details_apply_btn1:
			applyAction();
			break;
		case R.id.details_apply_btn2:
			evaluateAction();
			break;
		case R.id.details_btn_say_hello:
			greetingAction();
			break;
		case R.id.details_btn_add_friend:
			addFriendAction();
			break;

		default:
			break;
		}
	}
	
	// 打招呼
	void greetingAction(){}
	
	// 添加好友
	void addFriendAction(){}
	
	// 评价
	void evaluateAction(){}
	
	// 申请查看
	void applyAction(){}
}
