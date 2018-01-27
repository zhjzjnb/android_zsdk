package com.zsdk.client.util;

import android.util.Log;

public class LogUtil {

	private final static String TAG = LogUtil.class.getSimpleName();

	public static void i(Object... list) {
		StringBuffer sb = new StringBuffer();
		for (Object str : list) {
			sb.append(str);
		}

		Log.i(TAG, sb.toString());
	}

}
