/**
 * @Title: DistanceShared.java 
 * @Package com.shangwupanlv.app.ui.common 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-27 下午2:55:06 
 * @version V1.0
 */
package com.peiban.app.ui.common;

import com.peiban.SharedStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class DistanceShared {
	/**
	 * 距离key
	 * */
	private static final String DISTANCE_KEY="distance";
	private SharedPreferences sharedPreferences;
	private String distancevalue;
	
	public DistanceShared(Context context) {
    sharedPreferences=SharedStorage.getConfigSharedPreferences(context);
	}

	public void setDistancevalue(String distancevalue) {
		if(!TextUtils.isEmpty(distancevalue)){
			Editor editor=sharedPreferences.edit();
			editor.putString(DISTANCE_KEY, distancevalue);
			editor.commit();
		}
		this.distancevalue = distancevalue;
	}

	public String getDistancevalue() {
		if(TextUtils.isEmpty(distancevalue)){
			distancevalue=sharedPreferences.getString(DISTANCE_KEY, "50");
		}
		return distancevalue;
	}
	
}
