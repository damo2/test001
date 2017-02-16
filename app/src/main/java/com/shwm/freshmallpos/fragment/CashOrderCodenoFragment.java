package com.shwm.freshmallpos.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IOnCartListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueFuhao;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.base.BaseFragment;

/**
 * 无码商品
 * 
 * @author wr 2016-12-19
 */
public class CashOrderCodenoFragment extends BaseFragment {
	private StringBuilder str = new StringBuilder();

	private static final double MAX_SUM = 200000;

	private EditText tvMoney;

	private IOnCartListener iOnCartListener;// 通知ParentActivity 更新购物车

	public static CashOrderCodenoFragment newInstance(String title) {
		CashOrderCodenoFragment fragment = new CashOrderCodenoFragment();
		Bundle args = new Bundle();
		args.putString(ValueKey.TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof IOnCartListener) {
			iOnCartListener = (IOnCartListener) activity;
		} else {
			UL.e(TAG, "activity 必须实现 IOnCartListener 接口以便更新购物车");
		}
	}

	public CashOrderCodenoFragment() {

	}

	@Override
	protected int setLayoutResouceId() {
		return R.layout.fragment_cashorder_codeno;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		tvMoney = (EditText) findViewById(R.id.edt_orderno_money);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		findViewById(R.id.btn_orderno_add).setOnClickListener(this);
		findViewById(R.id.btn_orderno_delete).setOnClickListener(this);
		findViewById(R.id.btn_orderno_clear).setOnClickListener(this);
		findViewById(R.id.btn_orderno_num00).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num0).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_point).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num1).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num2).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num3).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num4).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num5).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num6).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num7).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num8).setOnClickListener(onClick);
		findViewById(R.id.btn_orderno_num9).setOnClickListener(onClick);
	}

	@Override
	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_orderno_add:
			addMoney();
			break;
		case R.id.btn_orderno_delete:
			deleteMoney();
			break;
		case R.id.btn_orderno_clear:
			clearMoney();
			break;
		default:
			break;
		}
	}

	private void clearMoney() {
		if (str != null && str.length() > 0) {
			str.delete(0, str.length());
			showMoney();
		}
	}

	private void deleteMoney() {
		if (str != null && str.length() > 0) {
			str.delete(str.length() - 1, str.length());
			showMoney();
		}
	}

	private void addMoney() {
		if (str == null || str.length() == 0)
			return;
		double money = StringUtil.getDouble(str.toString());
		if (money <= 0) {
			Toast.makeText(context, getString(R.string.cashOrder_code_moneyfail), Toast.LENGTH_SHORT).show();
			return;
		}
		FoodEntity food = new FoodEntity();
		int foodId = FoodListData.getFoodIdNocode();
		String namenocode = String.format(getString(R.string.cashOrder_codeno_foodname), Math.abs(foodId) + "");
		food.setId(foodId);
		food.setName(namenocode);
		food.setPrice(money);
		food.setNocode(true);
		food.setNum(1);
		food.setUnit(ValueFinal.foodNoCodeUnit);
		FoodListData.addToCart(food);
		iOnCartListener.changeCartByChooseFood(food);
		clearMoney();
	}

	// 可以快速点击
	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.btn_orderno_point:
				// 小数点前没有数字加个“0”
				if (str.length() == 0) {
					str.append(0);
				}
				// 没有. 才能添加小数点
				if (str.length() > 0 && str.indexOf(ValueFuhao.FUHAO_NUM_POINT) == -1)
					addStr(ValueFuhao.FUHAO_NUM_POINT);
				break;
			case R.id.btn_orderno_num00:
				addStr("00");
				break;
			case R.id.btn_orderno_num0:
				addStr("0");
				break;
			case R.id.btn_orderno_num1:
				addStr("1");
				break;
			case R.id.btn_orderno_num2:
				addStr("2");
				break;
			case R.id.btn_orderno_num3:
				addStr("3");
				break;
			case R.id.btn_orderno_num4:
				addStr("4");
				break;
			case R.id.btn_orderno_num5:
				addStr("5");
				break;
			case R.id.btn_orderno_num6:
				addStr("6");
				break;
			case R.id.btn_orderno_num7:
				addStr("7");
				break;
			case R.id.btn_orderno_num8:
				addStr("8");
				break;
			case R.id.btn_orderno_num9:
				addStr("9");
				break;
			}
		}
	};

	/** 添加输入的字符 */
	private void addStr(String n) {
		if (n == null || n.length() == 0)
			return;

		if (StringUtil.getDouble(str.toString()) > ValueFinal.MAX_SUM) {
			String failinfo = String.format(getString(R.string.cashOrder_code_maxmoney), ValueFinal.MAX_SUM + "");
			Toast.makeText(context, failinfo, Toast.LENGTH_SHORT).show();
			str.delete(str.length() - 1, str.length());
		} else {
			int pointPos = str.indexOf(ValueFuhao.FUHAO_NUM_POINT);
			int position = str.length() - pointPos - 1;
			// 小数点后面有2位数了 不添加
			if (pointPos < 0 || position < 2) {
				str.append(n);
			}
		}
		showMoney();
	}

	private void showMoney() {
		if (str != null) {
			tvMoney.setText(str.toString());
		} else {
			tvMoney.setText("");
		}
	}

}
