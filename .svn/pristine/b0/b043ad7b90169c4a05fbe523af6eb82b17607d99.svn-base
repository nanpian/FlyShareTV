package com.zgntech.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Sharepf {
	private static Sharepf sharepf;
	private static SharedPreferences sPreferences;
	private static SharedPreferences.Editor editor;

	public Sharepf(Context context) {
		super();
		sPreferences = PreferenceManager.getDefaultSharedPreferences(context);// 根据包名
		editor = sPreferences.edit();
	}

	public Sharepf(Context context, String sharepfName) {
		super();
		sPreferences = context.getSharedPreferences(sharepfName,
				Context.MODE_PRIVATE);
		editor = sPreferences.edit();
	}

	public synchronized static Sharepf getInstance(Context context) {
		if (sharepf == null) {
			sharepf = new Sharepf(context);
			return sharepf;
		}
		return sharepf;
	}

	public synchronized static Sharepf getInstance(Context context,
			String sharepfName) {
		if (sharepf == null) {
			if (!TextUtils.isEmpty(sharepfName)) {
				sharepf = new Sharepf(context, sharepfName);
				return sharepf;
			} else {
				sharepf = new Sharepf(context);
				return sharepf;
			}
		}
		return sharepf;
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param key
	 * @param object
	 */
	public static void put(String key, Object object) {
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

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(String key, Object defaultObject) {
		if (defaultObject instanceof String) {
			return sPreferences.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sPreferences.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sPreferences.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sPreferences.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sPreferences.getLong(key, (Long) defaultObject);
		}
		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(String key) {
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear() {
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
	public static boolean contains(String key) {
		return sPreferences.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll() {
		return sPreferences.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
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
