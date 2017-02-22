package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MLogPresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.view.ILoginView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 登录
 * 
 * @author wr 2016-11-29
 */
public class LogActivity extends BaseActivity<ILoginView, MLogPresenter> implements ILoginView {
	private TextInputLayout textInputLayoutUsername;
	private TextInputLayout textInputLayoutPassword;
	private EditText edtUsername;
	private EditText edtPassword;
	private AppCompatButton btnSubmit;
	private AppCompatButton btnRegin;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		edtUsername.setText(UtilSPF.getString(ValueKey.ADMIN_USERNAME, ""));
		edtPassword.setText(UtilSPF.getString(ValueKey.ADMIN_PASSWORD, ""));
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_pos_login;
	}

	@Override
	public MLogPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MLogPresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayout_username);
		textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayout_password);
		edtUsername = (EditText) findViewById(R.id.edt_login_username);
		edtPassword = (EditText) findViewById(R.id.edt_login_password);
		btnSubmit = (AppCompatButton) findViewById(R.id.btn_login_log);
		btnRegin = (AppCompatButton) findViewById(R.id.btn_login_reg);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		// edtUsername.setText(UtilSPF.getString(ValueKeyUtil.ADMIN_USERNAME, "").toString());
		// edtPassword.setText(UtilSPF.getString(ValueKeyUtil.ADMIN_PASSWORD, "").toString());
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPresenter.login();
			}
		});
		btnRegin.setOnClickListener(this);
		edtUsername.addTextChangedListener(watcherUsername);
		edtPassword.addTextChangedListener(watcherPassword);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnRegin.getId()) {
			startActivityForResult(new Intent(mActivity, LogReginCodeActivity.class).putExtra(ValueKey.TITLE,
					getString(R.string.title_reg_code)), ValueRequest.Login_Regin);
		}
	}

	private TextWatcher watcherUsername = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	};

	private TextWatcher watcherPassword = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (!StringUtil.isPwd(s.toString())) {
				textInputLayoutPassword.setErrorEnabled(true);
				textInputLayoutPassword.setError(getString(R.string.log_login_pwd_noformat));
			} else if (s.length() < 6) {
				textInputLayoutPassword.setErrorEnabled(true);
				textInputLayoutPassword.setError(getString(R.string.log_login_pwd_nosize));
			} else {
				textInputLayoutPassword.setErrorEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return edtUsername.getText().toString().trim();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return edtPassword.getText().toString().trim();
	}

	@Override
	public void clearUserName() {
		// TODO Auto-generated method stub
		edtUsername.setText("");
	}

	@Override
	public void clearPassword() {
		// TODO Auto-generated method stub
		edtPassword.setText("");
	}

	@Override
	public void showSuccess(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
