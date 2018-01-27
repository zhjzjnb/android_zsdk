package com.zsdk.client.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zsdk.client.callback.LoginCallback;
import com.zsdk.client.logic.AccountLogic;
import com.zsdk.client.util.ResourceUtil;

public class RegisterView extends BaseView implements
		android.view.View.OnClickListener {
	private EditText etAccount;
	private EditText etPassword;
	private EditText etPasswordConfirm;

	private Button btnRegister;
	private ImageView ivClose;
	private LoginCallback loginCallback;

	public RegisterView(Context context, LoginCallback loginCallback) {
		super(context);
		this.loginCallback = loginCallback;
	}

	@Override
	public void init() {
		ivClose = (ImageView) findViewById(ResourceUtil.getIdentifier(_context,
				"R.id.iv_close"));
		ivClose.setOnClickListener(this);

		btnRegister = (Button) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.btn_register"));
		btnRegister.setOnClickListener(this);

		etAccount = (EditText) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.et_account"));

		etPassword = (EditText) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.et_password"));
		etPasswordConfirm = (EditText) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.et_password_confirm"));

	}

	@Override
	public void onClick(View view) {
		if (view == ivClose) {
			dismiss();
			new LoginView(_context, loginCallback).show();
		} else if (view == btnRegister) {
			String account = etAccount.getText().toString();
			String password = etPassword.getText().toString();
			String password2 = etPasswordConfirm.getText().toString();

			if (checkInputInfo(account, password)) {
				if (!password2.equals(password)) {
					showToast("请输入相同的密码");
					return;
				}
				showWait();
				AccountLogic.registerAccount(account, password,
						new LoginCallback() {

							@Override
							public void onCall(boolean success, String uid,
									String uname, String token) {
								hideWait();
								RegisterView.this.loginCallback.onCall(success,
										uid, uname, token);
								if (success) {
									dismiss();
								}

							}
						});

			}

		}

	}

	@Override
	public int getLayoutId() {
		return ResourceUtil.getIdentifier(_context,
				"R.layout.sdk_view_register");
	}

}
