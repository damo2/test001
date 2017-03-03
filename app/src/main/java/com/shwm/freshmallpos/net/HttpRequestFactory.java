package com.shwm.freshmallpos.net;

import com.shwm.freshmallpos.inter.IHttpRequest;


public class HttpRequestFactory {
	private static IHttpRequest httpRequest;

	private HttpRequestFactory() {
	}

	/** http 请求工厂 */
	public static IHttpRequest getHttpRequest() {
		if (httpRequest == null) {
			synchronized (HttpRequestFactory.class) {
				if (httpRequest == null) {
					httpRequest = new HttpOkRequest();
				}
			}
		}
		return httpRequest;
	}
}
