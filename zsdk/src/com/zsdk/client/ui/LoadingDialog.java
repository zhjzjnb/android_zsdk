package com.zsdk.client.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.zsdk.client.util.ResourceUtil;

public class LoadingDialog extends Dialog {
	protected Context _context;



	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this._context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(ResourceUtil.getIdentifier(_context,
				"R.layout.sdk_view_wait"));
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		
	}

}
