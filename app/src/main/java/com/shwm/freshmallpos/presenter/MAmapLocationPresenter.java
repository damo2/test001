package com.shwm.freshmallpos.presenter;

import com.shwm.freshmallpos.been.AddressEntity;
import com.shwm.freshmallpos.model.biz.IBusinessListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnBusinessListener;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.view.IAmapLocationView;

public class MAmapLocationPresenter extends MBasePresenter<IAmapLocationView> {
	private IBusinessListener iBusinessListener = new OnBusinessListener();
	private AddressEntity addressEntity;

	// 改变商店地址
	public void setBusinessName() {
		addressEntity = mView.getAddressEntity();
		if (addressEntity == null) {
			UL.e(TAG, "addressEntity is null");
			return;
		}
		iBusinessListener.setBusinessAddress(addressEntity.getAddress(), addressEntity.getLat(), addressEntity.getLng(),
				new IRequestListener<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						mView.dismissDialogProgress();
						mView.changeBusinessAddressSuccess();
					}

					@Override
					public void onPreExecute(int type) {
						// TODO Auto-generated method stub
						mView.showDialogProgress();
					}

					@Override
					public void onFail(int statu, Exception exception) {
						// TODO Auto-generated method stub
						mView.dismissDialogProgress();
						mView.showFailInfo(statu, exception);
					}
				});
	}
}
