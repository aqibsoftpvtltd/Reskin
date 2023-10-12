package com.kasa777.ui.fragment.jackpot_fragments.modal;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;


public class JackpotGames implements Parcelable {

    @SerializedName("result")
    public LinkedHashMap<String, Result> result;

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public int status;


    protected JackpotGames(Parcel in) {
        message = in.readString();
        status = in.readInt();
    }

    public static final Creator<JackpotGames> CREATOR = new Creator<JackpotGames>() {
        @Override
        public JackpotGames createFromParcel(Parcel in) {
            return new JackpotGames(in);
        }

        @Override
        public JackpotGames[] newArray(int size) {
            return new JackpotGames[size];
        }
    };

    public LinkedHashMap<String, Result> getResult() {
        return result;
    }

    public void setResult(LinkedHashMap<String, Result> result) {
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