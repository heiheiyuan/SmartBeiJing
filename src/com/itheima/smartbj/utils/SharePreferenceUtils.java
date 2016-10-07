package com.itheima.smartbj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtils {
	public static SharedPreferences sp;
	public static void init(Context context) {
		sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
	}
	public static boolean getBoolean(Context context, String key) {
		init(context);
		return sp.getBoolean(key, false);
	}
	public static void putBoolean(Context context, String key,
			boolean value) {
		init(context);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static void putString(Context context, String cacheData,
			String result) {
		init(context);
		Editor editor = sp.edit();
		editor.putString(cacheData, result);
		editor.commit();
	}
	public static String getString(Context context, String cacheData) {
		init(context);
		return sp.getString(cacheData, null);
	}

}
