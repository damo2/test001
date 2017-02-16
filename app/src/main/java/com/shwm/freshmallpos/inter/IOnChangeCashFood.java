package com.shwm.freshmallpos.inter;

import com.shwm.freshmallpos.been.FoodEntity;

/**
 * 【购物车数量改变】 通知 【选择商品的改变】
 * 
 * @author wr 2016-12-8
 */
public interface IOnChangeCashFood {
	/** 【购物车数量改变】 通知 【选择商品的改变】 */
	void onChangeCashFoodByCart(FoodEntity food, boolean isClear);
}
