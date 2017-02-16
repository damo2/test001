package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.been.OrderEntity;

public interface IOrderManagerListiner {
	/**
	 * 取订单
	 * @param page  页数
	 * @param dayNo 为空时取全部
	 * @param dayNearly 近几天   dayNearly < 0时取全部
	 * @param iRequestListener 
	 */
	void getOrderList(int page, String dayNo, int dayNearly, IRequestListener<List<OrderEntity>> iRequestListener);

	/** 取订单详情 */
	void getOrderDetail(int orderId, IRequestListener<HashMap<String, Object>> iRequestListener);

	/** 现金退款 */
	void OrderRefund(String orderNo, IRequestListener iRequestListener);

}
