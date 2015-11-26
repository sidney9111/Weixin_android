package com.peiban.command;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 功能：网络信息 <br />
 * 日期：2012-12-11<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * 
 */
public class NetworkUtils {

	private static final String TAG = "NetworkUtil";

	/**
	 * 功能: 是否有网络 <br />
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean haveInternet(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return false;
		}
		return true;
	}

	/**
	 * 功能: 是否有网络 <br/>
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isnetWorkAvilable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			Log.e(TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			if (networkInfos != null) {
				for (int i = 0, count = networkInfos.length; i < count; i++) {
					if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 功能: ip地址<br/>
	 * 
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("getLocalIpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 功能: Mac地址
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 功能: WIFI连接状态
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isWiFiActive(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	

	/**
	 * 功能: 是否有多个连接
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean hasMoreThanOneConnection(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			int counter = 0;
			for (int i = 0; i < info.length; i++) {
				if (info[i].isConnected()) {
					counter++;
				}
			}
			if (counter > 1) {
				return true;
			}
		}

		return false;
	}

	/*
	 * HACKISH: These constants aren't yet available in my API level (7), but I
	 * need to handle these cases if they come up, on newer versions
	 */
	public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
	public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
	public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
	public static final int NETWORK_TYPE_IDEN = 11; // Level 8
	public static final int NETWORK_TYPE_LTE = 13; // Level 11

	/**
	 * 功能: 网络连接的状态 <br/>
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 功能:检查是否有快速连接<br />
	 * 
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isConnectedFast(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected() && isConnectionFast(
				info.getType(), info.getSubtype()));
	}

	/**
	 * 功能: 检查连接的速度快不快 <br />
	 * 
	 * @param type
	 * @param subType
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isConnectionFast(int type, int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			System.out.println("CONNECTED VIA WIFI");
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
				// NOT AVAILABLE YET IN API LEVEL 7
			case NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
			case NETWORK_TYPE_HSPAP:
				return true; // ~ 10-20 Mbps
			case NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 功能:ip转换为int <br/>
	 * 
	 * @param ip
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
	}

	/**
	 * 功能: 整型转换为ip
	 * 
	 * @param ipInt
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	
	public static void notWorkToast(Context context){
		Toast.makeText(context, "没有网络", Toast.LENGTH_SHORT).show();
	}
}