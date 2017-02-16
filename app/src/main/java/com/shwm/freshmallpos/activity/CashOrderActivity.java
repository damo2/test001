package com.shwm.freshmallpos.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.FoodManageFoodAdapter;
import com.shwm.freshmallpos.adapter.SectionsPagerAdapter;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.inter.ICashOrderFoodAddSub;
import com.shwm.freshmallpos.inter.IOnCartListener;
import com.shwm.freshmallpos.inter.IOnChangeCashFood;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.myviewutil.MyLinearLayoutManager;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.fragment.CashOrderCodeFragment;
import com.shwm.freshmallpos.fragment.CashOrderCodenoFragment;
import com.shwm.freshmallpos.fragment.CashOrderFoodFragment;
import com.zxing.fragment.CaptureFragment;

/**
 * 开单
 * 
 * @author wr 2016-12-2
 */
public class CashOrderActivity extends BaseActivity implements SearchView.OnQueryTextListener, IOnCartListener {
	private String title;
	private SearchView mSearchView;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private MyLinearLayoutManager mLayoutManage;
	private List<Fragment> listFragment;
	private List<String> listTabTitle;
	private TabLayout mTabLayout;

	private View viewTopP, viewBottomV;
	private TextView tvNumSumMain;
	private TextView tvPriceSumMain;
	private Dialog mBottomSheetDialog;

	private RecyclerView mRecyclerViewP;
	private FoodManageFoodAdapter mAdapter;
	private TextView tvPriceSumDialog;
	private TextView tvNumSumDialog;
	private TextView tvClearCart;

	private List<FoodEntity> listFood;

	private MyBrodcast brodcast;
	private IOnChangeCashFood iOnChangeCashFood;

	private CashOrderFoodFragment cashOrderFoodFragment;

	private double priceSum;
	private int numSum;
	public  BluetoothService mService;// 蓝牙

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setViewPagerAdapter();// 初始化ViewPage();
		setRegisterBrocast();
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// 二维码滑动闪屏
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (brodcast != null)
			unregisterReceiver(brodcast);
		if (mBottomSheetDialog != null) {
			mBottomSheetDialog.dismiss();
		}
		FoodListData.clearData();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_cash_order;
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
		setToolbar(R.id.toolbar_cashOrder,title);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);

		cashOrderFoodFragment = CashOrderFoodFragment.newInstance(getString(R.string.cashOrder_tab_chooseFood));
		listFragment = new ArrayList<Fragment>();
		listFragment.add(cashOrderFoodFragment);
		listFragment.add(CashOrderCodeFragment.newInstance(getString(R.string.cashOrder_tab_codeFood)));
		listFragment.add(CashOrderCodenoFragment.newInstance(getString(R.string.cashOrder_tab_nocodeFood)));
		listTabTitle = new ArrayList<String>();
		listTabTitle.add(getString(R.string.cashOrder_tab_chooseFood));
		listTabTitle.add(getString(R.string.cashOrder_tab_codeFood));
		listTabTitle.add(getString(R.string.cashOrder_tab_nocodeFood));

		setDialogBottom();
		if (mService == null) {
			mService = BluetoothService.getInstance(getApplicationContext(), null);
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mTabLayout = (TabLayout) findViewById(R.id.tablayout_cashOrder);
		viewBottomV = findViewById(R.id.rl_cashOrder_bottom);
		tvNumSumMain = (TextView) findViewById(R.id.tv_cashOrder_numSum);
		tvPriceSumMain = (TextView) findViewById(R.id.tv_cashOrder_priceSum);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapterGone();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		viewBottomV.setOnClickListener(this);
		findViewById(R.id.btn_cashOrder_cart).setOnClickListener(this);
		findViewById(R.id.btn_cashOrer_tosubmitOrder).setOnClickListener(onClickOrdersubmit);
		mAdapter.setICashOrderFoodAddSub(iCashOrderFoodAddSub);

		mBottomSheetDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (iOnChangeCashFood != null) {
					iOnChangeCashFood.onChangeCashFoodByCart(null, false);
				}
			}
		});
	}

	private ICashOrderFoodAddSub iCashOrderFoodAddSub = new ICashOrderFoodAddSub() {

		@Override
		public void onSub(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			setCart(position, food, false);
			if (listFood == null || listFood.size() == 0) {
				mBottomSheetDialog.dismiss();
			}
		}

		@Override
		public void onImg(int position, FoodEntity food) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAdd(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			if (food.isTypeDefault()) {
				setCart(position, food, true);
			}
			if (food.isTypeWeight()) {
				listFood.get(position).setAddstatu(true);
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onAddFoodWeight(int position, FoodEntity food, double weight) {
			// TODO Auto-generated method stub
			FoodListData.addToCart(food);
			setPriceSumAndNumSum();
		}

		@Override
		public void onRemoveFoodWeight(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			FoodListData.removeFromCart(food);
			listFood = FoodListData.getCartAll();
			mAdapter.setData(listFood);
			mAdapter.notifyDataSetChanged();
			if (iOnChangeCashFood != null) {
				iOnChangeCashFood.onChangeCashFoodByCart(food, false);
			}
			setPriceSumAndNumSum();
			if (listFood == null || listFood.size() == 0) {
				mBottomSheetDialog.dismiss();
			}
		}

		@Override
		public void onWeight(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			double weight = 0;
			if (BluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
				weight = BluetoothService.getWeight();
				food.setNum(weight);
				FoodListData.addToCart(food);
				listFood = FoodListData.getCartAll();
				mAdapter.setData(listFood);
				mAdapter.notifyDataSetChanged();
				setPriceSumAndNumSum();//设置总价和总数
			} else {
				startActivity(new Intent(mActivity, BluetoothListActivity.class).putExtra(ValueKey.TITLE,
						getString(R.string.title_devicemanage)));
			}
		}
	};

	private void setCart(int position, FoodEntity food, boolean isAdd) {
		int foodId = food.getId();
		if (isAdd) {
			FoodListData.setFoodSupByIdFromCart(foodId);
		} else {
			FoodListData.setFoodSubByIdFromCart(foodId);
		}
		listFood=FoodListData.getCartAll();
		mAdapter.setData(listFood);
		mAdapter.notifyDataSetChanged();
			if (food.getClasses() != null) {// 设置类型数量
				FoodListData.setClassesFoodNumAddOrSub(food.getClasses().getId(), isAdd);
			}
			setPriceSumAndNumSum();
			if (iOnChangeCashFood != null) {
				iOnChangeCashFood.onChangeCashFoodByCart(food, false);
			}
	}

	private void setViewPagerAdapter() {
		// TODO Auto-generated method stub
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager_cashOrder);
		mSectionsPagerAdapter.setListFragment(listFragment, listTabTitle);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(onPageChangelistener);
		mViewPager.setOffscreenPageLimit(3);// 预加载
		// tablayout绑定Viewpage
		mTabLayout.setupWithViewPager(mViewPager);
	}

	// 设置购物车的RecycleView适配器
	private void setAdapterGone() {
		mAdapter = new FoodManageFoodAdapter(context, listFood, ValueType.CART);
		mLayoutManage = new MyLinearLayoutManager(context);
		mLayoutManage.setScrollEnabled(false);
		mRecyclerViewP.setLayoutManager(mLayoutManage);
		mAdapter.setData(listFood);
		mRecyclerViewP.setAdapter(mAdapter);
		// 添加分割线
		// mRecyclerViewP.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		// 动画就不显示
		((SimpleItemAnimator) mRecyclerViewP.getItemAnimator()).setSupportsChangeAnimations(false);
	}

	// 初始化底部购物车弹出框
	private void setDialogBottom() {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.view_popup_cashorder, null);
		mBottomSheetDialog = new Dialog(CashOrderActivity.this, R.style.MaterialDialogSheet);
		viewTopP = view.findViewById(R.id.rl_cashOrder_gone);
		mRecyclerViewP = (RecyclerView) view.findViewById(R.id.rv_cashOrder);
		tvPriceSumDialog = (TextView) view.findViewById(R.id.tv_cashOrder_priceSum_popup);
		tvNumSumDialog = (TextView) view.findViewById(R.id.tv_cashOrder_numSum_popup);

		viewTopP.setOnClickListener(this);
		tvClearCart = (TextView) view.findViewById(R.id.tv_cashOrder_clearList);
		view.findViewById(R.id.btn_cashOrder_cart_gone).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mBottomSheetDialog.dismiss();
			}
		});
		view.findViewById(R.id.view_cashOrder_dismiss).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mBottomSheetDialog.dismiss();
			}
		});
		tvClearCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
				builder.setTitle(getString(R.string.tig));
				builder.setMessage(getString(R.string.cashOrder_cart_clear));
				builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						clearCart();
						mBottomSheetDialog.dismiss();
					}
				});
				builder.setNegativeButton(getString(R.string.cancel), null);
				builder.show();
			}
		});
		view.findViewById(R.id.btn_cashOrer_tosubmitOrder_popup).setOnClickListener(onClickOrdersubmit);
		mBottomSheetDialog.setContentView(view);
		mBottomSheetDialog.setCancelable(true);
		mBottomSheetDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
		mBottomSheetDialog.setCanceledOnTouchOutside(true);
		mBottomSheetDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				// mBottomSheetDialog.dismiss();
			}
		});

		// mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// mBottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		// WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// mBottomSheetDialog.getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		// 显示隐藏的View,点击隐藏
		if (v.getId() == viewTopP.getId()) {
			mBottomSheetDialog.dismiss();// 隐藏
		}
		// 底部View，点击显示
		if (v.getId() == viewBottomV.getId() || v.getId() == R.id.btn_cashOrder_cart) {
			if (listFood != null && listFood.size() > 0) {
				mBottomSheetDialog.show();// 显示
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.cashOrder_cart_no), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private OnPageChangeListener onPageChangelistener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			hideInput();
		}
	};

	private OnClickListener onClickOrdersubmit = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			List<FoodEntity> listCart = FoodListData.getCartAll();
			if (listCart != null && listCart.size() > 0) {
				Intent intent = new Intent(mActivity, OrdersubmitActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(ValueKey.TITLE, getString(R.string.title_ordersubmit));
				bundle.putSerializable(ValueKey.LISTCART, (Serializable) FoodListData.getCartAll());
				intent.putExtras(bundle);
				startActivityForResult(intent, ValueRequest.CashOrder_OrderSubmit);
			} else {
				Toast.makeText(context, getString(R.string.cashOrder_cart_no), Toast.LENGTH_SHORT).show();
			}
		}
	};

	@SuppressLint("ResourceAsColor")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_search, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search);
		// menuItem.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_search));
		// 设置SearchView v7包方式
		mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
		// 设置SearchView 的查询回调接口
		mSearchView.setOnQueryTextListener(this);

		SearchView.SearchAutoComplete mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
		mEdit.setTextColor(getResources().getColorStateList(R.color.white));
		return true;
	}

	/**
	 * 当用户在输入法中点击搜索按钮时,或者输入回车时,调用这个方法，发起实际的搜索功能
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		// Toast.makeText(CashOrderActivity.this, "Submit=" + query, Toast.LENGTH_SHORT).show();
		mSearchView.clearFocus();
		mViewPager.setCurrentItem(0);
		cashOrderFoodFragment.getFoodListByLike(query);
		return true;
	}

	/**
	 * 每一次输入字符，都会调用这个方法，实现搜索的联想功能
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
		// Toast.makeText(CashOrderActivity.this, "" + newText, Toast.LENGTH_SHORT).show();
		return true;
	}

	private void setRegisterBrocast() {
		brodcast = new MyBrodcast();
		IntentFilter intentFilter = new IntentFilter(CaptureFragment.SCAN_RESULT_ACTION);
		registerReceiver(brodcast, intentFilter);
	}

	public class MyBrodcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(CaptureFragment.SCAN_RESULT_ACTION)) {
				UL.d(TAG, intent.getExtras().getString(ValueKey.BARCODE));
			}
		}
	}

	@Override
	public void changeCartByChooseFood(FoodEntity food) {
		setPriceSumAndNumSum();
		mAdapter.setData(listFood);
		mAdapter.notifyDataSetChanged();
	}

	private void setPriceSumAndNumSum() {
		priceSum = 0;
		numSum = 0;
		double num = 0;
		listFood=FoodListData.getCartAll();
		for (FoodEntity foodEntity : listFood) {
			if (foodEntity.isTypeDefault()) {
				num = foodEntity.getNum();
			} else if (foodEntity.isTypeWeight()) {
				if (foodEntity.getNum() > 0) {
					num = 1;
				}
			}
			numSum += num;
			priceSum = UtilMath.add(priceSum, foodEntity.getPrice() * foodEntity.getNum());
		}
		setMoneyAndNum();
	}

	private void setMoneyAndNum() {
		tvPriceSumDialog.setText(UtilMath.currency(priceSum));
		tvNumSumDialog.setText(numSum + "");
		tvPriceSumMain.setText(UtilMath.currency(priceSum));
		tvNumSumMain.setText(numSum + "");
	}

	public void setIOnChangeCashFood(IOnChangeCashFood iOnChangeCashFood) {
		this.iOnChangeCashFood = iOnChangeCashFood;
	}

	// 清除购物车
	private void clearCart() {
		priceSum = 0;
		numSum = 0;
		setMoneyAndNum();
		FoodListData.clearCartNum();
		if (listFood != null) {
			listFood.clear();
		}
		mAdapter.setData(listFood);
		mAdapter.notifyDataSetChanged();
		if (iOnChangeCashFood != null) {
			iOnChangeCashFood.onChangeCashFoodByCart(null, true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.CashOrder_OrderSubmit && resultCode == RESULT_OK) {
			clearCart();
			mBottomSheetDialog.dismiss();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onBack() {
		if(priceSum>0) {
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
			builder.setTitle(getString(R.string.tig));
			builder.setMessage(getString(R.string.is_exit_cash));
			builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finish();
				}
			});
			builder.show();
		}else{
			finish();
		}
	}
}
