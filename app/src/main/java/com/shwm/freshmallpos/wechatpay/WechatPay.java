package com.shwm.freshmallpos.wechatpay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.model.biz.IPayWechatListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.net.HttpUrlRequest;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

/**
 * 微信扫码支付
 * 
 * @author wr 2017-2-4
 */
public class WechatPay implements IPayWechatListener {
	private static final String TAG = WechatPay.class.getSimpleName();
	private static String codeUrl;
	public static final String WX_SUCCESS = "SUCCESS";
	public static final String WX_FAIL = "FAIL";

	@Override
	public void scanCodeUser(final String trimCode, final int totalFee, final String detail,
			final IRequestListener<HashMap<String, Object>> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				hashmap = scanCodeUserForXML(hashmap);
				UL.d(TAG, hashmap.get(ValueKey.HTTP_RESUTL).toString());
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
				String xmlParam = scanCodeUserToXML(trimCode, totalFee, detail);
				UL.d(TAG, xmlParam);
				HashMap<String, Object> hashmap = new HttpUrlRequest().requestByPost(WechatConfi.WX_URL_Paymicro, xmlParam);
				return hashmap;
			}
		}).execute();
	}

	@Override
	public void getScanCodeBusiness(final int totalFee, final String detail, final IRequestListener<String> iRequestListener) {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				String result = hashmap.get(ValueKey.HTTP_RESUTL).toString();
				hashmap = getScanCodeBusinessXML(hashmap, result);
				ResultInfo resultInfoWx = (ResultInfo) hashmap.get(ValueKey.ResultInfoWx);
				String wx_returnCode = resultInfoWx.getReturnCode();
				String wx_resultCode = resultInfoWx.getResultCode();
				if (wx_returnCode != null && wx_returnCode.equals(WX_SUCCESS)) {// 通信标识
					if (wx_resultCode != null && wx_resultCode.equals(WX_SUCCESS)) {// 交易标识
						codeUrl = resultInfoWx.getCodeQR();
						iRequestListener.onSuccess(codeUrl);
					} else {
						String wx_errCodeDes = resultInfoWx.getErrCodeDes();
						iRequestListener.onFail(ValueStatu.FAIL, new Exception(wx_errCodeDes));
					}
				} else {
					String wx_returnMsg = resultInfoWx.getReturnMsg();
					iRequestListener.onFail(ValueStatu.FAIL, new Exception(wx_returnMsg));
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
				String xmlParam = getCodeBusinessToXML(totalFee, detail);
				UL.d(TAG, "xmlParam=\n" + xmlParam);
				HashMap<String, Object> hashmap = new HttpUrlRequest().requestByPost(WechatConfi.WX_URL_PayUnifiedorder, xmlParam);
				return hashmap;
			}
		}).execute();
	}

	/**
	 * <p>
	 * 扫商户二维码 扫码支付统一下单 <br>
	 * ◆ 参数名ASCII码从小到大排序（字典序）；<br>
	 * ◆ 如果参数的值为空不参与签名；<br>
	 * ◆ 参数名区分大小写；<br>
	 * ◆ 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。<br>
	 * ◆微信接口可能增加字段，验证签名时必须支持增加的扩展字段<br>
	 */
	private String getCodeBusinessToXML(int totalFee, String detail) {
		List<TwoTuple<String, String>> paramList = new ArrayList<TwoTuple<String, String>>();
		paramList.add(new TwoTuple<String, String>("appid", WechatConfi.WX_APP_ID));// 填入自己的appid
		paramList.add(new TwoTuple<String, String>("body", WechatConfi.WX_Body));
		// paramList.add(new TwoTuple<String, String>("detail", detail));
		paramList.add(new TwoTuple<String, String>("mch_id", WechatConfi.WX_PARTNER_ID));// 商户id
		paramList.add(new TwoTuple<String, String>("nonce_str", UUID.randomUUID().toString().replace("-", "")));
		paramList.add(new TwoTuple<String, String>("notify_url", WechatConfi.WX_NotifyUrl));
		paramList.add(new TwoTuple<String, String>("out_trade_no", WechatConfi.getOrderNoForRecharge(totalFee)));
		paramList.add(new TwoTuple<String, String>("spbill_create_ip", WechatConfi.WX_CreateIp));
		paramList.add(new TwoTuple<String, String>("total_fee", totalFee + ""));
		paramList.add(new TwoTuple<String, String>("trade_type", WechatConfi.WX_TradeType));
		return listToXml(paramList);
	}

	/**
	 * 得到固定二维码地址 {@link}https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4
	 * 
	 * @return 二维码地址
	 */
	public String getCodeFixed() {
		List<TwoTuple<String, String>> paramList = new ArrayList<TwoTuple<String, String>>();
		paramList.add(new TwoTuple<String, String>("appid", WechatConfi.WX_APP_ID));// 填入自己的appid
		paramList.add(new TwoTuple<String, String>("mch_id", WechatConfi.WX_PARTNER_ID));// 商户id
		paramList.add(new TwoTuple<String, String>("nonce_str", UUID.randomUUID().toString().replace("-", "")));
		paramList.add(new TwoTuple<String, String>("product_id", WechatConfi.getProductId()));
		paramList.add(new TwoTuple<String, String>("time_stamp", WechatConfi.getTimestamp()));
		String sign = generateWechatMD5Signature(paramList);
		paramList.add(new TwoTuple<String, String>("sign", sign));
		String urlParam = listToString(paramList);
		String url = "weixin://wxpay/bizpayurl?" + urlParam;
		return url;
	}

	/**
	 * 扫用户二维码 刷卡支付 <br>
	 */
	private String scanCodeUserToXML(String codeQR, int totalFee, String detail) {
		List<TwoTuple<String, String>> paramList = new ArrayList<TwoTuple<String, String>>();
		paramList.add(new TwoTuple<String, String>("appid", WechatConfi.WX_APP_ID));// 填入自己的appid
		paramList.add(new TwoTuple<String, String>("auth_code", codeQR));
		paramList.add(new TwoTuple<String, String>("body", WechatConfi.WX_Body));
		// paramList.add(new TwoTuple<String, String>("detail", detail));
		paramList.add(new TwoTuple<String, String>("mch_id", WechatConfi.WX_PARTNER_ID));// 商户id
		paramList.add(new TwoTuple<String, String>("nonce_str", UUID.randomUUID().toString().replace("-", "")));
		paramList.add(new TwoTuple<String, String>("out_trade_no", WechatConfi.getOrderNoForRecharge(totalFee)));
		paramList.add(new TwoTuple<String, String>("spbill_create_ip", WechatConfi.WX_CreateIp));
		paramList.add(new TwoTuple<String, String>("total_fee", totalFee + ""));
		return listToXml(paramList);
	}

	/**
	 * 
	 * @param paramList
	 * @return appid=wx2421b1c4370ec43b&mch_id=10000100&nonce_str=f6808210402125e30663234f94c87a8c
	 */
	private static String listToString(List<TwoTuple<String, String>> paramList) {
		StringBuilder urlBuilder = new StringBuilder();
		int i = 0;
		for (TwoTuple<String, String> paramTuple : paramList) {
			if (i > 0) {
				urlBuilder.append("&");
			}
			urlBuilder.append(paramTuple.first).append("=");
			urlBuilder.append(paramTuple.second);
			i++;
		}
		return urlBuilder.toString();
	}

	private static String listToXml(List<TwoTuple<String, String>> paramList) {
		// 获取MD5签名并追加到参数列表中
		String sign = generateWechatMD5Signature(paramList);
		paramList.add(new TwoTuple<String, String>("sign", sign));
		// 构建XML参数
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<xml>").append("\n");
		for (TwoTuple<String, String> paramTuple : paramList) {
			xmlBuilder.append("<").append(paramTuple.first).append(">");
			xmlBuilder.append(paramTuple.second);
			xmlBuilder.append("</").append(paramTuple.first).append(">").append("\n");
		}
		xmlBuilder.append("</xml>").append("\n");
		return xmlBuilder.toString();
	}

	// 根据参数列表生成MD5签名
	private static String generateWechatMD5Signature(List<TwoTuple<String, String>> paramList) {
		StringBuilder sb = new StringBuilder();
		for (TwoTuple<String, String> paramTuple : paramList) {
			sb.append(paramTuple.first);
			sb.append('=');
			sb.append(paramTuple.second);
			sb.append('&');
		}
		sb.append("key=").append(WechatConfi.WX_SECURITY_KEY);// key 秘钥
		return MD5.MD5Encode(sb.toString()).toUpperCase();
	}

	// 解析获取二维码地址的返回信息
	private static HashMap<String, Object> getScanCodeBusinessXML(HashMap<String, Object> hashmap, String xmlStr) {
		String returnCode = WX_FAIL;
		String resultCode = WX_FAIL;
		String codeUrl = "";
		ResultInfo wxResult = new ResultInfo();
		try {
			InputStream instream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
			XmlPullParser parser = Xml.newPullParser();// 得到Pull解析器
			parser.setInput(instream, "UTF-8");
			int eventType = parser.getEventType();// 得到第一个事件类型
			while (eventType != XmlPullParser.END_DOCUMENT) {// 如果事件类型不是文档结束的话则不断处理事件
				switch (eventType) {
				case (XmlPullParser.START_DOCUMENT):// 如果是文档开始事件
					break;
				case (XmlPullParser.START_TAG):// 如果遇到标签开始
					String tagName = parser.getName();// 获得解析器当前元素的名称
					if ("return_code".equals(tagName)) {// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
						returnCode = new String(parser.nextText());
						wxResult.setReturnCode(returnCode);
					}
					if ("return_msg".equals(tagName)) {// 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
						String returnMsg = new String(parser.nextText());
						wxResult.setReturnMsg(returnMsg);
					}
					if (returnCode.equals(WX_SUCCESS)) {// 以下字段在return_code为SUCCESS的时候有返回
						if ("result_code".equals(tagName)) {// 交易是否成功 SUCCESS/FAIL
							resultCode = new String(parser.nextText());
							wxResult.setResultCode(resultCode);
						}
						if ("err_code_des".equals(tagName)) {// 错误信息描述
							String errCodeDes = new String(parser.nextText());
							wxResult.setErrCodeDes(errCodeDes);
						}
						if (resultCode.equals(WX_SUCCESS)) {// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
							if ("code_url".equals(tagName)) {//
								codeUrl = new String(parser.nextText());
								wxResult.setCodeQR(codeUrl);
							}
							if ("prepay_id".equals(tagName)) {// 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
								String prepay_id = new String(parser.nextText());
								wxResult.setPrepayId(prepay_id);
							}
						}
					}

					break;
				case (XmlPullParser.END_TAG):// 如果遇到标签结束
					if ("xml".equals(parser.getName())) {// 如果是xml标签结束
					}
					break;
				}
				eventType = parser.next();// 进入下一个事件处理
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hashmap.put(ValueKey.ResultInfoWx, wxResult);
		return hashmap;
	}

	// 解析扫码二维码的返回信息
	private HashMap<String, Object> scanCodeUserForXML(HashMap<String, Object> hashmap) {
		// TODO Auto-generated method stub
		String resultXml = StringUtil.getString(hashmap.get(ValueKey.HTTP_RESUTL));
		String returnCode = WX_FAIL;
		String resultCode = WX_FAIL;
		ResultInfo wxResult = new ResultInfo();
		try {
			InputStream instream = new ByteArrayInputStream(resultXml.getBytes("UTF-8"));
			XmlPullParser parser = Xml.newPullParser();// 得到Pull解析器
			parser.setInput(instream, "UTF-8");
			int eventType = parser.getEventType();// 得到第一个事件类型
			while (eventType != XmlPullParser.END_DOCUMENT) {// 如果事件类型不是文档结束的话则不断处理事件
				switch (eventType) {
				case (XmlPullParser.START_DOCUMENT):// 如果是文档开始事件
					break;
				case (XmlPullParser.START_TAG):// 如果遇到标签开始
					String tagName = parser.getName();// 获得解析器当前元素的名称
					if ("return_code".equals(tagName)) {// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
						returnCode = new String(parser.nextText());
						wxResult.setReturnCode(returnCode);
					}
					if ("return_msg".equals(tagName)) {// 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
						String returnMsg = new String(parser.nextText());
						wxResult.setReturnMsg(returnMsg);
					}
					if (returnCode.equals(WX_SUCCESS)) {// 以下字段在return_code为SUCCESS的时候有返回
						if ("result_code".equals(tagName)) {// 交易是否成功 SUCCESS/FAIL
							resultCode = new String(parser.nextText());
							wxResult.setResultCode(resultCode);
						}
						if ("err_code_des".equals(tagName)) {// 错误信息描述
							String errCodeDes = new String(parser.nextText());
							wxResult.setErrCodeDes(errCodeDes);
						}
						if (resultCode.equals(WX_SUCCESS)) {// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
							if ("total_fee".equals(tagName)) {// 订单总金额
								int totalFee = StringUtil.getInt(parser.nextText());
								wxResult.setTotalFee(totalFee);
							}
							if ("settlement_total_fee".equals(tagName)) {// 订单总金额
								int settlementTotalFee = StringUtil.getInt(parser.nextText());
								wxResult.setSettlementTotalFee(settlementTotalFee);
							}
							if ("cash_fee".equals(tagName)) {// 订单现金支付金额
								int cashFee = StringUtil.getInt(parser.nextText());
								wxResult.setTotalFee(cashFee);
							}
						}
					}
					break;
				case (XmlPullParser.END_TAG):// 如果遇到标签结束
					if ("xml".equals(parser.getName())) {// 如果是xml标签结束

					}
					break;
				}
				eventType = parser.next();// 进入下一个事件处理
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hashmap.put(ValueKey.ResultInfoWx, wxResult);
		return hashmap;
	}

}
