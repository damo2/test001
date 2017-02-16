package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OrderSubmitRequest {
	public static HashMap<String, Object> setOrderSubmit(String items, int uid, String memCardNo, String mobile, int payType,
			double receiveMoney) {
		HashMap<String, Object> hashmap = HttpUtil.setOrderSubmit(items, uid, memCardNo, mobile, payType, receiveMoney);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = jsonOrderSubmit(hashmap, result);
		}
		return hashmap;
	}

	private static HashMap<String, Object> jsonOrderSubmit(HashMap<String, Object> hashmap, String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			int code = jsonObject.optInt("code");
			hashmap.put(ValueKey.RESULT_CODE, code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}
}
