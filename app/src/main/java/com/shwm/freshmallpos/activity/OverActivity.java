package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 完成收款/退款
 */
public class OverActivity extends BaseActivity {
	private String title;
	private double moneyReceivable;
	private int typeOver;
	private Button btnPrint;
	private Button btnGo;
	private TextView tvMoney;
	private TextView tvTag;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_over_suc;
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
		typeOver = bundle.getInt(ValueKey.TYPE);
		moneyReceivable = bundle.getDouble(ValueKey.MoneyReceivable);
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_orderrefund_top,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		btnPrint = (Button) findViewById(R.id.btn_orderrefund_print);
		btnGo = (Button) findViewById(R.id.btn_orderrefund_return);
		tvMoney = (TextView) findViewById(R.id.tv_orderrefund_money);
		tvTag = (TextView) findViewById(R.id.tv_orderrefund_refundover);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		tvMoney.setText(StringFormatUtil.moneyFormat(moneyReceivable));
		if (typeOver == ValueType.OverType_Pay) {
			tvTag.setText(getString(R.string.payover_overcash));
			btnGo.setText(getString(R.string.payover_cashGo));
		}
		if (typeOver == ValueType.OverType_Refund) {
			tvTag.setText(getString(R.string.montyReturn_overcash));
			btnGo.setText(getString(R.string.montyReturn_return));
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnPrint.setOnClickListener(this);
		btnGo.setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnPrint.getId()) {

		}
		if (v.getId() == btnGo.getId()) {
			onBack();
		}
	}

	@Override
	protected void onBack() {
		setResult(RESULT_OK);
		finish();
	}

}
