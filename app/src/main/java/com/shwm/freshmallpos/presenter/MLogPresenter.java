package com.shwm.freshmallpos.presenter;

import java.util.HashMap;

import com.shwm.freshmallpos.model.biz.ILoginListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnLoginListener;
import com.shwm.freshmallpos.view.ILoginView;

/**
 * 登录
 * 
 * @author wr 2016-11-29
 */
public class MLogPresenter extends MBasePresenter<ILoginView> {
	private ILoginView mView;

	private ILoginListener iLoginListener;

	public MLogPresenter(ILoginView iLoginView) {
		this.mView = iLoginView;
		this.iLoginListener = new OnLoginListener();
	}

	public void login() {
		iLoginListener.login(mView.getUserName(), mView.getPassword(), new IRequestListener<HashMap<String, Object>>() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showSuccess("登录成功");
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showFailInfo(statu, exception);
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}
		});
	}
}
