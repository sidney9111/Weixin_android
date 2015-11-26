package com.peiban.app.ui;

import android.os.Bundle;
import android.view.View;

import com.peiban.R;

public class StrangerFindPartnerActivity extends FindPartnerDetailActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shangwupanlv.app.ui.FindPartnerDetailActivity#baseInit()
	 */
	@Override
	protected void baseInit() {
		super.baseInit();
		meInit();
	}
	
	protected void meInit() {
		showApply();
		getBtnApply1().setVisibility(View.GONE);
		getBtnApply2().setVisibility(View.GONE);
		getBtnAddFriend().setVisibility(View.VISIBLE);
		getBtnSayHello().setVisibility(View.VISIBLE);
		checkUserAuth();
		getRefresh();
	}

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
		baseInit();
		getNetworkAlbum();
	}

	protected void getNetworkAlbum() {
		super.getNetworkAlbum();
	}}
