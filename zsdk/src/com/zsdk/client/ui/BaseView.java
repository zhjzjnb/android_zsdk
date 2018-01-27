package com.zsdk.client.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.zsdk.client.Constant;
import com.zsdk.client.util.ResourceUtil;
import com.zsdk.client.util.StringUtil;
import com.zsdk.client.util.ToastUtil;

public abstract class BaseView extends AlertDialog {
	protected Context _context;
	private LoadingDialog waitDialog;

	protected BaseView(Context context) {

		super(context);

		_context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(getLayoutId());

		setCancelable(false);
		setCanceledOnTouchOutside(false);
		getWindow().setDimAmount(0.1f);
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		init();
	}

	public abstract int getLayoutId();

	public abstract void init();

	protected void showToast(String msg) {
		ToastUtil.show(_context, msg);
	}

	public boolean checkInputInfo(String account, String password) {

		if (!StringUtil.checkInput(account)
				|| account.length() < Constant.INPUT_MIN_LEN
				|| account.length() > Constant.INPUT_MAX_LEN) {
			showToast(String.format("用户名长度为%d~%d字母和数字", Constant.INPUT_MIN_LEN,
					Constant.INPUT_MAX_LEN));
			return false;
		}
		if (!StringUtil.checkInput(password) || password == null
				|| password.length() < Constant.INPUT_MIN_LEN
				|| password.length() > Constant.INPUT_MAX_LEN) {
			showToast(String.format("密码长度为%d~%d字母和数字", Constant.INPUT_MIN_LEN,
					Constant.INPUT_MAX_LEN));
			return false;
		}
		return true;
	}

	protected void showWait() {
		if (waitDialog == null) {

			waitDialog = new LoadingDialog(_context,
					ResourceUtil.getIdentifier(_context,
							"R.style.sdk_wait_dialog"));
		}
		waitDialog.show();

	}

	protected void hideWait() {
		if (waitDialog != null) {
			waitDialog.dismiss();
		}
	}

}
