package com.zsdk.client.callback;

public interface LoginCallback {
	void onCall(boolean success, String uid, String uname, String token);
}
