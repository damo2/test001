package com.shwm.freshmallpos.inter;

import java.util.HashMap;

public interface IAsyncListener {
	void onPreExecute();

	HashMap<String, Object> doInBackground();

	void onSuccess(HashMap<String, Object> hashmap);

	void onFail(int statu, Exception exception);

}
