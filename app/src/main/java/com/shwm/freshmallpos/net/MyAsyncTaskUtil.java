package com.shwm.freshmallpos.net;

import java.util.HashMap;

import android.os.AsyncTask;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

/**
 * 公用异步请求
 * 
 * @param <T>
 */
public class MyAsyncTaskUtil extends AsyncTask<Object, Object, HashMap<String, Object>> {
	private String TAG = getClass().getSimpleName();
	private IAsyncListener iAsyncListener;

	public MyAsyncTaskUtil(IAsyncListener iAsyncListener) {
		this.iAsyncListener = iAsyncListener;
	}

	@Override
	protected HashMap<String, Object> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		if (iAsyncListener != null) {
			return iAsyncListener.doInBackground();
		}
		UL.i(TAG, "doInBackground  return null");
		return null;
	}

	@Override
	protected void onPostExecute(HashMap<String, Object> hashmap) {
		// TODO Auto-generated method stub
		if (hashmap != null) {
			int statu = StringUtil.getInt(hashmap.get(ValueKey.HTTP_STATU));
			if (statu == ValueStatu.REQUEST_SUCCESS) {
				UL.i(TAG, "onPostExecute  Success");
				if (iAsyncListener != null) {
					iAsyncListener.onSuccess(hashmap);
				}
			} else {
				UL.i(TAG, "onPostExecute  Fail");
				String failinfo = StringUtil.getString(hashmap.get(ValueKey.HTTP_FAILINFO));
				if (iAsyncListener != null) {
					iAsyncListener.onFail(statu, new ExceptionUtil(failinfo));
				}
			}
		} else {
			UL.i(TAG, "onPostExecute  Exception");
			if (iAsyncListener != null) {
				iAsyncListener.onFail(HttpOkRequest.statuException, HttpOkRequest.exception);
			}
		}
		super.onPostExecute(hashmap);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (iAsyncListener != null) {
			iAsyncListener.onPreExecute();
		}
		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	/**
	 * 取消一个正在执行的任务
	 */
	public void cancle() {
		if (!isCancelled()) {
			cancel(true);
		}
	}

}
