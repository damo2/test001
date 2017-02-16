package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.AdminRequest;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnAdminListener implements IAdminListener {

	@Override
	public void setAdminNickname(final String nickname, final IRequestListener<String> iRequestListener) {
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
				return AdminRequest.setAdminNickname(nickname);
			}
		}).execute();
	}

}
