package com.shwm.freshmallpos.view;

import java.util.List;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;

public interface IFoodManageView extends IBaseViewRefreshLoad {
	ClassesEntity getClassesCurrent();

	int getPageType();

	/** 显示商品类型 */
	void showClasses(List<ClassesEntity> listClasses);

	/** 显示右侧商品列表 */
	void showFoods(List<FoodEntity> listFood);

	int getClassesPosion();

	/** 显示删除商品对话框 */
	void showDialogDel(String info);

	/** 删除成功 */
	void delSuccess();
}
