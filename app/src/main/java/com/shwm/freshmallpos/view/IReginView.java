package com.shwm.freshmallpos.view;

public interface IReginView extends IBaseView {
	String getMobi();

	String getCode();

	String getPassword();

	String getStoreName();

	String getStoreDesc();

	String getStoreAddress();

	String getSotreContacts();

	double getLat();

	double getLng();

	void toNext();

	void reginSuccess();
}
