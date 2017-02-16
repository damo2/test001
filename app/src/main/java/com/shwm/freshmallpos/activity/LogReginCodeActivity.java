package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.AddressEntity;
import com.shwm.freshmallpos.presenter.MLogRegPresenter;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.view.IReginView;
import com.shwm.freshmallpos.base.BaseActivity;

public class LogReginCodeActivity extends BaseActivity<IReginView, MLogRegPresenter> implements IReginView {
	private String title;

	private View viewOne;
	private View viewTwo;
	private Button btnGetcode;
	private Button btnNext;
	private Button btnRegin;

	private EditText edtMobi;
	private EditText edtCode;
	private EditText edtPassword;
	private EditText edtStoreName;
	private EditText edtStoreAddr;
	private EditText edtStoreDesc;
	private EditText edtStoreContacts;

	private TextView tvMobi;
	private ImageButton ibtnLocal;
	private AddressEntity address;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_pos_log_reg;
	}

	@Override
	public MLogRegPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MLogRegPresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		title = getIntent().getExtras().getString(ValueKey.TITLE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		viewOne = findViewById(R.id.ll_reg_one);
		viewTwo = findViewById(R.id.ll_reg_two);
		btnNext = (Button) findViewById(R.id.btn_reg_next);
		btnGetcode = (Button) findViewById(R.id.btn_reg_getcode);
		btnRegin = (Button) findViewById(R.id.btn_reg_submit);

		edtMobi = (EditText) findViewById(R.id.edt_reg_mobi);
		edtCode = (EditText) findViewById(R.id.edt_reg_code);

		edtPassword = (EditText) findViewById(R.id.edt_reg_password);
		edtStoreName = (EditText) findViewById(R.id.edt_reg_storeName);
		edtStoreAddr = (EditText) findViewById(R.id.edt_reg_storeAddr);
		edtStoreDesc = (EditText) findViewById(R.id.edt_reg_storeDesc);
		edtStoreContacts = (EditText) findViewById(R.id.edt_reg_storeContacts);

		tvMobi = (TextView) findViewById(R.id.tv_reg_tel);
		ibtnLocal = (ImageButton) findViewById(R.id.ibtn_reg_location);
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_reg,title);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		viewOne.setVisibility(View.VISIBLE);
		viewTwo.setVisibility(View.GONE);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnNext.setOnClickListener(this);
		btnGetcode.setOnClickListener(this);
		btnRegin.setOnClickListener(this);
		ibtnLocal.setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnNext.getId()) {
			mPresenter.next();
		}
		if (v.getId() == btnGetcode.getId()) {
			mPresenter.getCode();
		}
		if (v.getId() == btnRegin.getId()) {
			mPresenter.regin();
		}
		if (v.getId() == ibtnLocal.getId()) {
			startActivityForResult(
					new Intent(mActivity, AmapLocationActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_mapchoose)),
					ValueRequest.Regin_Location);
		}
	}

	@Override
	public String getMobi() {
		// TODO Auto-generated method stub
		return edtMobi.getText().toString();
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return edtCode.getText().toString();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return edtPassword.getText().toString();
	}

	@Override
	public String getStoreName() {
		// TODO Auto-generated method stub
		return edtStoreName.getText().toString();
	}

	@Override
	public String getStoreDesc() {
		// TODO Auto-generated method stub
		return edtStoreDesc.getText().toString();
	}

	@Override
	public String getStoreAddress() {
		// TODO Auto-generated method stub
		return edtStoreAddr.getText().toString();
	}

	@Override
	public String getSotreContacts() {
		// TODO Auto-generated method stub
		return edtStoreContacts.getText().toString();
	}

	@Override
	public double getLat() {
		// TODO Auto-generated method stub
		if (address != null) {
			return address.getLat();
		}
		return 0;
	}

	@Override
	public double getLng() {
		// TODO Auto-generated method stub
		if (address != null) {
			return address.getLng();
		}
		return 0;
	}

	@Override
	public void toNext() {
		// TODO Auto-generated method stub
		viewOne.setVisibility(View.GONE);
		viewTwo.setVisibility(View.VISIBLE);
		tvMobi.setText(getMobi());
	}

	@Override
	public void reginSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), getString(R.string.log_regin_btn_regSuc), Toast.LENGTH_SHORT).show();
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.Regin_Location && resultCode == RESULT_OK) {
			address = (AddressEntity) data.getExtras().getSerializable(ValueKey.AddressEntity);
			if (address != null) {
				edtStoreAddr.setText(address.getAddress());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
