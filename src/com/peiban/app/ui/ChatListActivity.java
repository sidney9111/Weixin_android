package com.peiban.app.ui;

import com.peiban.R;

import android.os.Bundle;
/**
 * 
 * 功能：会话列表 <br />
 * 日期：2013-5-30<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ChatListActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.authorize_list_item);
		baseInit();
	}
	
	@Override
	protected void initTitle() {
		
	}
}
