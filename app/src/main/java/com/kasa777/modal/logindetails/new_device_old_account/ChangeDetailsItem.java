package com.kasa777.modal.logindetails.new_device_old_account;

import com.google.gson.annotations.SerializedName;


public class ChangeDetailsItem{

	@SerializedName("OldDeviceName")
	public String oldDeviceName;

	@SerializedName("OlddeviceId")
	public String olddeviceId;

	@SerializedName("changeOn")
	public String changeOn;
}