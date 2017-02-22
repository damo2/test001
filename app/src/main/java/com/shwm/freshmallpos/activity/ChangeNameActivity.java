package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.model.biz.IAdminListener;
import com.shwm.freshmallpos.model.biz.IBusinessListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnAdminListener;
import com.shwm.freshmallpos.model.biz.OnBusinessListener;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;

public class ChangeNameActivity extends BaseActivity {
	private String title;
	private IBusinessListener iBusinessListener;
	private IAdminListener iAdminListener;
	private EditText edtName;
	private String name;

	private int typeChange;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_businessmanage_businessname;
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
		typeChange = bundle.getInt(ValueKey.TYPE);
		iBusinessListener = new OnBusinessListener();
		iAdminListener = new OnAdminListener();
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_businessname,title);
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getItemId() == R.id.menu_right) {
					name = edtName.getText().toString().trim();
					if (StringUtil.isEmpty(name)) {
						toastInfo(getString(R.string.businessmanage_name_nonull));
					} else {
						switch (typeChange) {
							case ValueType.ChangeType_Businessname:
								setBusinessName();
								break;
							case ValueType.ChangeType_Nickname:
								setAdminNickname();
								break;
							default:
								break;
						}
					}
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.right, menu);
		MenuItem item = menu.findItem(R.id.menu_right);
		item.setTitle(getString(R.string.sure));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		edtName = (EditText) findViewById(R.id.edt_businessname_name);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		switch (typeChange) {
		case ValueType.ChangeType_Businessname:
			name = BusinessInfo.getBusinessName();
			break;
		case ValueType.ChangeType_Nickname:
			name = BusinessInfo.getAdminNickname();
			break;
		default:
			break;
		}

		edtName.setText(name);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub

	}

	private void setBusinessName() {
		iBusinessListener.setBusinessName(name, new IRequestListener<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				dismissDialogProgress();
				BusinessInfo.setBusinessName(name);
				Intent intent = new Intent();
				intent.putExtra(ValueKey.Business_NAME, name);
				setResult(RESULT_OK, intent);
				mActivity.finish();
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				dismissDialogProgress();
				showFailInfo(statu, exception);
			}
		});
	}

	private void setAdminNickname() {
		iAdminListener.setAdminNickname(name, new IRequestListener<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				dismissDialogProgress();
				BusinessInfo.setAdminNickname(name);
				Intent intent = new Intent();
				intent.putExtra(ValueKey.ADMIN_NIKENAME, name);
				setResult(RESULT_OK, intent);
				mActivity.finish();
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				dismissDialogProgress();
				showFailInfo(statu, exception);
			}
		});
	}

	@Override
	protected void onBack() {
		setResult(RESULT_CANCELED);
		super.onBack();
	}
}
