package com.kasa77.modal.history_data_modal.jackpotresulthistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("providerId")
@Expose
private String providerId;
@SerializedName("providerName")
@Expose
private String providerName;
@SerializedName("resultDate")
@Expose
private String resultDate;
@SerializedName("winningDigit")
@Expose
private Integer winningDigit;
@SerializedName("status")
@Expose
private String status;
@SerializedName("createdAt")
@Expose
private String createdAt;
public final static Parcelable.Creator<Datum> CREATOR = new Creator<Datum>() {


@SuppressWarnings({
"unchecked"
})
public Datum createFromParcel(Parcel in) {
return new Datum(in);
}

public Datum[] newArray(int size) {
return (new Datum[size]);
}

}
;

protected Datum(Parcel in) {
this.id = ((String) in.readValue((String.class.getClassLoader())));
this.providerId = ((String) in.readValue((String.class.getClassLoader())));
this.providerName = ((String) in.readValue((String.class.getClassLoader())));
this.resultDate = ((String) in.readValue((String.class.getClassLoader())));
this.winningDigit = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.status = ((String) in.readValue((String.class.getClassLoader())));
this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
}

public Datum() {
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

public String getProviderName() {
return providerName;
}

public void setProviderName(String providerName) {
this.providerName = providerName;
}

public String getResultDate() {
return resultDate;
}

public void setResultDate(String resultDate) {
this.resultDate = resultDate;
}

public Integer getWinningDigit() {
return winningDigit;
}

public void setWinningDigit(Integer winningDigit) {
this.winningDigit = winningDigit;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(providerId);
dest.writeValue(providerName);
dest.writeValue(resultDate);
dest.writeValue(winningDigit);
dest.writeValue(status);
dest.writeValue(createdAt);
}

public int describeContents() {
return 0;
}

}