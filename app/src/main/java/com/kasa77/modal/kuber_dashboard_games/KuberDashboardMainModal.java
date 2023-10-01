package com.kasa77.modal.kuber_dashboard_games;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KuberDashboardMainModal implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("provider")
    @Expose
    private List<Provider> provider = null;
    @SerializedName("OpenClose_time")
    @Expose
    private List<OpenCloseTime> openCloseTime = null;
    public final static Parcelable.Creator<KuberDashboardMainModal> CREATOR = new Creator<KuberDashboardMainModal>() {


        @SuppressWarnings({
                "unchecked"
        })
        public KuberDashboardMainModal createFromParcel(Parcel in) {
            return new KuberDashboardMainModal(in);
        }

        public KuberDashboardMainModal[] newArray(int size) {
            return (new KuberDashboardMainModal[size]);
        }

    }
            ;

    protected KuberDashboardMainModal(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.provider, (com.kasa77.modal.kuber_dashboard_games.Provider.class.getClassLoader()));
        in.readList(this.openCloseTime, (com.kasa77.modal.kuber_dashboard_games.OpenCloseTime.class.getClassLoader()));
    }

    public KuberDashboardMainModal() {
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

    public List<OpenCloseTime> getOpenCloseTime() {
        return openCloseTime;
    }

    public void setOpenCloseTime(List<OpenCloseTime> openCloseTime) {
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