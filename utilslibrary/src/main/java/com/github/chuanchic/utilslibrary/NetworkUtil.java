package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络信息工具类
 */
public class NetworkUtil {

	public static final int NETWORK_MOBILE_BUT_CONNECTED = -2;//mobile网络（已连接，区分不了2G，3G，4G...）
	public static final int NETWORK_UNKNOWN_BUT_CONNECTED = -1;//未识别网络（已连接）
	public static final int NETWORK_UNKNOWN_NOT_CONNECTED = 0;//未识别网络（未连接）
	public static final int NETWORK_WIFI = 1;//WiFi连接
	public static final int NETWORK_2_G = 2;//2G连接
	public static final int NETWORK_3_G = 3;//3G连接
	public static final int NETWORK_4_G = 4;//4G连接

	public static final int WIFI_LEVEL_0 = 0;//WiFi最好
	public static final int WIFI_LEVEL_1 = 1;//WiFi偏好
	public static final int WIFI_LEVEL_2 = 2;//WiFi偏差
	public static final int WIFI_LEVEL_3 = 3;//WiFi最差

	/**
	 * 获取网络
	 */
	public static int getNetwork(Context context, NetworkInfo networkInfo) {
		try {
			if (networkInfo != null && networkInfo.isConnected() && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
				int type = networkInfo.getType();
				if (type == ConnectivityManager.TYPE_WIFI) {
					return NETWORK_WIFI;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					int networkType = telephonyManager.getNetworkType();
					if(TelephonyManager.NETWORK_TYPE_GPRS == networkType ||
					   TelephonyManager.NETWORK_TYPE_EDGE == networkType ||
					   TelephonyManager.NETWORK_TYPE_CDMA == networkType ||
					   TelephonyManager.NETWORK_TYPE_1xRTT == networkType ||
					   TelephonyManager.NETWORK_TYPE_IDEN == networkType){
						return NETWORK_2_G;
					}else if(TelephonyManager.NETWORK_TYPE_UMTS == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_0 == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_A == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSDPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSUPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_B == networkType ||
							 TelephonyManager.NETWORK_TYPE_EHRPD == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSPAP == networkType){
						return NETWORK_3_G;
					}else if(TelephonyManager.NETWORK_TYPE_LTE == networkType){
						return NETWORK_4_G;
					}else{
						return NETWORK_MOBILE_BUT_CONNECTED;
					}
				} else {
					return NETWORK_UNKNOWN_BUT_CONNECTED;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NETWORK_UNKNOWN_NOT_CONNECTED;
	}

	/**
	 * 获取活动的网络信息
	 */
	public static NetworkInfo getActiveNetworkInfo(Context context){
		NetworkInfo networkInfo = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			networkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return networkInfo;
	}

	/**
	 * 获取网络
	 */
	public static int getNetwork(Context context) {
		return getNetwork(context, getActiveNetworkInfo(context));
	}

	/**
	 * true当前网络可用，false不可用
	 */
	public static boolean networkEnable(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if (networkInfo != null && networkInfo.isConnected() && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
			return true;
		}
		return false;
	}

	/**
	 * WiFi是否可用
	 */
	public static boolean isWifiAvailable(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
			return true;
		}
		return false;
	}

}
