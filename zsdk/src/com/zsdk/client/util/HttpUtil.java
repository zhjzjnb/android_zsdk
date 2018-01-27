package com.zsdk.client.util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.zsdk.client.HttpCode;
import com.zsdk.client.callback.PostCallback;

public class HttpUtil {

	private final static String HOST = "http://192.168.1.103:8081/sdkserver/";

	public static void post(String path, Map<String, String> map,
			final PostCallback postCallback) {
		if (postCallback == null) {
			return;
		}
		JSONObject json = new JSONObject(map);

		RequestBody body = RequestBody.create(
				MediaType.parse("application/json; charset=utf-8"),
				json.toString());

		OkHttpClient okHttpClient = new OkHttpClient();

		Request request = new Request.Builder().url(HOST + path).post(body)
				.build();

		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();

				LogUtil.i("onFailure:", e.getMessage(), "Thread:"
						+ Thread.currentThread().getId());

				postCallback.onCall(HttpCode.HttpCodeNetError, null,
						"网络出错，请稍后重试");

			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				String result = response.body().string();

				LogUtil.i("onResponse:", result, "Thread:"
						+ Thread.currentThread().getId());

				try {
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					String msg = json.getString("msg");
					if (code == 0) {
						postCallback.onCall(HttpCode.HttpCodeSuccess,
								json.getJSONObject("data"), msg);
					} else {
						postCallback.onCall(HttpCode.HttpCodeLogicError, null,
								msg);
					}
				} catch (JSONException e) {

					e.printStackTrace();

					postCallback.onCall(HttpCode.HttpCodeDataError, null,
							"数据出错，请稍后重试");
				}

			}

		});

	}
}
