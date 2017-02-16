package com.shwm.freshmallpos.been;

import java.io.Serializable;
import com.shwm.freshmallpos.util.StringUtil;

public class OrderEntity implements Serializable {
	private int id;
	private String orderno;
	private double money;// 订单金额
	private double totalDay;// 当天销售总额
	private double payMoney;// 订单支付金额
	private int type;// 订单类型
	private int statu;// 订单状态
	private String statuTag;
	private int payType;// 支付类型
	private String payTypeTag;
	private String time;// 时分
	private String date;// 日期
	private String timeCreat;// 开单时间
	private String refund;// 退款描述

	public OrderEntity() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public void setPayType(String payType) {
		this.payType = StringUtil.getInt(payType);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void setMoney(String money) {
		this.money = StringUtil.getDouble(money);
	}

	public double getTotalDay() {
		return totalDay;
	}

	public void setTotalDay(double totalDay) {
		this.totalDay = totalDay;
	}

	public String getPayTypeTag() {
		return payTypeTag;
	}

	public void setPayTypeTag(String payTypeTag) {
		this.payTypeTag = payTypeTag;
	}

	public String getTimeCreat() {
		return timeCreat;
	}

	public void setTimeCreat(String timeCreat) {
		this.timeCreat = timeCreat;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = StringUtil.getDouble(payMoney);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = StringUtil.getInt(type);
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public void setStatu(String statu) {
		this.statu = StringUtil.getInt(statu);
	}

	public String getStatuTag() {
		return statuTag;
	}

	public void setStatuTag(String statuTag) {
		this.statuTag = statuTag;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

}
