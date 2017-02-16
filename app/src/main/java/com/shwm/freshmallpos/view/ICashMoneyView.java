package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;

public interface ICashMoneyView extends IBaseView {
	// 取到购物车列表
	List<FoodEntity> getListCart();

	/** 显示选择的优惠劵 */
	void initCoupon(List<CouponEntity> listcoupon);

	/** 显示所有money */
	void showData();

	/** 显示选择的会员信息 */
	void showMember(MemberEntity member);

	/**
	 * 
	 * @param member
	 * @param listcart
	 * @param moneyReceivable
	 *            应收金额
	 */
	void paytypeByCash(MemberEntity member, List<FoodEntity> listcart, double moneyReceivable);
}
