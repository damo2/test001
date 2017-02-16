package com.shwm.freshmallpos.inter;

import java.util.HashMap;

public interface IHttpRequest {
	public static enum HttpType {
		HTTP_POST, HTTP_GET
	}

	HashMap<String, Object> requestByGet(String url, HashMap<String, Object> hashmap);

	HashMap<String, Object> requestByPost(String url, HashMap<String, Object> hashmap);

	HashMap<String, Object> requestByGet(String url);

	HashMap<String, Object> requestByPost(String url, String xml);
}
