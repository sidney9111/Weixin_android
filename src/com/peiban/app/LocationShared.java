/**
 * @Title: LocationShared.java 
 * @Package com.shangwupanlv.app 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-1 下午12:02:25 
 * @version V1.0
 */
package com.peiban.app;

import com.peiban.SharedStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class LocationShared {
	private SharedPreferences locsSharedPreferences;
	private Editor locEditor;
	/**
	 * sharedprefence文件key名 location.getLatitude() location.getLongitude()
	 * */
	public static final String LOCATION_ADDR = "loc_addr";
	public static final String LOCATION_LAT = "loc_lat";
	public static final String LOCATION_LON = "loc_lon";
	public static final String LOCATION_CITY = "loc_city";
	/**
	 * 属性相关
	 * */
	private String locationlat;
	private String locationlon;
	private String locationaddr;
	private String locationcity;
	
	private static LocationShared instance;

	private LocationShared(Context context) {
		locsSharedPreferences = context.getSharedPreferences(
				SharedStorage.SHARED_APP_NAME, Context.MODE_PRIVATE);
		locEditor = locsSharedPreferences.edit();
	}
	
	public synchronized static LocationShared getInstance(Context context){
		if(instance == null){
			instance = new LocationShared(context);
		}
		
		return instance;
	}

	public String getLocationaddr() {
		if (TextUtils.isEmpty(locationaddr)) {
			this.locationaddr = locsSharedPreferences.getString(LOCATION_ADDR,
					"");
		}
		return locationaddr;
	}

	public void setLocationaddr(String locationaddr) {
		locEditor.putString(LOCATION_ADDR, locationaddr);
		this.locationaddr = locationaddr;
	}

	public String getLocationcity() {
		if (TextUtils.isEmpty(locationcity)) {
			this.locationcity = locsSharedPreferences.getString(LOCATION_CITY,
					"");
		}
		return locationcity;
	}
	public void setLocationcity(String locationcity) {
		if (!TextUtils.isEmpty(locationcity)) {
			locEditor.putString(LOCATION_CITY, locationcity);
		}
		this.locationcity = locationcity;
	}

	public String getLocationlat() {
		if (TextUtils.isEmpty(locationlat)) {
			this.locationlat = locsSharedPreferences.getString(LOCATION_LAT,
					"");
		}
		return locationlat;
	}

	public String getLocationlon() {
		if (TextUtils.isEmpty(locationlon)) {
			this.locationlon = locsSharedPreferences.getString(LOCATION_LON,
					"");
		}
		return locationlon;
	}

	public void setLocationlat(String locationlat) {
		if (!TextUtils.isEmpty(locationlat)) {
			locEditor.putString(LOCATION_LAT, locationlat);
		}
		this.locationlat = locationlat;
	}

	public void setLocationlon(String locationlon) {
		if (!TextUtils.isEmpty(locationlon)) {
			locEditor.putString(LOCATION_LON, locationlon);
		}
		this.locationlon = locationlon;
	}

	public void commitLoc() {
		locEditor.commit();
	}
}
