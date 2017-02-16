package com.shwm.freshmallpos.myviewutil;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CouponAdapter;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.base.BasePopupWindow;

public class PopupCoupon extends BasePopupWindow {
	private WeakReference<Activity> weakReference;
	private View view;
	private RecyclerView recyclerView;
	private CouponAdapter mAdapter;
	private IPopupCouponListener iPopupCouponListener;
	private List<CouponEntity> listCoupon;

	public PopupCoupon(Activity activity) {
		super(activity);
		weakReference = new WeakReference<Activity>(activity);
		if (activity != null) {
			view = LayoutInflater.from(activity).inflate(R.layout.popup_coupon, null);
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
			setAnimationStyle(R.style.PopupAnimationTop);
			setContentView(view);
		}
	}

	public void setListCoupon(List<CouponEntity> listCoupon) {
		this.listCoupon = listCoupon;
		if (mAdapter != null) {
			mAdapter.setData(listCoupon);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_popup_coupon);
	}

	private void setValue() {
		mAdapter = new CouponAdapter(weakReference.get());
		mAdapter.setData(listCoupon);
		FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(weakReference.get());
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		linearLayoutManager.setScrollEnabled(false);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(mAdapter);
		recyclerView.addItemDecoration(new SpacesItemDecoration(12));
	}

	public void setIOnItemClickListener(IOnItemClickListener<CouponEntity> iOnItemClickListener) {
		mAdapter.setIOnItemClickListener(iOnItemClickListener);
	}

	private void setListener() {
		view.findViewById(R.id.view_popup_coupon_hide).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				PopupCoupon.this.dismiss();
			}
		});
		view.findViewById(R.id.tv_popup_coupon_addMember).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (iPopupCouponListener != null) {
					iPopupCouponListener.onChooseMember();
					dismiss();
				}
			}
		});
	}

	private OnDismissListener onDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			// if (weakReference.get() != null) {
			// CommonUtil.backgroundAlpha(weakReference.get(), 1f);
			// }
		}
	};

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (this != null) {
			// CommonUtil.backgroundAlpha(weakReference.get(), 1f);
			super.dismiss();
		}
	}

	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;

			// Add top margin only for the first item to avoid double space between items
			if (parent!=null&&parent.getChildPosition(view) == 0)
				outRect.top = space;
		}
	}

	public interface IPopupCouponListener {
		void onChooseMember();
	}

	public void setIPopupCouponListener(IPopupCouponListener iPopupCouponListener) {
		this.iPopupCouponListener = iPopupCouponListener;
	}
}
