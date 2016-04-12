package com.chunsoft.ttgo.util;

import android.content.Context;
import android.content.Intent;

public class IntentUti {
	public static void IntentTo(Context mContext, Class mClass) {
		Intent intent = new Intent(mContext, mClass);
		mContext.startActivity(intent);
	}
}
