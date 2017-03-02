package com.shwm.freshmallpos.contentprovider;

import java.util.HashMap;

import com.shwm.freshmallpos.util.UL;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FreshmallPosContentProvider extends ContentProvider {
	private static final String TAG = FreshmallPosContentProvider.class.getSimpleName();

	private static final String DB_NAME = "FreshmallPos03.db";
	private static final String DB_TABLE = "PosTable";
	private static final int DB_VERSION = 1;

	private static final String DB_CREATE;
	static {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(" create table " + DB_TABLE + " (");
		stringBuffer.append(FreshmallPosContent.ID + " integer primary key autoincrement, ");
		stringBuffer.append(FreshmallPosContent.ADMIN_ID + " integer, ");
		stringBuffer.append(FreshmallPosContent.TIME + " text, ");
		stringBuffer.append(FreshmallPosContent.ADMIN_USERNAME + " text, ");
		stringBuffer.append(FreshmallPosContent.ADMIN_PASSWORD + " text, ");
		stringBuffer.append(FreshmallPosContent.ADMIN_NIKENAME + " text, ");
		stringBuffer.append(FreshmallPosContent.ADMIN_IMG + " text);");
		DB_CREATE = stringBuffer.toString();
	}
	private static final HashMap<String, String> articleProjectionMap;
	static {
		/*
		 * 使用SQLiteQueryBuilder来辅助数据库查询操作，使用这个类的好处是我们可以不把数据库表的字段暴露出来，而是提供别名给第三方应用程序使用，
		 * 这样就可以把数据库表内部设计隐藏起来，方便后续扩展和维护。第一个参数表示列的别名，第二个参数表示列的真实名称。
		 */
		articleProjectionMap = new HashMap<String, String>();
		articleProjectionMap.put(FreshmallPosContent.ID, FreshmallPosContent.ID);
		articleProjectionMap.put(FreshmallPosContent.TIME, FreshmallPosContent.TIME);
		articleProjectionMap.put(FreshmallPosContent.ADMIN_ID, FreshmallPosContent.ADMIN_ID);
		articleProjectionMap.put(FreshmallPosContent.ADMIN_USERNAME, FreshmallPosContent.ADMIN_USERNAME);
		articleProjectionMap.put(FreshmallPosContent.ADMIN_PASSWORD, FreshmallPosContent.ADMIN_PASSWORD);
		articleProjectionMap.put(FreshmallPosContent.ADMIN_NIKENAME, FreshmallPosContent.ADMIN_NIKENAME);
		articleProjectionMap.put(FreshmallPosContent.ADMIN_IMG, FreshmallPosContent.ADMIN_IMG);
	}

	private static final UriMatcher URI_MATCHER;// 用于匹配Uri
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		/*
		 * 注册需要的Uri #表示数字
		 * 1、content://com.shwm.freshmallpos.authority/item。2、content://com.shwm.freshmallpos.authority/item/123。
		 * 3、content://com.shwm.freshmallpos.authority/pos/1
		 */
		URI_MATCHER.addURI(FreshmallPosContent.AUTHORITY, FreshmallPosContent.PATH_ITEM, FreshmallPosContent.ITEM);
		URI_MATCHER.addURI(FreshmallPosContent.AUTHORITY, FreshmallPosContent.PATH_ITEM + "/#", FreshmallPosContent.ITEM_ID);
		URI_MATCHER.addURI(FreshmallPosContent.AUTHORITY, FreshmallPosContent.PATH_POS + "/#", FreshmallPosContent.ITEM_POS);
		URI_MATCHER.addURI(FreshmallPosContent.AUTHORITY, FreshmallPosContent.PATH_ADMINID + "/#", FreshmallPosContent.ITEM_ADMINID);
	}

	private DBHelper dbHelper = null;
	private ContentResolver resolver = null;

	// 用来执行一些初始化的工作。
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		resolver = context.getContentResolver();
		dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
		UL.d(TAG, "onCreate()");
		return true;
	}

	// 用来插入新的数据。
	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		// TODO Auto-generated method stub
		if (URI_MATCHER.match(uri) != FreshmallPosContent.ITEM) {
			throw new IllegalArgumentException("Error Uri: " + uri);
		}
		if (URI_MATCHER.match(uri) == FreshmallPosContent.ITEM) {
			SQLiteDatabase database = dbHelper.getWritableDatabase();
			long id = database.insert(DB_TABLE, FreshmallPosContent.ID, contentvalues);
			if (id < 0) {
				// throw new SQLiteException("Unable to insert " + contentvalues + " for " + uri);
				UL.e(TAG, "Unable to insert " + contentvalues + " for " + uri);
			}
			Uri newUri = ContentUris.withAppendedId(uri, id);
			resolver.notifyChange(newUri, null);
			return newUri;
		}
		return null;
	}

	// 用来返回数据给调用者。
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		String limit = null;
		// 判断用户请求，查询所有还是单个
		switch (URI_MATCHER.match(uri)) {
		case FreshmallPosContent.ITEM: {
			// 查询所有
			sqlBuilder.setTables(DB_TABLE);// 设置要查询的表名
			sqlBuilder.setProjectionMap(articleProjectionMap);// 设置表字段的别名
			break;
		}
		case FreshmallPosContent.ITEM_POS: {
			// 查询第pos个
			String pos = uri.getPathSegments().get(1);// 如 content://com.shwm.freshmallpos.authority/pos/3 得到pos3
			sqlBuilder.setTables(DB_TABLE);// 设置要查询的表名
			sqlBuilder.setProjectionMap(articleProjectionMap);// 设置表字段的别名
			limit = pos + ", 1";// LIMIT <跳过的数据数目>, <取数据数目>
			break;
		}
		case FreshmallPosContent.ITEM_ID: {
			// 查询id为#的一个
			String id = uri.getPathSegments().get(1);
			sqlBuilder.setTables(DB_TABLE);
			sqlBuilder.setProjectionMap(articleProjectionMap);
			sqlBuilder.appendWhere(FreshmallPosContent.ID + "=" + id);// 查询条件
			break;
		}
		case FreshmallPosContent.ITEM_ADMINID: {
			String adminId = uri.getPathSegments().get(1);
			sqlBuilder.setTables(DB_TABLE);
			sqlBuilder.setProjectionMap(articleProjectionMap);
			sqlBuilder.appendWhere(FreshmallPosContent.ADMIN_ID + "=" + adminId);// 查询条件
			break;
		}
		default:
			// throw new IllegalArgumentException("Error Uri: " + uri);
			UL.e(TAG, "Error Uri: " + uri);
		}
		/**
		 * qb.query(db, projection, selection, selectionArgs, null,null, orderBy) 第一个参数为要查询的数据库实例。
		 * 第二个参数是一个字符串数组，里边的每一项代表了需要返回的列名。 第三个参数相当于SQL语句中的where部分。 第四个参数是一个字符串数组，里边的每一项依次替代在第三个参数中出现的问号（?）。
		 * 第五个参数相当于SQL语句当中的groupby部分。 第六个参数相当于SQL语句当中的having部分。 第七个参数描述是怎么进行排序。 第八个参数相当于SQL当中的limit部分 LIMIT <跳过的数据数目>,
		 * <取数据数目>，控制返回的数据的个数。
		 */
		Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null,
				TextUtils.isEmpty(sortOrder) ? FreshmallPosContent.DEFAULT_SORT_ORDER : sortOrder, limit);
		cursor.setNotificationUri(resolver, uri);
		return cursor;
	}

	// 用来删除数据。
	@Override
	public int delete(Uri uri, String s, String[] as) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 用来返回数据的MIME类型。
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		/*
		 * 为ITEM 时查找所有的信息,为ITEM_ID或ITEM_POS时 根据条件查找返回一条信息
		 */
		switch (URI_MATCHER.match(uri)) {
		case FreshmallPosContent.ITEM:
			return FreshmallPosContent.CONTENT_TYPE;
		case FreshmallPosContent.ITEM_ID:
		case FreshmallPosContent.ITEM_POS:
		case FreshmallPosContent.ITEM_ADMINID:
			return FreshmallPosContent.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Error Uri: " + uri);
		}
	}

	// 用来更新已有的数据
	@Override
	public int update(Uri uri, ContentValues contentvalues, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int count = 0;

		switch (URI_MATCHER.match(uri)) {
		case FreshmallPosContent.ITEM: {
			count = db.update(DB_TABLE, contentvalues, selection, selectionArgs);
			break;
		}
		case FreshmallPosContent.ITEM_ADMINID: {
			String adminId = uri.getPathSegments().get(1);
			count = db.update(DB_TABLE, contentvalues, FreshmallPosContent.ADMIN_ID + "=" + adminId
					+ (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
			break;
		}
		default:
			throw new IllegalArgumentException("Error Uri: " + uri);
		}

		resolver.notifyChange(uri, null);

		return count;
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(db);
		}

	}

}
