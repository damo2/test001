package com.shwm.freshmallpos.model.biz;

import java.util.List;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.inter.IGetCouponListener;

public interface ICouponListener {
	/**
	 * 保存所有优惠
	 * 
	 * @param 需要申请读写权限STORAGE
	 * @return
	 */
	void saveCouponDataToFile(List<CouponEntity> listDiscount, List<CouponEntity> listMoneydown);

	/**
	 * 获取所有优惠
	 * 
	 * @param iGetCouponListener
	 *            分类获取
	 * @category 需要申请读写权限STORAGE
	 * @return
	 */
	void getCouponDataFromFile(IGetCouponListener iGetCouponListener);

	/**
	 * 获取所有优惠
	 * 
	 * @param 需要申请读写权限STORAGE
	 * @return
	 */
	List<CouponEntity> getCouponAll();
}
