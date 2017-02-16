package com.shwm.freshmallpos.inter;

import com.shwm.freshmallpos.been.FoodEntity;

/**
 * 【选择商品的改变】 通知 【购物车数量改变】
 * 
 * @author wr 2016-12-8
 */
public interface IOnCartListener {
	/**【选择商品的改变】 通知 【购物车数量改变】*/
	void changeCartByChooseFood(FoodEntity food);
}
