package com.shwm.freshmallpos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.inter.IOnItemClickListener;
import com.shwm.freshmallpos.model.biz.ICouponListener;
import com.shwm.freshmallpos.model.biz.OnCouponListener;
import com.shwm.freshmallpos.myview.MyFlowLayout;
import com.shwm.freshmallpos.myviewutil.PopupCashType;
import com.shwm.freshmallpos.myviewutil.PopupCoupon;
import com.shwm.freshmallpos.myviewutil.PopupCoupon.IPopupCouponListener;
import com.shwm.freshmallpos.presenter.MOrdersubmitPresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueFinal;
import com.shwm.freshmallpos.value.ValueFuhao;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICashMoneyView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 收款
 *
 * @author wr 2016-12-5
 */
public class CashReceiptActivity extends BaseActivity<ICashMoneyView, MOrdersubmitPresenter> implements ICashMoneyView {
    private String title;
    private List<FoodEntity> listfood = new ArrayList<FoodEntity>();
    private StringBuilder str = new StringBuilder();
    // private String strInfo;
    private TextView tvSumInfo;
    private TextView tvMoneySum;
    private TextView tvMoneyYouhui;
    private TextView tvMoneyReceivable;

    private PopupCashType mPopupCashType;
    private View viewparentCashType;

    private PopupCoupon mPopupCoupon;
    // private MemberEntity member;

    private static final int LEN = 2;

    private String add = "";
    // 折扣显示
    private MyFlowLayout mFlowLayout;

    // 会员
    private View viewMember;
    private TextView tvMemberName;
    private TextView tvMemberMobi;
    private ImageButton iBtnDel;

    private int i = 0;// 第i个无码商品

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
    }

    @Override
    public int bindLayout() {
        // TODO Auto-generated method stub
        return R.layout.activity_cash_receipt;
    }

    @Override
    public MOrdersubmitPresenter initPresenter() {
        // TODO Auto-generated method stub
        return new MOrdersubmitPresenter(this);
    }

    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_cashReceipt, title);
        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                menuItem=item;
                if (item.getItemId() == R.id.menu_right) {
                    boolean couponOpenStatus = UtilSPF.getBoolean(ValueKey.CouponOpenStatu, false);
                    if (couponOpenStatus) {
                        requestPermission(ValuePermission.PermissionRequest_STORAGE, ValuePermission.PermissionGroupSTORAGE, new Runnable() {
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right, menu);
        MenuItem item = mToolbar.getMenu().findItem(R.id.menu_right);
        item.setTitle(getString(R.string.toolbar_chooseCoupon));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(ValueKey.TITLE);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        tvSumInfo = (TextView) findViewById(R.id.tv_cash_receipt_info);
        tvMoneySum = (TextView) findViewById(R.id.tv_cash_receipt_moneyResult);
        viewparentCashType = findViewById(R.id.rl_cashReceipt_moneyinfo);
        tvMoneyYouhui = (TextView) findViewById(R.id.tv_orderReceipt_moneyYouhui);
        tvMoneyReceivable = (TextView) findViewById(R.id.tv_orderReceipt_money);

        viewMember = findViewById(R.id.include_cashReceipt_member);
        tvMemberName = (TextView) viewMember.findViewById(R.id.tv_member_memberName);
        tvMemberMobi = (TextView) viewMember.findViewById(R.id.tv_member_memberMobi);
        iBtnDel = (ImageButton) viewMember.findViewById(R.id.ibtn_member_memberDel);

        mPopupCashType = new PopupCashType(mActivity, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupCoupon = new PopupCoupon(mActivity);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stub
        super.setValue();
        viewMember.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        findViewById(R.id.btn_cash_receipt_ok).setOnClickListener(this);
        findViewById(R.id.btn_cash_receipt_add).setOnClickListener(this);
        findViewById(R.id.btn_cash_receipt_del).setOnClickListener(this);
        findViewById(R.id.btn_cash_receipt_00).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_0).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_point).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_1).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_2).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_3).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_4).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_5).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_6).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_7).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_8).setOnClickListener(onClick);
        findViewById(R.id.btn_cash_receipt_9).setOnClickListener(onClick);

        mPopupCashType.setIOnItemClickListener(iOnItemClickListener);
        mPopupCoupon.setIPopupCouponListener(iPopupCouponListener);
        iBtnDel.setOnClickListener(this);
        mPopupCoupon.setIOnItemClickListener(iOnItemClickListenerCoupon);
        mPopupCoupon.setOnDismissListener(onDismissListener);
    }

    private OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            MenuItem item =  mToolbar.getMenu().findItem(R.id.menu_right);
            if(menuItem!=null) {
                menuItem.setTitle(getString(R.string.toolbar_chooseCoupon));
                menuItem.setIcon(0);
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
    private IPopupCouponListener iPopupCouponListener = new IPopupCouponListener() {
        @Override
        public void onChooseMember() {
            // TODO Auto-generated method stub
            Intent intent = new Intent(mActivity, MemberActivity.class);
            intent.putExtra(ValueKey.TITLE, getString(R.string.title_member_mainAddMember));
            intent.putExtra(ValueKey.TYPE, ValueType.CHOOSE);
            startActivityForResult(intent, ValueRequest.CashReceipt_Member);
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
                            bundle.putSerializable(ValueKey.LISTFOOD, (Serializable) listfood);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, ValueRequest.Ordersubmit_PaytypeWechat);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
                            bundle.putSerializable(ValueKey.LISTFOOD, (Serializable) listfood);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, ValueRequest.Ordersubmit_PaytypeAlipay);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
        switch (v.getId()) {
            case R.id.btn_cash_receipt_add:
                if (str.length() > 0) {
                    // addNum(str.toString());
                    strDelete();// 在显示之前删除
                    add = ValueFuhao.FUHAO_NUM_ADD;
                    tvSumInfo.setText(getNumInfo());
                    add = "";
                }
                break;
            case R.id.btn_cash_receipt_del:
                strDelete();
                delNum();
                add = ValueFuhao.FUHAO_NUM_ADD;
                tvSumInfo.setText(getNumInfo());
                add = "";
                break;
            case R.id.btn_cash_receipt_ok:
                if (mPresenter.getMoneyReceivable() > 0) {
                    mPopupCashType.showAsDropDown(viewparentCashType);
                } else {
                    Toast.makeText(context, getString(R.string.cashReceipt_zore), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        if (v.getId() == iBtnDel.getId()) {
            mPresenter.deleteMember();
        }
    }

    // 可以快速点击
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.btn_cash_receipt_point:
                    // 小数点前没有数字加个“0”
                    if (str.length() == 0) {
                        str.append(0);
                    }
                    // 没有. 才能添加小数点
                    if (str.length() > 0 && str.indexOf(ValueFuhao.FUHAO_NUM_POINT) == -1)
                        addStr(ValueFuhao.FUHAO_NUM_POINT);
                    break;
                case R.id.btn_cash_receipt_00:
                    if (str.length() == 0) {
                        addStr("0");
                    } else if (StringUtil.getDouble(str.toString()) > 0) {
                        addStr("00");
                    }
                    break;
                case R.id.btn_cash_receipt_0:
                    addStr("0");
                    break;
                case R.id.btn_cash_receipt_1:
                    addStr("1");
                    break;
                case R.id.btn_cash_receipt_2:
                    addStr("2");
                    break;
                case R.id.btn_cash_receipt_3:
                    addStr("3");
                    break;
                case R.id.btn_cash_receipt_4:
                    addStr("4");
                    break;
                case R.id.btn_cash_receipt_5:
                    addStr("5");
                    break;
                case R.id.btn_cash_receipt_6:
                    addStr("6");
                    break;
                case R.id.btn_cash_receipt_7:
                    addStr("7");
                    break;
                case R.id.btn_cash_receipt_8:
                    addStr("8");
                    break;
                case R.id.btn_cash_receipt_9:
                    addStr("9");
                    break;
            }
        }
    };

    /**
     * 添加输入的字符
     */
    private void addStr(String n) {
        if (n == null || n.length() == 0)
            return;
        if (str.length() > 0) {
            delNum();
        }
        int pointPos = str.indexOf(ValueFuhao.FUHAO_NUM_POINT);
        int position = str.length() - pointPos - 1;
        // 小数点后面有2位数了 不添加
        if ((pointPos < 0 || position < 2) && UtilMath.sub(StringUtil.getDouble(str.toString() + n), ValueFinal.MAX_SUM) < 0) {
            str.append(n);
        }
        addNum(str.toString());
        mPresenter.setDataCart();
        tvSumInfo.setText(getNumInfo());
    }

    // 添加
    private void addNum(String priceStr) {
        i++;
        double price = UtilMath.round(priceStr, LEN);
        FoodEntity food = new FoodEntity();
        food.setName(ValueFinal.foodNoName + i);
        food.setId(ValueFinal.foodNoCodeId);
        food.setUnit(ValueFinal.foodNoCodeUnit);
        food.setPrice(price);
        food.setPriceTag(priceStr);
        food.setNum(1);
        listfood.add(food);
        mPresenter.setDataCart();
    }

    // 删除最后一个
    private void delNum() {
        if (listfood != null && listfood.size() > 0) {
            i--;
            listfood.remove(listfood.size() - 1);
            mPresenter.setDataCart();
        }
    }

    // 得到所有输入的信息
    private SpannableString getNumInfo() {
        StringBuilder sb = new StringBuilder();
        int lengthBefore = 0;
        if (listfood != null && listfood.size() > 0) {
            int size = listfood.size();
            for (int i = 0; i < size; i++) {
                String priceS = "";
                if (i != size - 1) {
                    priceS = UtilMath.round(listfood.get(i).getPrice() + "", LEN) + "";
                    sb.append(priceS).append(ValueFuhao.FUHAO_NUM_ADD);

                } else {
                    lengthBefore = sb.length();
                    priceS = listfood.get(i).getPriceTag();
                    sb.append(priceS).append(add);
                }
            }
        }
        SpannableString spannableString = new SpannableString(sb.toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), lengthBefore, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void strDelete() {
        if (str == null || str.length() == 0)
            return;
        str.delete(0, str.length());
    }

    @Override
    public List<FoodEntity> getListCart() {
        // TODO Auto-generated method stub
        return listfood;
    }

    @Override
    public void initCoupon(List<CouponEntity> listcoupon) {
        // TODO Auto-generated method stub
        LayoutInflater mInflater = LayoutInflater.from(this);
        if (mFlowLayout == null) {
            mFlowLayout = (MyFlowLayout) findViewById(R.id.tagview_cash_receipt_coupon);
        }
        mFlowLayout.removeAllViews();
        for (int i = 0; i < listcoupon.size(); i++) {
            final int pos = i;
            CouponEntity coupon = listcoupon.get(i);
            View view = mInflater.inflate(R.layout.item_ordersubmitcoupon, mFlowLayout, false);
            TextView tv = (TextView) view.findViewById(R.id.tv_item_ordersubmitcoupon);
            if (coupon.getType() == ValueType.CouponType_DiscountMember) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_discountMember), UtilMath.currency(coupon.getDiscount())));
                tv.setBackgroundResource(R.drawable.d_bg_round_bluel_xs);
            } else if (coupon.getType() == ValueType.CouponType_Discount) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_discount), coupon.getDiscount() + ""));
                tv.setBackgroundResource(R.drawable.d_bg_round_red_xs);
            } else if (coupon.getType() == ValueType.CouponType_Moneydown) {
                tv.setText(String.format(getString(R.string.orderSubmit_coupon_moneydown), coupon.getMoneydown() + ""));
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
    public void showData() {
        // TODO Auto-generated method stub
        tvMoneySum.setText(mPresenter.getMoneySum() + "");
        tvMoneyYouhui.setText(mPresenter.getMoneyYouhui() + "");
        tvMoneyReceivable.setText(mPresenter.getMoneyReceivable() + "");
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
        startActivityForResult(intent, ValueRequest.CashReceipt_PaytypeCash);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == ValueRequest.CashReceipt_Member && resultCode == RESULT_OK) {
            MemberEntity member = (MemberEntity) data.getExtras().getSerializable(ValueKey.Member);
            mPresenter.addMember(member);
        }
        if (requestCode == ValueRequest.CashReceipt_PaytypeCash && resultCode == RESULT_OK) {
            if (listfood != null) {
                listfood.clear();
                tvSumInfo.setText("");
            }
            mPresenter.setDataCart();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onBack() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.tig));
        builder.setMessage(getString(R.string.is_exit_cash));
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel),null);
        builder.show();
    }
}
