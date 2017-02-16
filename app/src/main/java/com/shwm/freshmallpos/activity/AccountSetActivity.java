package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 账号设置
 * 
 * @author wr 2016-12-28
 */
public class AccountSetActivity extends BaseActivity {
	private String title;
	private TextView tvUsername;
	private TextView tvNickname;
	private View viewNickname;
	private View viewChangePwd;
	private Button btnExit;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_account_set;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();

		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);

	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_accountset,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		tvUsername = (TextView) findViewById(R.id.tv_accountset_username);
		tvNickname = (TextView) findViewById(R.id.tv_accountset_nickname);
		viewNickname = findViewById(R.id.rl_accountset_nickname);
		viewChangePwd = findViewById(R.id.rl_accountset_changepassword);
		btnExit = (Button) findViewById(R.id.btn_accountset_exit);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		tvUsername.setText(BusinessInfo.getAdminName());
		tvNickname.setText(BusinessInfo.getAdminNickname());
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnExit.setOnClickListener(this);
		viewNickname.setOnClickListener(this);
		viewChangePwd.setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnExit.getId()) {
			startActivity(new Intent(mActivity, LogActivity.class));
		}
		if (v.getId() == viewChangePwd.getId()) {
			startActivity(new Intent(mActivity, ChangePasswordActivity.class).putExtra(ValueKey.TITLE,
					getString(R.string.title_changepassword)));
		}
		if (v.getId() == viewNickname.getId()) {
			startActivityForResult(
					new Intent(mActivity, ChangeNameActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_nicknameChange))
							.putExtra(ValueKey.TYPE, ValueType.ChangeType_Nickname), ValueRequest.AccountSet_Name);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.AccountSet_Name && resultCode == RESULT_OK) {
			String nickname = data.getExtras().getString(ValueKey.ADMIN_NIKENAME);
			tvNickname.setText(nickname);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
