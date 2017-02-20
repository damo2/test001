package com.shwm.freshmallpos.model.biz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.inter.IGetCouponListener;
import com.shwm.freshmallpos.util.FileUtil;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;

public class OnCouponListener implements ICouponListener {
	private String TAG = getClass().getSimpleName();
	private FileUtil fileUtil;

	public OnCouponListener() {
		// TODO Auto-generated constructor stub
		fileUtil = new FileUtil();
	}

	@Override
	public void saveCouponDataToFile(List<CouponEntity> listDiscount, List<CouponEntity> listMoneydown) {
		// TODO Auto-generated method stub
		fileUtil.save(ConfigUtil.CouponFileNamePre, couponToJson(listDiscount, listMoneydown));
	}

	@Override
	public void getCouponDataFromFile(IGetCouponListener iGetCouponListener) {
		// TODO Auto-generated method stub
		String json = fileUtil.read(ConfigUtil.CouponFileNamePre);
		jsonToCoupon(json, iGetCouponListener);
	}

	@Override
	public List<CouponEntity> getCouponAll() {
		// TODO Auto-generated method stub
		final List<CouponEntity> listCouponEntities = new ArrayList<CouponEntity>();
		getCouponDataFromFile(new IGetCouponListener() {
			@Override
			public void getMoneydownData(List<CouponEntity> listmoneydown) {
				// TODO Auto-generated method stub
				listCouponEntities.addAll(listmoneydown);
			}

			@Override
			public void getDiscountData(List<CouponEntity> listdiscount) {
				// TODO Auto-generated method stub
				listCouponEntities.addAll(listdiscount);
			}
		});
		return listCouponEntities;
	}

	private String couponToJson(List<CouponEntity> listCouponDiscount, List<CouponEntity> listCouponMoneydown) {
		JSONObject objectData = new JSONObject();
		try {
			JSONArray arrayCouponDiscount = new JSONArray();
			if (listCouponDiscount != null) {
				for (CouponEntity couponDiscount : listCouponDiscount) {
					JSONObject objectCoupon = new JSONObject();
					objectCoupon.put(ValueKey.CouponType, couponDiscount.getType());
					objectCoupon.put(ValueKey.CouponValue, couponDiscount.getDiscount());
					objectCoupon.put(ValueKey.CouponTag, couponDiscount.getTag());
					arrayCouponDiscount.put(objectCoupon);
				}
			}
			objectData.put(ValueKey.CouponTypeDiscount, arrayCouponDiscount);

			JSONArray arrayCouponMoneydown = new JSONArray();
			if (listCouponMoneydown != null) {
				for (CouponEntity couponMoneydown : listCouponMoneydown) {
					JSONObject objectCoupon = new JSONObject();
					objectCoupon.put(ValueKey.CouponType, couponMoneydown.getType());
					objectCoupon.put(ValueKey.CouponValue, couponMoneydown.getMoneydown());
					objectCoupon.put(ValueKey.CouponTag, couponMoneydown.getTag());
					arrayCouponMoneydown.put(objectCoupon);
				}
			}
			objectData.put(ValueKey.CouponTypeMoneydown, arrayCouponMoneydown);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UL.d(TAG, "保存=" + objectData.toString());
		return objectData.toString();
	}

	private void jsonToCoupon(String json, IGetCouponListener iGetCouponListener) {
		if (TextUtils.isEmpty(json)) {
			return;
		}
		UL.d(TAG, "取出=" + json.toString());
		try {
			JSONObject object = new JSONObject(json);
			JSONArray arrayCouponDiscount = object.optJSONArray(ValueKey.CouponTypeDiscount);
			if (arrayCouponDiscount != null) {
				List<CouponEntity> listCouponDiscount = new ArrayList<CouponEntity>();
				for (int i = 0; i < arrayCouponDiscount.length(); i++) {
					JSONObject objectCoupon = arrayCouponDiscount.getJSONObject(i);
					int type = objectCoupon.optInt(ValueKey.CouponType);
					double value = objectCoupon.optDouble(ValueKey.CouponValue);
					String tag = objectCoupon.optString(ValueKey.CouponTag);
					CouponEntity coupon = new CouponEntity();
					coupon.setType(type);
					coupon.setDiscount(value);
					coupon.setTag(tag);
					listCouponDiscount.add(coupon);
				}
				if (iGetCouponListener != null) {
					iGetCouponListener.getDiscountData(listCouponDiscount);
				}
			}

			JSONArray arrayCouponMoneydown = object.optJSONArray(ValueKey.CouponTypeMoneydown);
			if (arrayCouponDiscount != null) {
				List<CouponEntity> listCouponMoneydown = new ArrayList<CouponEntity>();
				for (int i = 0; i < arrayCouponMoneydown.length(); i++) {
					JSONObject objectCoupon = arrayCouponMoneydown.getJSONObject(i);
					int type = objectCoupon.optInt(ValueKey.CouponType);
					double value = objectCoupon.optDouble(ValueKey.CouponValue);
					String tag = objectCoupon.optString(ValueKey.CouponTag);
					CouponEntity coupon = new CouponEntity();
					coupon.setType(type);
					coupon.setMoneydown(value);
					coupon.setTag(tag);
					listCouponMoneydown.add(coupon);
				}
				if (iGetCouponListener != null) {
					iGetCouponListener.getMoneydownData(listCouponMoneydown);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
