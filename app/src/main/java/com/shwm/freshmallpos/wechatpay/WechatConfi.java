package com.shwm.freshmallpos.wechatpay;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.manage.BusinessInfo;

public class WechatConfi {
	public static final String WX_APP_ID = "wx851133c2cb199099";
	public static final String WX_PARTNER_ID = "1363353302";
	public static final String WX_SECURITY_KEY = "vidreamfreshmallvidreamfreshmall";
	public static final String WX_CreateIp = "127.0.0.1";// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	public static final String WX_NotifyUrl = "www.shwm.xxx";// 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	public static final String WX_TradeType = "NATIVE";// 交易类型
	public static final String WX_Body = "新鲜收银-商店";// 商品简单描述(店名-销售商品类目) eg:天虹南山店-超市 String(128)
	
	public static final String WX_URL_Paymicro = "https://api.mch.weixin.qq.com/pay/micropay";// 刷卡支付接口，扫用户
	public static final String WX_URL_PayUnifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 扫码支付统一下单，扫商户

	// 订单格式rec_adminId_时间戳_money
	public static String getOrderNoForRecharge(int totalFee) {
		return "rec_" + BusinessInfo.getAdminID() + "_" + System.currentTimeMillis() + "_" + totalFee;
	}

	public static String getProductId() {
		return BusinessInfo.getAdminID() + "000" + System.currentTimeMillis();
	}

	// 商户定义的商品id 或者订单号
	public static String getTimestamp() {
		return "123";
	}

	/**
	 * <p>
	 * 商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。<br>
	 * └ goods_id String 必填 32 商品的编号<br>
	 * └wxpay_goods_id String 可选 32 微信支付定义的统一商品编号<br>
	 * └ goods_name String 必填 256 商品名称 └ goods_num Int 必填 商品数量<br>
	 * └price Int 必填 商品单价，单位为分<br>
	 * 注意： a、单品总金额应<=订单总金额total_fee，否则会导致下单失败。 b、 单品单价，如果商户有优惠，需传输商户优惠后的单价<br>
	 * {"goods_detail":[<br>
	 * { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001","goods_name":"iPhone6s 16G","goods_num":1,"price":528800, },
	 * <br>
	 * { "goods_id":"iphone6s_32G","wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1,"price":608800, }
	 * ] }<br>
	 */
	public static String getDetailByListFood(List<FoodEntity> listFood) {
		String detail = "";
		try {
			JSONObject json = new JSONObject();
			JSONArray arrayFoods = new JSONArray();
			for (FoodEntity food : listFood) {
				JSONObject objFood = new JSONObject();
				objFood.put("goods_id", food.getId());
				objFood.put("goods_name", food.getName());
				objFood.put("price", food.getPrice());
				arrayFoods.put(objFood);
			}
			json.putOpt("goods_detail", arrayFoods);
			detail = "<![CDATA[" + json.toString() + "]]>";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return detail;
	}
}
