package com.shwm.freshmallpos.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.MemberAdapter;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.presenter.MMemberPresenter;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.RecyclerViewDivider;
import com.shwm.freshmallpos.util.UT;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IMemberView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 会员 / 选择会员
 * 
 * @author wr 2016-12-19
 */
public class MemberActivity extends BaseActivity<IMemberView, MMemberPresenter> implements OnRefreshListener, IMemberView,
		SearchView.OnQueryTextListener {
	private String title;
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwiperefreshlayout;
	private LinearLayoutManager mLayoutManager;
	private MemberAdapter mAdapter;
	private Button btnAdd;
	private SearchView searchView;
	private int pageType = ValueType.PAGE_DEFAULT;

	private int type = ValueType.DEFAULT;

	private List<MemberEntity> listMember;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_member;
	}

	@Override
	public MMemberPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MMemberPresenter(this);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mPresenter.getListMember();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		title = getIntent().getExtras().getString(ValueKey.TITLE);
		type = getIntent().getExtras().getInt(ValueKey.TYPE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mRecyclerView = (RecyclerView) findViewById(R.id.rv_member_main);
		mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout_member_main);
		btnAdd = (Button) findViewById(R.id.btn_member_main_add);
	}

	

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapter();
		// 设置下拉刷新样式
		setSwipRefreshLayout();
		if (type == ValueType.CHOOSE) {
			if (searchView != null) {
				searchView.setIconified(false);
			}
		}
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
		btnAdd.setOnClickListener(this);
		mSwiperefreshlayout.setOnRefreshListener(this);
		mAdapter.setIOnItemClickListener(iOnItemClickListener);
	}

	private void setAdapter() {
		mLayoutManager = new LinearLayoutManager(context);
		mRecyclerView.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(context,
				R.color.bg_red)));
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new MemberAdapter(context);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addOnScrollListener(onScroll);
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_member_main,title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_search, menu);
		// 设置搜索输入框的步骤
		// 1.查找指定的MemuItem
		MenuItem menuItem = menu.findItem(R.id.action_search);
		// 2.设置SearchView v7包方式
		View view = MenuItemCompat.getActionView(menuItem);
		if (view != null) {
			searchView = (SearchView) view;
			// 4.设置SearchView 的查询回调接口
			searchView.setOnQueryTextListener(this);
			// 在搜索输入框没有显示的时候 点击Action ,回调这个接口，并且显示输入框
			// searchView.setOnSearchClickListener();
			// 当自动补全的内容被选中的时候回调接口
			// searchView.setOnSuggestionListener();
			// 可以设置搜索的自动补全，或者实现搜索历史
			// searchView.setSuggestionsAdapter();

		}

		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		UT.showShort(context, "search " + query);
		return true;
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnAdd.getId()) {
			Intent intent = new Intent(MemberActivity.this, MemberAddActivity.class);
			intent.putExtra(ValueKey.TITLE, getString(R.string.title_member_add));
			startActivityForResult(intent, ValueRequest.Member_MemberAdd);
		}
	}

	private IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {

		@Override
		public void onItemClick(View view, Object t, int postion) {
			// TODO Auto-generated method stub
			if (type == ValueType.CHOOSE) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(ValueKey.Member, listMember.get(postion));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	};

	private OnScrollListener onScroll = new OnScrollListener() {
		private int lastVisibleItem;

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
				// 设置pageType 为load 然后加载数据
				pageType = ValueType.PAGE_LOAD;
				mAdapter.setLoadType(ValueType.LOAD_LOADING);
				mPresenter.getListMember();
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
			lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
		};
	};

	@Override
	public void showDialogProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dissmissDialogProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		pageType = ValueType.PAGE_REFRESH;
		mPresenter.getListMember();
	}

	@Override
	public void showMemberList(int loadType, List<MemberEntity> listMember) {
		// TODO Auto-generated method stub
		this.listMember = listMember;
		if (loadType > 0) {
			mAdapter.setLoadType(loadType);
		}
		mAdapter.setData(listMember);
		mAdapter.notifyDataSetChanged();
		mSwiperefreshlayout.setRefreshing(false);
	}

	@Override
	public void refreshCancel() {
		// TODO Auto-generated method stub
		mSwiperefreshlayout.setRefreshing(false);
	}

	@Override
	public String getLike() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public int getPageType() {
		// TODO Auto-generated method stub
		return pageType;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.Member_MemberAdd && resultCode == RESULT_OK) {
			pageType = ValueType.PAGE_DEFAULT;
			mPresenter.getListMember();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
