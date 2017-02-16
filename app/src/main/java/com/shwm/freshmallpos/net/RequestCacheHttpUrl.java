package com.shwm.freshmallpos.net;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * HttpURLConnection 缓存简单配置
 */
public class RequestCacheHttpUrl {
	private static RequestCacheHttpUrl requestCacheHttpUrl;
	// 缓存的最大个数
	private static int CACHE_LIMIT = 50;

	private LinkedList<String> history;
	private Hashtable<String, String> cache;

	public static RequestCacheHttpUrl getInstance() {
		if (requestCacheHttpUrl == null) {
			synchronized (RequestCacheHttpUrl.class) {
				if (requestCacheHttpUrl == null) {
					requestCacheHttpUrl = new RequestCacheHttpUrl();
				}
			}
		}
		return requestCacheHttpUrl;
	}

	private RequestCacheHttpUrl() {
		history = new LinkedList<String>();
		cache = new Hashtable<String, String>();
	}

	public void put(String url, String data) {
		history.add(url);
		// 超过了最大缓存个数,则清理最早缓存的条目
		if (history.size() > CACHE_LIMIT) {
			String old_url = history.poll();
			cache.remove(old_url);
		}
		cache.put(url, data);
	}

	public String get(String url) {
		return cache.get(url);
	}
}
