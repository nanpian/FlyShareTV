package com.zgntech.core.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * 
 * 
 * @Description: TODO(单例Toast-解决重复显示)
 * 
 * @author gufei 562401002@qq.com
 * 
 * @date 2015年3月17日 上午11:21:07
 */
public class T {
	private static Toast toast;
	private static T t;

	public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static int LENGTH_LONG = Toast.LENGTH_LONG;

	public T(Context context) {
		View v = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
		toast = new Toast(context);
		toast.setView(v);
	}

	public static T create(Context context) {
		if (t == null) {
			t = new T(context);
		}
		return t;
	}

	public static void toast(CharSequence text, int duration) {
		toast.setText(text);
		toast.setDuration(duration);
		toast.show();
	}

	public static void toast(int Resid, int duration) {
		toast.setText(Resid);
		toast.setDuration(duration);
		toast.show();
	}

	public static void toast(CharSequence text) {
		toast.setText(text);
		toast.setDuration(LENGTH_LONG);
		toast.show();
	}

	public static void toast(int Resid) {
		toast.setText(Resid);
		toast.setDuration(LENGTH_LONG);
		toast.show();
	}
}
