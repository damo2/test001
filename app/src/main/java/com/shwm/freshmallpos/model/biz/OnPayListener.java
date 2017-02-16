package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.OrderSubmitRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnPayListener implements IPayListener {

	@Override
	public void payInCash(int payType, MemberEntity member, List<FoodEntity> listcart, double moneyReceive,
			IRequestListener<HashMap<String, Object>> iRequestListener) {
		// TODO Auto-generated method stub
		String items = "";
		int uid = 0;
		String memCardNo = "";
		String mobile = "";
		if (member != null) {
			uid = member.getId();
			memCardNo = member.getCardno();
			mobile = member.getTel();
		}
		if (listcart != null) {
			try {
				JSONArray array = new JSONArray();
				for (FoodEntity food : listcart) {
					JSONObject objectFood = new JSONObject();
					int foodId = food.getId();
					if (foodId > 0) {
						objectFood.put("id", food.getId());
						objectFood.put("name", food.getName());
						objectFood.put("unit", food.getUnit());
						objectFood.put("price", food.getPrice());
						objectFood.put("count", food.getNum());
					} else {
						objectFood.put("id", ValueFinal.foodNoCodeId);
						objectFood.put("name", food.getName());
						objectFood.put("unit", ValueFinal.foodNoCodeUnit);
						objectFood.put("price", food.getPrice());
						objectFood.put("count", food.getNum());
					}
					array.put(objectFood);
				}
				if (array != null) {
					items = array.toString();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		requstOrderSubmit(items, uid, memCardNo, mobile, payType, moneyReceive, iRequestListener);
	}

	private void requstOrderSubmit(final String items, final int uid, final String memCardNo, final String mobile, final int payType,
			final double receiveMoney, final IRequestListener<HashMap<String, Object>> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int rslt = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				if (rslt == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(hashmap);
				} else {
					String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
					iRequestListener.onFail(rslt, new ExceptionUtil(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(-1);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return OrderSubmitRequest.setOrderSubmit(items, uid, memCardNo, mobile, payType, receiveMoney);
			}
		}).execute();
	}

}
