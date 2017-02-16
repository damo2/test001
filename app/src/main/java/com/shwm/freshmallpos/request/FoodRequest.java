package com.shwm.freshmallpos.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class FoodRequest {
	private static final String TAG = "ClassesServise";

	// 获取商品通过类型
	public static HashMap<String, Object> getFoodsByClasses(int page, String tag) {
		HashMap<String, Object> hashmap = HttpUtil.getFoodsByClasses(page, tag);
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return FoodsJSON(hashmap, result);
		} else {
			return null;
		}
	}

	private static HashMap<String, Object> FoodsJSON(HashMap<String, Object> hashMap, String result) {
		List<FoodEntity> listFoods = new ArrayList<FoodEntity>();
		try {
			JSONObject json = new JSONObject(result);
			String rslt = json.optString("code");
			hashMap.put("rslt", rslt);
			if (rslt.equals("1001")) {
				JSONArray array = json.optJSONArray("imlt");
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String foodId = object.optString("id");
						String foodName = object.optString("nm");
						String foodImg = object.optString("img");
						String foodPrice = object.optString("price");
						String foodUnit = object.optString("unit");
						String barcode = object.optString("barcode");
						double priceMember = object.optDouble("price2");
						String typeWeight = object.optString("itemType");
						double price = 0;
						int foodIdI = 0;
						price = StringUtil.getDouble(foodPrice);
						foodIdI = StringUtil.getInt(foodId);
						FoodEntity foods = new FoodEntity();
						foods.setId(foodIdI);
						foods.setName(foodName);
						foods.setImg(foodImg);
						foods.setPrice(price);
						foods.setPriceMember(priceMember);
						foods.setTypeWeight(typeWeight);
						foods.setUnit(foodUnit);
						foods.setBarcode(barcode);
						listFoods.add(foods);
					}
				}
				hashMap.put(ValueKey.LISTFOOD, listFoods);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return hashMap;
	}

}
