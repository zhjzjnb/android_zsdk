package com.zsdk.client.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.zsdk.client.Constant;
import com.zsdk.client.HttpCode;
import com.zsdk.client.ZSDK;
import com.zsdk.client.callback.LoginCallback;
import com.zsdk.client.callback.PostCallback;
import com.zsdk.client.model.AccountInfo;
import com.zsdk.client.util.DeviceUtil;
import com.zsdk.client.util.EncryptUtil;
import com.zsdk.client.util.HttpUtil;
import com.zsdk.client.util.LogUtil;
import com.zsdk.client.util.StringUtil;
import com.zsdk.client.util.ToastUtil;

public class AccountLogic {

	private static List<AccountInfo> accounts = new ArrayList<AccountInfo>();

	public static AccountInfo getLast() {
		int size = accounts.size();
		if (size > 0) {
			return accounts.get(size - 1);
		}
		return null;
	}
	
	public static int getAccountSize() {
		return accounts.size();
	}
	public static List<AccountInfo> getAccounts() {
		return accounts;
	}

	@SuppressWarnings("unchecked")
	public static void loadLocalAccount() {
		accounts.clear();

		String path = DeviceUtil.getWritablePath();
		File f = new File(path + File.separator + "accouont");
		if (f.exists()) {

			try {
				InputStream in = new FileInputStream(f);
				ObjectInputStream os = new ObjectInputStream(in);

				accounts = (List<AccountInfo>) os.readObject();

				os.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		LogUtil.i("accounts", accounts);
	}

	public static Map<String, String> getCommon() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("systemName", android.os.Build.DISPLAY);
		map.put("systemVersion", android.os.Build.VERSION.RELEASE);
		map.put("deviceModel", android.os.Build.MODEL);
		map.put("deviceName", android.os.Build.USER);
		map.put("appId", ZSDK.getInstance().getAppId());
		map.put("platform", "android");
		map.put("root", "0");

		return map;
	}

	public static void login(String username, String password,
			LoginCallback callback) {
		loginOrRegister("login.do", username, password, callback);
	}

	public static void registerAccount(String username, String password,
			LoginCallback callback) {
		loginOrRegister("register.do", username, password, callback);
	}

	private static AccountInfo findAccountByUid(int uid) {
		for (AccountInfo accountInfo : accounts) {
			if (accountInfo.uid == uid) {
				return accountInfo;
			}
		}

		return null;
	}

	public static void saveLocalAccount() {
		try {
			String path = DeviceUtil.getWritablePath();
			File f = new File(path + File.separator + "accouont");
			if (!f.exists()) {
				f.createNewFile();
			}

			Collections.sort(accounts, new Comparator<AccountInfo>() {
				@Override
				public int compare(AccountInfo o1, AccountInfo o2) {
					return (int) (o2.lastLoginTime - o1.lastLoginTime);
				}
			});

			OutputStream os = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(accounts);
			oos.close();
			os.close();
		} catch (Exception e) {
			//
			e.printStackTrace();
		}
	}

	private static void loginOrRegister(String path, final String username,
			final String password, final LoginCallback callback) {

		Map<String, String> map = getCommon();
		map.put("username", username);
		map.put("password", EncryptUtil.md5(password));
		map.put("sign", genSign(map));
		HttpUtil.post(path, map, new PostCallback() {

			@Override
			public void onCall(HttpCode err, JSONObject json, final String msg) {

				if (err == HttpCode.HttpCodeSuccess) {
					int uid = -1;
					String token = "";
					try {
						uid = json.getInt("uid");
						token = json.getString("token");
					} catch (JSONException e) {

						e.printStackTrace();
					}
					AccountInfo accountInfo = findAccountByUid(uid);
					if (accountInfo == null) {
						accountInfo = new AccountInfo();
						accounts.add(accountInfo);

					}
					accountInfo.uid = uid;
					accountInfo.password = password;
					accountInfo.username = username;
					accountInfo.lastLoginTime = System.currentTimeMillis() / 1000L;
					saveLocalAccount();

					final String ttkoen = token;
					final String uuid = "" + uid;

					ZSDK.getInstance().getActivity()
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.show(ZSDK.getInstance()
											.getActivity(), msg);
									callback.onCall(true, uuid, username,
											ttkoen);

								}
							});

				} else {
					ZSDK.getInstance().getActivity()
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.show(ZSDK.getInstance()
											.getActivity(), msg);
									callback.onCall(false, null, null, null);

								}
							});
				}

			}

		});
	}

	private static String genSign(Map<String, String> map) {

		List<String> keys = new ArrayList<String>(map.keySet());

		String content = StringUtil.getSignData(map, keys, "&", "=") + "&"
				+ Constant.CLIENT_SIGN_KEY;
		return EncryptUtil.md5(content);
	}

}
