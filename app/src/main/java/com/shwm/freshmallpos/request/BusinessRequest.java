package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.text.TextUtils;

import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class BusinessRequest {

	public static HashMap<String, Object> setBusinessName(String businessname) {
		HashMap<String, Object> hashmap = HttpUtil.setBusinessName(businessname);
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

	public static HashMap<String, Object> setBusinessAddress(String businessAddress, double lat, double lng) {
		HashMap<String, Object> hashmap = HttpUtil.setBusinessAddress(businessAddress, lat, lng);
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

	public static HashMap<String, Object> setBusinessLogo(Uri bitmapUri) {
		HashMap<String, Object> hashmap = HttpUtil.setBusinessLogo(bitmapUri);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return UpLogoJson(hashmap, result);
		} else {
			return null;
		}
	}

	private static HashMap<String, Object> UpLogoJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				String code = object.optString("code", "");
				hashmap.put(ValueKey.RESULT_CODE, code);
				if (StringUtil.getInt(code) == ValueStatu.SUCCESS) {
					String img = object.optString("img", "");
					hashmap.put(ValueKey.Image, img);
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
				String code = object.optString("code", "");
				hashmap.put(ValueKey.RESULT_CODE, code);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}
}
