package com.zsdk.client.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.zsdk.client.adapt.AccountSelectAdapter;
import com.zsdk.client.callback.LoginCallback;
import com.zsdk.client.logic.AccountLogic;
import com.zsdk.client.model.AccountInfo;
import com.zsdk.client.util.ResourceUtil;

public class LoginView extends BaseView implements
		android.view.View.OnClickListener {
	private EditText etAccount;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnRegister;
	private ImageView ivClose;
	private ImageView ivDrop;
	private LoginCallback loginCallback;
	private PopupWindow popupWindow;
	private AccountInfo accountInfo;

	public LoginView(Context context, LoginCallback loginCallback) {
		super(context);
		this.loginCallback = loginCallback;
	}

	@Override
	public void init() {
		ivClose = (ImageView) findViewById(ResourceUtil.getIdentifier(_context,
				"R.id.iv_close"));
		ivClose.setOnClickListener(this);

		ivDrop = (ImageView) findViewById(ResourceUtil.getIdentifier(_context,
				"R.id.iv_drop"));
		ivDrop.setOnClickListener(this);

		btnLogin = (Button) findViewById(ResourceUtil.getIdentifier(_context,
				"R.id.btn_login"));
		btnLogin.setOnClickListener(this);

		btnRegister = (Button) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.btn_register"));
		btnRegister.setOnClickListener(this);

		etAccount = (EditText) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.et_account"));

		etPassword = (EditText) findViewById(ResourceUtil.getIdentifier(
				_context, "R.id.et_password"));

		update(AccountLogic.getLast());
	}

	public void update(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
		if (accountInfo != null) {
			etAccount.setText(accountInfo.username);
			etPassword.setText(accountInfo.password);
		} else {
			etAccount.setText("");
			etPassword.setText("");
		}
	}

	public void deleteAccount(AccountInfo accountInfo) {
		if (this.accountInfo != null && this.accountInfo == accountInfo) {
			update(AccountLogic.getLast());
		}

	}

	public void hideDrop() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	private void showDrop() {
		if (popupWindow == null) {

			View view = LayoutInflater.from(_context).inflate(
					ResourceUtil.getIdentifier(_context,
							"R.layout.sdk_account_list_view"), null);

			ListView listView = (ListView) view.findViewById(ResourceUtil
					.getIdentifier(_context, "R.id.listView"));
			AccountSelectAdapter accountSelectAdapter = new AccountSelectAdapter(
					_context, this);
			listView.setAdapter(accountSelectAdapter);

			listView.setOnItemClickListener(accountSelectAdapter);

			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			popupWindow.setOutsideTouchable(false);
			popupWindow.setFocusable(true);
		}

		popupWindow.showAsDropDown(etAccount);

	}

	@Override
	public void onClick(View view) {
		if (view == ivClose) {
			dismiss();
			this.loginCallback.onCall(false, null, null, null);
		} else if (view == ivDrop) {
			showDrop();

		} else if (view == btnLogin) {
			String account = etAccount.getText().toString();
			String password = etPassword.getText().toString();

			if (checkInputInfo(account, password)) {
				showWait();
				AccountLogic.login(account, password, new LoginCallback() {

					@Override
					public void onCall(boolean success, String uid,
							String uname, String token) {
						hideWait();
						LoginView.this.loginCallback.onCall(success, uid,
								uname, token);
						if (success) {
							dismiss();
						}

					}
				});

			}

		} else if (view == btnRegister) {
			dismiss();
			new RegisterView(_context, loginCallback).show();
		}

	}

	@Override
	public int getLayoutId() {
		return ResourceUtil.getIdentifier(_context, "R.layout.sdk_view_login");
	}

}
