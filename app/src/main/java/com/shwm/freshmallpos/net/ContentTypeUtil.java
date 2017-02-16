package com.shwm.freshmallpos.net;

public class ContentTypeUtil {
	// 不同类型查找
	/** context_type 商品分类 */
	public static final int TYPE_Classes = 1;
	/** context_type 商品 */
	public static final int TYPE_Food = 2;
	/** context_type 订单 */
	public static final int TYPE_Order = 3;
	/** context_type Model */
	public static final int TYPE_Model = 4;
	/** context_type 功能模块 */
	public static final int TYPE_Function = 5;
	/** context_type 试吃 */
	public static final int TYPE_Eattry = 7;
	/** context_type 商家 */
	public static final int TYPE_Shop = 9;
	/** context_type 吃法 */
	public static final int TYPE_Cooking = 10;
	/** context_type 轮播 */
	public static final int TYPE_Lunbo = 13;
	/** context_type 红包 */
	public static final int TYPE_Redbag = 15;
	/** context_type 抢购 */
	public static final int TYPE_Buyqiang = 21;
	/** context_type 店铺管理员列表 */
	public static final int TYPE_AdminList = 40;
	/** context_type 用户 */
	public static final int TYPE_User = 14;

	/** context_type 日统计 */
	public static final int TYPE_tongji_day = 20;
	/** context_type 月统计 */
	public static final int TYPE_tongji_month = 18;
	/** context_type 年统计 */
	public static final int TYPE_tongji_year = 19;

	/** context_type 充值项 */
	public static final int TYPE_RechargeItem = 24;

	/** type 订单查未完成 */
	public static final int Order_Type_Undo = 1;
	/** type 订单查取消 */
	public static final int Order_Type_Canael = 2;

	/** type 订单查微信支付 */
	public static final int Order_Type_PayWx = 3;
	/** type 订单查货到付款 */
	public static final int Order_Type_PayTo = 4;
}
