package com.shwm.freshmallpos.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.ImageEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.value.ValueType;

public class FoodManageRequests {
	private static final String TAG = "FoodManageRequest";

	public static HashMap<String, Object> setAddFood(String name, String barcode, String typeTag, double price, double priceMember,
			String unit, double numSum, int type, String comefrom, String desc, String eatIds, String eatIdsDel) {
		HashMap<String, Object> hashmap = HttpUtil.setFoodAdd(name, barcode, typeTag, price, priceMember, unit, numSum, type, comefrom,
				desc, eatIds, eatIdsDel);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = AddFoodJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> setEditFood(int foodId, String name, String barcode, String typeTag, double price,
			double priceMember, String unit, double numSum, int type, String comefrom, String desc, String eatIds, String eatIdsDel) {
		HashMap<String, Object> hashmap = HttpUtil.setFoodEdit(foodId, name, barcode, typeTag, price, priceMember, unit, numSum, type,
				comefrom, desc, eatIds, eatIdsDel);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = EditJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> getFoodByName(String foodname, int page) {
		HashMap<String, Object> hashmap = HttpUtil.getFoodByName(foodname, page);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = searchFoodByNameJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> getFoodDetail(int itemId) {
		HashMap<String, Object> hashmap = HttpUtil.getFoodDetail(itemId);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = FoodDetailJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> addFoodImg(int itemId, int index, Bitmap bitmap) {
		HashMap<String, Object> hashmap = HttpUtil.addFoodImage(itemId, index, bitmap);
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

	public static HashMap<String, Object> addFoodImg(int itemId, int index, Uri bitmapUri) {
		HashMap<String, Object> hashmap = HttpUtil.addFoodImage(itemId, index, bitmapUri);
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

	public static HashMap<String, Object> getFoodByCode(String code) {
		HashMap<String, Object> hashmap = HttpUtil.getFoodByCode(code);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = FoodByCodeJson(hashmap, result);
		}
		return hashmap;
	}

	private static HashMap<String, Object> AddFoodJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				String code = object.optString("code", "");
				String itemId = object.optString("itemId", "");
				String msg = object.optString("result");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.ItemId, itemId);
				hashmap.put(ValueKey.RESULT_MSG, msg);
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
				String msg = object.optString("result");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> EditJson(HashMap<String, Object> hashmap, String result) {
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

	private static HashMap<String, Object> FoodDetailJson(HashMap<String, Object> hashmap, String result) {
		FoodEntity food = new FoodEntity();
		try {
			JSONObject json = new JSONObject(result);
			String rslt = json.optString("code");
			hashmap.put(ValueKey.RESULT_CODE, rslt);
			// if (rslt.equals(UtilConstant.Success + "")) {
			String id = json.optString("id");
			String nm = json.optString("nm");
			String desc = json.optString("desc");
			String price = json.optString("price");
			String priceMember = json.optString("memPrice");
			String unit = json.optString("unit");
			String statu = json.optString("statu");
			String classtag = json.optString("tag");
			String itemType = json.optString("itemType");
			String comefrom = json.optString("comefrom");
			String barcode = json.optString("barcode");
			String numSum = json.optString("leftCount");
			JSONArray array = json.optJSONArray("imgs");
			if (array != null && array.length() > 0) {
				List<ImageEntity> listImg = new ArrayList<ImageEntity>();
				int size = array.length();
				for (int i = 0; i < size; i++) {
					JSONObject objectImg = array.getJSONObject(i);
					String img = objectImg.optString("img");
					String indexS = objectImg.optString("i");
					ImageEntity image = new ImageEntity();
					image.setImg(img);
					image.setTypeNet(ValueType.IMAGE_NET);
					image.setIndex(indexS);
					listImg.add(image);
					if (i == 0) {
						food.setImg(img);
					}
				}
				food.setListImg(listImg);
			}

			food.setId(id);
			food.setName(nm);
			food.setDesc(desc);
			food.setPrice(price);
			food.setPriceMember(priceMember);
			food.setUnit(unit);
			food.setStatu(statu);
			food.setTypeWeight(itemType);
			food.setNumsum(numSum);
			food.setFrom(comefrom);
			food.setBarcode(barcode);
			food.setTag(classtag);
			hashmap.put(ValueKey.FOOD, food);
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> FoodByCodeJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				hashmap.put(ValueKey.RESULT_CODE, code);
				if (code == ValueStatu.SUCCESS) {
					FoodEntity food = new FoodEntity();
					String id = object.optString("id");
					String nm = object.optString("nm");
					String tag = object.optString("tag");
					String comefrom = object.optString("comefrom");
					String desc = object.optString("desc");
					String price = object.optString("price");
					String barcode = object.optString("barcode");
					String memPrice = object.optString("memPrice");
					String unit = object.optString("unit");
					String statu = object.optString("statu");
					int itemType = object.optInt("itemType");
					food.setId(id);
					food.setName(nm);
					food.setTag(tag);
					food.setFrom(comefrom);
					food.setDesc(desc);
					food.setPrice(price);
					food.setPriceMember(memPrice);
					food.setBarcode(barcode);
					food.setUnit(unit);
					food.setStatu(statu);
					food.setTypeWeight(itemType);
					JSONArray arrayImg = object.optJSONArray("imgs");
					if (arrayImg != null) {
						List<ImageEntity> listImg = new ArrayList<ImageEntity>();
						int sizeImg = arrayImg.length();
						for (int i = 0; i < sizeImg; i++) {
							JSONObject objectImg = arrayImg.optJSONObject(i);
							if (objectImg != null) {
								String img = objectImg.optString("img");
								String item = objectImg.optString("i");
								ImageEntity image = new ImageEntity();
								image.setIndex(item);
								image.setImg(img);
								listImg.add(image);
							}
						}
						food.setListImg(listImg);
					}
					hashmap.put(ValueKey.FOOD, food);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> searchFoodByNameJson(HashMap<String, Object> hashMap, String result) {
		List<FoodEntity> listFood = new ArrayList<FoodEntity>();
		try {
			JSONObject json = new JSONObject(result);
			int code = StringUtil.getInt(json.optString("code"));
			hashMap.put(ValueKey.RESULT_CODE, code);
			if (code == ValueStatu.SUCCESS) {
				JSONArray array = json.optJSONArray("list");
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String id = object.optString("id");
						String name = object.optString("name");
						String unit = object.optString("unit");
						String price = object.optString("price");
						String img = object.optString("img");
						String desc = object.optString("desc");
						String count = object.optString("count");
						String sellcount = object.optString("saledCount");
						FoodEntity food = new FoodEntity();
						food.setId(id);
						food.setName(name);
						food.setPrice(price);
						food.setUnit(unit);
						food.setImg(img);
						food.setDesc(desc);
						food.setNumsum(count);
						food.setNumSell(sellcount);
						listFood.add(food);
					}
				}
			}
			hashMap.put(ValueKey.LISTFOOD, listFood);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return hashMap;
	}
}
