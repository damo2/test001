package com.shwm.freshmallpos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.model.biz.IPayListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnPayListener;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.EnumMoneyColor;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueFuhao;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 现金收款
 */
public class PaytypeCashActivity extends BaseActivity {
	private IPayListener iPayListener = new OnPayListener();

	private String title;
	private int payType = 4;
	private double moneyReceivable;// 应收金额
	private List<FoodEntity> listcart;
	private static MemberEntity member;
	private List<CouponEntity> listCoupon = new ArrayList<CouponEntity>();

	private double moneyPaid;// 实收金额
	private double moneyChange;// 找零金额
	private StringBuilder str = new StringBuilder();
	private TextView tvMoneyReceivable;
	private TextView tvMoneyPaid;
	private TextView tvMoneyChange;

	private static final int LEN = 3;

	private boolean isFirstInput = true;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_paytype_cash;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_cashReceipt,title);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);
		moneyReceivable = bundle.getDouble(ValueKey.MoneyReceivable);
		listcart = (List<FoodEntity>) bundle.getSerializable(ValueKey.LISTFOOD);
		member = (MemberEntity) bundle.getSerializable(ValueKey.Member);
		moneyPaid = moneyReceivable;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		tvMoneyReceivable = (TextView) findViewById(R.id.tv_paytype_cash_moneyReceivable);
		tvMoneyPaid = (TextView) findViewById(R.id.tv_paytype_cash_moneyPaid);
		tvMoneyChange = (TextView) findViewById(R.id.tv_paytype_cash_moneyChange);

	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		tvMoneyReceivable.setText(StringFormatUtil.moneyFormat(moneyReceivable));
		tvMoneyPaid.setText(StringFormatUtil.moneyFormat(moneyPaid, EnumMoneyColor.BLACK, true));
		tvMoneyChange.setText(StringFormatUtil.moneyFormat(0.00, EnumMoneyColor.BLACK, false));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		findViewById(R.id.btn_paytype_cash_ok).setOnClickListener(this);
		findViewById(R.id.btn_paytype_cash_clear).setOnClickListener(this);
		findViewById(R.id.btn_paytype_cash_del).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_00).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_0).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_point).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_1).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_2).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_3).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_4).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_5).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_6).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_7).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_8).setOnClickListener(onClick);
		findViewById(R.id.btn_paytype_cash_9).setOnClickListener(onClick);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_paytype_cash_clear:
			strClear();
			break;
		case R.id.btn_paytype_cash_ok:
			if (moneyChange >= 0) {
				submit();
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.paytype_cash_moneyNo), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	// 可以快速点击
	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.btn_paytype_cash_del:
				strDelete();
				break;
			case R.id.btn_paytype_cash_point:
				// 小数点前没有数字加个“0”
				if (str.length() == 0) {
					str.append(0);
				}
				// 没有. 才能添加小数点
				if (str.length() > 0 && str.indexOf(ValueFuhao.FUHAO_NUM_POINT) == -1)
					addStr(ValueFuhao.FUHAO_NUM_POINT);
				break;
			case R.id.btn_paytype_cash_00:
				addStr("00");
				break;
			case R.id.btn_paytype_cash_0:
				addStr("0");
				break;
			case R.id.btn_paytype_cash_1:
				addStr("1");
				break;
			case R.id.btn_paytype_cash_2:
				addStr("2");
				break;
			case R.id.btn_paytype_cash_3:
				addStr("3");
				break;
			case R.id.btn_paytype_cash_4:
				addStr("4");
				break;
			case R.id.btn_paytype_cash_5:
				addStr("5");
				break;
			case R.id.btn_paytype_cash_6:
				addStr("6");
				break;
			case R.id.btn_paytype_cash_7:
				addStr("7");
				break;
			case R.id.btn_paytype_cash_8:
				addStr("8");
				break;
			case R.id.btn_paytype_cash_9:
				addStr("9");
				break;
			}
		}
	};

	private void submit() {
		iPayListener.payInCash(payType, member, listcart, moneyPaid, new IRequestListener<HashMap<String, Object>>() {
			@Override
			public void onSuccess(HashMap<String, Object> t) {
				// TODO Auto-generated method stub
				dismissDialogProgress();
				Intent intent = new Intent(mActivity, OverActivity.class);
				intent.putExtra(ValueKey.TITLE, getString(R.string.title_paycashOver));
				intent.putExtra(ValueKey.TYPE, ValueType.OverType_Pay);
				intent.putExtra(ValueKey.MoneyReceivable, moneyReceivable);
				startActivityForResult(intent, ValueRequest.PayTypeCash_Over);
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				showFailInfo(statu, exception);
				dismissDialogProgress();
			}
		});
	}

	/** 添加输入的字符 */
	private void addStr(String n) {
		if (n == null || n.length() == 0)
			return;
		if (isFirstInput) {
			strClear();
			isFirstInput = false;
		}
		int pointPos = str.indexOf(ValueFuhao.FUHAO_NUM_POINT);
		int position = str.length() - pointPos - 1;
		// 小数点后面有2位数了 不添加
		if (pointPos < 0 || position < 2) {
			str.append(n);
		}
		if (UtilMath.sub(StringUtil.getDouble(str.toString(), 0), ValueFinal.MAX_SUM) > 0) {
			if (str.length() > 0) {
				str = str.delete(str.length() - 1, str.length());
			}
		}
		showInfoAndSum();
	}

	private void strDelete() {
		if (str == null || str.length() == 0)
			return;
		str.delete(str.length() - 1, str.length());
		showInfoAndSum();
	}

	private void strClear() {
		if (str == null || str.length() == 0)
			return;
		str.delete(0, str.length());
		showInfoAndSum();
	}

	/** 显示实收金额和找零金额 */
	private void showInfoAndSum() {
		moneyPaid = StringUtil.getDouble(str.toString());
		moneyChange = UtilMath.sub(moneyPaid, moneyReceivable);
		tvMoneyReceivable.setText(StringFormatUtil.moneyFormat(moneyReceivable));
		tvMoneyPaid.setText(StringFormatUtil.moneyFormat(str.toString(), EnumMoneyColor.BLACK, false));
		if (moneyChange > 0) {
			tvMoneyChange.setText(StringFormatUtil.moneyFormat(moneyChange, EnumMoneyColor.RED, true));
		} else {
			tvMoneyChange.setText(StringFormatUtil.moneyFormat(0.00, EnumMoneyColor.BLACK, false));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.PayTypeCash_Over && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
