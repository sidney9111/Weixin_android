package com.peiban.app.ui;

import android.content.IntentFilter;
import android.os.Bundle;

import com.peiban.app.receiver.RegisterReceiver;

public abstract class BaseRegisterActivity extends BaseActivity{
	private RegisterReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		receiver = new RegisterReceiver(this);
		
		registerReceiver();
	}
	
	private void registerReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(RegisterReceiver.ACIONT_INTENT);
		
		registerReceiver(receiver, filter);
	}
	
	private void unregisterReceiver(){
		unregisterReceiver(receiver);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver();
	}
}
