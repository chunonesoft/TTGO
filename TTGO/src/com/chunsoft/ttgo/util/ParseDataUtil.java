package com.chunsoft.ttgo.util;

import android.util.Log;

public class ParseDataUtil {
	public static int stringNumbers(String str) {
		// int counter = 1;
		// if (str.indexOf(",") == -1) {
		// return 1;
		// } else if (str.indexOf(",") != -1) {
		// counter++;
		// stringNumbers(str.substring(str.indexOf(",") + 1));
		// return counter;
		// }
		// return 1;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ',') {
				count++;
			}
		}
		return count + 1;
	}

	public static String[] getPicPath(String str) {
		String[] strs = new String[stringNumbers(str)];
		Log.e("[stringNumbers(str)]", stringNumbers(str) + "");
		int start = 0;
		int end = str.indexOf(",");
		if (strs.length == 1) {
			strs[0] = str;
			return strs;
		} else {
			for (int i = 0; i < strs.length; i++) {
				if (i != strs.length - 1) {
					strs[i] = str.substring(start, end);
					str = str.substring(end + 1);
					end = str.indexOf(",");
				} else {
					strs[i] = str;
				}
			}
			return strs;
		}
	}
}
