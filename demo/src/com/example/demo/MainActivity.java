package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.zsdk.client.ZSDK;
import com.zsdk.client.callback.LoginCallback;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ZSDK.getInstance().init(this, "1");

		findViewById(R.id.but1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ZSDK.getInstance().login(MainActivity.this,
						new LoginCallback() {

							@Override
							public void onCall(boolean success, String uid,
									String uname, String token) {
								Log.i("xxx ", "success:" + success);

								if (success) {
									Log.i("xxx ", "uid:" + uid + ":" + uname
											+ ":" + token);
								}

							}
						});

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
