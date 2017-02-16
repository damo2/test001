package com.shwm.freshmallpos.been;

import java.io.Serializable;

import com.shwm.freshmallpos.util.UtilMath;

public class CouponEntity implements Serializable {
	private int type;
	private double discount;// 折扣 9折为9
	private double moneydown;// 减单
	private double discountRate;// 折扣率 9折为0.9
	private String tag;

	public CouponEntity() {
	}

	public CouponEntity(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMoneydown() {
		return moneydown;
	}

	public double getDiscount() {
		if (discount == 0 && discountRate != 0) {
			return UtilMath.mul(discountRate, 10);
		}
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setMoneydown(double moneydown) {
		this.moneydown = moneydown;
	}

	public double getDiscountRate() {
		if (discountRate == 0 && discount != 0) {
			return UtilMath.div(discount, 10, 2);
		}
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
