package com.zsdk.client.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsdk.client.logic.AccountLogic;
import com.zsdk.client.model.AccountInfo;
import com.zsdk.client.ui.LoginView;
import com.zsdk.client.util.ResourceUtil;

public class AccountSelectAdapter extends BaseAdapter implements
		OnClickListener, OnItemClickListener {

	private Context context;
	private LoginView loginView;

	public AccountSelectAdapter(Context context, LoginView loginView) {
		this.context = context;
		this.loginView = loginView;
	}

	@Override
	public int getCount() {
		return AccountLogic.getAccountSize();
	}

	@Override
	public Object getItem(int position) {
		return AccountLogic.getAccounts().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					ResourceUtil.getIdentifier(context,
							"R.layout.sdk_account_list_item"), null);
			vh.account = (TextView) convertView.findViewById(ResourceUtil
					.getIdentifier(context, "R.id.tx_account"));
			vh.delete = (ImageView) convertView.findViewById(ResourceUtil
					.getIdentifier(context, "R.id.iv_del"));
			vh.delete.setTag(position);
			vh.delete.setOnClickListener(this);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.account.setText(AccountLogic.getAccounts().get(position).username);
		return convertView;
	}

	@Override
	public void onClick(View view) {
		int position = (Integer) view.getTag();
		AccountInfo accountInfo = AccountLogic.getAccounts().remove(position);
		this.notifyDataSetChanged();
		loginView.deleteAccount(accountInfo);
		AccountLogic.saveLocalAccount();

	}

	static class ViewHolder {
		TextView account;
		ImageView delete;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		loginView.update((AccountLogic.getAccounts().get(position)));
		loginView.hideDrop();
	}
}