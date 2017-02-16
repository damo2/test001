package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MChangePasswordPresenter;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IChangePasswordView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 修改密码
 * 
 * @author wr 2016-12-28
 */
public class ChangePasswordActivity extends BaseActivity<IChangePasswordView, MChangePasswordPresenter> implements IChangePasswordView {

	private String title;

	private TextInputLayout textInputPasswordOld;
	private TextInputLayout textInputPasswordNew;
	private TextInputLayout textInputPasswordNew2;

	private Button btnSubmit;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_changepassword;
	}

	@Override
	public MChangePasswordPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MChangePasswordPresenter(this);
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
		setToolbar(R.id.toolbar_changepassword,title);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		textInputPasswordOld = (TextInputLayout) findViewById(R.id.textinput_pwdOld);
		textInputPasswordNew = (TextInputLayout) findViewById(R.id.textinput_pwdNew);
		textInputPasswordNew2 = (TextInputLayout) findViewById(R.id.textinput_pwdNew2);
		btnSubmit = (Button) findViewById(R.id.btn_changepassword_submit);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnSubmit.setOnClickListener(this);

	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnSubmit.getId()) {
			mPresenter.changePassword();
		}
	}

	@Override
	public String getPasswordOld() {
		// TODO Auto-generated method stub
		return textInputPasswordOld.getEditText().getText().toString();
	}

	@Override
	public String getPasswordNew() {
		// TODO Auto-generated method stub
		return textInputPasswordNew.getEditText().getText().toString();
	}

	@Override
	public String getPasswordNew2() {
		// TODO Auto-generated method stub
		return textInputPasswordNew2.getEditText().getText().toString();
	}

	@Override
	public void changeSuccess() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void errorOld(boolean isError, String errorInfo) {
		// TODO Auto-generated method stub
		if (isError) {
			textInputPasswordOld.setError(errorInfo);
		} else {
			textInputPasswordOld.setErrorEnabled(false);
		}
	}

	@Override
	public void errorNew(boolean isError, String errorInfo) {
		// TODO Auto-generated method stub
		if (isError) {
			textInputPasswordNew.setError(errorInfo);
		} else {
			textInputPasswordNew.setErrorEnabled(false);
		}
	}

	@Override
	public void errorNew2(boolean isError, String errorInfo) {
		// TODO Auto-generated method stub
		if (isError) {
			textInputPasswordNew2.setError(errorInfo);
		} else {
			textInputPasswordNew2.setErrorEnabled(false);
		}
	}

}
