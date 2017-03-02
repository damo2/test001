package com.shwm.freshmallpos.contentprovider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * FreshPos ContentProvider 跨进程共享数据
 * 增删改查
 */
public class FreshPosHelper {
	private ContentResolver resolver = null;
	private String[] columnsAll;
	private Cursor cur = null;// 游标

	public FreshPosHelper(Context context) {
		// TODO Auto-generated constructor stub
		resolver = context.getContentResolver();
		columnsAll = new String[] { FreshmallPosContent.ID, FreshmallPosContent.ADMIN_ID, FreshmallPosContent.ADMIN_USERNAME,
				FreshmallPosContent.ADMIN_PASSWORD, FreshmallPosContent.ADMIN_NIKENAME, FreshmallPosContent.ADMIN_IMG,
				FreshmallPosContent.TIME, };
	}

	/**
	 * 判断是否存在
	 * @return
	 */
	public boolean isExit() {
		Cursor cursor = resolver.query(FreshmallPosContent.CONTENT_ITEM_URI, columnsAll, null, null, null);
		return cursor != null;
	}

	/**
	 * 新增
	 * @param admin 新增Admin对象
	 */
	public void insert(Admin admin) {
		ContentValues values = getContentValues(admin);
		try {
			resolver.insert(FreshmallPosContent.CONTENT_ITEM_URI, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据adminId 更新数据库
	 * @param adminId 用户Id
	 * @param admin Admin 要更新的对象
	 * @return
	 */
	public int updateByAdminId(int adminId, Admin admin) {
		int count = 0;
		ContentValues values = getContentValues(admin);
		Uri uri = Uri.parse("content://" + FreshmallPosContent.AUTHORITY + "/" + FreshmallPosContent.PATH_ADMINID + "/" + adminId);
		try {
			count = resolver.update(uri, values, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 通过adminId查找
	 * @param adminId 用户Id
	 * @return 返回Admin 找不到返回null
	 */
	public Admin queryByAdminId(int adminId) {
		Uri myUri = Uri.parse("content://" + FreshmallPosContent.AUTHORITY + "/" + FreshmallPosContent.PATH_ADMINID + "/" + adminId);
		try {
			cur = resolver.query(myUri, columnsAll, null, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Admin> listAdmin = getListAdmin(cur);
		if (listAdmin != null && listAdmin.size() > 0) {
			return listAdmin.get(0);// 其实只找一个，为了统一就放到集合里面了。
		}
		return null;
	}

	/**
	 * 查找最后一个
	 * @return 返回Admin 找不到返回null
	 */
	public Admin queryLast() {
		Uri myUri = Uri.parse("content://" + FreshmallPosContent.AUTHORITY + "/" + FreshmallPosContent.PATH_POS + "/0");
		try {
			cur = resolver.query(myUri, columnsAll, null, null, FreshmallPosContent.ORDER_TIME);// 倒序查第一个
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Admin> listAdmin = getListAdmin(cur);
		if (listAdmin != null && listAdmin.size() > 0) {
			return listAdmin.get(0);
		}
		return null;
	}

	/**
	 * 查找所有admin
	 * @return 返回List<Admin>
	 */
	public List<Admin> queryAll() {
		Uri myUri = Uri.parse("content://" + FreshmallPosContent.AUTHORITY + "/" + FreshmallPosContent.PATH_ITEM);
		try {
			cur = resolver.query(myUri, columnsAll, null, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getListAdmin(cur);
	}

	// 将对象的字段值保存到集合对应的键，用于数据库操作
	private ContentValues getContentValues(Admin admin) {
		ContentValues values = new ContentValues();
		values.put(FreshmallPosContent.ADMIN_ID, admin.getId());
		values.put(FreshmallPosContent.ADMIN_USERNAME, admin.getUsername());
		values.put(FreshmallPosContent.ADMIN_PASSWORD, admin.getPassword());
		values.put(FreshmallPosContent.ADMIN_NIKENAME, admin.getNickname());
		values.put(FreshmallPosContent.ADMIN_IMG, admin.getImg());
		values.put(FreshmallPosContent.TIME, admin.getTime());
		return values;
	}

	// 遍历游标
	private List<Admin> getListAdmin(Cursor cur) {
		List<Admin> listAdmin = new ArrayList<Admin>();
		if (cur != null && cur.moveToFirst()) {// cursor不为空，查询得到的cursor是指向第一条记录之前的，用moveToFirst
												// 移动到第一个，moveToFirst为true说明有数据 。
			int _id = 0;
			int adminId = 0;
			String userName = null;
			String password = null;
			String nickname = null;
			String userImg = null;
			String time = null;
			do {
				// 返回-1表示不存在字段
				if (cur.getColumnIndex(FreshmallPosContent.ID) > -1) {
					_id = cur.getInt(cur.getColumnIndex(FreshmallPosContent.ID));
				}
				if (cur.getColumnIndex(FreshmallPosContent.ADMIN_ID) > -1) {
					adminId = cur.getInt(cur.getColumnIndex(FreshmallPosContent.ADMIN_ID));
				}
				if (cur.getColumnIndex(FreshmallPosContent.ADMIN_USERNAME) > -1) {
					userName = cur.getString(cur.getColumnIndex(FreshmallPosContent.ADMIN_USERNAME));
				}
				if (cur.getColumnIndex(FreshmallPosContent.ADMIN_PASSWORD) > -1) {
					password = cur.getString(cur.getColumnIndex(FreshmallPosContent.ADMIN_PASSWORD));
				}
				if (cur.getColumnIndex(FreshmallPosContent.ADMIN_NIKENAME) > -1) {
					nickname = cur.getString(cur.getColumnIndex(FreshmallPosContent.ADMIN_NIKENAME));
				}
				if (cur.getColumnIndex(FreshmallPosContent.ADMIN_IMG) > -1) {
					userImg = cur.getString(cur.getColumnIndex(FreshmallPosContent.ADMIN_IMG));
				}
				if (cur.getColumnIndex(FreshmallPosContent.TIME) > -1) {
					time = cur.getString(cur.getColumnIndex(FreshmallPosContent.TIME));
				}
				Admin admin = new Admin();
				admin.set_id(_id);
				admin.setId(adminId);
				admin.setUsername(userName);
				admin.setPassword(password);
				admin.setNickname(nickname);
				admin.setImg(userImg);
				admin.setTime(time);
				listAdmin.add(admin);
			} while (cur.moveToNext());
			cur.close();// 关闭游标，释放资源
		}
		return listAdmin;
	}

}
