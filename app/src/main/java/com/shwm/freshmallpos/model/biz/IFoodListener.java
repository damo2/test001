package com.shwm.freshmallpos.model.biz;

import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;

public interface IFoodListener {

	/** 通过分类查找商品 */
	void getFoodByClasses(ClassesEntity classes, int page, int pageType, IRequestListener<List<FoodEntity>> iRequestListener);

	/** 通过商品名查找商品 */
	void getFoodByLike(String foodname, int page, int pageType, IRequestListener<List<FoodEntity>> iRequestListener);

	/** 通过二维码查找商品 */
	void getFoodByCode(String code, IRequestListener<FoodEntity> iRequestListener);

	/** 通过商品id查找商品 */
	void getFoodByFoodId(int foodId, IRequestListener<FoodEntity> iRequestListener);
}
