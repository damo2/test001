package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.FoodEntity;

/**
 * 开单 - activity 购物车
 * 
 * @author wr 2016-12-5
 */
public interface ICashOrderView {
	void showShopcart(List<FoodEntity> listFood);
}
