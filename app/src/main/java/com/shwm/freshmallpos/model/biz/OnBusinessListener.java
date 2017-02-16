package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

import android.net.Uri;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.BusinessRequest;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnBusinessListener implements IBusinessListener {

	@Override
	public void setBusinessName(final String businessname, final IRequestListener<String> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(msg);
				} else {
					iRequestListener.onFail(code, ValueFinal.getExceptionFailInfo(msg));
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
				return BusinessRequest.setBusinessName(businessname);
			}
		}).execute();

	}

	@Override
	public void setBusinessAddress(final String address, final double lat, final double lng, final IRequestListener<String> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(msg);
				} else {
					iRequestListener.onFail(code, ValueFinal.getExceptionFailInfo(msg));
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
				return BusinessRequest.setBusinessAddress(address, lat, lng);
			}
		}).execute();
	}

	@Override
	public void setBusinessIcon(final Uri bitmapUri, final IRequestListener<String> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					String img = StringUtil.getString(hashmap.get(ValueKey.Image));
					iRequestListener.onSuccess(img);
				} else {
					iRequestListener.onFail(code, ValueFinal.getExceptionFailInfo(msg));
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

				return BusinessRequest.setBusinessLogo(bitmapUri);
			}
		}).execute();
	}

}
