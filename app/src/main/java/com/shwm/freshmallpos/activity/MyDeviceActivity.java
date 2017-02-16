package com.shwm.freshmallpos.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.MyDeviceAdapter;
import com.shwm.freshmallpos.presenter.MMyDevicePresenter;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IMyDeviceView;
import com.shwm.freshmallpos.base.BaseActivity;

public class MyDeviceActivity extends BaseActivity<IMyDeviceView, MMyDevicePresenter> {
	private String title;
	private RecyclerView mRecycleview;
	private MyDeviceAdapter mAdapter;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_my_device;
	}

	@Override
	public MMyDevicePresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MMyDevicePresenter();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		title = getIntent().getExtras().getString(ValueKey.TITLE);
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_mydevice,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mRecycleview = (RecyclerView) findViewById(R.id.rv_mydevice_list);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapter();
	}

	private void setAdapter() {
		// TODO Auto-generated method stub
		mAdapter = new MyDeviceAdapter(mActivity);
		mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecycleview.setAdapter(mAdapter);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub

	}
}
