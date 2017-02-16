package com.shwm.freshmallpos.sys;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class SysInfo {
	private static String IMEI;
	private static String IMSI;
	private static String PLMN;
	
	private static int screenWidth;
	private static int screenHeight;
	private static int densityDpi;
	
	public static String getIMEI() {
		return IMEI;
	}

	public static void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public static String getIMSI() {
		return IMSI;
	}

	public static void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public static String getPLMN() {
		return PLMN;
	}

	public static void setPLMN(String pLMN) {
		PLMN = pLMN;
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		SysInfo.screenWidth = screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		SysInfo.screenHeight = screenHeight;
	}

	public static int getDensityDpi() {
		return densityDpi;
	}

	public static void setDensityDpi(int densityDpi) {
		SysInfo.densityDpi = densityDpi;
	}

	public static String getBuildID(){
		return android.os.Build.ID;
	}
	
	public static String getProduct(){
		return android.os.Build.PRODUCT;
	}
	
	public static String getBoard(){
		return android.os.Build.BOARD;
	}
	
	public static String getOSNo(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static int getSDK(){
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static void getSysParams(Activity activity) {
		  DisplayMetrics dm = new DisplayMetrics();
		  activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		  screenWidth = dm.widthPixels;
		  screenHeight = dm.heightPixels;
		  densityDpi = dm.densityDpi;	
		  		  
		  TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		  setIMEI(tm.getDeviceId());
		  setIMSI(tm.getSubscriberId());
		  setPLMN(tm.getNetworkOperatorName());
		  		  
	}
	
	public static void getScreenParams(Activity activity) {
		  DisplayMetrics dm = new DisplayMetrics();
		  activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		  screenWidth = dm.widthPixels;
		  screenHeight = dm.heightPixels;
		  densityDpi = dm.densityDpi;	  
	}

}
