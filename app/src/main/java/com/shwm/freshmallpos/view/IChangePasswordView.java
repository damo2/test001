package com.shwm.freshmallpos.view;

public interface IChangePasswordView extends IBaseView {
	String getPasswordOld();

	String getPasswordNew();

	String getPasswordNew2();

	void errorOld(boolean isError, String errorInfo);

	void errorNew(boolean isError, String errorInfo);

	void errorNew2(boolean isError, String errorInfo);

	void changeSuccess();
}
