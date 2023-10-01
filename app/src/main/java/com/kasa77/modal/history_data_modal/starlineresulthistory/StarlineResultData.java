package com.kasa77.modal.history_data_modal.starlineresulthistory;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StarlineResultData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public final static Parcelable.Creator<StarlineResultData> CREATOR = new Creator<StarlineResultData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public StarlineResultData createFromParcel(Parcel in) {
            return new StarlineResultData(in);
        }

        public StarlineResultData[] newArray(int size) {
            return (new StarlineResultData[size]);
        }

    }
            ;

    protected StarlineResultData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (com.kasa77.modal.history_data_modal.starlineresulthistory.Datum.class.getClassLoader()));
    }

    public StarlineResultData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}