package com.shwm.freshmallpos.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.inter.IHttpRequest;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.NetUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.base.ApplicationMy;

/**
 * HttpURLConnection网络请求
 */
public class HttpUrlRequest implements IHttpRequest {
	private static final String TAG = HttpUrlRequest.class.getSimpleName();

	/**
	 * 同步post get 请求参数<br>
	 * out.print(parm) 请求参数parm 是 name1=value1&name2=value2 的形式。
	 */
	public HashMap<String, Object> requestByPostOrGet(HttpType type, String url, String param) {
		HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		InputStream inputStreamReturn = null;

		String urlAllGet = "";
		if (type == HttpType.HTTP_GET && !TextUtils.isEmpty(param)) {
			url = url + "&" + param;
			urlAllGet=url;
		}else{
			urlAllGet=url+" |Param=| "+param;
		}

		UL.i(TAG + " / " + (type == HttpType.HTTP_POST ? "post" : "get"), urlAllGet);
		// 没网时取缓存
		if (NetUtil.isNetworkAvailable(ApplicationMy.getContext())) {
			String rsultCache = RequestCacheHttpUrl.getInstance().get(url);
			if (!TextUtils.isEmpty(rsultCache)) {
				hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_SUCCESS);
				hashmapResult.put(ValueKey.HTTP_RESUTL, rsultCache);
			} else {
				hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_FAIL);
				hashmapResult.put(ValueKey.HTTP_FAILINFO, ApplicationMy.getStringRes(R.string.statu_noNet));
			}
		}
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();// 打开和URL之间的连接
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(ConfigUtil.OutTimeConnect);// 设置连接超时
			conn.setReadTimeout(ConfigUtil.OutTimeRead);// 设置读取超时
			if (type == HttpType.HTTP_POST) {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);// Post请求必须设置允许输出 默认false
				conn.setDoInput(true);// 设置请求允许输入 默认是true
				out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流
				out.print(param);// 发送请求参数
				out.flush();// flush输出流的缓冲
			} else {
				conn.setRequestMethod("GET");
			}
			int requesetCode = conn.getResponseCode();
			if (requesetCode == 200) {
				inputStreamReturn = conn.getInputStream();// 得到字节流
				InputStreamReader isr = new InputStreamReader(inputStreamReturn);// 把字节流转化成字符流 InputStreamReader
				in = new BufferedReader(isr);// 把字符流转换成缓冲字符流
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				UL.i(TAG + "  SUCCESS", result);
				hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_SUCCESS);
				hashmapResult.put(ValueKey.HTTP_RESUTL, result);
				hashmapResult.put(ValueKey.HTTP_InputStream, inputStreamReturn);
				RequestCacheHttpUrl.getInstance().put(url, result);
			} else {
				hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_FAIL);
				hashmapResult.put(ValueKey.HTTP_FAILINFO, requesetCode + "");
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return  hashmapResult;
		} catch (Exception e) {
			e.printStackTrace();
			hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_Execute);
			hashmapResult.put(ValueKey.HTTP_FAILINFO, e.getMessage());
		}
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return hashmapResult;
	}

	private String mapToString(HashMap<String, Object> paramsMap) {
		StringBuilder tempParams = new StringBuilder();// 合成参数
		int pos = 0;
		for (String key : paramsMap.keySet()) {
			if (pos > 0) {
				tempParams.append("&");
			}
			try {
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key).toString(), "utf-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			pos++;
		}
		String params = tempParams.toString();
		return params;
	}


	private static byte[] Bitmap2Bytes(Bitmap paramBitmap) {
		if (paramBitmap == null) {
			return new byte[0];
		}
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
		return localByteArrayOutputStream.toByteArray();
	}

	@Override
	public HashMap<String, Object> requestByGet(String url, HashMap<String, Object> hashmap) {
		// TODO Auto-generated method stub
		return requestByPostOrGet(HttpType.HTTP_GET, url, mapToString(hashmap));
	}

	@Override
	public HashMap<String, Object> requestByPost(String url, String xml) {
		// TODO Auto-generated method stub
		return requestByPostOrGet(HttpType.HTTP_POST, url, xml);
	}

	@Override
	public HashMap<String, Object> requestByPost(String url, HashMap<String, Object> hashmap) {
		// TODO Auto-generated method stub
		return requestByPostOrGet(HttpType.HTTP_POST, url, mapToString(hashmap));
	}

	@Override
	public HashMap<String, Object> requestByGet(String url) {
		// TODO Auto-generated method stub
		return requestByPostOrGet(HttpType.HTTP_GET, url, null);
	}
}
