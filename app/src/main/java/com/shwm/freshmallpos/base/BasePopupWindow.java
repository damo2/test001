package com.shwm.freshmallpos.base;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {
	public BasePopupWindow(Activity context) {
		super(context);// 没有会出错
	}

	/**
	 * 7.0 显示位置不正确问题
	 */
	@Override
	public void showAsDropDown(View anchor) {
		if (Build.VERSION.SDK_INT >= 24) {
			Rect visibleFrame = new Rect();
			anchor.getGlobalVisibleRect(visibleFrame);
			int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
			setHeight(height);
		}
		super.showAsDropDown(anchor);
	}

}
