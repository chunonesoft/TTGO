package com.chunsoft.ttgo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
	/**
	 * 普通字段存放地址
	 */
	public static String TTGO = "TTGO";

	public static String getSharePreStr(Context mContext, String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		String s = sp.getString(field, "");
		return s;
	}

	// 取出whichSp中field字段对应的int类型的值
	public static int getSharePreInt(Context mContext, String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		int i = sp.getInt(field, 0);// 如果该字段没对应值，则取出0
		return i;
	}

	public static boolean getSharePreBoolean(Context mContext, String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		boolean i = sp.getBoolean(field, false);
		return i;
	}

	public static void putSharePre(Context mContext, String field, String value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		sp.edit().putString(field, value).commit();
	}

	// 保存int类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext, String field, int value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		sp.edit().putInt(field, value).commit();
	}

	public static void putSharePre(Context mContext, String field, Boolean value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(TTGO, 0);
		sp.edit().putBoolean(field, value).commit();
	}

	public static void clearSharePre(Context mContext) {
		try {
			SharedPreferences sp = (SharedPreferences) mContext
					.getSharedPreferences(TTGO, 0);
			sp.edit().clear().commit();
		} catch (Exception e) {
		}
	}

}
