package com.shwm.freshmallpos.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

/**
 * 分类管理
 * 
 * @author wr 2016-12-14
 */
public class ClassesRequest {
	private static final String TAG = "ClassesRequest";

	// 得到头部分类
	public static HashMap<String, Object> getClasses() {
		HashMap<String, Object> hashmap = HttpUtil.getClasses();
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return ClassesJSON(hashmap, result);
		} else {
			return null;
		}
	}

	// 添加分类
	public static HashMap<String, Object> addClasses(int pid, String className, int classLv) {
		HashMap<String, Object> hashmap = HttpUtil.addClasses(pid, className, classLv);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = ResultJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> editClasses(int id, int pid, String className, int classLv) {
		HashMap<String, Object> hashmap = HttpUtil.editClasses(id, pid, className, classLv);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = ResultJson(hashmap, result);
		}
		return hashmap;
	}

	private static HashMap<String, Object> ClassesJSON(HashMap<String, Object> hashMap, String result) {
		List<ClassesEntity> listClasses = new ArrayList<ClassesEntity>();
		try {
			JSONObject json = new JSONObject(result);
			// imlt
			JSONArray array = json.optJSONArray("imlt");
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String classesId = object.optString("id");
					String classesName = object.optString("nm");
					String isLast = object.optString("isLast");
					ClassesEntity classes = new ClassesEntity();
					classes.setId(classesId);
					classes.setName(classesName);
					classes.setFirst(true);
					classes.setIsLast(isLast);
					classes.setLv(1);
					JSONArray arrayTwo = object.optJSONArray("two");
					if (arrayTwo != null && arrayTwo.length() > 0) {
						for (int j = 0; j < arrayTwo.length(); j++) {
							JSONObject objectTwo = arrayTwo.getJSONObject(j);
							String classTwoId = objectTwo.optString("id");
							String classTwoName = objectTwo.optString("nm");
							String classTwoImg = objectTwo.optString("img");
							ClassesEntity classesTwo = new ClassesEntity();
							classesTwo.setId(classTwoId);
							classesTwo.setName(classTwoName);
							classesTwo.setImg(classTwoImg);
							classesTwo.setSupId(classesId);
							classes.setLv(2);
							classes.getListSub().add(classesTwo);
						}
					}
					listClasses.add(classes);
				}
			}
			hashMap.put("listClasses", listClasses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashMap;
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
