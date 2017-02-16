package com.shwm.freshmallpos.view;

import com.shwm.freshmallpos.been.FoodEntity;

public interface ICashOrderCodeView extends IBaseView {
	String getCode();

	void showDilogNum(FoodEntity food);

	void showCodeSearch(String code);
}
