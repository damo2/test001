package com.shwm.freshmallpos.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/*
 * 判断网络类型
 */
public class NetUtil {

	public static final int NETWORN_NONE = 0; // 无网络
	public static final int NETWORN_WIFI = 1; // 网络为wifi
	public static final int NETWORN_MOBILE_2G = 2; // 2G网络
	public static final int NETWORN_MOBILE_3G = 3; // 3G网络
	public static int strNetworkType;// 网络状态

	public static int getNetworkState(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
		NetworkInfo gprs = connectivity
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = connectivity
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected()) { // 有网络
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = NETWORN_WIFI;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();

				Log.e("Network", "Network getSubtypeName : " + _strSubTypeName);

				// TD-SCDMA networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by11
					strNetworkType = NETWORN_MOBILE_2G;
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
					// case TelephonyManager.NETWORK_TYPE_EVDO_B://api<9 :
					// replace by 14
					// case TelephonyManager.NETWORK_TYPE_EHRPD: //api<11 :
					// replace by 12
					// case TelephonyManager.NETWORK_TYPE_HSPAP: //api<13 :
					// replace by 15
					strNetworkType = NETWORN_MOBILE_3G;
					break;
				// case TelephonyManager.NETWORK_TYPE_LTE: //api<11 : replace by
				// 13
				// strNetworkType = "4G";
				default:
					// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
					if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
							|| _strSubTypeName.equalsIgnoreCase("WCDMA")
							|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
						strNetworkType = NETWORN_MOBILE_3G;
					} else {
						// strNetworkType = _strSubTypeName;
					}

					break;
				}

				Log.e("Network",
						"Network getSubtype : "
								+ Integer.valueOf(networkType).toString());
			}

		} else if (!gprs.isConnected() && !wifi.isConnected()) {
			strNetworkType = NETWORN_NONE;
		}
		return strNetworkType;
	}

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * 
	 * @param context
	 *            Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}
}
