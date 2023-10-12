package com.kasa777.ui.fragment.startline_game_fragment.modal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;


public class StartLineTimeGames implements Parcelable {

	@SerializedName("result")
	public LinkedHashMap<String, Result> result;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;


	protected StartLineTimeGames(Parcel in) {
		message = in.readString();
		status = in.readInt();
	}

	public static final Creator<StartLineTimeGames> CREATOR = new Creator<StartLineTimeGames>() {
		@Override
		public StartLineTimeGames createFromParcel(Parcel in) {
			return new StartLineTimeGames(in);
		}

		@Override
		public StartLineTimeGames[] newArray(int size) {
			return new StartLineTimeGames[size];
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