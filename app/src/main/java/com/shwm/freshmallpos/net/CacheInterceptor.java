package com.shwm.freshmallpos.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp
 * 拦截器，在服务器不支持缓存的情况下使用缓存
 * Get请求才能缓存
 * okhttpClient 中添加  
 * .addNetworkInterceptor(new CacheInterceptor()).cache(new Cache(缓存目录, 缓存大小))
 */
public class CacheInterceptor implements Interceptor {
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		Response response = chain.proceed(request);
		Response response1 = response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
		// cache for 1 days
				.header("Cache-Control", "max-age=" + 3600 * 24).build();
		return response1;
	}
}
