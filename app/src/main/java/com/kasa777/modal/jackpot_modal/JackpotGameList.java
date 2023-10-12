package com.kasa777.modal.jackpot_modal;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JackpotGameList implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("provider")
    @Expose
    private List<Provider> provider = new ArrayList<>();
    @SerializedName("OpenClose_time")
    @Expose
    private List<Object> openCloseTime = null;
    public final static Parcelable.Creator<JackpotGameList> CREATOR = new Creator<JackpotGameList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public JackpotGameList createFromParcel(Parcel in) {
            return new JackpotGameList(in);
        }

        public JackpotGameList[] newArray(int size) {
            return (new JackpotGameList[size]);
        }

    }
            ;

    protected JackpotGameList(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.provider, (com.kasa777.modal.jackpot_modal.Provider.class.getClassLoader()));
        in.readList(this.openCloseTime, (java.lang.Object.class.getClassLoader()));
    }

    public JackpotGameList() {
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

    public List<Provider> getProvider() {
        return provider;
    }

    public void setProvider(List<Provider> provider) {
        this.provider = provider;
    }

    public List<Object> getOpenCloseTime() {
        return openCloseTime;
    }

    public void setOpenCloseTime(List<Object> openCloseTime) {
        this.openCloseTime = openCloseTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(provider);
        dest.writeList(openCloseTime);
    }

    public int describeContents() {
        return 0;
    }

}