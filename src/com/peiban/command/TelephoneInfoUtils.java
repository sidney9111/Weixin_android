package com.peiban.command;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 
 * 功能：获取电话信息 <br />
 * 需要加入权限:<br/>
 * android.permission.READ_PHONE_STATE
 * <br/>
 * 日期：2013-1-14<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public final class TelephoneInfoUtils {
	
	/**
	 * 获取手机号码
	 * @param context
	 * @return 
	 *   "" sim卡中没有电话号码信息
	 * 作者:fighter <br />
	 * 创建时间:2013-1-14<br />
	 * 修改时间:<br />
	 */
	public static String getTelephoneNumber(Context context){
		String number = getTelephonyManager(context).getLine1Number();
		return null == number ? "" : number;
	}
	
	
	public static String getIMEI(Context context){
		return getTelephonyManager(context).getDeviceId();
	}
	
	/**
	 * 获取手机SIME码
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-1-14<br />
	 * 修改时间:<br />
	 */
	public static String getSIME(Context context){
		return getTelephonyManager(context).getSimSerialNumber();
	}
	
	/**
	 * 获取手机型号
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-1-14<br />
	 * 修改时间:<br />
	 */
	public static String getPhoneModel(){
		return Build.MODEL;
	}
	
	public static TelephonyManager getTelephonyManager(Context context){
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
}
