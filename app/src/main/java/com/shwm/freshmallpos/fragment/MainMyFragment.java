package com.shwm.freshmallpos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.ImageLoadUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.activity.AboutusActivity;
import com.shwm.freshmallpos.activity.AccountSetActivity;
import com.shwm.freshmallpos.activity.BluetoothListActivity;
import com.shwm.freshmallpos.activity.BusinessManageActivity;
import com.shwm.freshmallpos.activity.CouponSetActivity;
import com.shwm.freshmallpos.base.BaseFragment;

/**
 * 我的
 * 
 * @author wr 2016-11-29
 */
public class MainMyFragment extends BaseFragment {

	private String title;

	private TextView tvNickname;
	private TextView tvUsesrname;
	private ImageView ivLogo;

	private View viewAccount;
	private TextView tvCashierManage;
	private TextView tvDeviceManage;
	private TextView tvCashfeed;
	private TextView tvCashFee;
	private TextView tvCashSet;
	private TextView tvAbout;
	private View viewUserInfo;

	public static MainMyFragment newInstance(String title) {
		MainMyFragment fragment = new MainMyFragment();
		Bundle args = new Bundle();
		args.putString(ValueKey.TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	public MainMyFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tvNickname.setText(BusinessInfo.getAdminNickname());
		ImageLoadUtil.displayImage(ivLogo, BusinessInfo.getBusinessLogo(), ImageLoadUtil.getOptionsImgRoundedUser());
	}

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_main_my;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);
		title = arguments.getString(ValueKey.TITLE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		setToolbar();
		tvNickname = (TextView) findViewById(R.id.tv_my_nickname);
		tvUsesrname = (TextView) findViewById(R.id.tv_my_username);
		ivLogo=(ImageView) findViewById(R.id.iv_my_img);

		viewAccount = findViewById(R.id.ll_my_accountset);
		tvCashierManage = (TextView) findViewById(R.id.tv_my_cashiermanage);
		tvDeviceManage = (TextView) findViewById(R.id.tv_my_devicemanage);
		tvCashfeed = (TextView) findViewById(R.id.tv_my_cashfeed);
		tvCashFee = (TextView) findViewById(R.id.tv_my_feeBusiness);
		tvCashSet = (TextView) findViewById(R.id.tv_my_cashSet);
		tvAbout = (TextView) findViewById(R.id.tv_my_aboutWe);
		viewUserInfo = findViewById(R.id.rl_my_userinfo);
	}

	public void setToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_my);
		TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
		tvTitle.setText(title);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		// tvNickname.setText(BusinessInfo.getAdminNickname());
		tvUsesrname.setText(BusinessInfo.getAdminName());
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		viewAccount.setOnClickListener(this);
		tvCashierManage.setOnClickListener(this);
		tvDeviceManage.setOnClickListener(this);
		tvCashfeed.setOnClickListener(this);
		tvCashFee.setOnClickListener(this);
		tvCashSet.setOnClickListener(this);
		tvAbout.setOnClickListener(this);
		viewUserInfo.setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == viewAccount.getId()) {
			startActivity(new Intent(mActivity, AccountSetActivity.class)
					.putExtra(ValueKey.TITLE, getString(R.string.title_accountset)));
		}
		if (v.getId() == tvCashSet.getId()) {
			startActivity(new Intent(mActivity, CouponSetActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_couponset)));
		}
		if (v.getId() == tvDeviceManage.getId()) {
			startActivity(new Intent(mActivity, BluetoothListActivity.class).putExtra(ValueKey.TITLE,
					getString(R.string.title_devicemanage)));
		}
		if (v.getId() == tvAbout.getId()) {
			startActivity(new Intent(mActivity, AboutusActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_aboutus)));
		}
		if (v.getId() == viewUserInfo.getId()) {
			startActivity(new Intent(mActivity, BusinessManageActivity.class).putExtra(ValueKey.TITLE,
					getString(R.string.title_businessmanage)));
		}
	}

}
