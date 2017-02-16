package com.shwm.freshmallpos.presenter;

import java.util.List;

import com.shwm.freshmallpos.been.CouponEntity;
import com.shwm.freshmallpos.inter.IGetCouponListener;
import com.shwm.freshmallpos.model.biz.ICouponListener;
import com.shwm.freshmallpos.model.biz.OnCouponListener;
import com.shwm.freshmallpos.view.ICouponSetView;

public class MCouponSetPresenter extends MBasePresenter<ICouponSetView> {
	private String TAG = getClass().getSimpleName();
	private ICouponSetView mView;
	private ICouponListener iCouponListener;

	public MCouponSetPresenter(ICouponSetView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		iCouponListener = new OnCouponListener();
	}

	public void saveCouponDataToFile() {
		iCouponListener.saveCouponDataToFile(mView.getCouponDiscount(), mView.getCouponMoneydown());
	}

	/**
	 * @category 需要申请读写权限STORAGE
	 */
	public void getCouponDataFromFile() {
		iCouponListener.getCouponDataFromFile(new IGetCouponListener() {
			@Override
			public void getMoneydownData(List<CouponEntity> listmoneydown) {
				// TODO Auto-generated method stub
				mView.showListCouponMoneydown(listmoneydown);
			}

			@Override
			public void getDiscountData(List<CouponEntity> listdiscount) {
				// TODO Auto-generated method stub

				mView.showListCouponDiscount(listdiscount);
			}
		});
	}

}
