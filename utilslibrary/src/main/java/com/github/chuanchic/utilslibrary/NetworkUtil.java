package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * 网络连接状态工具类
 */
public class NetworkUtil {

	/**
	 * 网络是否连接
	 */
	public static boolean networkEnable(Context context){
		try {
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			//检测API是不是小于21，API 21之后该方法被弃用
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
				NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					return true;
				}
			} else {
				Network[] networkArr = connManager.getAllNetworks();//获取所有网络连接的信息
				for (int i = 0; i < networkArr.length; i++) {
					NetworkInfo networkInfo = connManager.getNetworkInfo(networkArr[i]);
					if (networkInfo != null && networkInfo.isConnected()) {
						int networkType = networkInfo.getType();
						if(networkType == ConnectivityManager.TYPE_WIFI || networkType == ConnectivityManager.TYPE_MOBILE){
							return true;
						}
					}

				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
