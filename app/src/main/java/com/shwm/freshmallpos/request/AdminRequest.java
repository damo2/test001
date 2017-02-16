package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class AdminRequest {
	public static HashMap<String, Object> setAdminNickname(String adminName) {
		HashMap<String, Object> hashmap = HttpUtil.setAdminName(adminName);
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
