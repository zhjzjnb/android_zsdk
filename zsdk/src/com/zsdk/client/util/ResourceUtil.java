package com.zsdk.client.util;

import android.content.Context;

public class ResourceUtil {
	public static int getIdentifier(Context context, String paramString) {
		if (paramString != null) {
			String[] splits = paramString.split("\\.", 3);
			if (splits.length == 3) {
				return context.getResources().getIdentifier(splits[2],
						splits[1], context.getPackageName());
			}
		}

		return 0;
	}
}
