package com.shwm.freshmallpos.contentprovider;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.net.Uri;

/**
 * [content://][com.shwm.freshmallpos.authority][/query][/123] <br/>
 * [Scheme][Authority][资源路径][资源ID]
 * 
 * content://com.shwm.freshmallpos.authority/item表示访问所有的信息；<br/>
 * content://com.shwm.freshmallpos.authority/item/123表示只访问ID值为123的信息条目<br/>
 * content://com.shwm.freshmallpos.authority/pos/1表示访问数据库表中的第1条信息，这条信息条的ID值不一定为1。通过常量CONTENT_POS_URI来访问文章信息条目时 ，必须要指定位置
 */
public class FreshmallPosContent {
	// Authority
	public static final String AUTHORITY = "com.shwm.freshmallpos.authority";

	// 资源路径
	public static final String PATH_ITEM = "item";// 根据id获取
	public static final String PATH_POS = "pos";// 查找第pos个
	public static final String PATH_ADMINID = "adminId";// 根据adminId查找
	/* Content URI */
	public static final Uri CONTENT_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_ITEM);
	public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_POS);
	public static final Uri CONTENT_ADMINID_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_ADMINID);
	/* 数据表数据字段 */
	public static final String ID = "_id";// _id不变 i
	public static final String TIME = "logintime";// 时间 s

	public static final String ADMIN_ID = "adminId";// 管理员Id i
	public static final String ADMIN_USERNAME = "adminName";// 管理员用户名 s
	public static final String ADMIN_PASSWORD = "adminPassword";// s
	public static final String ADMIN_IMG = "adminImg";// s
	public static final String ADMIN_NIKENAME = "adminNikeName";// 管理员名字 s

	/* 类型 */
	/** 查找所有 */
	public static final int ITEM = 1;
	/** 根据Id查找 */
	public static final int ITEM_ID = 2;
	/** 根据位置查找 */
	public static final int ITEM_POS = 3;
	/** 根据adminId查找 */
	public static final int ITEM_ADMINID = 4;
	/**
	 * MIME<br>
	 * vnd.android.cursor.dir代表返回结果为多列数据 <br>
	 * vnd.android.cursor.item 代表返回结果为单列数据
	 */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.shwm.freshmallpos.admin";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.shwm.freshmallpos.admin";

	/* 排列方式 */
	/** 按 _id 正序排列（默认） */
	public static final String DEFAULT_SORT_ORDER = "_id asc";
	/** 按 _id 倒序排列 */
	public static final String LAST_SORT_ORDER = " _id desc";
	/** 按时间倒序排列 */
	public static final String ORDER_TIME = " " + TIME + " desc";

	public static String getDBCurrentTime() {
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(d);
	}
}
