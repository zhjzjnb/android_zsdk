package com.zsdk.client.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	public static String getImei(Context context) {
		String imei = null;
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (phoneManager != null)
			imei = phoneManager.getDeviceId();
		if (imei == null)
			imei = "";
		return imei;
	}

	public static String getWritablePath() {
		File f = Environment.getExternalStoragePublicDirectory("zsdk");
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}
}
