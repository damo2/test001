package com.shwm.freshmallpos.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.MainOrderAdapter;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.presenter.MMainOrderPresenter;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IMainOrderView;
import com.shwm.freshmallpos.activity.OrderDetailActivity;
import com.shwm.freshmallpos.base.BaseFragment;

/**
 * 流水
 * 
 * @author wr 2016-11-29
 */
public class MainOrderFragment extends BaseFragment<IMainOrderView, MMainOrderPresenter> implements IMainOrderView {
	private RecyclerView mRecyclerView;
	// RecyclerView的ListView模式
	private LinearLayoutManager mLayoutManager;
	private SwipeRefreshLayout mSwiperefreshlayout;

	private MainOrderAdapter mAdapter;
	private List<OrderEntity> listOrder;

	private TextView tvNoData;

	private RelativeLayout mRecycleBar;
	private TextView tvTime;
	private TextView tvTotalDay;
	private int mRecycleBarHeight;
	private int mRecycleCurrentPosition = 0;

	int countA = 0;

	private String title;

	private int pageType = ValueType.PAGE_DEFAULT;

	private int dayNear = -1;

	public static MainOrderFragment newInstance(String title) {
		MainOrderFragment fragment = new MainOrderFragment();
		Bundle args = new Bundle();
		args.putString(ValueKey.TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	public MainOrderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return mRootView;
	}

	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		mPresenter.getListOrder(dayNear);
	}

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_main_order;
	}

	@Override
	public MMainOrderPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MMainOrderPresenter(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		findViewById(R.id.toolbar_main_order).setVisibility(View.GONE);
	}

	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);
		title = arguments.getString(ValueKey.TITLE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_main_order);
		mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_order);
		mRecycleBar = (RelativeLayout) findViewById(R.id.rl_mainOrder_top_view);
		tvTime = (TextView) findViewById(R.id.tv_mainOrder_top_text);
		tvTotalDay = (TextView) findViewById(R.id.tv_mainOrder_top_totalDay);
		tvNoData = (TextView) findViewById(R.id.tv_mainOrder_noData);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setRecyclerView();
		setSwiperefreshLayout();
		tvNoData.setVisibility(View.GONE);
		mRecycleBar.setVisibility(View.GONE);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		mSwiperefreshlayout.setOnRefreshListener(onRefresh);
		mRecyclerView.addOnScrollListener(onScroll);
		mAdapter.setIOnItemClick(iOnItemClickListener);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub

	}

	private IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {
		@Override
		public void onItemClick(View view, Object t, int postion) {
			// TODO Auto-generated method stub
			OrderEntity order = listOrder.get(postion);
			Intent intent = new Intent(mActivity, OrderDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(ValueKey.TITLE, order.getPayTypeTag());
			bundle.putSerializable(ValueKey.ORDER, order);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
	SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			pageType = ValueType.PAGE_REFRESH;
			mPresenter.getListOrder(dayNear);
		}
	};

	private OnScrollListener onScroll = new OnScrollListener() {
		private int lastVisibleItem;

		// 滑动状态改变
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			super.onScrollStateChanged(recyclerView, newState);
			/**
			 * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
			 * SCROLL_STATE_IDLE是当屏幕停止滚动时 SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
			 * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
			 */
			if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() && mAdapter.isShowFooter()) {
				// 加载更多
				pageType = ValueType.PAGE_LOAD;
				mPresenter.getListOrder(dayNear);
				mAdapter.setLoadType(ValueType.LOAD_LOADING);
			}
			mRecycleBarHeight = mRecycleBar.getHeight();
		}

		// 滑动位置
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			// findLastVisibleItemPosition()是返回最后一个item的位置
			lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

			int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
			if (firstVisibleItem == 0) {
				mRecycleBar.setVisibility(View.GONE);
			} else {
				mRecycleBar.setVisibility(View.VISIBLE);
			}
			// 获取指定位置的item的view
			View view = mLayoutManager.findViewByPosition(mRecycleCurrentPosition + 1);
			if (view == null)
				return;
			if (listOrder.size() <= 1) {
				return;
			}
			// date 不相同才滑动
			if (!listOrder.get(mRecycleCurrentPosition + 1).getDate().equals(listOrder.get(mRecycleCurrentPosition).getDate())) {
				if (view.getTop() <= mRecycleBarHeight) {
					// 判断出顶部的顶部布局与上边距的距离,如果<=顶部布局的距离 那么就让把顶部布局的Y轴距离向上偏移,已达到向上滚动的效果
					mRecycleBar.setY(-(mRecycleBarHeight - view.getTop()));
				} else {
					// 直接让顶部布局到顶部
					mRecycleBar.setY(0);
				}
			}
			/**
			 * 获取最上面完全可见的item位置,判断是否是第0位 如果不是第0位,则让顶部布局置顶
			 */
			if (mRecycleCurrentPosition != mLayoutManager.findFirstVisibleItemPosition()) {
				mRecycleCurrentPosition = mLayoutManager.findFirstVisibleItemPosition();
				mRecycleBar.setY(0);
				// 更新RecycleView顶部
				updateRecycleBar();
			}
		}
	};

	private void updateRecycleBar() {
		tvTime.setText(listOrder.get(mRecycleCurrentPosition).getDate());
		String format = getString(R.string.order_totalDay);
		String money = UtilMath.currency(listOrder.get(mRecycleCurrentPosition).getTotalDay());
		tvTotalDay.setText(String.format(format, money));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.right, menu);
		MenuItem item = menu.findItem(R.id.menu_right);
		item.setTitle(getString(R.string.toolbar_order_search));
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setRecyclerView() {
		mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);
		//
		mAdapter = new MainOrderAdapter(context);
		mAdapter.setData(listOrder);
		mRecyclerView.setAdapter(mAdapter);
	}

	private void setSwiperefreshLayout() {
		// 设置刷新时动画的颜色，可以设置4个
		mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
		mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
				android.R.color.holo_orange_light, android.R.color.holo_green_light);
		mSwiperefreshlayout.setProgressViewEndTarget(true, ConfigUtil.SwiperefreshHeight);
	}

	@Override
	public int getPageType() {
		// TODO Auto-generated method stub
		return pageType;
	}

	@Override
	public void showListOrder(List<OrderEntity> listOrder) {
		// TODO Auto-generated method stub
		this.listOrder = listOrder;
		mAdapter.setData(listOrder);
		mAdapter.notifyDataSetChanged();
		if (pageType == ValueType.PAGE_DEFAULT || pageType == ValueType.PAGE_REFRESH) {
			if (listOrder != null && listOrder.size() > 0) {
				updateRecycleBar();
			}
		}
		if (listOrder != null && listOrder.size() > 0) {
			tvNoData.setVisibility(View.GONE);
		} else {
			tvNoData.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setLoadType(int loadType) {
		// TODO Auto-generated method stub
		if (loadType >= 0) {
			mAdapter.setLoadType(loadType);

		}
	}

	@Override
	public void refreshCancel() {
		// TODO Auto-generated method stub
		mSwiperefreshlayout.setRefreshing(false);
	}

}
