package com.shwm.freshmallpos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.activity.CashOrderActivity;
import com.shwm.freshmallpos.activity.CashReceiptActivity;
import com.shwm.freshmallpos.activity.CouponSetActivity;
import com.shwm.freshmallpos.activity.FoodManageActivity;
import com.shwm.freshmallpos.activity.IncomeActivity;
import com.shwm.freshmallpos.activity.MemberActivity;
import com.shwm.freshmallpos.adapter.MainCashAdapter;
import com.shwm.freshmallpos.base.BaseFragment;
import com.shwm.freshmallpos.been.IconEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.myviewutil.FullyGridLayoutManager;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * 收银
 *
 * @author wr 2016-11-29
 */
public class MainCashFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<IconEntity> listIcon;
    private MainCashAdapter mAdapter;


    public MainCashFragment() {
    }

    public static MainCashFragment newInstance(String title) {
        MainCashFragment fragment = new MainCashFragment();
        Bundle args = new Bundle();
        args.putString(ValueKey.TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int setLayoutResouceId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_main_cash;
    }

    @Override
    public MBasePresenter initPresenter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void initData(Bundle arguments) {
        // TODO Auto-generated method stub
        super.initData(arguments);

        listIcon = new ArrayList<IconEntity>();
        listIcon.add(new IconEntity(R.drawable.ic_manage_product, getString(R.string.main_cash_gv_product)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_income, getString(R.string.main_cash_gv_myincome)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_message, getString(R.string.main_cash_gv_message)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_desk_card, getString(R.string.main_cash_gv_taika)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_member_desk, getString(R.string.main_cash_gv_member)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_coupon_desk, getString(R.string.main_cash_gv_coupon)));
        listIcon.add(new IconEntity(R.drawable.ic_manage_activities, getString(R.string.main_cash_gv_activity)));
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_cash);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stub
        super.setValue();
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(context, 3);
        fullyGridLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(fullyGridLayoutManager);
        mAdapter = new MainCashAdapter(context);
        mAdapter.setData(listIcon);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        mAdapter.setIOnItemClick(iOnItemClickListener);
        mRootView.findViewById(R.id.tv_main_cash_right_order).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_main_cash_left_cash).setOnClickListener(this);
    }

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub
        // 开单
        switch (v.getId()) {
            case R.id.tv_main_cash_right_order:
                startActivity(new Intent(mActivity, CashOrderActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_cash_order)));
                break;
            case R.id.tv_main_cash_left_cash:
                startActivity(new Intent(mActivity, CashReceiptActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_cash_receipt)));
                break;
            default:
                break;
        }
    }

    private IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, Object t, int postion) {
            switch (postion) {
                case 0:
                    startActivity(new Intent(mActivity, FoodManageActivity.class).putExtra(ValueKey.TITLE,
                            getResources().getString(R.string.title_cash_foodmanage)));
                    break;
                case 4:
                    Intent intent4 = new Intent(mActivity, MemberActivity.class);
                    intent4.putExtra(ValueKey.TITLE, getString(R.string.title_member_main));
                    intent4.putExtra(ValueKey.TYPE, ValueType.DEFAULT);
                    startActivity(intent4);
                    break;
                case 1:
                    Intent intent1 = new Intent(mActivity, IncomeActivity.class);
                    intent1.putExtra(ValueKey.TITLE, getString(R.string.title_member_main));
                    startActivity(intent1);
                    break;
                case 5:
                    startActivity(new Intent(mActivity, CouponSetActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_couponset)));
                    break;
                default:
                    break;
            }
        }
    };

}
