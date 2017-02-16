package com.shwm.freshmallpos.presenter;

import android.text.TextUtils;

import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.model.biz.ILoginListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnLoginListener;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IChangePasswordView;

public class MChangePasswordPresenter extends MBasePresenter<IChangePasswordView> {
	private String TAG = getClass().getSimpleName();
	private IChangePasswordView mView;
	private ILoginListener iLoginListener;
	private String passwordOld, passwordNew, passwordNew2;

	public MChangePasswordPresenter(IChangePasswordView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		iLoginListener = new OnLoginListener();
	}

	/** 修改密码 */
	public void changePassword() {
		passwordOld = mView.getPasswordOld();
		passwordNew = mView.getPasswordNew();
		passwordNew2 = mView.getPasswordNew2();
		if (isSureFormat()) {
			iLoginListener.changePassword(BusinessInfo.getAdminID(), BusinessInfo.getAdminName(), passwordOld, passwordNew,
					new IRequestListener<Boolean>() {
						@Override
						public void onPreExecute(int type) {
							// TODO Auto-generated method stub
							mView.showDialogProgress();
						}

						@Override
						public void onSuccess(Boolean t) {
							// TODO Auto-generated method stub
							mView.dismissDialogProgress();
							mView.changeSuccess();
							UtilSPF.putString(ValueKey.ADMIN_PASSWORD, passwordNew);
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

	/** 判断密码格式是否正确 */
	private boolean isSureFormat() {
		String errorInfo = "";
		if (TextUtils.isEmpty(passwordOld)) {
			errorInfo = "输入旧密码";
			mView.errorOld(true, errorInfo);
			mView.errorNew(false, null);
			mView.errorNew2(false, null);
		} else if (TextUtils.isEmpty(passwordNew)) {
			errorInfo = "输入新密码";
			mView.errorNew(true, errorInfo);
			mView.errorOld(false, null);
			mView.errorNew2(false, null);
		} else if (passwordNew.length() < 6) {
			errorInfo = "新密码为6位数以上";
			mView.errorNew(true, errorInfo);
			mView.errorOld(false, null);
			mView.errorNew2(false, null);
		} else if (!StringUtil.isPwd(passwordNew)) {
			errorInfo = "新密码格式错误   密码只可有英文、数字、英文字符";
			mView.errorNew(true, errorInfo);
			mView.errorOld(false, null);
			mView.errorNew2(false, null);
		} else if (TextUtils.isEmpty(passwordNew2)) {
			errorInfo = "请确认密码";
			mView.errorNew2(true, errorInfo);
			mView.errorOld(false, null);
			mView.errorNew(false, null);
		} else if (!StringUtil.isPwd(passwordNew2)) {
			errorInfo = "确认密码格式错误  密码只可有英文、数字、英文字符";
			mView.errorNew2(true, errorInfo);
			mView.errorOld(false, null);
			mView.errorNew(false, null);
		} else if (!passwordNew.equals(passwordNew2)) {
			errorInfo = "2次输入密码不一致";
			mView.errorNew(true, errorInfo);
			mView.errorNew2(true, errorInfo);
			mView.errorOld(false, null);
		} else {
			mView.errorOld(false, null);
			mView.errorNew(false, null);
			mView.errorNew2(false, null);
			return true;
		}
		return false;
	}
}
