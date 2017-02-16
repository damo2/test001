package com.shwm.freshmallpos.presenter;

import android.app.Activity;

public abstract class MBasePresenter<T> {
	protected String TAG = getClass().getSimpleName();
	private Activity mActivityPresenter;
	public T mView;

	public void setActivity(Activity mActivity) {
		this.mActivityPresenter = mActivity;
	}

	public void deleteActivity() {
		mActivityPresenter = null;
	}

	public void attach(T mView) {
		this.mView = mView;
	}

	public void dettach() {
		mView = null;
	}

	protected Activity getActivity() {
		return mActivityPresenter;
	}

	protected void onDestoryPresenter() {

	}
}
