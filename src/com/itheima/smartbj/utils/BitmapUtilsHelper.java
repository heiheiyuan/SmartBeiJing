package com.itheima.smartbj.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapUtilsHelper {
	private static BitmapUtils instance;
	public static BitmapUtils getBitmapUtils(Context context) {
		if (instance == null) {
			instance = new BitmapUtils(context);
		}
		return instance;
	}
}
