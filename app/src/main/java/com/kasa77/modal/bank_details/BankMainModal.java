package com.kasa77.modal.bank_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankMainModal implements Parcelable
{

    @SerializedName("bank")
    @Expose
    private BankDetailData data;
    public final static Parcelable.Creator<BankMainModal> CREATOR = new Creator<BankMainModal>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BankMainModal createFromParcel(Parcel in) {
            return new BankMainModal(in);
        }

        public BankMainModal[] newArray(int size) {
            return (new BankMainModal[size]);
        }

    }
            ;

    protected BankMainModal(Parcel in) {
        this.data = ((BankDetailData) in.readValue((BankDetailData.class.getClassLoader())));
    }

    public BankMainModal() {
    }

    public BankDetailData getData() {
        return data;
    }

    public void setData(BankDetailData prdataoduct) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}