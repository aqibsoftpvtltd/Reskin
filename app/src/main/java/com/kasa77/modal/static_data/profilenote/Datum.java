package com.kasa77.modal.static_data.profilenote;

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
@SerializedName("note")
@Expose
private String note;
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
this.note = ((String) in.readValue((String.class.getClassLoader())));
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

public String getNote() {
return note;
}

public void setNote(String note) {
this.note = note;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(modified);
dest.writeValue(id);
dest.writeValue(note);
}

public int describeContents() {
return 0;
}

}