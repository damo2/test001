package com.shwm.freshmallpos.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.IncomeEntity;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.been.SaleEntity;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OrderRequest {
	private static final String TAG = "OrderRequest";

	public static HashMap<String, Object> getListOrder(int page, String dayNo, int dayNearly) {
		HashMap<String, Object> hashmap = HttpUtil.getListOrder(page, dayNo, dayNearly);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return ListOrderJSON(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> getOrderDetail(int orderId) {
		HashMap<String, Object> hashmap = HttpUtil.getOrderDetail(orderId);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return OrderDetailJSON(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> OrderRefund(String orderNo) {
		HashMap<String, Object> hashmap = HttpUtil.OrderRefund(orderNo);
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return OrderDetailJSON(hashmap, result);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> getIncome() {
		HashMap<String, Object> hashmap = HttpUtil.getIncome();
		if (hashmap == null) {
			return null;
		}
		int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
		String result = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		if (statu == ValueStatu.REQUEST_SUCCESS && !TextUtils.isEmpty(result)) {
			return GetIncomeJson(hashmap, result);
		} else {
			return null;
		}
	}

	private static HashMap<String, Object> ListOrderJSON(HashMap<String, Object> hashMap, String result) {
		try {
			JSONObject json = new JSONObject(result);
			if (json != null) {
				int code = json.optInt("code");
				hashMap.put(ValueKey.RESULT_CODE, code);
				if (code == ValueStatu.SUCCESS) {
					List<OrderEntity> listOrder = new ArrayList<OrderEntity>();
					JSONArray arrayDates = json.optJSONArray("dateList");
					if (arrayDates != null) {
						int sizeDates = arrayDates.length();
						for (int i = 0; i < sizeDates; i++) {
							JSONObject objectDate = arrayDates.optJSONObject(i);
							if (objectDate != null) {
								JSONArray arrayOrders = objectDate.optJSONArray("orderList");
								String date = objectDate.optString("date");
								Double totalDay = StringUtil.getDouble(objectDate.optString("saleTotal"), 0.00);
								if (arrayOrders != null) {
									int sizeOrders = arrayOrders.length();
									for (int j = 0; j < sizeOrders; j++) {
										JSONObject objectOrder = arrayOrders.optJSONObject(j);
										if (objectOrder != null) {
											int id = objectOrder.optInt("id");
											String orderNo = objectOrder.optString("orderNo");
											double money = objectOrder.optDouble("total");
											String payTypeTag = objectOrder.optString("payType");
											String refund = objectOrder.optString("refund");
											String time = objectOrder.optString("time");
											OrderEntity order = new OrderEntity();
											order.setId(id);
											order.setMoney(money);
											order.setTotalDay(totalDay);
											order.setOrderno(orderNo);
											order.setTime(time);
											order.setPayTypeTag(payTypeTag);
											order.setDate(date);
											order.setRefund(refund);
											listOrder.add(order);
										}
									}
								}
							}
						}
					}
					hashMap.put(ValueKey.LISTORDER, listOrder);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashMap;
	}

	private static HashMap<String, Object> OrderDetailJSON(HashMap<String, Object> hashMap, String result) {
		try {
			JSONObject json = new JSONObject(result);
			if (json != null) {
				int code = json.optInt("code");
				hashMap.put(ValueKey.RESULT_CODE, code);
				if (code == ValueStatu.SUCCESS) {
					OrderEntity order = new OrderEntity();

					JSONObject objectOrder = json.optJSONObject("oInfo");
					if (objectOrder != null) {
						String type = objectOrder.optString("type");
						String statu = objectOrder.optString("statu");
						String statuCode = objectOrder.optString("statuCode");
						String ctime = objectOrder.optString("ctime");
						String price = objectOrder.optString("price");
						String payMoney = objectOrder.optString("payMoney");
						String orderNo = objectOrder.optString("orderNo");
						String payType = objectOrder.optString("payType");
						String payTypeInfo = objectOrder.optString("payTypeInfo");
						order.setOrderno(orderNo);
						order.setType(type);
						order.setStatu(statuCode);
						order.setStatuTag(statu);
						order.setTimeCreat(ctime);
						order.setPayMoney(payMoney);
						order.setPayType(payType);
						order.setPayTypeTag(payTypeInfo);
						order.setMoney(price);
					}

					JSONArray arrayFood = json.optJSONArray("oilt");
					List<FoodEntity> listFood = new ArrayList<FoodEntity>();
					if (arrayFood != null) {
						int sizeFood = arrayFood.length();
						for (int i = 0; i < sizeFood; i++) {
							JSONObject objectFood = arrayFood.optJSONObject(i);
							if (objectFood != null) {
								String id = objectFood.optString("id");
								String img = objectFood.optString("img");
								String price = objectFood.optString("price");
								String iprice = objectFood.optString("iprice");
								String nm = objectFood.optString("nm");
								String unit = objectFood.optString("unit");
								String count = objectFood.optString("count");
								String itemType = objectFood.optString("itemType");
								String demo = objectFood.optString("demo");
								String desc = objectFood.optString("desc");
								FoodEntity food = new FoodEntity();
								food.setId(id);
								food.setImg(img);
								food.setPrice(price);
								food.setPriceMember(iprice);
								food.setName(nm);
								food.setUnit(unit);
								food.setNum(count);
								food.setTypeWeight(itemType);
								food.setDesc(desc);
								listFood.add(food);
							}
						}
						// order.setListFood(listFood);
					}
					hashMap.put(ValueKey.ORDER, order);
					hashMap.put(ValueKey.LISTFOOD, listFood);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashMap;
	}

	private static HashMap<String, Object> OrderRefundJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				String msg = object.optString("msg");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}

	private static HashMap<String, Object> GetIncomeJson(HashMap<String, Object> hashmap, String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object != null) {
				int code = object.optInt("code");
				String msg = object.optString("msg");
				hashmap.put(ValueKey.RESULT_CODE, code);
				hashmap.put(ValueKey.RESULT_MSG, msg);
				if (code == ValueStatu.SUCCESS) {
					IncomeEntity income = new IncomeEntity();
					double weekSaleTotal = object.optDouble("weekSaleTotal");
					double monthSaleTotal = object.optDouble("monthSaleTotal");
					double daySaleTotal = object.optDouble("daySaleTotal");
					income.setToday(daySaleTotal);
					income.setWeek(weekSaleTotal);
					income.setMonth(monthSaleTotal);
					JSONArray arraySale = object.optJSONArray("list");
					if (arraySale != null) {
						List<SaleEntity> listSale = new ArrayList<SaleEntity>();
						int size = arraySale.length();
						for (int i = 0; i < size; i++) {
							JSONObject objectSale = arraySale.optJSONObject(i);
							String dayNo = objectSale.optString("dayNo");
							String no = objectSale.optString("no");
							double total = objectSale.optDouble("total");
							SaleEntity sale = new SaleEntity();
							sale.setDay(dayNo);
							sale.setDayTag(no);
							sale.setMoney(total);
							listSale.add(sale);
						}
						income.setListSale(listSale);
					}
					hashmap.put(ValueKey.Income, income);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hashmap;
	}
}
