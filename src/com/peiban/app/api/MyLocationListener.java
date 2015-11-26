/**
 * @Title: MyLocationListener.java 
 * @Package com.shangwupanlv.app.api 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-31 下午6:08:38 
 * @version V1.0
 */
package com.peiban.app.api;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import android.content.Context;

public class MyLocationListener implements BDLocationListener {
	private Context context;

	public MyLocationListener(Context context) {
      
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

}
