package com.shwm.freshmallpos.manage;

import com.shwm.freshmallpos.inter.IHttpRequest;
import com.shwm.freshmallpos.net.HttpOkRequest;


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
