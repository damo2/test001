package com.shwm.freshmallpos.been;

import java.io.Serializable;
import com.shwm.freshmallpos.value.ValueType;

public class MemberEntity implements Serializable {
	private int id;
	private String nick;
	private String tel;
	private String cardno;
	private String cardtype;
	private double money;
	// private double discount = 10;// 折扣
	private CouponEntity coupon;

	public MemberEntity() {
		// TODO Auto-generated constructor stub
		coupon = new CouponEntity(ValueType.CouponType_DiscountMember);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public CouponEntity getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponEntity coupon) {
		this.coupon = coupon;
	}

	// public double getDiscount() {
	// return discount;
	// }
	//
	// public void setDiscount(double discount) {
	// this.discount = discount;
	// }

}
