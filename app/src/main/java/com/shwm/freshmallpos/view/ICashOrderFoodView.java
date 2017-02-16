package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;

/**
 * 开单-选择商品
 * 
 * @author wr 2016-12-2
 */
public interface ICashOrderFoodView extends IBaseView {

	ClassesEntity getClassesCurrent();

	int getPageType();

	/** 显示类型 */
	void showClassesList(List<ClassesEntity> listClasses);

	/** 显示商品 */
	void showFoodList(List<FoodEntity> listFood);

	void showFoodListByLike(List<FoodEntity> listFood);

	/** adapter更新商品 */
	void changeItemFood(int position);

	/** adapter更新类型 */
	void changeItemClasses(int position);

	/** 通知购物车改变 */
	void changeCart(FoodEntity food);

	int getClassesPosion();

	/** 取消刷新 */
	void dismissRefresh();

	void setLoadType(int loadtype);

}
