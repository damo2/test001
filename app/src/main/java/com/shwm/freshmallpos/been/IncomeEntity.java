package com.shwm.freshmallpos.been;

import java.util.ArrayList;
import java.util.List;

public class IncomeEntity {
	private double today;
	private double week;
	private double month;
	private List<SaleEntity> listSale;

	public IncomeEntity() {
		super();
		listSale = new ArrayList<SaleEntity>();
	}

	public double getToday() {
		return today;
	}

	public void setToday(double today) {
		this.today = today;
	}

	public double getWeek() {
		return week;
	}

	public void setWeek(double week) {
		this.week = week;
	}

	public double getMonth() {
		return month;
	}

	public void setMonth(double month) {
		this.month = month;
	}

	public List<SaleEntity> getListSale() {
		return listSale;
	}

	public void setListSale(List<SaleEntity> listSale) {
		this.listSale = listSale;
	}

}
