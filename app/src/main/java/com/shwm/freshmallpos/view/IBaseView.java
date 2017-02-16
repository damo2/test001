package com.shwm.freshmallpos.view;

public interface IBaseView {

	/** 显示进度框 */
	void showDialogProgress();

	/** 隐藏进度框 */
	void dismissDialogProgress();

	/** 显示失败信息 */
	void showFailInfo(int statu, Exception exception);

	/** 弹出提示信息 */
	void toastInfo(String info);

}
