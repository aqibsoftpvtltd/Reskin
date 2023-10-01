package com.kasa77.modal.static_data.noticeboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

@SerializedName("modified")
@Expose
private String modified;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("title1")
@Expose
private String title1;
@SerializedName("title2")
@Expose
private String title2;
@SerializedName("title3")
@Expose
private String title3;
@SerializedName("description1")
@Expose
private String description1;
@SerializedName("description2")
@Expose
private String description2;
@SerializedName("description3")
@Expose
private String description3;
@SerializedName("contact")
@Expose
private String contact;
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
this.modified = ((String) in.readValue((String.class.getClassLoader())));
this.id = ((String) in.readValue((String.class.getClassLoader())));
this.title1 = ((String) in.readValue((String.class.getClassLoader())));
this.title2 = ((String) in.readValue((String.class.getClassLoader())));
this.title3 = ((String) in.readValue((String.class.getClassLoader())));
this.description1 = ((String) in.readValue((String.class.getClassLoader())));
this.description2 = ((String) in.readValue((String.class.getClassLoader())));
this.description3 = ((String) in.readValue((String.class.getClassLoader())));
this.contact = ((String) in.readValue((String.class.getClassLoader())));
}

public Datum() {
}

public String getModified() {
return modified;
}

public void setModified(String modified) {
this.modified = modified;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getTitle1() {
return title1;
}

public void setTitle1(String title1) {
this.title1 = title1;
}

public String getTitle2() {
return title2;
}

public void setTitle2(String title2) {
this.title2 = title2;
}

public String getTitle3() {
return title3;
}

public void setTitle3(String title3) {
this.title3 = title3;
}

public String getDescription1() {
return description1;
}

public void setDescription1(String description1) {
this.description1 = description1;
}

public String getDescription2() {
return description2;
}

public void setDescription2(String description2) {
this.description2 = description2;
}

public String getDescription3() {
return description3;
}

public void setDescription3(String description3) {
this.description3 = description3;
}

public String getContact() {
return contact;
}

public void setContact(String contact) {
this.contact = contact;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(modified);
dest.writeValue(id);
dest.writeValue(title1);
dest.writeValue(title2);
dest.writeValue(title3);
dest.writeValue(description1);
dest.writeValue(description2);
dest.writeValue(description3);
dest.writeValue(contact);
}

public int describeContents() {
return 0;
}

}