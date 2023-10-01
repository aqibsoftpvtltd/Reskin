package com.kasa77.modal.kuber_dashboard_games;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("providerName")
@Expose
private String providerName;
@SerializedName("providerResult")
@Expose
private String providerResult;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;
@SerializedName("resultStatus")
@Expose
private Integer resultStatus;
public final static Parcelable.Creator<Provider> CREATOR = new Creator<Provider>() {


@SuppressWarnings({
"unchecked"
})
public Provider createFromParcel(Parcel in) {
return new Provider(in);
}

public Provider[] newArray(int size) {
return (new Provider[size]);
}

}
;

protected Provider(Parcel in) {
this.id = ((String) in.readValue((String.class.getClassLoader())));
this.providerName = ((String) in.readValue((String.class.getClassLoader())));
this.providerResult = ((String) in.readValue((String.class.getClassLoader())));
this.modifiedAt = ((String) in.readValue((String.class.getClassLoader())));
this.resultStatus = ((Integer) in.readValue((Integer.class.getClassLoader())));
}

public Provider() {
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getProviderName() {
return providerName;
}

public void setProviderName(String providerName) {
this.providerName = providerName;
}

public String getProviderResult() {
return providerResult;
}

public void setProviderResult(String providerResult) {
this.providerResult = providerResult;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

public Integer getResultStatus() {
return resultStatus;
}

public void setResultStatus(Integer resultStatus) {
this.resultStatus = resultStatus;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(providerName);
dest.writeValue(providerResult);
dest.writeValue(modifiedAt);
dest.writeValue(resultStatus);
}

public int describeContents() {
return 0;
}

}
