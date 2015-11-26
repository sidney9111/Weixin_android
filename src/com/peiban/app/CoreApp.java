package com.peiban.app;

import android.app.Application;
import android.content.res.Configuration;

public class CoreApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		System.out.println("内容变化..");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		System.out.println("应用结束...");
	}
	
	
}
