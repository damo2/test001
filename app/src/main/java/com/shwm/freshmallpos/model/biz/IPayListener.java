package com.shwm.freshmallpos.model.biz;

import java.util.HashMap;
import java.util.List;

import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.MemberEntity;

public interface IPayListener {
	/**
	 * 
	 * @param payType  支付方式
	 * @param member 会员
	 * @param listcart 购物车商品
	 * @param moneyReceive 实付金额
	 * @param iRequestListener 
	 */
	void payInCash(int payType, MemberEntity member, List<FoodEntity> listcart, double moneyReceive,
				   IRequestListener<HashMap<String, Object>> iRequestListener);
}
