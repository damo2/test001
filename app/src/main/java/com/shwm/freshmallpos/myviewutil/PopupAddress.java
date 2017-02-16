package com.shwm.freshmallpos.myviewutil;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CommonAdapter;
import com.shwm.freshmallpos.been.AddressEntity;
import com.shwm.freshmallpos.base.BasePopupWindow;

public class PopupAddress extends BasePopupWindow {
	private WeakReference<Activity> weakReference;
	private View view;

	private EditText edtAddr;
	private ListView listviewAddr;
	private CommonAdapter<AddressEntity> mAdapter;

	public PopupAddress(Activity activity) {
		super(activity);
		weakReference = new WeakReference<Activity>(activity);
		if (activity != null) {
			view = LayoutInflater.from(activity).inflate(R.layout.popup_address, null);
			initView();
			setValue();
			setListener();
			setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			// setFocusable(true);
			// setTouchable(true);
			// setOutsideTouchable(false);
			setBackgroundDrawable(new BitmapDrawable());
			setOnDismissListener(onDismissListener);
			setAnimationStyle(R.style.PopupAnimationBottom);
			setContentView(view);
		}
	}

	private void initView() {
		edtAddr = (EditText) view.findViewById(R.id.edt_popupAddress_addr);
	}

	private void setValue() {
	}

	private void setListener() {
	}

	private OnDismissListener onDismissListener = new OnDismissListener() {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
	};

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		// TODO Auto-generated method stub
		super.showAsDropDown(anchor, xoff, yoff);
	}

	@SuppressLint("NewApi")
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
		// TODO Auto-generated method stub
		super.showAsDropDown(anchor, xoff, yoff, gravity);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// TODO Auto-generated method stub
		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (this != null) {
			super.dismiss();
		}
	}

}
