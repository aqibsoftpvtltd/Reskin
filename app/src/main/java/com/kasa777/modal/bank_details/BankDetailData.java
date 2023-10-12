
package com.kasa777.modal.bank_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankDetailData implements Parcelable
{

    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("micr")
    @Expose
    private String micr;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    public final static Parcelable.Creator<BankDetailData> CREATOR = new Creator<BankDetailData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BankDetailData createFromParcel(Parcel in) {
            return new BankDetailData(in);
        }

        public BankDetailData[] newArray(int size) {
            return (new BankDetailData[size]);
        }

    }
            ;

    protected BankDetailData(Parcel in) {
        this.bank = ((String) in.readValue((String.class.getClassLoader())));
        this.ifsc = ((String) in.readValue((String.class.getClassLoader())));
        this.micr = ((String) in.readValue((String.class.getClassLoader())));
        this.branch = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.contact = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.district = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BankDetailData() {
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bank);
        dest.writeValue(ifsc);
        dest.writeValue(micr);
        dest.writeValue(branch);
        dest.writeValue(address);
        dest.writeValue(contact);
        dest.writeValue(city);
        dest.writeValue(district);
        dest.writeValue(state);
    }

    public int describeContents() {
        return 0;
    }

}