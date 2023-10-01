package com.kasa77.modal.history_data_modal.debithistory.credithistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("userId")
@Expose
private String userId;
@SerializedName("reqAmount")
@Expose
private Integer reqAmount;
@SerializedName("fullname")
@Expose
private String fullname;
@SerializedName("username")
@Expose
private String username;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("reqType")
@Expose
private String reqType;
@SerializedName("reqStatus")
@Expose
private String reqStatus;
@SerializedName("reqDate")
@Expose
private String reqDate;
@SerializedName("reqTime")
@Expose
private String reqTime;
@SerializedName("withdrawalMode")
@Expose
private String withdrawalMode;
@SerializedName("UpdatedBy")
@Expose
private String updatedBy;
@SerializedName("reqUpdatedAt")
@Expose
private String reqUpdatedAt;
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
this.userId = ((String) in.readValue((String.class.getClassLoader())));
this.reqAmount = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.fullname = ((String) in.readValue((String.class.getClassLoader())));
this.username = ((String) in.readValue((String.class.getClassLoader())));
this.mobile = ((String) in.readValue((String.class.getClassLoader())));
this.reqType = ((String) in.readValue((String.class.getClassLoader())));
this.reqStatus = ((String) in.readValue((String.class.getClassLoader())));
this.reqDate = ((String) in.readValue((String.class.getClassLoader())));
this.reqTime = ((String) in.readValue((String.class.getClassLoader())));
this.withdrawalMode = ((String) in.readValue((String.class.getClassLoader())));
this.updatedBy = ((String) in.readValue((String.class.getClassLoader())));
this.reqUpdatedAt = ((String) in.readValue((String.class.getClassLoader())));
}

public Datum() {
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public Integer getReqAmount() {
return reqAmount;
}

public void setReqAmount(Integer reqAmount) {
this.reqAmount = reqAmount;
}

public String getFullname() {
return fullname;
}

public void setFullname(String fullname) {
this.fullname = fullname;
}

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getReqType() {
return reqType;
}

public void setReqType(String reqType) {
this.reqType = reqType;
}

public String getReqStatus() {
return reqStatus;
}

public void setReqStatus(String reqStatus) {
this.reqStatus = reqStatus;
}

public String getReqDate() {
return reqDate;
}

public void setReqDate(String reqDate) {
this.reqDate = reqDate;
}

public String getReqTime() {
return reqTime;
}

public void setReqTime(String reqTime) {
this.reqTime = reqTime;
}

public String getWithdrawalMode() {
return withdrawalMode;
}

public void setWithdrawalMode(String withdrawalMode) {
this.withdrawalMode = withdrawalMode;
}

public String getUpdatedBy() {
return updatedBy;
}

public void setUpdatedBy(String updatedBy) {
this.updatedBy = updatedBy;
}

public String getReqUpdatedAt() {
return reqUpdatedAt;
}

public void setReqUpdatedAt(String reqUpdatedAt) {
this.reqUpdatedAt = reqUpdatedAt;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(userId);
dest.writeValue(reqAmount);
dest.writeValue(fullname);
dest.writeValue(username);
dest.writeValue(mobile);
dest.writeValue(reqType);
dest.writeValue(reqStatus);
dest.writeValue(reqDate);
dest.writeValue(reqTime);
dest.writeValue(withdrawalMode);
dest.writeValue(updatedBy);
dest.writeValue(reqUpdatedAt);
}

public int describeContents() {
return 0;
}

}
