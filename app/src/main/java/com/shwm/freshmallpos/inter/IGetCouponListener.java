package com.shwm.freshmallpos.inter;

import java.util.List;

import com.shwm.freshmallpos.been.CouponEntity;

public interface IGetCouponListener {
	void getDiscountData(List<CouponEntity> listdiscount);
	void getMoneydownData(List<CouponEntity> listmoneydown);
}
