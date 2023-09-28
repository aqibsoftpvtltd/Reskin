package com.kasa777.modal.logindetails.new_device_old_account;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RequestNewDevice{

	@SerializedName("firebaseToken")
	public String firebaseToken;

	@SerializedName("mobileNumber")
	public String mobileNumber;

	@SerializedName("OTP")
	public String oTP;

	@SerializedName("userId")
	public String userId;

	@SerializedName("OldDeviceName")
	public String oldDeviceName;

	@SerializedName("deviceId")
	public String deviceId;

	@SerializedName("deviceName")
	public String deviceName;

	@SerializedName("OlddeviceId")
	public String olddeviceId;

	@SerializedName("changeDetails")
	public List<ChangeDetailsItem> changeDetails;
}