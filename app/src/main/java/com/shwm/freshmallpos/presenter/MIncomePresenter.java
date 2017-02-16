package com.shwm.freshmallpos.presenter;

import java.util.HashMap;

import com.shwm.freshmallpos.been.IncomeEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.OrderRequest;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.view.IIncomeView;

public class MIncomePresenter extends MBasePresenter<IIncomeView> {
	private IIncomeView mView;

	public MIncomePresenter(IIncomeView mView) {
		this.mView = mView;
	}
	public void getIncome() {
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					IncomeEntity income = (IncomeEntity) hashmap.get(ValueKey.Income);
					mView.showIncome(income);
				} else {
					mView.showFailInfo(code, ValueFinal.getExceptionFailInfo(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showFailInfo(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return OrderRequest.getIncome();
			}
		}).execute();
	}
}
