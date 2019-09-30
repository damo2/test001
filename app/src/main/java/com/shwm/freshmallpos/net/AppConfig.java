package com.shwm.freshmallpos.net;

public final class AppConfig {
	public static final String packageName = "com.shwm.freshmallmanage";
	public static final String apkName = "freshmallmanage";
	public static final String apkNameSuffix = ".apk";
	public static final String PT_ID = "SHWM";

	 public static final String SERVER_IP = "http://172.16.1.84/freshmall3";
//	public static final String SERVER_IP = "http://m.vidream.cn/freshmall";

	public static final String SLASH = "/";
	public static final String ROOT_PATH = "/freshmall" + SLASH;
	public static final String VER = "1";
	// app  http://www.vidream.cn/MyApp/AppDown?pkg=com.shwm.freshmallmanage
	public static final String UPDATE_URL = "http://www.vidream.cn" + "/MyApp/AppUpdate?ptid=" + PT_ID + "&ver=" + VER + "&pkg=";
	public static final String DOWNLOAD_URL = "http://www.vidream.cn" + "/MyApp/AppDown?ptid=" + PT_ID + "&ver=" + VER + "&pkg=";
	// 登录注册
	public static final String LOGIN_URL = "/adminLoginMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String REG_GETCODE = "/store/storeVal?ptid=" + PT_ID + "&ver=" + VER;
	public static final String REGIN_URL = "/store/storeReg?ptid=" + PT_ID + "&ver=" + VER;
	// 得到头部分类
	public static final String GET_CLASSES = "/getHeadTypes?ptid=" + PT_ID + "&ver=" + VER;
	// 获取对应类型商品
	public static final String GET_FOODSBYCLASSES = "/getItemByType?ptid=" + PT_ID + "&ver=" + VER;
	public static final String ORDER_SUBMIT = "/underLineOrder?ptid=" + PT_ID + "&ver=" + VER;
	public static final String DEL = "/deleteContentInMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String MANAGER_FOOD_ADD = "/addItemInMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String MANAGER_FOOD_EDIT = "/editItemInMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String GET_FOODDETAIL = "/getItemInfo?ptid=" + PT_ID + "&ver=" + VER;
	public static final String CLASSES_ADD = "/addTypeInMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String CLASSES_UPDATE = "/editTypeInMobi?ptid=" + PT_ID + "&ver=" + VER;
	public static final String MANAGER_FOOD_ADD_IMGS = "/uploadMultItemFile?ptid=" + PT_ID + "&ver=" + VER;
	public static final String MEMBER_SEARCH = "/mem/search?ptid=" + PT_ID + "&ver=" + VER;
	public static final String MEMBER_ADD = "/mem/addMem?ptid=" + PT_ID + "&ver=" + VER;
	public static final String GET_ORDER = "/findUnderLineOrder?ptid=" + PT_ID + "&ver=" + VER;
	public static final String GET_FOOD_BYCODE = "/findItemByBarcode?ptid=" + PT_ID + "&ver=" + VER;

	public static final String GET_ORDER_DETAIL = "/getOrderDetailItem?ptid=" + PT_ID + "&ver=" + VER;

	// 退款
	public static final String ORDER_REFUND = "/applyForRefund?ptid=" + PT_ID + "&ver=" + VER;
	// 修改密码
	public static final String CHANGE_PWD = "/ichange?ptid=" + PT_ID + "&ver=" + VER;
	// 收入
	public static final String GET_INCOME = "/saleRecords?ptid=" + PT_ID + "&ver=" + VER;

	public static final String SEARCH_FOOD_LIKE = "/searchItem?ptid=" + PT_ID + "&ver=" + VER;

	public static final String BUSINESS_CHANGE = "/editBusinessAdmin?ptid=" + PT_ID + "&ver=" + VER;
}
