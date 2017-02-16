package com.shwm.freshmallpos.model.biz;

public interface IAdminListener {

	/** 改昵称 */
	void setAdminNickname(String nickname, IRequestListener<String> iRequestListener);
}
