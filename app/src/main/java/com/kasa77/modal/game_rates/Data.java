package com.kasa77.modal.game_rates;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

@SerializedName("gameRates")
@Expose
private List<GameRate> gameRates = new ArrayList<>();
@SerializedName("starGameRates")
@Expose
private List<StarGameRate> starGameRates = new ArrayList<>();
@SerializedName("ABgameRates")
@Expose
private List<ABgameRate> aBgameRates = new ArrayList<>();
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
in.readList(this.gameRates, (com.kasa77.modal.game_rates.GameRate.class.getClassLoader()));
in.readList(this.starGameRates, (com.kasa77.modal.game_rates.StarGameRate.class.getClassLoader()));
in.readList(this.aBgameRates, (com.kasa77.modal.game_rates.ABgameRate.class.getClassLoader()));
}

public Data() {
}

public List<GameRate> getGameRates() {
return gameRates;
}

public void setGameRates(List<GameRate> gameRates) {
this.gameRates = gameRates;
}

public List<StarGameRate> getStarGameRates() {
return starGameRates;
}

public void setStarGameRates(List<StarGameRate> starGameRates) {
this.starGameRates = starGameRates;
}

public List<ABgameRate> getABgameRates() {
return aBgameRates;
}

public void setABgameRates(List<ABgameRate> aBgameRates) {
this.aBgameRates = aBgameRates;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeList(gameRates);
dest.writeList(starGameRates);
dest.writeList(aBgameRates);
}

public int describeContents() {
return 0;
}

}