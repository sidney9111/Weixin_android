package com.peiban.app.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RegisterReceiver extends BroadcastReceiver{
	public static final String ACIONT_INTENT = "android.intent.register.COLSE";
	
	private Activity activity;
	
	public RegisterReceiver(Activity activity) {
		super();
		this.activity = activity;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(ACIONT_INTENT.equals(intent.getAction())){
			activity.finish();
		}
	}

}
