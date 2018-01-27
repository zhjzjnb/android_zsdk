package com.zsdk.client;

import android.app.Activity;

import com.zsdk.client.callback.LoginCallback;
import com.zsdk.client.logic.AccountLogic;
import com.zsdk.client.ui.LoginView;
import com.zsdk.client.util.DeviceUtil;
import com.zsdk.client.util.LogUtil;
import com.zsdk.client.util.ToastUtil;

public class ZSDK {
	private final static ZSDK _instance = new ZSDK();

	private static Activity _activity;
	private static String _appId;

	public static ZSDK getInstance() {
		return _instance;
	}

	public Activity getActivity() {
		return _activity;
	}

	public String getAppId() {
		return _appId;
	}

	private ZSDK() {
	}

	public void init(Activity activity, String appId) {
		_activity = activity;
		_appId = appId;

		LogUtil.i("path", DeviceUtil.getWritablePath());
		AccountLogic.loadLocalAccount();
	}

	public void login(Activity activity, LoginCallback loginCallback) {
		if (loginCallback == null) {
			ToastUtil.show(activity, "参数错误");
			return;
		}
		new LoginView(activity, loginCallback).show();

	}

}
