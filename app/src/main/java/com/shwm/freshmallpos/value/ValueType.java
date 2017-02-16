package com.shwm.freshmallpos.value;

public class ValueType {

	/****************************** 分割线 *****************************/
	/** 默认 */
	public static final int DEFAULT = 0;
	/** 添加 */
	public static final int ADD = 1;
	/** 编辑 */
	public static final int EDIT = 2;
	/** 选择 */
	public static final int CHOOSE = 5;

	public static final int CART = 6;
	/****************************** 分割线 *****************************/
	/** 默认 ，不刷新不加载*/
	public static final int PAGE_DEFAULT = 0;
	/** 刷新 */
	public static final int PAGE_REFRESH = 1;
	/** 加载 */
	public static final int PAGE_LOAD = 2;
	/****************************** 页加载 *****************************/

	public static final int LOAD_NO = 0;
	/** 加载完成 */
	public static final int LOAD_OVER = 10;
	/** 全部加载完成 */
	public static final int LOAD_OVERALL = 11;
	/** 加载中 */
	public static final int LOAD_LOADING = 12;
	/** 加载失败 */
	public static final int LOAD_FAIL = 13;

	/** 网络图片 */
	public static final int IMAGE_NET = 301;

	public static final int HttpType_Default = 0;
	/** http请求注册时传session */
	public static final int HttpType_Regin = 1;
	// 优惠劵类型
	public static final int CouponType_Discount = 30;
	public static final int CouponType_Moneydown = 31;
	public static final int CouponType_DiscountMember = 32;
	//
	public static final int ChangeType_Nickname = 33;
	public static final int ChangeType_Businessname = 34;
	//
	public static final int OverType_Refund = 35;// 退款完成
	public static final int OverType_Pay = 36;// 支付完成
}
