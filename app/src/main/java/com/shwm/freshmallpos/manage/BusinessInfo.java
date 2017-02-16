package com.shwm.freshmallpos.manage;

import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;

public class BusinessInfo {

	public static String getServerIp() {
		return UtilSPF.getString(ValueKey.Business_IP, "");
	}

	public static String getBusinessName() {
		return UtilSPF.getString(ValueKey.Business_NAME, "");
	}

	public static Integer getBusinessID() {
		return UtilSPF.getInt(ValueKey.Business_ID, -1);
	}

	public static Integer getAdminID() {
		return UtilSPF.getInt(ValueKey.ADMIN_ID, -1);
	}

	public static Integer getAdminType() {
		return UtilSPF.getInt(ValueKey.ADMIN_TYPE, -1);
	}

	public static String getAdminName() {
		return UtilSPF.getString(ValueKey.ADMIN_USERNAME, "");
	}

	public static String getPassword() {
		return UtilSPF.getString(ValueKey.ADMIN_PASSWORD, "");
	}

	public static String getAdminNickname() {
		return UtilSPF.getString(ValueKey.ADMIN_NIKENAME, "");
	}

	public static String getBusinessAddress() {
		return UtilSPF.getString(ValueKey.Business_ADDRESS, "");
	}

	public static String getBusinessLogo() {
		return UtilSPF.getString(ValueKey.Business_LOGO, "");
	}

	public static void setAdminNickname(String nickname) {
		UtilSPF.putString(ValueKey.ADMIN_NIKENAME, nickname);
	}

	public static void setBusinessName(String businessName) {
		UtilSPF.putString(ValueKey.Business_NAME, businessName);
	}

	public static void setBusinessAddress(String address) {
		UtilSPF.putString(ValueKey.Business_ADDRESS, address);
	}

	public static void setBusinessLogo(String logo) {
		UtilSPF.putString(ValueKey.Business_LOGO, logo);
	}

}
