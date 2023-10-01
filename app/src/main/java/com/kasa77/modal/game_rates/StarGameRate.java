package com.kasa77.modal.game_rates;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StarGameRate implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("gameName")
@Expose
private String gameName;
@SerializedName("gamePrice")
@Expose
private Double gamePrice;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;
public final static Parcelable.Creator<StarGameRate> CREATOR = new Creator<StarGameRate>() {


@SuppressWarnings({
"unchecked"
})
public StarGameRate createFromParcel(Parcel in) {
return new StarGameRate(in);
}

public StarGameRate[] newArray(int size) {
return (new StarGameRate[size]);
}

}
;

protected StarGameRate(Parcel in) {
this.id = ((String) in.readValue((String.class.getClassLoader())));
this.gameName = ((String) in.readValue((String.class.getClassLoader())));
this.gamePrice = ((Double) in.readValue((Integer.class.getClassLoader())));
this.modifiedAt = ((String) in.readValue((String.class.getClassLoader())));
}

public StarGameRate() {
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getGameName() {
return gameName;
}

public void setGameName(String gameName) {
this.gameName = gameName;
}

public Double getGamePrice() {
return gamePrice;
}

public void setGamePrice(Double gamePrice) {
this.gamePrice = gamePrice;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(gameName);
dest.writeValue(gamePrice);
dest.writeValue(modifiedAt);
}

public int describeContents() {
return 0;
}

}