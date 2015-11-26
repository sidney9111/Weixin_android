package com.peiban;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 共享存储信息
 * 功能：<br />
 * 日期：2013-5-5<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public final class SharedStorage {
	/** 当读取Shared文件中的内容失败后,将发送该广播  */
	public static final String ACTION_SHARED_READ_ERR = "com.shared.storage.READERROR";
	
	/** 应用信息存储位置 */
	public static final String SHARED_APP_NAME = "shared_core";
	/** 应用配置信息存储位置 */
	public static final String SHARED_CONFIG_NAME = "shared_config";
	/** 应用缓存信息存储位置 */
	public static final String SHARED_CACHE_NAME = "shared_cache";
	
	public static SharedPreferences getSharedPreferences(Context context, String sharedName){
		return context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
	}
	/**
	 * 缓存共享区
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-5<br />
	 * 修改时间:<br />
	 */
	public static SharedPreferences getCacheSharedPreferences(Context context){
		return context.getSharedPreferences(SHARED_CACHE_NAME, Context.MODE_PRIVATE);		
	}
	/**
	 * 配置共享区
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-5<br />
	 * 修改时间:<br />
	 */
	public static SharedPreferences getConfigSharedPreferences(Context context){
		return context.getSharedPreferences(SHARED_CONFIG_NAME, Context.MODE_PRIVATE);
	}
	/**
	 * 应用共享区
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-5<br />
	 * 修改时间:<br />
	 */
	public static SharedPreferences getCoreSharedPreferences(Context context){
		return context.getSharedPreferences(SHARED_APP_NAME, Context.MODE_PRIVATE);
	}
	
	
}
