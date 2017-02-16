package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MMemberAddPresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UT;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.view.IMemberAddView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 会员卡添加
 * 
 * @author wr 2016-12-19
 */
public class MemberAddActivity extends BaseActivity<IMemberAddView,MMemberAddPresenter> implements IMemberAddView {
	private String title;
	private MenuItem item;

	private View viewOne, viewTwo;
	private EditText edtTel;
	private Button btnNext;
	private TextView tvTel;
	private EditText edtName;
	private EditText edtCardno;
	private EditText edtCardtype;
	private EditText edtDiscount;
	private TextView tvDesc;
	private View viewDesc;

	private String desc;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_member_add;
	}
	@Override
	public MMemberAddPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MMemberAddPresenter(this);
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
		viewOne = findViewById(R.id.ll_memberadd_one);
		viewTwo = findViewById(R.id.ll_memberadd_two);
		edtTel = (EditText) findViewById(R.id.edt_memberadd_tel);
		btnNext = (Button) findViewById(R.id.btn_memberadd_next);
		tvTel = (TextView) findViewById(R.id.tv_memberadd_tel);
		edtName = (EditText) findViewById(R.id.edt_memberadd_name);
		edtCardno = (EditText) findViewById(R.id.edt_memberadd_cardNo);
		edtCardtype = (EditText) findViewById(R.id.edt_memberadd_cardType);
		edtDiscount = (EditText) findViewById(R.id.edt_memberadd_discount);
		tvDesc= (TextView) findViewById(R.id.tv_memberAdd_desc);
		viewDesc=findViewById(R.id.view_memberAdd_desc);
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
		StringUtil.getIntFomat(edtDiscount, 1);
		viewDesc.setOnClickListener(this);

		edtDiscount.addTextChangedListener(new TextWatcher() {

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
				String str = edtDiscount.getText().toString().trim();
				if (!TextUtils.isEmpty(str)) {
					if (UtilMath.sub(StringUtil.getDouble(str), 10) > 0) {
						str = str.substring(0, str.length() - 1);
						edtDiscount.setText(str);
						edtDiscount.setSelection(str.length());
					}
				}
			}
		});
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_memberadd,title);
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mPresenter.addMember();
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.right, menu);
		item = menu.findItem(R.id.menu_right);
		item.setTitle("");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnNext.getId()) {
			String tel = edtTel.getText().toString();
			if (StringUtil.isMobile(tel)) {
				item.setTitle(getString(R.string.save));
				viewOne.setVisibility(View.GONE);
				viewTwo.setVisibility(View.VISIBLE);
				tvTel.setText(tel);
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.member_add_tel_fail), Toast.LENGTH_SHORT).show();
			}
		}
		if(v.getId()==viewDesc.getId()){
			startActivityForResult(new Intent(mActivity,EditContentActivity.class).putExtra(ValueKey.TITLE,getString(R.string.title_memberDesc)).putExtra(ValueKey.Content,desc), ValueRequest.MemberAdd_EditContent);
		}
	}

	@Override
	public String getTel() {
		// TODO Auto-generated method stub
		return tvTel.getText().toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return edtName.getText().toString();
	}

	@Override
	public String getCardno() {
		// TODO Auto-generated method stub
		return edtCardno.getText().toString();
	}

	@Override
	public String getCardtype() {
		// TODO Auto-generated method stub
		return edtCardtype.getText().toString();
	}

	@Override
	public void showDialogProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dismissDialogProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDiscount() {
		// TODO Auto-generated method stub
		return edtDiscount.getText().toString();
	}

	@Override
	public void addSuccess() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void addFail(int statu, Exception exception) {
		// TODO Auto-generated method stub
		UT.showLong(getApplicationContext(), exception.toString());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==ValueRequest.MemberAdd_EditContent&&resultCode==RESULT_OK){
			desc=data.getExtras().getString(ValueKey.Content);
			tvDesc.setText(desc);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
