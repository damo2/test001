package com.shwm.freshmallpos.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.value.ValueType;

public class MemberRequest {
	private static final String TAG = "MemberRequest";

	public static HashMap<String, Object> getMemberList(int page, String like) {
		HashMap<String, Object> hashmap = HttpUtil.getMemberList(page, like);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = MemberListJson(hashmap, result);
		}
		return hashmap;
	}

	public static HashMap<String, Object> addMember(String tel, String name, String cardno, String cardtype, String discount,
			String shopname) {
		HashMap<String, Object> hashmap = HttpUtil.setAddMember(tel, name, cardno, cardtype, discount, shopname);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			hashmap = MemberListJson(hashmap, result);
		}
		return hashmap;
	}

	private static HashMap<String, Object> MemberListJson(HashMap<String, Object> hashmap, String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			int code = jsonObject.optInt("code");
			hashmap.put(ValueKey.RESULT_CODE, code);
			if (code == ValueStatu.SUCCESS) {
				List<MemberEntity> listMember = new ArrayList<MemberEntity>();
				JSONArray array = jsonObject.optJSONArray("list");
				if (array != null && array.length() > 0) {
					int size = array.length();
					for (int i = 0; i < size; i++) {
						JSONObject objectMember = array.optJSONObject(i);
						int id = objectMember.optInt("id");
						String mobi = objectMember.optString("mobi");
						String nick = objectMember.optString("nick");
						double money = objectMember.optDouble("money");
						String cardNo = objectMember.optString("cardNo");
						String cardType = objectMember.optString("cardType");
						double discount = objectMember.optDouble("discount");
						MemberEntity member = new MemberEntity();
						member.setId(id);
						member.setNick(nick);
						member.setTel(mobi);
						member.setMoney(money);
						member.setCardno(cardNo);
						member.setCardtype(cardType);
						CouponEntity couponEntity = new CouponEntity(ValueType.CouponType_DiscountMember);
						couponEntity.setDiscountRate(discount);
						member.setCoupon(couponEntity);
						listMember.add(member);
					}
				}
				hashmap.put(ValueKey.LISTMEMBER, listMember);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> ResultJson(HashMap<String, Object> hashmap, String json) {
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
