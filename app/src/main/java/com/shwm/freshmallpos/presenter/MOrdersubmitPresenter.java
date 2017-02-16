package com.shwm.freshmallpos.presenter;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;
import com.shwm.freshmallpos.model.biz.ICouponListener;
import com.shwm.freshmallpos.model.biz.OnCouponListener;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.ICashMoneyView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MOrdersubmitPresenter extends MBasePresenter<ICashMoneyView> {
    private static final String TAG = "MOrdersubmitPresenter";
    // private double moneyChange;// 优惠金额
    private static MemberEntity member;
    private ICashMoneyView mView;
    private List<FoodEntity> listcart;
    private int numSum;
    private double moneySum;// 原来总价
    private double moneyReceivable;// 打折后总价 应收金额
    private List<CouponEntity> listCoupon = new ArrayList<CouponEntity>();
    private ICouponListener iCouponListener = new OnCouponListener();

    public MOrdersubmitPresenter(ICashMoneyView mView) {
        if (mView instanceof ICashMoneyView) {
            this.mView = mView;
        } else {
            throw new Error("必须实现接口");
        }
        listcart = mView.getListCart();
    }

    /**
     * 设置购物车
     */
    public void setDataCart() {
        listcart = mView.getListCart();
        resDataShow();
    }

    /**
     * 重置数据显示
     */
    public void resDataShow() {
        getMoneyAllBefore();
        moneyAllByCoupon();
        mView.showData();
        mView.initCoupon(listCoupon);
    }

    /**
     * 添加会员
     */
    public void addMember(MemberEntity member) {
        MOrdersubmitPresenter.member = member;
        // 添加会员卡显示
        CouponEntity coupon = new CouponEntity();
        if (member != null && member.getCoupon() != null) {
            coupon = member.getCoupon();
            coupon.setType(ValueType.CouponType_DiscountMember);
            UL.d(TAG, coupon.getDiscount() + "折  addMember()");
        }
        addCoupon(coupon);
        mView.showMember(member);
    }

    /**
     * 删除会员
     */
    public void deleteMember() {
        MOrdersubmitPresenter.member = null;
        if (listCoupon != null) {
            for (int i = 0; i < listCoupon.size(); i++) {
                if (listCoupon.get(i).getType() == ValueType.CouponType_DiscountMember) {
                    removeCoupon(i);
                }
            }
        }
        mView.showMember(member);
    }

    private void getMoneyAllBefore() {
        numSum = 0;
        moneySum = 0;
        if (listcart != null) {
            for (FoodEntity food : listcart) {
                numSum += food.getNum();
                double foodMoney = UtilMath.mul(food.getNum(), food.getPrice());
                moneySum = UtilMath.add(moneySum, foodMoney);
            }
        }
        moneyReceivable = moneySum;
    }

    // 打折
    private void moneyAllByCoupon() {
        moneyReceivable = moneySum;
//		UL.d(TAG, "总价=" + moneySum);
        if (listCoupon != null) {
            int size = listCoupon.size();
            for (int i = 0; i < size; i++) {
                CouponEntity coupon = listCoupon.get(i);
                if (coupon != null && coupon.getType() == ValueType.CouponType_Moneydown) {
                    moneyReceivable = UtilMath.sub(moneyReceivable, coupon.getMoneydown());
                    UL.d(TAG, "减单=" + coupon.getMoneydown() + "  减单后总价=" + moneyReceivable);
                }
            }
            for (int i = 0; i < size; i++) {
                CouponEntity couponTemp = listCoupon.get(i);
                if (couponTemp != null && couponTemp.getType() == ValueType.CouponType_DiscountMember
                        || couponTemp.getType() == ValueType.CouponType_Discount) {
                    moneyReceivable = UtilMath.mul(moneyReceivable, couponTemp.getDiscountRate());
                    UL.d(TAG, (couponTemp.getType() == ValueType.CouponType_DiscountMember ? "会员折扣=" : "整单折扣=") + couponTemp.getMoneydown()
                            + "  打折后总价=" + moneyReceivable);
                }
            }
        }
    }

    /**
     * 优惠金额
     */
    public double getMoneyYouhui() {
        return UtilMath.sub(moneySum, moneyReceivable);
    }

    /**
     * 合计金额
     */
    public double getMoneySum() {
        return moneySum;
    }

    /**
     * 数量金额
     */
    public double getNumSum() {
        return numSum;
    }

    /**
     * 应付金额
     */
    public double getMoneyReceivable() {
        return moneyReceivable;
    }

    /**
     * 现金支付
     */
    public void submitOrderByCash() {
        mView.paytypeByCash(member, listcart, moneyReceivable);
    }

    /**
     * 添加优惠劵
     */
    public void addCoupon(CouponEntity coupon) {
        int couponType = coupon.getType();
        // 如果存在这个类型的优惠劵就移除掉再添加
        Iterator<CouponEntity> iterator = listCoupon.iterator();
        for (int i = 0; i < listCoupon.size(); i++) {
            if (iterator.hasNext()) {
                CouponEntity couponEntity = iterator.next();
                int couponEntityType = couponEntity.getType();
                if (couponType == couponEntityType
                        || (couponEntityType == ValueType.CouponType_Discount && couponType == ValueType.CouponType_DiscountMember)
                        || (couponEntityType == ValueType.CouponType_DiscountMember && couponType == ValueType.CouponType_Discount)) {
                    iterator.remove();
                }
            }
        }
        listCoupon.add(coupon);
        resDataShow();
    }

    /**
     * 删除优惠劵
     */
    public void removeCoupon(int pos) {
        listCoupon.remove(pos);
        resDataShow();
    }

}
