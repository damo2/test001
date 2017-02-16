package com.shwm.freshmallpos.value;

public class ValueKey {
	public static final String TITLE = "title";
	public static final String SPF_CONTENT = "spf_pos";
	public static final int SPF_MODEL = 0;
	/** HTTP 请求返回状态 */
	public static final String HTTP_STATU = "request_statu";
	/** HTTP 请求返回成功信息 */
	public static final String HTTP_RESUTL = "request_result";
	public static final String HTTP_InputStream = "request_InputStream";
	/** HTTP 请求返回失败信息 */
	public static final String HTTP_FAILINFO = "request_fail_info";

	/** 请求成功返回状态码 ValueStatuUtil.SUCCESS 为成功 */
	public static final String RESULT_CODE = "code";
	public static final String RESULT_MSG = "msg";
	// login
	public static final String Vcode = "vcode";
	public static final String VcodeMobi = "vcodemobi";
	public static final String IsLoginAuto = "autoLogin";
	public static final String IsLogin = "beLogin";
	// SharedPreferences
	public static final String ADMIN_ID = "adminId";// 商家Id i
	public static final String ADMIN_USERNAME = "adminName";// 管理员用户名 s
	public static final String ADMIN_PASSWORD = "adminPassword";// s
	public static final String ADMIN_TYPE = "admintype";// 管理员类型
	public static final String ADMIN_NIKENAME = "adminType";// 管理员名字 s
	public static final String Business_ID = "shopId";// 商家Id i
	public static final String Business_IP = "ipAddr";// s
	public static final String Business_ADDRESS = "address";// s
	public static final String Business_NAME = "shopname";// s
	public static final String Business_LOGO = "logo";// s
	//
	public static final String BARCODE = "barcode";
	public static final String CouponOpenStatu = "couponOpenStatu";

	//
	public static final String CLASSES = "classes";
	public static final String CLASSESListAll = "classesListAll";
	public static final String FOOD = "food";
	public static final String ORDER = "order";
	/** 条形码 */
	public static final String CODEBAR = "return_barcode";
	/** 二维码 */
	public static final String CODERQ = "RQcode";
	public static final String FOODEDIT_TYPE = "food_type";
	public static final String CLASSESMANAGE_TYPE = "manage_type";

	public static final String ItemId = "itemId";

	public static final String TYPE = "type";

	// 优惠劵转json保存文件
	public static final String CouponType = "type";
	public static final String CouponValue = "value";
	public static final String CouponTag = "valuetag";
	public static final String CouponTypeDiscount = "couponTypeDiscount";
	public static final String CouponTypeMoneydown = "couponTypeMoneydown";
	//
	public static final String Admin = "admin";
	public static final String Member = "member";
	public static final String Money = "money";
	/** 应收金额 */
	public static final String MoneyReceivable = "moneyReceivable";
	public static final String LISTFOOD = "listFood";
	public static final String LISTCART = "listcart";
	public static final String LISTMEMBER = "listMember";
	public static final String LISTORDER = "listOrder";

	public static final String Income = "income";
	public static final String DayNearly = "dayNearly";
	public static final String TypePay = "TypePay";
	public static final String Image = "image";
	public static final String AddressEntity = "addressentity";// 对象

	public static final String Version = "version";// 版本

	public static final String ResultInfoWx = "resultInfoWx";//微信支付返回信息

	public static final String Content = "content";// 版本
}
