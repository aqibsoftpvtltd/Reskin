package com.kasa777.modal.transaction_statement;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("transaction_amount")
@Expose
private Integer transactionAmount;
@SerializedName("description")
@Expose
private String description;
@SerializedName("transaction_date")
@Expose
private String transactionDate;
@SerializedName("transaction_status")
@Expose
private String transactionStatus;
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
this.transactionAmount = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.description = ((String) in.readValue((String.class.getClassLoader())));
this.transactionDate = ((String) in.readValue((String.class.getClassLoader())));
this.transactionStatus = ((String) in.readValue((String.class.getClassLoader())));
}

public Datum() {
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public Integer getTransactionAmount() {
return transactionAmount;
}

public void setTransactionAmount(Integer transactionAmount) {
this.transactionAmount = transactionAmount;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getTransactionDate() {
return transactionDate;
}

public void setTransactionDate(String transactionDate) {
this.transactionDate = transactionDate;
}

public String getTransactionStatus() {
return transactionStatus;
}

public void setTransactionStatus(String transactionStatus) {
this.transactionStatus = transactionStatus;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(transactionAmount);
dest.writeValue(description);
dest.writeValue(transactionDate);
dest.writeValue(transactionStatus);
}

public int describeContents() {
return 0;
}

}