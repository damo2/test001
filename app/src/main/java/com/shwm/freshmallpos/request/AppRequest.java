package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.been.VersionEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class AppRequest {
	public static HashMap<String, Object> getUpdateInfo(String pkgName) {
		HashMap<String, Object> hashmap = HttpUtil.getUpdateUrl(pkgName);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return GetUpdateInfoJson(hashmap, result);
		} else {
			return null;
		}
	}

	private static HashMap<String, Object> GetUpdateInfoJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				String verName = object.optString("verName");
				int verCode = object.optInt("verCode");
				String noticeText = object.optString("text");
				int opt = object.optInt("opt");
				VersionEntity version = new VersionEntity();
				version.setCode(verCode);
				version.setName(verName);
				version.setNoticeText(noticeText);
				version.setOpt(opt);
				hashmap.put(ValueKey.Version, version);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

}
