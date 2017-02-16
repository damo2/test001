package com.shwm.freshmallpos.been;

import java.util.ArrayList;
import java.util.List;

public class FoodSaveEntity {
	private ClassesEntity classes;
	private List<FoodEntity> listFood;
	private int pageCurrent;

	public FoodSaveEntity() {
		classes = new ClassesEntity();
		listFood = new ArrayList<FoodEntity>();
	}

	public ClassesEntity getClasses() {
		return classes;
	}

	public void setClasses(ClassesEntity classes) {
		this.classes = classes;
	}

	public List<FoodEntity> getListFood() {
		return listFood;
	}

	public void setListFood(List<FoodEntity> listFood) {
		this.listFood = listFood;
	}

	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

}
