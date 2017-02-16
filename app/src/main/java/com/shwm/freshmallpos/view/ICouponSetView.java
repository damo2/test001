package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.CouponEntity;

public interface ICouponSetView extends IBaseView {
	List<CouponEntity> getCouponDiscount();

	List<CouponEntity> getCouponMoneydown();

	void showListCouponDiscount(List<CouponEntity> listDiscount);

	void showListCouponMoneydown(List<CouponEntity> listMoneydown);
}
