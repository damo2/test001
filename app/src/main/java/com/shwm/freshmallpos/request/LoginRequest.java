package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import com.shwm.freshmallpos.been.AdminEntity;
import com.shwm.freshmallpos.been.BusinessEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class LoginRequest {
	private static final String TAG = "LoginRequest";

	public static HashMap<String, Object> log(String username, String password) {
		HashMap<String, Object> hashmap = HttpUtil.getLogUrl(username, password);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return LogReginJson(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> getCode(String mobi) {
		HashMap<String, Object> hashmap = HttpUtil.getCode(mobi);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return GetCodeJson(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> regin(String mobi, String code, String password, String storeName, String storeAddr,
			String storeDesc, String storeContact, double lat, double lng) {
		HashMap<String, Object> hashmap = HttpUtil.regin(mobi, code, password, storeName, storeAddr, storeDesc, storeContact, lat, lng);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return LogReginJson(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> changePassword(int adminId, String adminName, String pwdOld, String pwdNew) {
		HashMap<String, Object> hashmap = HttpUtil.changePassword(adminId, adminName, pwdOld, pwdNew);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return ResultJson(hashmap, result);
		} else {
			return null;
		}
	}

	private static HashMap<String, Object> GetCodeJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				String msg = object.optString("msg", "");
				String vcode = object.optString("vcode");
				String cookie = object.optString("cookie");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
				hashmap.put(ValueKey.Vcode, vcode);
				UtilSPF.putString("cookie", cookie);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> LogReginJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				String msg = object.optString("msg", "");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
				if (code == ValueStatu.SUCCESS) {
					AdminEntity admin = new AdminEntity();
					JSONObject objectUser = object.optJSONObject("admin");
					if (objectUser != null) {
						String id = objectUser.optString("id");
						String userName = objectUser.optString("userName");
						String adminType = objectUser.optString("adminType");
						String businessId = objectUser.optString("businessId");
						String serverUrl = objectUser.optString("serverUrl");
						String partner = objectUser.optString("partner");
						String name = objectUser.optString("name");
						String storeName = objectUser.optString("storeName");
						String addr = objectUser.optString("addr");
						String logo = objectUser.optString("logo");
						admin.setId(id);
						admin.setUsername(userName);
						admin.setNickname(name);
						admin.setType(adminType);

						BusinessEntity business = new BusinessEntity();
						business.setId(businessId);
						business.setName(storeName);
						business.setPartner(partner);
						business.setServerIp(serverUrl);
						business.setAddr(addr);
						business.setImg(logo);

						admin.setBusiness(business);
					}
					hashmap.put(ValueKey.Admin, admin);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> ResultJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				String msg = object.optString("msg", "");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

}
