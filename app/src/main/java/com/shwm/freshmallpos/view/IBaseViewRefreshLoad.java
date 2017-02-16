package com.shwm.freshmallpos.view;

public interface IBaseViewRefreshLoad extends IBaseView {
	/** 隐藏刷新 */
	void dismissRefresh();
	
	/** 设置加载状态 */
	void setLoadType(int loadtype);
}
