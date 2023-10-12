
package com.kasa777.modal.static_data.walletcontact;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletContactData implements Parcelable
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
    public final static Parcelable.Creator<WalletContactData> CREATOR = new Creator<WalletContactData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WalletContactData createFromParcel(Parcel in) {
            return new WalletContactData(in);
        }

        public WalletContactData[] newArray(int size) {
            return (new WalletContactData[size]);
        }

    }
            ;

    protected WalletContactData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (com.kasa777.modal.static_data.walletcontact.Datum.class.getClassLoader()));
    }

    public WalletContactData() {
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
