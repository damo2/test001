package com.shwm.freshmallpos.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.FoodManageClassesAdapter;
import com.shwm.freshmallpos.adapter.FoodManageFoodAdapter;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.inter.ICashOrderFoodAddSub;
import com.shwm.freshmallpos.inter.IOnCartListener;
import com.shwm.freshmallpos.inter.IOnChangeCashFood;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.presenter.MCashOrderFoodPresenter;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICashOrderFoodView;
import com.shwm.freshmallpos.activity.BluetoothListActivity;
import com.shwm.freshmallpos.activity.CashOrderActivity;
import com.shwm.freshmallpos.base.BaseFragment;

/**
 * 开单-选择商品
 * 
 * @author wr 2016-12-2
 */
public class CashOrderFoodFragment extends BaseFragment<ICashOrderFoodView, MCashOrderFoodPresenter> implements OnRefreshListener,
		ICashOrderFoodView, IOnChangeCashFood {
	private RecyclerView rvLeft, rvRight;// 左右列表
	private LinearLayoutManager layoutManagerLeft, layoutManagerRight;// 列表布局管理器
	private FoodManageClassesAdapter mAdapterLeft;// 左边类型适配器
	private FoodManageFoodAdapter mAdapterRight;// 右边商品适配器
	private SwipeRefreshLayout mSwiperefreshlayout;
	private List<ClassesEntity> listClasses;
	private List<FoodEntity> listFood;
	private int classesPosition;
	private ClassesEntity classesCurrent;
	private int pageType;// 刷新还是加载

	private boolean isRefreshing;

	private IOnCartListener iOnCartListener;// 通知ParentActivity 更新购物车
	public  BluetoothService mService;

	private boolean isSearch;
	private String foodNameSearch;

	public static CashOrderFoodFragment newInstance(String title) {
		CashOrderFoodFragment fragment = new CashOrderFoodFragment();
		Bundle args = new Bundle();
		args.putString(ValueKey.TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	public CashOrderFoodFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		mPresenter.getClassesList();
		return mRootView;
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
		// 设置监听 ， 购物车改变 通知 选择商品 改变
		((CashOrderActivity) activity).setIOnChangeCashFood(this);
	}

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_cashorder_food;
	}

	@Override
	public MCashOrderFoodPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MCashOrderFoodPresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();

		if (mService == null) {
			mService = BluetoothService.getInstance(context, null);
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		rvLeft = (RecyclerView) findViewById(R.id.rv_cashorder_food_left);
		rvRight = (RecyclerView) findViewById(R.id.rv_cashorder_food_right);
		mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_cashorder_food);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		// 设置左右边列表适配器
		setRecyclerViewLeft();
		setRecyclerViewRight();
		// 设置下拉刷新
		setSwipRefreshLayout();
	}

	private void setSwipRefreshLayout() {
		mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
		mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
				android.R.color.holo_orange_light, android.R.color.holo_green_light);
		mSwiperefreshlayout.setProgressViewEndTarget(true, ConfigUtil.SwiperefreshHeight);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		mAdapterLeft.setIOnItemClick(iOnItemClickListenerLeft);
		mSwiperefreshlayout.setOnRefreshListener(this);
		rvRight.addOnScrollListener(onScroll);
		rvRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mAdapterRight.setICashOrderFoodAddSub(iCashOrderFoodAddSub);

	}

	private ICashOrderFoodAddSub iCashOrderFoodAddSub = new ICashOrderFoodAddSub() {

		@Override
		public void onAdd(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			if (food.isTypeDefault()) {
				mPresenter.foodAddNum(position, food);
			}
			if (food.isTypeWeight()) {
				listFood.get(position).setAddstatu(true);
				mAdapterRight.notifyDataSetChanged();
			}
		}

		@Override
		public void onSub(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			mPresenter.foodSubNum(position, food);
		}

		@Override
		public void onImg(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			UL.d(TAG, "onImg");
		}

		@Override
		public void onAddFoodWeight(int position, FoodEntity food, double weight) {
			// TODO Auto-generated method stub
			mPresenter.foodAddWeight(food);
		}

		@Override
		public void onRemoveFoodWeight(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			mPresenter.foodRemoveWeight(food);
			mAdapterRight.notifyDataSetChanged();
		}

		@Override
		public void onWeight(int position, FoodEntity food) {
			// TODO Auto-generated method stub
			double weight = 0;
			UL.e(TAG, "onWeight");
			if (BluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
				UL.e(TAG, "STATE_CONNECTED");
				weight = BluetoothService.getWeight();
				food.setNum(weight);
				FoodListData.addToCart(food);
				mAdapterRight.notifyItemChanged(position);
			} else {
				startActivity(new Intent(mActivity, BluetoothListActivity.class).putExtra(ValueKey.TITLE,
						getString(R.string.title_devicemanage)));
			}
		}
	};

	private OnScrollListener onScroll = new OnScrollListener() {
		private int lastVisibleItem;

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapterRight.getItemCount()
					&& mAdapterRight.isShowFooter()) {
				// 设置pageType 为load 然后加载数据
				pageType = ValueType.PAGE_LOAD;
				mAdapterRight.setLoadType(ValueType.LOAD_LOADING);
				mPresenter.getFoodListByClasses();
			}
			switch (newState) {
			case RecyclerView.SCROLL_STATE_DRAGGING:
				// 正在滑动
				// ImageLoader.getInstance().pause();
				break;
			case RecyclerView.SCROLL_STATE_IDLE:
				// 滑动停止
				// ImageLoader.getInstance().resume();
				break;
			}
		};

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			lastVisibleItem = layoutManagerRight.findLastVisibleItemPosition();
		};
	};

	private IOnItemClickListener iOnItemClickListenerLeft = new IOnItemClickListener() {
		@Override
		public void onItemClick(View view, Object t, int postion) {
			// TODO Auto-generated method stub
			if (!isSearch && classesPosition == postion) {
				UL.e(TAG, "classesPosition == postion ");
				return;
			}
			isSearch = false;
			classesPosition = postion;
			if (listClasses != null && listClasses.size() > postion) {
				setClassesCurrent(listClasses.get(postion));
				pageType = ValueType.DEFAULT;
				mPresenter.getFoodListByClasses();
			} else {
				UL.e(TAG, "listClasses  is null ");
			}
		}
	};

	private void setRecyclerViewLeft() {
		// TODO Auto-generated method stub
		layoutManagerLeft = new LinearLayoutManager(context);
		rvLeft.setLayoutManager(layoutManagerLeft);
		rvLeft.getItemAnimator().setChangeDuration(0);
		mAdapterLeft = new FoodManageClassesAdapter(context, ValueType.DEFAULT);
		rvLeft.setAdapter(mAdapterLeft);
	}

	private void setRecyclerViewRight() {
		// TODO Auto-generated method stub
		layoutManagerRight = new LinearLayoutManager(context);
		rvRight.setLayoutManager(layoutManagerRight);
		rvRight.getItemAnimator().setChangeDuration(0);
		mAdapterRight = new FoodManageFoodAdapter(context, listFood, ValueType.DEFAULT);
		rvRight.setAdapter(mAdapterRight);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub

	}

	public void getFoodListByLike(String like) {
		pageType = ValueType.PAGE_DEFAULT;
		isSearch = true;
		foodNameSearch = like;
		mPresenter.getFoodListByLike(like);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isRefreshing = true;
		pageType = ValueType.PAGE_REFRESH;
		if (isSearch) {
			mPresenter.getFoodListByLike(foodNameSearch);
		} else {
			mPresenter.getFoodListByClasses();
		}
		isRefreshing = false;

	}

	@Override
	public void showClassesList(List<ClassesEntity> listClasses) {
		// TODO Auto-generated method stub
		this.listClasses = listClasses;
		if (listClasses != null && listClasses.size() > 0) {
			setClassesCurrent(listClasses.get(0));
			pageType = ValueType.PAGE_DEFAULT;
			listClasses.get(0).setSelected(true);
			mPresenter.getFoodListByClasses();
		}
		mAdapterLeft.setData(listClasses);
		mAdapterLeft.notifyDataSetChanged();
	}

	@Override
	public void showFoodList(List<FoodEntity> listFood) {
		// TODO Auto-generated method stub
		this.listFood = listFood;
		isRefreshing = false;
		mAdapterRight.setLoadType(ValueType.LOAD_OVER);
		mAdapterRight.setData(this.listFood);
		mAdapterRight.notifyDataSetChanged();
	}

	@Override
	public void showFoodListByLike(List<FoodEntity> listFood) {
		// TODO Auto-generated method stub
		this.listFood = listFood;
		isRefreshing = false;
		mAdapterRight.setLoadType(ValueType.LOAD_OVER);
		mAdapterRight.setData(this.listFood);
		mAdapterRight.notifyDataSetChanged();
		if (listClasses != null) {
			for (ClassesEntity classes : listClasses) {
				classes.setSelected(false);
			}
		}
		mAdapterLeft.setData(listClasses);
		mAdapterLeft.notifyDataSetChanged();
	}

	@Override
	public ClassesEntity getClassesCurrent() {
		return classesCurrent;
	}

	// 刷新还是加载
	@Override
	public int getPageType() {
		return pageType;
	}

	public void setClassesCurrent(ClassesEntity classesCurrent) {
		this.classesCurrent = classesCurrent;
	}

	@Override
	public void changeItemFood(int position) {
		// TODO Auto-generated method stub
		UL.d(TAG, "changeItemFood");
		if (position >= 0) {
			mAdapterRight.notifyItemChanged(position);
		} else {
			mAdapterRight.notifyDataSetChanged();
		}
	}

	@Override
	public void changeItemClasses(int position) {
		// TODO Auto-generated method stub
		mAdapterLeft.notifyDataSetChanged();
	}

	@Override
	public int getClassesPosion() {
		// TODO Auto-generated method stub
		return classesPosition;
	}

	@Override
	public void onChangeCashFoodByCart(FoodEntity food, boolean isClear) {
		// TODO Auto-generated method stub
		UL.d(TAG, "购物车改变导致选择商品页面改变");
		mAdapterRight.notifyDataSetChanged();
		mAdapterLeft.notifyDataSetChanged();
	}

	@Override
	public void dismissRefresh() {
		// TODO Auto-generated method stub
		mSwiperefreshlayout.setRefreshing(false);
	}

	@Override
	public void setLoadType(int loadtype) {
		// TODO Auto-generated method stub
		if (loadtype > 0) {
			mAdapterRight.setLoadType(loadtype);
		}
	}

	@Override
	public void changeCart(FoodEntity food) {
		// TODO Auto-generated method stub
		if (iOnCartListener != null) {
			iOnCartListener.changeCartByChooseFood(food);
		}
	}

}
