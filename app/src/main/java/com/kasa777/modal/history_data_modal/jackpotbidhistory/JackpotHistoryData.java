package com.kasa777.modal.history_data_modal.jackpotbidhistory;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JackpotHistoryData implements Parcelable
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
    public final static Parcelable.Creator<JackpotHistoryData> CREATOR = new Creator<JackpotHistoryData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public JackpotHistoryData createFromParcel(Parcel in) {
            return new JackpotHistoryData(in);
        }

        public JackpotHistoryData[] newArray(int size) {
            return (new JackpotHistoryData[size]);
        }

    }
            ;

    protected JackpotHistoryData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (com.kasa777.modal.history_data_modal.jackpotbidhistory.Datum.class.getClassLoader()));
    }

    public JackpotHistoryData() {
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
