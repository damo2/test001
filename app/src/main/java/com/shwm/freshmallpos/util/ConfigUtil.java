package com.shwm.freshmallpos.util;

import com.shwm.freshmallpos.manage.BusinessInfo;

public class ConfigUtil {
	public static final int TimeOut = 8000;
	public static final int OutTimeConnect = 8000;// 网络请求连接超时时间
	public static final int OutTimeRead = 8000;
	public static final int OutTimeWrite = 8000;
	public static final int OutTimeLocal = 12000;
	public static final int OutTimeConnectDown = 30 * 1000;// 下载文件连接超时时间
	public static final int OutTimeReadDown = 30 * 1000;// 下载文件读取超时时间
	public static final int RequestDelayMillis = 0;// 网络请求延迟
	public static final int AsyncTaskHandler = 0;// 网络请求延迟
	public static final int OnClickTime = 300;// 防止过快点击

	public static final int SwiperefreshHeight = 140;

	/** 商品图片宽高 */
	public static final int FoodImageWH = 400;
	/** 支付二维码保存宽高 */
	public static final int CodeQRWH = 400;
	public static String CouponFileNamePre = "coupon_" + BusinessInfo.getAdminName() + "_" + BusinessInfo.getBusinessID();// 保存优惠劵的文件名

	public static final int BusinessLogoWH = 120;// 店铺logo上传图片宽高

	public static final int Weight_Size = 1;// 称重商品精确到1位

}