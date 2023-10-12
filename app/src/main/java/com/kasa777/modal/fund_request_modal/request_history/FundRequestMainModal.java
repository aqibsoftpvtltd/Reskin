package com.kasa777.modal.fund_request_modal.request_history;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundRequestMainModal implements Parcelable
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
    public final static Parcelable.Creator<FundRequestMainModal> CREATOR = new Creator<FundRequestMainModal>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FundRequestMainModal createFromParcel(Parcel in) {
            return new FundRequestMainModal(in);
        }

        public FundRequestMainModal[] newArray(int size) {
            return (new FundRequestMainModal[size]);
        }

    }
            ;

    protected FundRequestMainModal(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (com.kasa777.modal.fund_request_modal.request_history.Datum.class.getClassLoader()));
    }

    public FundRequestMainModal() {
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
