package com.shwm.freshmallpos.value;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.util.ExceptionUtil;

public class ValueFinal {

	/** 称重类型-普通 */
	public static final int TypeWeight_Default = 0;
	/** 称重类型-称重 */
	public static final int TypeWeight_Weight = 1;

	public static final int DayNearly_Today = 0;
	public static final int DayNearly_Week = 7;
	public static final int DayNearly_Month = 30;

	public static final int foodNoCodeId = 0;// 无码商品Id
	public static final String foodNoCodeUnit = "个";// 无码商品单位
	public static final String foodNoName = "无码商品";// 无码商品
	public static final double MAX_SUM = 200000;// 最大金额
	public static final int CLASSES_ISLAST = 1;// 类型是最后一级

	public static final int MAX_NUM = 999;

	/** AsyncTaskUtil code!=1001 */
	public static String getFailInfo() {
		return ApplicationMy.getContext().getString(R.string.statu_fail);
	}

	public static Exception getExceptionFailInfo() {
		return getExceptionFailInfo(ApplicationMy.getContext().getString(R.string.statu_fail));
	}

	public static Exception getExceptionFailInfo(String str) {
		return new ExceptionUtil(str);
	}

	public static final int DiscountMax = 10;// 最大折扣
	public static final int MoneydownMax = 9999;// 最大减单
	
	
	
	
}
