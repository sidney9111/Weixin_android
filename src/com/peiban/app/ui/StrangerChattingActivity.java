package com.peiban.app.ui;

import android.os.Bundle;
import android.view.View;

import com.peiban.R;

/**
 * 
 * 功能：陌生人(陪聊用户.)<br />
 * 日期：2013-5-29<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class StrangerChattingActivity extends ChattingDetailActivity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shangwupanlv.app.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.chatting_info);
		this.baseInit();
		getNetworkAlbum();
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		meInit();
	}
	
	protected void meInit() {
		showApply();
		showInfo();
		getBtnApply1().setVisibility(View.GONE);
		getBtnApply2().setVisibility(View.GONE);
		getBtnAddFriend().setVisibility(View.VISIBLE);
		getBtnSayHello().setVisibility(View.VISIBLE);

		checkUserAuth();
		getRefresh();
	}

	
	protected void getNetworkAlbum() {
		super.getNetworkAlbum();
	}
	
}
