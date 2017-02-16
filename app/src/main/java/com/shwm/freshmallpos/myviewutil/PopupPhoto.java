package com.shwm.freshmallpos.myviewutil;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.util.CommonUtil;
import com.shwm.freshmallpos.base.BasePopupWindow;

public class PopupPhoto extends BasePopupWindow {
	private WeakReference<Activity> weakReference;
	private View view;
	private LinearLayout ll_popup;
	private Button bt1, bt2, bt3;
	private RelativeLayout parent;

	private IphotoChose iphotoChose;

	public PopupPhoto(Activity activity) {
		super(activity);
		weakReference = new WeakReference<Activity>(activity);
		if (activity != null) {
			view = LayoutInflater.from(activity).inflate(R.layout.item_pos_popupwindows, null);
			initView();
			setValue();
			setListener();
			setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			setFocusable(true);
			setTouchable(true);
			setOutsideTouchable(true);
			setBackgroundDrawable(new BitmapDrawable());
			setOnDismissListener(onDismissListener);
			setAnimationStyle(R.style.PopupAnimationBottom);
			setContentView(view);
		}
	}

	private void initView() {
		parent = (RelativeLayout) view.findViewById(R.id.parent);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
	}

	private void setValue() {
	}

	private void setListener() {
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (iphotoChose != null) {
					iphotoChose.onCamera();
				}
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (iphotoChose != null) {
					iphotoChose.onPhoto();
				}
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	private OnDismissListener onDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			if (weakReference.get() != null) {
				CommonUtil.backgroundAlpha(weakReference.get(), 1.0f);
			}
		}
	};

	/**
	 * PopupPhoto 选择照片接口 拍照、从相册中选择、 取消
	 */
	public interface IphotoChose {
		void onCamera();

		void onPhoto();
	}

	public void setIPopupPhoto(IphotoChose iphotoChose) {
		this.iphotoChose = iphotoChose;
	}

	public void show(View parent) {
		// TODO Auto-generated method stub
		if (weakReference.get() != null) {
			CommonUtil.backgroundAlpha(weakReference.get(), 0.5f);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (weakReference.get() != null) {
			CommonUtil.backgroundAlpha(weakReference.get(), 1.0f);
			super.dismiss();
		}
	}
}
