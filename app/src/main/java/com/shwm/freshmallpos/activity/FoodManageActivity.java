package com.shwm.freshmallpos.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.FoodManageClassesAdapter;
import com.shwm.freshmallpos.adapter.FoodManageFoodAdapter;
import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.manage.FoodListData;
import com.shwm.freshmallpos.presenter.MFoodManagePresenter;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IFoodManageView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 商品管理
 * 
 * @author wr 2016-11-30
 */
public class FoodManageActivity extends BaseActivity<IFoodManageView, MFoodManagePresenter> implements IFoodManageView, OnRefreshListener {
	private String title;// 标题

	private RecyclerView rvLeft, rvRight;// 左右列表
	private LinearLayoutManager layoutManagerLeft, layoutManagerRight;// 列表布局管理器
	private FoodManageClassesAdapter mAdapterLeft;// 左边类型适配器
	private FoodManageFoodAdapter mAdapterRight;// 右边商品适配器
	private SwipeRefreshLayout mSwiperefreshlayout;
	private List<ClassesEntity> listClasses;
	private List<FoodEntity> listFood;
	private Button btnEdtCancel, btnAddDel;

	private int typeEdit = ValueType.ADD;
	private int pageType;// 刷新还是加载
	private ClassesEntity classesCurrent;
	private int classesPosition = 0;

	private AlertDialog dialog;

	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		mPresenter.getClassesList();// 取类型列表
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FoodListData.clearData();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_cash_foodmanage;
	}

	@Override
	public MFoodManagePresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MFoodManagePresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE, ValueKey.TITLE);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		rvLeft = (RecyclerView) findViewById(R.id.rv_cash_product_left);
		rvRight = (RecyclerView) findViewById(R.id.rv_cash_product_right);
		mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_cash_product);
		btnEdtCancel = (Button) findViewById(R.id.btn_cash_product_edtCancel);
		btnAddDel = (Button) findViewById(R.id.btn_cash_product_addDelete);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		// 设置左右边列表适配器
		setRecyclerViewLeft();
		setRecyclerViewRight();
		// 设置下拉刷新样式
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
		mAdapterRight.setIOnItemClick(iOnItemClickListenerRight);
		btnEdtCancel.setOnClickListener(this);
		btnAddDel.setOnClickListener(this);
		rvRight.addOnScrollListener(onScroll);
		mSwiperefreshlayout.setOnRefreshListener(this);
	}

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
			classesPosition = postion;
			if (listClasses != null && listClasses.size() > 0 && listClasses.get(postion) != null) {
				classesCurrent = listClasses.get(postion);
				mPresenter.getFoodListByClasses();
			}

		}
	};
	private IOnItemClickListener iOnItemClickListenerRight = new IOnItemClickListener() {
		@Override
		public void onItemClick(View view, Object t, int position) {
			// TODO Auto-generated method stub
			if (typeEdit == ValueType.EDIT) {
				FoodListData.addOrRemoveToListChoose(listFood.get(position));
				mAdapterRight.notifyItemChanged(position);
				mAdapterLeft.notifyItemChanged(classesPosition);
			}
			if (typeEdit == ValueType.ADD) {// 默认状态
				Intent intent = new Intent(FoodManageActivity.this, FoodEditActivity.class);
				intent.putExtra(ValueKey.TITLE, getString(R.string.title_foodedit));
				intent.putExtra(ValueKey.FOODEDIT_TYPE, ValueType.EDIT);
				intent.putExtra(ValueKey.CLASSES, classesCurrent);
				intent.putExtra(ValueKey.FOOD, listFood.get(position));
				startActivityForResult(intent, ValueRequest.FoodManage_FoodEdit_EDIT);
			}
		}
	};
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_cash_product, title);
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getItemId() == R.id.menu_right) {
					Intent intent = new Intent(FoodManageActivity.this, ClassesManageActivity.class);
					intent.putExtra(ValueKey.TITLE, getString(R.string.title_classesmanage_one));
					intent.putExtra(ValueKey.CLASSESMANAGE_TYPE, ValueType.DEFAULT);
					startActivityForResult(intent, ValueRequest.FoodManage_Classesmanage);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		getMenuInflater().inflate(R.menu.right, menu);
		MenuItem item = menu.findItem(R.id.menu_right);
		item.setTitle(getString(R.string.cash_foodmanage_top_classesmanage));
		return true;
	}

	private void setRecyclerViewLeft() {
		// TODO Auto-generated method stub
		layoutManagerLeft = new LinearLayoutManager(context);
		rvLeft.setLayoutManager(layoutManagerLeft);
		mAdapterLeft = new FoodManageClassesAdapter(context, ValueType.DEFAULT);
		mAdapterLeft.setData(listClasses);
		rvLeft.setAdapter(mAdapterLeft);
	}

	private void setRecyclerViewRight() {
		// TODO Auto-generated method stub
		layoutManagerRight = new LinearLayoutManager(context);
		rvRight.setLayoutManager(layoutManagerRight);
		rvRight.setHasFixedSize(true);
		mAdapterRight = new FoodManageFoodAdapter(context, listFood, typeEdit);
		mAdapterRight.setData(listFood);
		rvRight.setAdapter(mAdapterRight);
	}

	@Override
	public void showClasses(List<ClassesEntity> listClassesAll) {
		// TODO Auto-generated method stub
		UL.d(TAG, "showClasses");
		this.listClasses = listClassesAll;
		// 取到类型后去取商品
		if (isFirst && this.listClasses != null && this.listClasses.size() > 0) {
			// 默认取第一类商品
			isFirst = false;
			classesPosition = 0;
			pageType = ValueType.PAGE_DEFAULT;
			mPresenter.getFoodListByClasses();
		}
		if (listClasses != null && listClasses.size() > 0 && classesPosition < listClasses.size()) {
			classesCurrent = listClasses.get(classesPosition);
			listClasses.get(classesPosition).setSelected(true);
		}
		mAdapterLeft.setData(listClasses);
		mAdapterLeft.notifyDataSetChanged();
	}

	@Override
	public void showFoods(List<FoodEntity> listfood) {
		// TODO Auto-generated method stub
		this.listFood = listfood;
		mAdapterRight.setData(listFood);
		mAdapterRight.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageType = ValueType.PAGE_REFRESH;
		mPresenter.getFoodListByClasses();
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		// 编辑/取消
		if (v.getId() == btnEdtCancel.getId()) {
			if (typeEdit == ValueType.ADD) {
				typeEdit = ValueType.EDIT;
			} else {
				typeEdit = ValueType.ADD;
				FoodListData.clearChoose();
			}
			changeBottom();
			mAdapterRight.setTypeEdit(typeEdit);
			mAdapterRight.notifyDataSetChanged();
			mAdapterLeft.setTypeEdit(typeEdit);
			mAdapterLeft.notifyDataSetChanged();
		}
		if (v.getId() == btnAddDel.getId()) {
			if (typeEdit == ValueType.EDIT) {
				mPresenter.sureDelChooseFood();
			}
			if (typeEdit == ValueType.ADD) {
				Intent intent = new Intent(FoodManageActivity.this, FoodEditActivity.class);
				intent.putExtra(ValueKey.TITLE, getString(R.string.title_foodadd));
				intent.putExtra(ValueKey.FOODEDIT_TYPE, ValueType.ADD);
				intent.putExtra(ValueKey.CLASSES, classesCurrent);
				startActivityForResult(intent, ValueRequest.FoodManage_FoodEdit_ADD);
			}
		}
	}

	private void changeBottom() {
		if (typeEdit == ValueType.ADD) {
			btnEdtCancel.setText(getString(R.string.cash_foodmanage_bottom_editAll));
			btnAddDel.setText(getString(R.string.cash_foodmanage_bottom_addFood));
		} else {
			btnEdtCancel.setText(getString(R.string.cash_foodmanage_bottom_cancel));
			btnAddDel.setText(getString(R.string.cash_foodmanage_bottom_delete));
		}
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

	@Override
	public int getClassesPosion() {
		// TODO Auto-generated method stub
		return classesPosition;
	}

	@Override
	public void dismissRefresh() {
		// TODO Auto-generated method stub
		mSwiperefreshlayout.setRefreshing(false);
	}

	@Override
	public void setLoadType(int pagetype) {
		// TODO Auto-generated method stub
		mAdapterRight.setLoadType(pagetype);
		mAdapterRight.notifyDataSetChanged();
	}

	@Override
	public void showDialogDel(String context) {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.delete));
		builder.setMessage(context);
		builder.setPositiveButton(getString(R.string.btn_sure), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				mPresenter.delChooseFood();
			}
		});
		builder.setNegativeButton(getString(R.string.btn_cancel), null);
		builder.show();
	}

	@Override
	public void delSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), getString(R.string.foodmanage_delSuccess), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultiCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.FoodManage_FoodEdit_ADD && resultiCode == RESULT_OK) {
			pageType = ValueType.PAGE_REFRESH;
			mPresenter.getClassesListByData();
			mPresenter.getFoodListByClasses();
		}
		if (requestCode == ValueRequest.FoodManage_FoodEdit_EDIT && resultiCode == RESULT_OK) {
			mPresenter.getClassesListByData();
			FoodEntity foodEdit = (FoodEntity) data.getExtras().getSerializable(ValueKey.FOOD);
			if (listFood != null && foodEdit != null) {
				int size = listFood.size();
				for (int i = 0; i < size; i++) {
					if (foodEdit.getId() == listFood.get(i).getId()) {
						listFood.set(i, foodEdit);
						mAdapterRight.setData(listFood);
						mAdapterRight.notifyDataSetChanged();
					}
				}
			}
		}
		if (requestCode == ValueRequest.FoodManage_Classesmanage && resultiCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			int type = bundle.getInt(ValueKey.TYPE);
			if (type == ValueType.EDIT) {
				listClasses = (List<ClassesEntity>) bundle.getSerializable(ValueKey.CLASSESListAll);
				mPresenter.getClassesList();// 取类型列表
			}
		}
		super.onActivityResult(requestCode, resultiCode, data);
	}

}
