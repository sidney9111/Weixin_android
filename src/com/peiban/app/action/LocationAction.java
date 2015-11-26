/**
 * @Title: LocationAction.java 
 * @Package com.shangwupanlv.app.action 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-1 上午10:24:23 
 * @version V1.0
 */
package com.peiban.app.action;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.peiban.app.LocationShared;

public class LocationAction {
	private static final String TAG = LocationAction.class.getCanonicalName();
	private Context activitycontext;
	private LocationClient locationClient = null;
	private PanlvLocation panlvLocation;
	private String addr, lat, lon,city;
	public LocationAction(Context context, Context activitycontext) {
		locationClient = new LocationClient(context);
		panlvLocation = new PanlvLocation();
		this.activitycontext = activitycontext;
		locationClient.registerLocationListener(panlvLocation);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);// 设置发起定位请求的间隔时间为30分钟
		option.disableCache(true);// 禁止启用缓存定位
		option.setServiceName("com.baidu.location.service_v3.3");
		option.setPriority(LocationClientOption.NetWorkFirst);
		locationClient.setLocOption(option);
		locationClient.requestLocation();
		
	}

	public  void locationStart() {
		Log.d(TAG, "locationStart()");
		locationClient.start();
	}
     public class PanlvLocation implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				if (!"".equals(location.getAddrStr())
						|| location.getAddrStr() != null) {
					city=location.getCity();
					addr = location.getAddrStr();
					lat = String.valueOf(location.getLatitude());
					lon = String.valueOf(location.getLongitude());
					setShared();

				}
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
		}
	}

	public void locationStop() {
		Log.d(TAG, "locationStop()");
		locationClient.stop();
	}

	/** 将获取到的位置信息保存到缓存 */
	protected void setShared() {
		// TODO Auto-generated method stub
		LocationShared locationShared = LocationShared.getInstance(activitycontext);
		locationShared.setLocationcity(city);
		locationShared.setLocationaddr(addr);
		locationShared.setLocationlat(lat);
		locationShared.setLocationlon(lon);
		locationShared.commitLoc();
		locationStop();
	}
}
