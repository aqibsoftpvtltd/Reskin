package com.kasa77.ui.fragment.jackpot_fragments.modal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Result implements Parcelable {

	@SerializedName("resultStatus")
	public int resultStatus;

	@SerializedName("displayText")
	public String displayText;

	@SerializedName("providerResult")
	public String providerResult;

	@SerializedName("isClosed")
	public String isClosed;

	@SerializedName("CloseBidTime")
	public String closeBidTime;

	@SerializedName("OpenBidTime")
	public String openBidTime;

	@SerializedName("providerId")
	public String providerId;

	@SerializedName("gameDay")
	public String gameDay;

	@SerializedName("gameDate")
	public String gameDate;

	@SerializedName("providerName")
	public String providerName;

	@SerializedName("colorCode")
	public String colorCode;



	protected Result(Parcel in) {
		resultStatus = in.readInt();
		displayText = in.readString();
		providerResult = in.readString();
		isClosed = in.readString();
		closeBidTime = in.readString();
		openBidTime = in.readString();
		providerId = in.readString();
		gameDay = in.readString();
		providerName = in.readString();
		gameDate = in.readString();
		colorCode = in.readString();
	}

	public static final Creator<Result> CREATOR = new Creator<Result>() {
		@Override
		public Result createFromParcel(Parcel in) {
			return new Result(in);
		}

		@Override
		public Result[] newArray(int size) {
			return new Result[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(resultStatus);
		dest.writeString(displayText);
		dest.writeString(providerResult);
		dest.writeString(isClosed);
		dest.writeString(closeBidTime);
		dest.writeString(openBidTime);
		dest.writeString(providerId);
		dest.writeString(gameDay);
		dest.writeString(providerName);
		dest.writeString(colorCode);
		dest.writeString(gameDate);
	}
}