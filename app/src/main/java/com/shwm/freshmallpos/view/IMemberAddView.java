package com.shwm.freshmallpos.view;

public interface IMemberAddView {
	String getTel();

	String getName();

	String getCardno();

	String getCardtype();

	String getDiscount();

	void showDialogProgress();

	void dismissDialogProgress();

	void addSuccess();

	void addFail(int statu, Exception exception);
	
}
