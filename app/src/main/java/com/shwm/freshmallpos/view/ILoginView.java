package com.shwm.freshmallpos.view;
/**
 * 登录
 */
public interface ILoginView extends IBaseView{
	String getUserName();

	String getPassword();

	void clearUserName();

	void clearPassword();

	void showSuccess(String msg);

}
