package com.shwm.freshmallpos.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.value.ValueKey;

public class UtilSPF {
	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */

	private static SharedPreferences getSPF() {
		return ApplicationMy.getContext().getSharedPreferences(ValueKey.SPF_CONTENT, ValueKey.SPF_MODEL);
	}

	private static SharedPreferences.Editor getEditor() {
		return getSPF().edit();
	}

	// put
	public static void putString(String key, String valueStr) {
		SharedPreferences.Editor editor = getEditor();
		editor.putString(key, valueStr);
		editor.commit();
	}

	public static void putInt(String key, int valueInt) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(key, valueInt);
		editor.commit();
	}

	public static void putBoolean(String key, boolean valueBoolean) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(key, valueBoolean);
		editor.commit();
	}

	// get
	public static String getString(String key, String defaultString) {
		SharedPreferences sp = getSPF();
		return sp.getString(key, defaultString);
	}

	public static int getInt(String key, int defaultInt) {
		SharedPreferences sp = getSPF();
		return sp.getInt(key, defaultInt);
	}

	public static boolean getBoolean(String key, boolean defaultBoolean) {
		SharedPreferences sp = getSPF();
		return sp.getBoolean(key, defaultBoolean);
	}

	//
	public static void put(Context context, String key, Object object) {
		SharedPreferences.Editor editor = getEditor();
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}
		SharedPreferencesCompat.apply(editor);
	}

	public static void put(String key, Object object) {
		put(ApplicationMy.getContext(), key, object);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	// public static Object get(Context context, String key, Object defaultObject) {
	// SharedPreferences sp = context.getSharedPreferences(ValueKeyUtil.SPF_CONTENT, ValueKeyUtil.SPF_MODEL);
	// if (defaultObject instanceof String) {
	// return sp.getString(key, (String) defaultObject);
	// } else if (defaultObject instanceof Integer) {
	// return sp.getInt(key, (Integer) defaultObject);
	// } else if (defaultObject instanceof Boolean) {
	// return sp.getBoolean(key, (Boolean) defaultObject);
	// } else if (defaultObject instanceof Float) {
	// return sp.getFloat(key, (Float) defaultObject);
	// } else if (defaultObject instanceof Long) {
	// return sp.getLong(key, (Long) defaultObject);
	// }
	//
	// return null;
	// }

	// public static Object get(String key, Object defaultObject) {
	// return get(ApplicationMy.getContext(), key, defaultObject);
	// }

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(ValueKey.SPF_CONTENT, ValueKey.SPF_MODEL);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(ValueKey.SPF_CONTENT, ValueKey.SPF_MODEL);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(ValueKey.SPF_CONTENT, ValueKey.SPF_MODEL);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(ValueKey.SPF_CONTENT, ValueKey.SPF_MODEL);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}
