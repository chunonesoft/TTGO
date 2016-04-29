package com.chunsoft.ttgo.util;

import android.content.Context;
import android.content.Intent;

public class IntentUti {
	public static void IntentTo(Context mContext, Class mClass) {
		Intent intent = new Intent(mContext, mClass);
		mContext.startActivity(intent);
	}

	public static void IntentToH(Context mContext, Class mClass, String name,
			int type) {
		Intent intent = new Intent(mContext, mClass);
		intent.putExtra("name", name);
		intent.putExtra("type", type);
		mContext.startActivity(intent);
	}
}
