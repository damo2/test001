package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;

import com.shwm.freshmallpos.been.AdminEntity;
import com.shwm.freshmallpos.been.BusinessEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.LoginRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;

public class OnLoginListener implements ILoginListener {
	private static final String TAG = "OnLoginListener";

	@Override
	public void login(final String username, final String password, final IRequestListener<Boolean> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					AdminEntity admin = (AdminEntity) hashmap.get(ValueKey.Admin);
					BusinessEntity shop = admin.getBusiness();
					UtilSPF.putString(ValueKey.ADMIN_USERNAME, username);
					UtilSPF.putString(ValueKey.ADMIN_PASSWORD, password);
					UtilSPF.putInt(ValueKey.ADMIN_ID, admin.getId());
					UtilSPF.putInt(ValueKey.ADMIN_TYPE, admin.getType());
					UtilSPF.putString(ValueKey.ADMIN_NIKENAME, admin.getNickname());
					UtilSPF.putInt(ValueKey.Business_ID, shop.getId());
					UtilSPF.putString(ValueKey.Business_IP, shop.getServerIp());
					UtilSPF.putString(ValueKey.Business_NAME, shop.getName());
					UtilSPF.putString(ValueKey.Business_ADDRESS, shop.getAddr());
					UtilSPF.putString(ValueKey.Business_LOGO, shop.getImg());
					UtilSPF.putBoolean(ValueKey.IsLoginAuto, true);
					UtilSPF.putBoolean(ValueKey.IsLogin, true);
					iRequestListener.onSuccess(true);
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(0);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return LoginRequest.log(username, password);
			}
		}).execute();
	}

	@Override
	public void getCode(final String mobi, final IRequestListener iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				String vcode = StringUtil.getString(hashmap.get(ValueKey.Vcode));
				if (code == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(vcode);
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(0);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return LoginRequest.getCode(mobi);
			}
		}).execute();
	}

	@Override
	public void regin(final String mobi, final String code, final String password, final String storeName, final String storeAddr,
			final String storeDesc, final String storeContact, final double lat, final double lng, final IRequestListener<Boolean> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					AdminEntity admin = (AdminEntity) hashmap.get(ValueKey.Admin);
					BusinessEntity shop = admin.getBusiness();
					UtilSPF.putString(ValueKey.ADMIN_USERNAME, mobi);
					UtilSPF.putString(ValueKey.ADMIN_PASSWORD, password);
					UtilSPF.putInt(ValueKey.ADMIN_ID, admin.getId());
					UtilSPF.putInt(ValueKey.ADMIN_TYPE, admin.getType());
					UtilSPF.putString(ValueKey.ADMIN_NIKENAME, admin.getNickname());
					UtilSPF.putInt(ValueKey.Business_ID, shop.getId());
					UtilSPF.putString(ValueKey.Business_IP, shop.getServerIp());
					UtilSPF.putString(ValueKey.Business_NAME, shop.getName());
					UtilSPF.putString(ValueKey.Business_ADDRESS, shop.getAddr());
					UtilSPF.putString(ValueKey.Business_LOGO, shop.getImg());
					UtilSPF.putBoolean(ValueKey.IsLoginAuto, true);
					UtilSPF.putBoolean(ValueKey.IsLogin, true);
					iRequestListener.onSuccess(true);
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(0);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return LoginRequest.regin(mobi, code, password, storeName, storeAddr, storeDesc, storeContact, lat, lng);
			}
		}).execute();
	}

	@Override
	public void changePassword(final int adminId, final String adminName, final String pwdOld, final String pwdNew,
			final IRequestListener<Boolean> iRequestListener) {
		// TODO Auto-generated method stub
		new MyAsyncTaskUtil(new IAsyncListener() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				int code = StringUtil.getInt(hashmap.get(ValueKey.RESULT_CODE));
				String msg = StringUtil.getString(hashmap.get(ValueKey.RESULT_MSG));
				if (code == ValueStatu.SUCCESS) {
					iRequestListener.onSuccess(true);
				} else {
					iRequestListener.onFail(code, new ExceptionUtil(msg));
				}
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				iRequestListener.onPreExecute(0);
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				iRequestListener.onFail(statu, exception);
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return LoginRequest.changePassword(adminId, adminName, pwdOld, pwdNew);
			}
		}).execute();
	}

}
