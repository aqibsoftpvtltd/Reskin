package com.kasa777.modal.logindetails.new_device_old_account;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class Data implements Parcelable {



	@SerializedName("newDeviceId")
	public String newDeviceId;

	@SerializedName("mobileNumber")
	public String mobileNumber;

	@SerializedName("userId")
	public String userId;

	@SerializedName("token")
	public String token;

	@SerializedName("userName")
	public String userName;

	@SerializedName("oldDeviceName")
	public String oldDeviceName;

	@SerializedName("oldDeviceId")
	public String oldDeviceId;

	@SerializedName("changeDetails")
	public ArrayList<ChangeDetailsItem> changeDetails = new ArrayList<>();


	protected Data(Parcel in) {
		newDeviceId = in.readString();
		mobileNumber = in.readString();
		userId = in.readString();
		token = in.readString();
		oldDeviceName = in.readString();
		oldDeviceId = in.readString();
		userName = in.readString();
	}

	public static final Creator<Data> CREATOR = new Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(newDeviceId);
		dest.writeString(mobileNumber);
		dest.writeString(userId);
		dest.writeString(token);
		dest.writeString(oldDeviceName);
		dest.writeString(oldDeviceId);
		dest.writeString(userName);
	}
}