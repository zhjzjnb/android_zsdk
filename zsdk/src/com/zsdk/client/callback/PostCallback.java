package com.zsdk.client.callback;

import org.json.JSONObject;

import com.zsdk.client.HttpCode;

public interface PostCallback {
	void onCall(HttpCode err, JSONObject json, String msg);

}
