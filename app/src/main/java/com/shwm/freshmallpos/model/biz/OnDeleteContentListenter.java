package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.DeleteRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnDeleteContentListenter implements IDeleteContentListener {

	@Override
	public void deleteContent(final int content_type, final String ids, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				iRequestListener.onSuccess(hashmap);
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
				return DeleteRequest.Delete(content_type, ids);
			}
		}).execute();
	}

	@Override
	public void deleteClasses(final int content_type, final String ids, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = (Integer) hashmap.get(ValueKey.RESULT_CODE);
				String msg = (String) hashmap.get(ValueKey.RESULT_MSG);
				if (code == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(hashmap);
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(msg));
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
				return DeleteRequest.DeleteClasses(content_type, ids);
			}
		}).execute();
	}

}
