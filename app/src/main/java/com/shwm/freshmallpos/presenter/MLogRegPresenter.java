package com.shwm.freshmallpos.presenter;

import java.util.HashMap;

import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.model.biz.ILoginListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnLoginListener;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IReginView;
import com.shwm.freshmallpos.base.ApplicationMy;

public class MLogRegPresenter extends MBasePresenter<IReginView>{
	private String TAG = "MLogRegPresenter";
	private IReginView mView;
	private ILoginListener iLoginListener;
	private String mobi, code, password, storeName, storeAddr, storeDesc, storeContacts;
	private String codeReturn;
	private double lat, lng;

	public MLogRegPresenter(IReginView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		iLoginListener = new OnLoginListener();
	}

	public void getCode() {
		mobi = mView.getMobi();
		iLoginListener.getCode(mobi, new IRequestListener<String>() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onSuccess(String vcode) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				UtilSPF.putString(ValueKey.Vcode, vcode);
				UtilSPF.putString(ValueKey.VcodeMobi, mobi);
				UL.d(TAG, "验证码以保存在本地  codeReturn=" + vcode + "  mobi=" + mobi);
				mView.toastInfo(ApplicationMy.getStringRes(R.string.log_regin_sendcodesuc));
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showFailInfo(statu, exception);
			}
		});
	}

	public void next() {
		UL.d(TAG,
				"验证验证码   code=" + mView.getCode() + " | codeReturn=" + UtilSPF.getString(ValueKey.Vcode, "") + "   mobi="
						+ mView.getMobi() + " | mobiSendCode=" + UtilSPF.getString(ValueKey.VcodeMobi, ""));
		if (TextUtils.isEmpty(mView.getMobi())) {
			mView.toastInfo(ApplicationMy.getStringRes(R.string.log_regin_mobi_hint));
		} else if (TextUtils.isEmpty(mView.getCode())) {
			mView.toastInfo(ApplicationMy.getStringRes(R.string.log_regin_code_hint));
		} else if (!mView.getMobi().equals(UtilSPF.getString(ValueKey.VcodeMobi, ""))
				|| !mView.getCode().equals(UtilSPF.getString(ValueKey.Vcode, ""))) {
			mView.toastInfo(ApplicationMy.getStringRes(R.string.log_regin_codeError));
		} else {
			mView.toNext();
		}
	}

	public void regin() {
		mobi = mView.getMobi();
		code = mView.getCode();
		password = mView.getPassword();
		storeName = mView.getStoreName();
		storeAddr = mView.getStoreAddress();
		storeDesc = mView.getStoreDesc();
		storeContacts = mView.getSotreContacts();
		lat = mView.getLat();
		lng = mView.getLng();

		iLoginListener.regin(mobi, code, password, storeName, storeAddr, storeDesc, storeContacts, lat, lng,
				new IRequestListener<HashMap<String, Object>>() {
					@Override
					public void onPreExecute(int type) {
						// TODO Auto-generated method stub
						mView.showDialogProgress();
					}

					@Override
					public void onSuccess(HashMap<String, Object> hashmap) {
						// TODO Auto-generated method stub
						mView.dismissDialogProgress();
						mView.reginSuccess();
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
