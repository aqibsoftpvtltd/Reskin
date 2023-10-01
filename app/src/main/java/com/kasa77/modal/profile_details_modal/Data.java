package com.kasa77.modal.profile_details_modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("userId")
@Expose
private String userId;
@SerializedName("address")
@Expose
private String address;
@SerializedName("city")
@Expose
private String city;
@SerializedName("pincode")
@Expose
private String pincode;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("paytm_number")
@Expose
private String paytmNumber;
@SerializedName("phonePe_number")
@Expose
private String phonePeNumber;
@SerializedName("tez_number")
@Expose
private String tezNumber;
@SerializedName("account_holder_name")
@Expose
private String accountHolderName;
@SerializedName("account_no")
@Expose
private String accountNo;
@SerializedName("bank_name")
@Expose
private String bankName;
@SerializedName("ifsc_code")
@Expose
private String ifscCode;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


@SuppressWarnings({
"unchecked"
})
public Data createFromParcel(Parcel in) {
return new Data(in);
}

public Data[] newArray(int size) {
return (new Data[size]);
}

}
;

protected Data(Parcel in) {
this.id = ((String) in.readValue((String.class.getClassLoader())));
this.userId = ((String) in.readValue((String.class.getClassLoader())));
this.address = ((String) in.readValue((String.class.getClassLoader())));
this.city = ((String) in.readValue((String.class.getClassLoader())));
this.pincode = ((String) in.readValue((String.class.getClassLoader())));
this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
this.paytmNumber = ((String) in.readValue((String.class.getClassLoader())));
this.phonePeNumber = ((String) in.readValue((String.class.getClassLoader())));
this.tezNumber = ((String) in.readValue((String.class.getClassLoader())));
this.accountHolderName = ((String) in.readValue((String.class.getClassLoader())));
this.accountNo = ((String) in.readValue((String.class.getClassLoader())));
this.bankName = ((String) in.readValue((String.class.getClassLoader())));
this.ifscCode = ((String) in.readValue((String.class.getClassLoader())));
this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
}

public Data() {
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

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getPincode() {
return pincode;
}

public void setPincode(String pincode) {
this.pincode = pincode;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getPaytmNumber() {
return paytmNumber;
}

public void setPaytmNumber(String paytmNumber) {
this.paytmNumber = paytmNumber;
}

public String getPhonePeNumber() {
return phonePeNumber;
}

public void setPhonePeNumber(String phonePeNumber) {
this.phonePeNumber = phonePeNumber;
}

public String getTezNumber() {
return tezNumber;
}

public void setTezNumber(String tezNumber) {
this.tezNumber = tezNumber;
}

public String getAccountHolderName() {
return accountHolderName;
}

public void setAccountHolderName(String accountHolderName) {
this.accountHolderName = accountHolderName;
}

public String getAccountNo() {
return accountNo;
}

public void setAccountNo(String accountNo) {
this.accountNo = accountNo;
}

public String getBankName() {
return bankName;
}

public void setBankName(String bankName) {
this.bankName = bankName;
}

public String getIfscCode() {
return ifscCode;
}

public void setIfscCode(String ifscCode) {
this.ifscCode = ifscCode;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(userId);
dest.writeValue(address);
dest.writeValue(city);
dest.writeValue(pincode);
dest.writeValue(createdAt);
dest.writeValue(paytmNumber);
dest.writeValue(phonePeNumber);
dest.writeValue(tezNumber);
dest.writeValue(accountHolderName);
dest.writeValue(accountNo);
dest.writeValue(bankName);
dest.writeValue(ifscCode);
dest.writeValue(updatedAt);
}

public int describeContents() {
return 0;
}

}
