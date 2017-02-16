package com.shwm.freshmallpos.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.OrdersubmitAdapter;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.model.biz.ICouponListener;
import com.shwm.freshmallpos.model.biz.OnCouponListener;
import com.shwm.freshmallpos.myview.MyFlowLayout;
import com.shwm.freshmallpos.myviewutil.MyLinearLayoutManager;
import com.shwm.freshmallpos.myviewutil.PopupCashType;
import com.shwm.freshmallpos.myviewutil.PopupCoupon;
import com.shwm.freshmallpos.myviewutil.PopupCoupon.IPopupCouponListener;
import com.shwm.freshmallpos.presenter.MOrdersubmitPresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICashMoneyView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 收款(提交订单)
 */
public class OrdersubmitActivity extends BaseActivity<ICashMoneyView, MOrdersubmitPresenter> implements ICashMoneyView {
    private String title;
    private List<FoodEntity> listcart;
    private RecyclerView mRecyclerView;
    private MyLinearLayoutManager mLayoutManager;
    private OrdersubmitAdapter mAdapter;
    //
    private ScrollView mScrollview;
    private TextView tvMoneyAll;
    private Button btnSubmit;
    // 会员
    private View viewMember;
    private TextView tvMemberName;
    private TextView tvMemberMobi;
    private ImageButton iBtnDel;

    // 折扣显示
    private MyFlowLayout mFlowLayout;
    private PopupCoupon mPopupCoupon;

    //支付方式
    private PopupCashType mPopupCashType;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
    }

    @Override
    public int bindLayout() {
        // TODO Auto-generated method stub
        return R.layout.activity_ordersubmit;
    }

    @Override
    public MOrdersubmitPresenter initPresenter() {
        // TODO Auto-generated method stub
        return new MOrdersubmitPresenter(this);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(ValueKey.TITLE);
        listcart = (List<FoodEntity>) bundle.getSerializable(ValueKey.LISTCART);

        mPopupCoupon = new PopupCoupon(mActivity);
        mPopupCashType = new PopupCashType(mActivity, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ordersubmit_food);
        tvMoneyAll = (TextView) findViewById(R.id.tv_orderSubmit_moneyAll);
        btnSubmit = (Button) findViewById(R.id.btn_ordersubmit_submit);
        mScrollview = (ScrollView) findViewById(R.id.scrollview_ordersubmit);

        viewMember = findViewById(R.id.include_ordersubmit_member);
        tvMemberName = (TextView) viewMember.findViewById(R.id.tv_member_memberName);
        tvMemberMobi = (TextView) viewMember.findViewById(R.id.tv_member_memberMobi);
        iBtnDel = (ImageButton) viewMember.findViewById(R.id.ibtn_member_memberDel);

    }
    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_ordersubmit_top,title);

        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.menu_right:
                        boolean couponOpenStatu = UtilSPF.getBoolean(ValueKey.CouponOpenStatu, false);
                        if (couponOpenStatu) {
                            requestPermission(ValuePermission.PermissionRequest_STORAGE, ValuePermission.PermissionGroupSTORAGE,
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            ICouponListener iCouponListener = new OnCouponListener();
                                            mPopupCoupon.setListCoupon(iCouponListener.getCouponAll());
                                        }
                                    }, new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                                                    mActivity);
                                            builder.setTitle(getString(R.string.storage_camcel_coupon_title));
                                            builder.setPositiveButton(getString(R.string.know), null);
                                            builder.show();
                                        }
                                    });
                        }
                        if (mPopupCoupon.isShowing()) {
                            item.setTitle(getString(R.string.toolbar_chooseCoupon));
                            item.setIcon(0);
                            mPopupCoupon.dismiss();
                        } else {
                            item.setTitle(getString(R.string.toolbar_chooseCoupon_cancel));
                            item.setIcon(R.drawable.l_arrow_top_orange);
                            mPopupCoupon.showAsDropDown(mToolbar);
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right, menu);
        MenuItem item = (MenuItem) mToolbar.getMenu().findItem(R.id.menu_right);
        item.setTitle(getString(R.string.toolbar_chooseCoupon));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stub
        super.setValue();
        setAdapter();
        mPresenter.setDataCart();
        mScrollview.smoothScrollTo(0, 0);
        viewMember.setVisibility(View.GONE);

        ((TextView) findViewById(R.id.tv_ordersubmit_numSum)).setText(StringUtil.doubleTrans(mPresenter.getNumSum()));
        ((TextView) findViewById(R.id.tv_ordersubmit_moneySum)).setText(UtilMath.currency(mPresenter.getMoneySum()));

    }

    private void setAdapter() {
        mLayoutManager = new MyLinearLayoutManager(context);
        mLayoutManager.setScrollEnabled(false);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrdersubmitAdapter(context);
        mAdapter.setData(listcart);
        mRecyclerView.setAdapter(mAdapter);
        //
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        btnSubmit.setOnClickListener(this);

        mPopupCoupon.setIPopupCouponListener(iPopupCouponListener);
        iBtnDel.setOnClickListener(this);
        mPopupCoupon.setIOnItemClickListener(iOnItemClickListenerCoupon);
        mPopupCoupon.setOnDismissListener(onDismissListener);
        mPopupCashType.setIOnItemClickListener(iOnItemClickListener);
    }

    private OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            MenuItem item =  mToolbar.getMenu().findItem(R.id.menu_right);
            if(item!=null) {
                item.setTitle(getString(R.string.toolbar_chooseCoupon));
                item.setIcon(0);
            }
            mPopupCoupon.dismiss();
        }
    };
    private IOnItemClickListener<CouponEntity> iOnItemClickListenerCoupon = new IOnItemClickListener<CouponEntity>() {
        @Override
        public void onItemClick(View view, CouponEntity t, int postion) {
            // TODO Auto-generated method stub
            mPresenter.addCoupon(t);
            mPopupCoupon.dismiss();
        }
    };
    private IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, Object t, int postion) {
            // TODO Auto-generated method stub
            switch (postion) {
                case 1:// 微信收款
                    requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mActivity, PaytypeWechatScanUserActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(ValueKey.TITLE, getString(R.string.title_paytype_wechat));
                            bundle.putDouble(ValueKey.Money, mPresenter.getMoneyReceivable());
                            bundle.putSerializable(ValueKey.LISTFOOD, (Serializable) listcart);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, ValueRequest.Ordersubmit_PaytypeWechat);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                            builder.setTitle(getString(R.string.camera_camcel_scancode_title));
                            builder.setPositiveButton(getString(R.string.sure), null);
                            builder.show();
                        }
                    });
                    break;
                case 3:// 现金收款
                    mPresenter.submitOrderByCash();
                    break;

                case 2:// 支付宝收款
                    requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mActivity, PaytypeAlipayScanUserActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(ValueKey.TITLE, getString(R.string.title_paytype_wechat));
                            bundle.putDouble(ValueKey.Money, mPresenter.getMoneyReceivable());
                            bundle.putSerializable(ValueKey.LISTFOOD, (Serializable) listcart);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, ValueRequest.Ordersubmit_PaytypeAlipay);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                            builder.setTitle(getString(R.string.camera_camcel_scancode_title));
                            builder.setPositiveButton(getString(R.string.sure), null);
                            builder.show();
                        }
                    });
                    break;

                default:
                    break;
            }
            mPopupCashType.dismiss();
        }
    };

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btnSubmit.getId()) {
//            mPresenter.submitOrderByCash();
            mPopupCashType.showAtLocation(mViewParent, Gravity.BOTTOM, 0, 0);


        }

        if (v.getId() == iBtnDel.getId()) {
            mPresenter.deleteMember();
        }
    }

    private IPopupCouponListener iPopupCouponListener = new IPopupCouponListener() {
        @Override
        public void onChooseMember() {
            // TODO Auto-generated method stub
            Intent intent = new Intent(mActivity, MemberActivity.class);
            intent.putExtra(ValueKey.TITLE, getString(R.string.title_member_mainAddMember));
            intent.putExtra(ValueKey.TYPE, ValueType.CHOOSE);
            startActivityForResult(intent, ValueRequest.OrderSub_Member);
        }
    };

    @Override
    public List<FoodEntity> getListCart() {
        // TODO Auto-generated method stub
        return listcart;
    }

    @Override
    public void showData() {
        // TODO Auto-generated method stub
        tvMoneyAll.setText(UtilMath.currency(mPresenter.getMoneyReceivable()));
    }

    @Override
    public void initCoupon(List<CouponEntity> listCoupon) {
        // TODO Auto-generated method stub
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (mFlowLayout == null) {
            mFlowLayout = (MyFlowLayout) findViewById(R.id.tagview_ordersubmit_coupon);
        }
        mFlowLayout.removeAllViews();
        for (int i = 0; i < listCoupon.size(); i++) {
            final int pos = i;
            CouponEntity coupon = listCoupon.get(i);
            View view = mInflater.inflate(R.layout.item_ordersubmitcoupon, mFlowLayout, false);
            TextView tv = (TextView) view.findViewById(R.id.tv_item_ordersubmitcoupon);
            String info = "";
            if (coupon.getType() == ValueType.CouponType_DiscountMember) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_discountMember), coupon.getDiscount() + ""));
                tv.setBackgroundResource(R.drawable.d_bg_round_bluel_xs);
            } else if (coupon.getType() == ValueType.CouponType_Discount) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_discount), coupon.getDiscount() + ""));
                tv.setBackgroundResource(R.drawable.d_bg_round_red_xs);
            } else if (coupon.getType() == ValueType.CouponType_Moneydown) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_moneydown), UtilMath.currency(coupon.getMoneydown())));
                tv.setBackgroundResource(R.drawable.d_bg_round_orange_xs);
            }
            ImageButton ibtnDel = (ImageButton) view.findViewById(R.id.ibtn_item_ordersubmitcoupon_del);
            // 点击事件
            ibtnDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.removeCoupon(pos);
                }
            });
            mFlowLayout.addView(view);// 添加到父View
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == ValueRequest.OrderSub_Member && resultCode == RESULT_OK) {
            MemberEntity member = (MemberEntity) data.getExtras().getSerializable(ValueKey.Member);
            mPresenter.addMember(member);
        }
        if (requestCode == ValueRequest.Ordersubmit_PaytypeCash && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            mActivity.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showMember(MemberEntity member) {
        // TODO Auto-generated method stub
        if (member == null) {
            viewMember.setVisibility(View.GONE);
        } else {
            viewMember.setVisibility(View.VISIBLE);
            tvMemberName.setText(member.getNick());
            tvMemberMobi.setText(member.getTel());
        }
    }

    @Override
    public void paytypeByCash(MemberEntity member, List<FoodEntity> listcart, double moneyReceivable) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mActivity, PaytypeCashActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ValueKey.TITLE, getString(R.string.title_paytype_cash));
        bundle.putSerializable(ValueKey.Member, member);
        bundle.putSerializable(ValueKey.LISTFOOD, (Serializable) listcart);
        bundle.putDouble(ValueKey.MoneyReceivable, moneyReceivable);
        intent.putExtras(bundle);
        startActivityForResult(intent, ValueRequest.Ordersubmit_PaytypeCash);
    }
}
