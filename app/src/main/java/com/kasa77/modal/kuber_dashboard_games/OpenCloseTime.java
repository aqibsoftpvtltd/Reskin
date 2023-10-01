package com.kasa77.modal.kuber_dashboard_games;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenCloseTime implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("providerId")
    @Expose
    private String providerId;
    @SerializedName("gameDay")
    @Expose
    private String gameDay;
    @SerializedName("OBT")
    @Expose
    private String oBT;
    @SerializedName("CBT")
    @Expose
    private String cBT;
    @SerializedName("OBRT")
    @Expose
    private String oBRT;
    @SerializedName("CBRT")
    @Expose
    private String cBRT;
    @SerializedName("isClosed")
    @Expose
    private String isClosed;
    @SerializedName("modifiedAt")
    @Expose
    private String modifiedAt;



    public final static Parcelable.Creator<OpenCloseTime> CREATOR = new Creator<OpenCloseTime>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OpenCloseTime createFromParcel(Parcel in) {
            return new OpenCloseTime(in);
        }

        public OpenCloseTime[] newArray(int size) {
            return (new OpenCloseTime[size]);
        }

    };

    protected OpenCloseTime(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.providerId = ((String) in.readValue((String.class.getClassLoader())));
        this.gameDay = ((String) in.readValue((String.class.getClassLoader())));
        this.oBT = ((String) in.readValue((String.class.getClassLoader())));
        this.cBT = ((String) in.readValue((String.class.getClassLoader())));
        this.oBRT = ((String) in.readValue((String.class.getClassLoader())));
        this.cBRT = ((String) in.readValue((String.class.getClassLoader())));
        this.isClosed = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedAt = ((String) in.readValue((String.class.getClassLoader())));

    }





    public OpenCloseTime() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getGameDay() {
        return gameDay;
    }

    public void setGameDay(String gameDay) {
        this.gameDay = gameDay;
    }

    public String getOBT() {
        return oBT;
    }

    public void setOBT(String oBT) {
        this.oBT = oBT;
    }

    public String getCBT() {
        return cBT;
    }

    public void setCBT(String cBT) {
        this.cBT = cBT;
    }

    public String getOBRT() {
        return oBRT;
    }

    public void setOBRT(String oBRT) {
        this.oBRT = oBRT;
    }

    public String getCBRT() {
        return cBRT;
    }

    public void setCBRT(String cBRT) {
        this.cBRT = cBRT;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(providerId);
        dest.writeValue(gameDay);
        dest.writeValue(oBT);
        dest.writeValue(cBT);
        dest.writeValue(oBRT);
        dest.writeValue(cBRT);
        dest.writeValue(isClosed);
        dest.writeValue(modifiedAt);

    }

    public int describeContents() {
        return 0;
    }

}