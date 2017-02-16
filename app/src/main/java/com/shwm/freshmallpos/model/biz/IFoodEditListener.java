package com.shwm.freshmallpos.model.biz;

public interface IFoodEditListener {
	void addFood(String name, String barcode, String typeTag, double price, double priceMember, String unit, double numSum, int type,
				 String comefrom, String desc, String eatIds, String eatIdsDel, IRequestListener iRequestListener);

	void editFood(int foodId, String name, String barcode, String typeTag, double price, double priceMember, String unit, double numSum,
				  int type, String comefrom, String desc, String eatIds, String eatIdsDel, IRequestListener iRequestListener);
}
