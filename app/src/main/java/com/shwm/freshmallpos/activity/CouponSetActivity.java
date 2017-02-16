package com.shwm.freshmallpos.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CouponDiscountAdapter;
import com.shwm.freshmallpos.adapter.CouponDiscountAdapter.ICouponSetListener;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.myviewbutton.CheckSwitchButton;
import com.shwm.freshmallpos.myviewutil.FullyGridLayoutManager;
import com.shwm.freshmallpos.myviewutil.FullyLinearLayoutManager;
import com.shwm.freshmallpos.presenter.MCouponSetPresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICouponSetView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 优惠劵折扣设置
 */
public class CouponSetActivity extends BaseActivity<ICouponSetView, MCouponSetPresenter> implements ICouponSetView {
	private String title;
	private RecyclerView mRecyclerViewDiscount, mRecyclerViewMoneydown;
	private FullyLinearLayoutManager mLayoutManageDiscount, mLayoutManageMoneydown;
	private CouponDiscountAdapter mCouponDiscountAdapter, mCouponDiscountAdapter2;
	private List<CouponEntity> listCouponDiscount, listCouponMoneydown;
	private CheckSwitchButton mCheckSwithcButton;
	private boolean couponOpenStatu;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_cashset;
	}

	@Override
	public MCouponSetPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MCouponSetPresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = (String) bundle.get(ValueKey.TITLE);
		try {
			couponOpenStatu = UtilSPF.getBoolean(ValueKey.CouponOpenStatu, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_couponset,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mRecyclerViewDiscount = (RecyclerView) findViewById(R.id.recyclerview_couponset_discount);
		mRecyclerViewMoneydown = (RecyclerView) findViewById(R.id.recyclerview_couponset_moneydown);
		mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.checkSwitchButton_openoff);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapter();
		mCheckSwithcButton.setChecked(couponOpenStatu);
		if (couponOpenStatu) {
			requestPermission(ValuePermission.PermissionRequest_STORAGE, ValuePermission.PermissionGroupSTORAGE, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mPresenter.getCouponDataFromFile();
				}
			}, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
					builder.setTitle(getString(R.string.storage_camcel_coupon_title));
					builder.setPositiveButton(getString(R.string.know), null);
					builder.show();
				}
			});
		}
	}

	private void setAdapter() {
		// TODO Auto-generated method stub
		mLayoutManageDiscount = new FullyLinearLayoutManager(context);
		mLayoutManageDiscount.setScrollEnabled(false);
		mLayoutManageMoneydown = new FullyLinearLayoutManager(context);
		mLayoutManageMoneydown.setScrollEnabled(false);
		mRecyclerViewDiscount.setLayoutManager(new FullyGridLayoutManager(context, 3));
		mRecyclerViewMoneydown.setLayoutManager(new FullyGridLayoutManager(context, 2));
		mRecyclerViewDiscount.setHasFixedSize(true);
		mRecyclerViewMoneydown.setHasFixedSize(true);

		mCouponDiscountAdapter = new CouponDiscountAdapter(context, ValueType.CouponType_Discount);
		mCouponDiscountAdapter2 = new CouponDiscountAdapter(context, ValueType.CouponType_Moneydown);

		mCouponDiscountAdapter.setCouponOpenStatu(couponOpenStatu);
		mCouponDiscountAdapter.setData(listCouponDiscount);
		mCouponDiscountAdapter2.setCouponOpenStatu(couponOpenStatu);
		mCouponDiscountAdapter2.setData(listCouponMoneydown);

		mRecyclerViewDiscount.setAdapter(mCouponDiscountAdapter);
		mRecyclerViewMoneydown.setAdapter(mCouponDiscountAdapter2);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		mCouponDiscountAdapter.setIOnCouponSetListener(iCouponSetListener);
		mCouponDiscountAdapter2.setIOnCouponSetListener(iCouponSetListener);
		mCheckSwithcButton.setOnCheckedChangeListener(checkedChangeListener);
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			UL.d(TAG, isChecked ? "打开" : "关闭");
			couponOpenStatu = isChecked;

			mCouponDiscountAdapter.setCouponOpenStatu(couponOpenStatu);
			mCouponDiscountAdapter.notifyDataSetChanged();
			mCouponDiscountAdapter2.setCouponOpenStatu(couponOpenStatu);
			mCouponDiscountAdapter2.notifyDataSetChanged();

			UtilSPF.put(ValueKey.CouponOpenStatu, couponOpenStatu);
		}
	};
	private ICouponSetListener iCouponSetListener = new ICouponSetListener() {
		@Override
		public void onDelete(View view, int posiont, int couponType) {
			// TODO Auto-generated method stub
			if (couponType == ValueType.CouponType_Discount) {
				if (listCouponDiscount != null && posiont < listCouponDiscount.size()) {
					listCouponDiscount.remove(posiont);
					mCouponDiscountAdapter.notifyItemRemoved(posiont);
				}
			}
			if (couponType == ValueType.CouponType_Moneydown) {
				if (listCouponMoneydown != null && posiont < listCouponMoneydown.size()) {
					listCouponMoneydown.remove(posiont);
					mCouponDiscountAdapter2.notifyItemRemoved(posiont);
				}
			}
		}

		@Override
		public void onAdd(View view, int posiont, int couponType) {
			// TODO Auto-generated method stub
			if (couponType == ValueType.CouponType_Discount) {
				showAddDialog(couponType);
			}
			if (couponType == ValueType.CouponType_Moneydown) {
				showAddDialog(couponType);
			}
		}
	};

	@Override
	public void showListCouponDiscount(List<CouponEntity> listDiscount) {
		// TODO Auto-generated method stub
		this.listCouponDiscount = listDiscount;
		mCouponDiscountAdapter.setData(listCouponDiscount);
		mCouponDiscountAdapter.notifyDataSetChanged();
	}

	@Override
	public void showListCouponMoneydown(List<CouponEntity> listMoneydown) {
		// TODO Auto-generated method stub
		this.listCouponMoneydown = listMoneydown;
		mCouponDiscountAdapter2.setData(listCouponMoneydown);
		mCouponDiscountAdapter2.notifyDataSetChanged();
	}

	@Override
	public List<CouponEntity> getCouponDiscount() {
		// TODO Auto-generated method stub
		return listCouponDiscount;
	}

	@Override
	public List<CouponEntity> getCouponMoneydown() {
		// TODO Auto-generated method stub
		return listCouponMoneydown;
	}

	private void showAddDialog(final int type) {
		String title = "";
		String hint = "";
		int count = 1;// 保留几位小数
		if (type == ValueType.CouponType_Discount) {
			title = getString(R.string.couponset_add_discount);
			hint = getString(R.string.couponset_add_discount_hint);
			count = 1;
		}
		if (type == ValueType.CouponType_Moneydown) {
			title = getString(R.string.couponset_add_moneydown);
			hint = getString(R.string.couponset_add_moneydown_hint);
			count = 2;
		}
		final String edtInfoNull = hint;
		final EditText edt = new EditText(mActivity);
		edt.setHint(hint);
		edt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		StringUtil.getIntFomat(edt, count);
		edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(s)) {
					int length = s.length();
					if (type == ValueType.CouponType_Discount && StringUtil.getDouble(s.toString()) > ValueFinal.DiscountMax) {
						Toast.makeText(getApplicationContext(), getString(R.string.member_add_discount_max), Toast.LENGTH_SHORT).show();
						s.delete(length - 1, length);
					}
					if (type == ValueType.CouponType_Moneydown && StringUtil.getDouble(s.toString()) > ValueFinal.MoneydownMax) {
						Toast.makeText(getApplicationContext(), getString(R.string.member_add_moneydown_max), Toast.LENGTH_SHORT).show();
						s.delete(length - 1, length);
					}
				}
			}
		});
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
		builder.setTitle(title);
		builder.setView(edt);
		builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String info = edt.getText().toString();
				if (TextUtils.isEmpty(info) || StringUtil.getDouble(info) == 0) {
					Toast.makeText(getApplicationContext(), edtInfoNull, Toast.LENGTH_SHORT).show();
					return;
				}
				double value = StringUtil.getDouble(info);
				if (isExistCoupon(type, value)) {
					Toast.makeText(getApplicationContext(), getString(R.string.couponset_add_exist), Toast.LENGTH_SHORT).show();
					return;
				}

				CouponEntity coupon = new CouponEntity();
				coupon.setType(type);
				if (type == ValueType.CouponType_Discount) {
					coupon.setDiscount(value);
					if (listCouponDiscount == null) {
						listCouponDiscount = new ArrayList<CouponEntity>();
					}
					listCouponDiscount.add(coupon);
					mCouponDiscountAdapter.setData(listCouponDiscount);
					mCouponDiscountAdapter.notifyDataSetChanged();
				} else if (type == ValueType.CouponType_Moneydown) {
					coupon.setMoneydown(value);
					if (listCouponMoneydown == null) {
						listCouponMoneydown = new ArrayList<CouponEntity>();
					}
					listCouponMoneydown.add(coupon);
					mCouponDiscountAdapter2.setData(listCouponMoneydown);
					mCouponDiscountAdapter2.notifyDataSetChanged();
				}
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.show();
	}

	private boolean isExistCoupon(int type, double value) {
		List<CouponEntity> listCouponEntities = null;
		if (type == ValueType.CouponType_Discount) {
			listCouponEntities = listCouponDiscount;
		}
		if (type == ValueType.CouponType_Moneydown) {
			listCouponEntities = listCouponMoneydown;
		}
		if (listCouponEntities != null) {
			for (CouponEntity couponEntity : listCouponEntities) {
				if (type == ValueType.CouponType_Discount && UtilMath.sub(couponEntity.getDiscount(), value) == 0) {
					return true;
				}
				if (type == ValueType.CouponType_Moneydown && UtilMath.sub(couponEntity.getMoneydown(), value) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (couponOpenStatu) {
			requestPermission(ValuePermission.PermissionRequest_STORAGE, ValuePermission.PermissionGroupSTORAGE, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mPresenter.saveCouponDataToFile();
				}
			}, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
					builder.setTitle(getString(R.string.storage_camcel_coupon_title));
					builder.setPositiveButton(getString(R.string.know), null);
					builder.show();
				}
			});
		}
		super.onDestroy();
	}
}
