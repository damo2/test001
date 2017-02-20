package com.shwm.freshmallpos.model.biz;

public interface ILoginListener {
	void login(String username, String password, IRequestListener<Boolean> iRequestListener);

	void getCode(String mobi, IRequestListener<String> iRequestListener);

	void regin(String mobi, String code, String password, String storeName, String storeAddr, String storeDesc, String storeContact,
			   double lat, double lng, IRequestListener<Boolean> iRequestListener);

	void changePassword(int adminId, String adminName, String pwdOld, String pwdNew, IRequestListener<Boolean> iRequestListener);
}
