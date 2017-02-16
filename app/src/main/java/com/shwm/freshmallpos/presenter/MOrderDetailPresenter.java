package com.shwm.freshmallpos.presenter;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.model.biz.IOrderManagerListiner;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnOrderManagerListiner;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IOrderDetailView;

public class MOrderDetailPresenter extends MBasePresenter<IOrderDetailView> {
	private IOrderDetailView mView;
	private IOrderManagerListiner iOrderManagerListiner;
	private OrderEntity order, orderDetail;
	private List<FoodEntity> listFood;
	private int orderId;
	private String orderNo;

	public MOrderDetailPresenter(IOrderDetailView mView) {
		// TODO Auto-generated constructor stub
		this.mView = mView;
		iOrderManagerListiner = new OnOrderManagerListiner();
	}

	public void getOrderDetail() {
		order = mView.getOrder();
		if (order != null) {
			orderId = order.getId();
		}
		iOrderManagerListiner.getOrderDetail(orderId, new IRequestListener<HashMap<String, Object>>() {
			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				if (hashmap != null) {
					listFood = (List<FoodEntity>) hashmap.get(ValueKey.LISTFOOD);
					orderDetail = (OrderEntity) hashmap.get(ValueKey.ORDER);
					mView.showListFood(listFood);
					mView.setOrderDetail(orderDetail);
				}
			}

			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.showFailInfo(statu, exception);
			}
		});
	}

	public void orderRefund() {
		order = mView.getOrder();
		if (order == null)
			return;
		orderNo = order.getOrderno();
		iOrderManagerListiner.OrderRefund(orderNo, new IRequestListener() {
			@Override
			public void onPreExecute(int type) {
				// TODO Auto-generated method stub
				mView.showDialogProgress();
			}

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
				mView.refundSuccess();
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
				mView.dismissDialogProgress();
			}
		});
	}

}
