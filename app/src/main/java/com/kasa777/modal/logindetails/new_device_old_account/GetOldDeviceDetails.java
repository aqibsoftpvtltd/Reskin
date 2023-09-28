package com.kasa777.modal.logindetails.new_device_old_account;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class GetOldDeviceDetails implements Parcelable {

	@SerializedName("data")
	public Data data;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;


	protected GetOldDeviceDetails(Parcel in) {
		data = in.readParcelable(Data.class.getClassLoader());
		message = in.readString();
		status = in.readInt();
	}

	public static final Creator<GetOldDeviceDetails> CREATOR = new Creator<GetOldDeviceDetails>() {
		@Override
		public GetOldDeviceDetails createFromParcel(Parcel in) {
			return new GetOldDeviceDetails(in);
		}

		@Override
		public GetOldDeviceDetails[] newArray(int size) {
			return new GetOldDeviceDetails[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(data, flags);
		dest.writeString(message);
		dest.writeInt(status);
	}
}