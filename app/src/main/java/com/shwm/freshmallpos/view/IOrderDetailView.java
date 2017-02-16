package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.OrderEntity;

public interface IOrderDetailView extends IBaseView {
	/**
	 * 取到订单
	 * 
	 * @return 订单
	 */
	OrderEntity getOrder();

	/**
	 * 显示商品列表
	 */
	void showListFood(List<FoodEntity> listfood);

	/**
	 * 更新订单详情
	 */
	void setOrderDetail(OrderEntity orderdetail);

	/**
	 * 退款成功
	 */
	void refundSuccess();
}
