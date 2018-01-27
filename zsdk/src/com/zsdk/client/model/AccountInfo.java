package com.zsdk.client.model;

import java.io.Serializable;

public class AccountInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6357019483578581915L;
	public int uid;
	public String username;
	public String password;
	public long lastLoginTime;

	@Override
	public String toString() {
		return "uid:" + uid + " username:" + username + " password:" + password
				+ " lastLoginTime:" + lastLoginTime;
	}

}
