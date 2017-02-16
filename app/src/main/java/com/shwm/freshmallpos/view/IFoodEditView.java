package com.shwm.freshmallpos.view;

import android.net.Uri;

import com.shwm.freshmallpos.been.ClassesEntity;
import com.shwm.freshmallpos.been.FoodEntity;

public interface IFoodEditView extends IBaseView{
	int getEditType();

	String getFoodName();

	String getFoodCode();

	ClassesEntity getFoodClasses();

	double getFoodPrice();

	double getFoodPriceMember();

	String getFoodUnit();

	double getFoodNumsum();

	String getFoodFrom();

	int getFoodWeightType();

	String getFoodDesc();

	void editSuccess(FoodEntity foodNew);

	Uri getFoodImgBitmapUri();

	void upFoodImgBitmapSuc();

	void showEditInfo(FoodEntity food);

	FoodEntity getFoodEdit();

	int getFoodEditId();
}
