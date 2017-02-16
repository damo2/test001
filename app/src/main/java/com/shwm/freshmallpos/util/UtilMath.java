package com.shwm.freshmallpos.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 价格计算 BigDecimal 类型的加减乘除
 */
public class UtilMath {
	/**
	 * 进行加法运算
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double add(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.add(b2).doubleValue();
	}

	/**
	 * 进行减法运算
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double sub(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 进行乘法运算
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double mul(String s1, String s2) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 进行乘法运算
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double mul(String s1, String s2, int len) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 进行除法运算
	 * 
	 * @param s1
	 * @param s2
	 * @param len
	 * @return
	 */
	public static double div(String s1, String s2, int len) {
		BigDecimal b1 = new BigDecimal(s1);
		BigDecimal b2 = new BigDecimal(s2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 进行四舍五入 操作
	 * 
	 * @param d
	 * @param len
	 * @return
	 */
	public static double round(String s, int len) {
		BigDecimal b1 = new BigDecimal(s);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 转化为货币 例如 “¥20”
	 * 
	 * @param o
	 * @return
	 */
	public static String currency(Object o) {
		NumberFormat currency = NumberFormat.getCurrencyInstance(); // 建立货币格式化引用
		return currency.format(o);
	}

	/** 进行加法运算 */
	public static double add(double d1, double d2) {
		return add(Double.toString(d1), Double.toString(d2));
	}

	/** 进行减法运算 */
	public static double sub(double d1, double d2) {
		return sub(Double.toString(d1), Double.toString(d2));
	}

	/** 进行乘法运算 */
	public static double mul(double d1, double d2) {
		return mul(Double.toString(d1), Double.toString(d2));
	}

	/** 进行除法运算 */
	public static double div(double d1, double d2, int len) {
		return div(Double.toString(d1), Double.toString(d2), len);
	}

	
	
}
