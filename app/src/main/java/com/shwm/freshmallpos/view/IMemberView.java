package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.MemberEntity;

public interface IMemberView {
	void refreshCancel();

	void showDialogProgress();

	void dissmissDialogProgress();

	/** 获取查询条件 ""是查询全部 */
	String getLike();

	/** 获取页面类型 是刷新还是加载还是第一次进入default */
	int getPageType();

	void showMemberList(int loadtype, List<MemberEntity> listMember);

}
