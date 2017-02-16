package com.shwm.freshmallpos.request;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class DeleteRequest {
	private static final String TAG = "DeleteRequest";

	/**
	 * 删除接口
	 * 
	 * @param content_type
	 * @param ids
	 *            id1,id2,id3 拼接
	 * @return
	 */
	public static HashMap<String, Object> Delete(int content_type, String ids) {
		HashMap<String, Object> hashmap = HttpUtil.delete(content_type, ids);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = ManagerDelJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> DeleteClasses(int content_type, String ids) {
		HashMap<String, Object> hashmap = HttpUtil.deleteClasses(content_type, ids);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = ManagerDelJson(hashmap, result);
		}
		return hashmap;
	}

	private static HashMap<String, Object> ManagerDelJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = StringUtil.getInt(object.optString("code", "0"));
				String msg = object.optString("msg");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}
}
