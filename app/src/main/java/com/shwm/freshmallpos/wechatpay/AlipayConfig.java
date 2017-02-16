package com.shwm.freshmallpos.wechatpay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlipayConfig {
	/** 请求服务器地址（固定支付网关）（调试：http://openapi.alipaydev.com/gateway.do 线上：https://openapi.alipay.com/gateway.do ） */
	public static final String URL = "https://openapi.alipay.com/gateway.do";
	public static final String APP_ID = "2016080100139021";//

	public static final String APP_PRIVATE_KEY="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDOckWeUHA+jY1Qc65Mr9JSmnJbZC+ux6YeWmBwbRzLIMRTOBN4hFrNWKfLG2x1Cr45HIKs6W3XHK8x1Nf3t0jBFuLjSHpShYIan51HYb1I6NVdWsmu0ARp7WZFcSA2GELImv9IwdPmreCiW/Fq6Ki3P7nkD//5etBs3B29X4wVOzTJ0P4/vpEpolr1eUzWVUJIxqk8lS6pZvY6o7Wy9bOwznUaA4tElroR3W2LIqoTq3upBMKVfYHis+ewqqAJruD6ol3y9opPxPhKShlnAQeI0Qj9KpTvAMNRgXaGX2jSIjQlrs/HGBDqE+ypmut0eGaO9ngJ1Ej3sV5gtKvEDRAhAgMBAAECggEADBpC3o8UpXYi7K57b193UwRe4+CxsutdX7YopS6gW4AmQkKtUYlg0Vz7KTsIVqw0jqCPQJfGqq+xzZgOuSTzjO5Misp68U+GWw00aTTfpeCQcZKa9DaZq6QRNMz2+HTfpWcAxEEorQNQgfQl6QCOs3925FnTxcpfYaJd0/Y//haJcyjLxU92E2dffsWT5Aa7/8PjyztSeqBze8AB3b84S0v5ziJvUevKRmyJ+uApdO8ckvK15lsdjiulk7dlaVvEbKg4udt/nZv7X9P3tjnoqEuxmkWKEFoMPIO3q1sfKGZc8HJqIO/vQYLXUmY6CVzO9Sr8AgoZSm3UB4Ml2+0wAQKBgQD2MK8O49usnyXjhsmpRH+bdd5nwjp35HRvxjsi+ITysp1MGmVnciFhk86HgjoGk7QY0qKY0i5S5RaZEAVNGbpPXS1aL7disIjLS2gmytPf8F1QqOIebnkK91/chfyqAzL7g86mg5aAOcmAtz2rz8ZSF/k9vTBlM4Ybp9WeJD1FQQKBgQDWrCxEgert8e+L8gNI1+PGpAF410xXOPVRAKLVxsL5mH5hOoEf7wlBbGnB9ZN1+90qdnspBoo5swozWdFb8vEeUEEVNwnARMaSSfQ8SscEABujrPXzPo6cVEVN1haeDqqiY9FUnh8R/dgGlWBI3a0gugZYNy12dGcQysiS0Qyy4QKBgQDXGPoanGLWk8Za4O1ZAwwrG/TR2xWSovxInH4Ws+QU2JIuZ/GpTEJ2KtoL83AXRv3XolGGDD2FF7uCyEiZcJ0jsllprr8fbvZWCPBa1GQCCNS/II9P313KgxAycK6tLFZjAmqPefEI33R7DZ7IeqTgHSobEy0xfq7xgtZVcXxHAQKBgQCyx1Jaf2PaY9keUB+iI4FjJaTAMXU3Uo0it4fkxxILcY8Wg/WTVav8mz/8Mvtqj6WhvmhVQ0gMQ2nkWBG+JLXfi/CXrW1mfSBuJj7Lk27oivp5tgpiWB/GW3wYFYpieU5rzPtBICo5/pBQ80frhwmurp2oC9PFya0n0+l0M7sCoQKBgQDOwpT5jJ0kKUW+9ioqbHaCsLPXA5gFuoJ+BvGSmU25WAhnZPaB1AXmiY3DraY8Liesoa8O7fQOgnzoKCEns1Gcapz8yzJqUNJg8aKT90v1L5eKzwtt0oXU0pJQHdpvDmXE+3dBI4vXSEdtu47e5CE5XFUuubjGp2Mrk2fF0cbagQ==";//我自己的私钥
	public static final String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAznJFnlBwPo2NUHOuTK/SUppyW2QvrsemHlpgcG0cyyDEUzgTeIRazVinyxtsdQq+ORyCrOlt1xyvMdTX97dIwRbi40h6UoWCGp+dR2G9SOjVXVrJrtAEae1mRXEgNhhCyJr/SMHT5q3golvxauiotz+55A//+XrQbNwdvV+MFTs0ydD+P76RKaJa9XlM1lVCSMapPJUuqWb2OqO1svWzsM51GgOLRJa6Ed1tiyKqE6t7qQTClX2B4rPnsKqgCa7g+qJd8vaKT8T4SkoZZwEHiNEI/SqU7wDDUYF2hl9o0iI0Ja7PxxgQ6hPsqZrrdHhmjvZ4CdRI97FeYLSrxA0QIQIDAQAB";
// public static final String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMlCrN8bwpkxlcfKAQaDkge7Sd1nL6aP3NBtL9/jA7naOtDZQUopd9BvVrPa/JQG+0ao8DeLvoycABRnZt1jdWj37Cb0LTSzPOr57Urfx5wBTKgi2jHqKFaRPhILi0bXF3UsWU/bzuBw8167lQgiHyRRR6by6chdliuJNbPhY925AgMBAAECgYAMr6I78gEDLQf27vm/kl2LFTgjt5ReWGUf0jIZV7LtbR8V+QvPg+ukfNAnSB9xqTr/ijiSF8HsFQ1APABU5icJWS56AWd+/OwK+hDM667L3FmYTC0AN4QXahR/qZHZNHIkjHresQSOiWFBrSUBqFi+xtnpLbHePwXzudy9AORpXQJBAOnOLEUXtzzzThkJ4subtYaFfsSLS3t5AdFleJCaVLpQ2wvA6nD5qJG0ZkR56y4CDCjiPuBlz4D2VDnU7gJ0Le8CQQDcXZx/BpfGCqg2XAB/Bq8xYG+4Fb0woiK6haMU2BwMVQKx492Vw1YeGMsT+QqmgGzlfMR+pq3iYBbHgo4ezVbXAkEA2Ui8J7H0nvZWvDpNxX994fiqaSozChrZL6snsutalpSSHYg52KiMmmyXJkhP4kAQ4OXyHUVA5M8IU36peypRjQJBAMYlgl+R3Q0dbg2bAnM13ngGn84l9fzx/Uo8edx3Shkoo4izPP66KwhbANfPumzm04QygTjkxTvlbPYuhX5a4vcCQCli3ZOSQtNF0zI7dTdCOY75fhbRie3tghGGBfAJSOEATSimymD4dtfKjZBqUJ9sO+G2llXujFlGHhhfN9HVtec=";
//	public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static final String FORMAT = "json";// 参数返回格式，只支持json
	public static final String CHARSET = "UTF-8";// 请求和签名使用的字符编码格式，支持GBK和UTF-8
	public static final String SIGN_TYPE = "RSA2";// 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	public static final String ALIPAY_VERSION="1.0";//调用的接口版本，固定为：1.0

	public static final String METHOD_PAY="alipay.trade.pay";//统一收单交易支付接口

	public static final String SCENE="bar_code";//支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code

	public static final String Subject="food";

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


	public static String getTimeExpire(){
		//有效期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time_expire= sdf.format(System.currentTimeMillis()+5*60*1000);
		return time_expire;
	}
	public static String getTimeNow(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}



	public static String getBizContent(double total_amount,String auth_code){
		StringBuilder sb=new StringBuilder();
		sb.append("{\"out_trade_no\":\"" + getOrderNoForRecharge(1) + "\",");
		sb.append("{\"scene\":\"" + SCENE + "\",");
		sb.append("{\"auth_code\":\"" + auth_code + "\",");
		sb.append("\"subject\":\""+Subject+"\",");
		sb.append("\"total_amount\":\""+total_amount+"\",");
		return sb.toString();
	}
	/**
	 * @param total_amount 金额 元
	 * @param auth_code 二维码
     * @return
     */
	public static String getURlParam(double total_amount,String auth_code){
		List<TwoTuple<String, String>> paramList = new ArrayList<>();
		paramList.add(new TwoTuple<String, String>("app_id",APP_ID));
		paramList.add(new TwoTuple<String, String>("biz_content",getBizContent(total_amount,auth_code)));
		paramList.add(new TwoTuple<String, String>("charset",CHARSET));
		paramList.add(new TwoTuple<String, String>("format",FORMAT));
		paramList.add(new TwoTuple<String, String>("method",METHOD_PAY));
		paramList.add(new TwoTuple<String, String>("sign_type",SIGN_TYPE));
		paramList.add(new TwoTuple<String, String>("timestamp",getTimeNow()));
		paramList.add(new TwoTuple<String, String>("version",ALIPAY_VERSION));
		String sign=getSign(paramList);
		paramList.add(new TwoTuple<String, String>("sign",sign));
		String url=listToStringEncoder(paramList);
		return  url;
	}


	/**
	 * @param paramList
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
	private static String listToStringEncoder(List<TwoTuple<String, String>> paramList) {
		StringBuilder urlBuilder = new StringBuilder();
		int i = 0;
		for (TwoTuple<String, String> paramTuple : paramList) {
			if (i > 0) {
				urlBuilder.append("&");
			}
			urlBuilder.append(paramTuple.first).append("=");
			String value= StringUtil.toEncode(paramTuple.second);
            urlBuilder.append(value);
			i++;
		}
		return urlBuilder.toString();
	}
	/**
	 * 自行实现签名
	 * https://doc.open.alipay.com/docs/doc.htm?docType=1&articleId=106118
	 */

private static String getSign(List<TwoTuple<String, String>> paramList){
	String signContent=listToString(paramList);
	String sign="";
	try {
		sign=AlipaySignature.rsaSign(signContent, AlipayConfig.APP_PRIVATE_KEY,  AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
	} catch (AlipayApiException e) {
		e.printStackTrace();
	}
	return sign;
}

}
