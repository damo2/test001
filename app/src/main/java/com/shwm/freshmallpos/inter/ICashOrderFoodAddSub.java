package com.shwm.freshmallpos.inter;

import com.shwm.freshmallpos.been.FoodEntity;

public interface ICashOrderFoodAddSub {
	void onAdd(int position, FoodEntity food);

	void onSub(int position, FoodEntity food);

	void onImg(int position, FoodEntity food);

	void onAddFoodWeight(int position, FoodEntity food, double weight);

	void onRemoveFoodWeight(int position, FoodEntity food);

	void onWeight(int position, FoodEntity food);
}
