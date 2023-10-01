package com.kasa77.modal.dashboard_gamelist;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;


public class DashBoardGames implements Parcelable {

	@SerializedName("result")
	public LinkedHashMap<String, Result> result;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;


	protected DashBoardGames(Parcel in) {
		message = in.readString();
		status = in.readInt();
	}

	public static final Creator<DashBoardGames> CREATOR = new Creator<DashBoardGames>() {
		@Override
		public DashBoardGames createFromParcel(Parcel in) {
			return new DashBoardGames(in);
		}

		@Override
		public DashBoardGames[] newArray(int size) {
			return new DashBoardGames[size];
		}
	};

	public LinkedHashMap<String, Result> getResult() {
		return result;
	}

	public void setResult(LinkedHashMap<String, Result>  result) {
		this.result = result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(message);
		dest.writeInt(status);
	}
}