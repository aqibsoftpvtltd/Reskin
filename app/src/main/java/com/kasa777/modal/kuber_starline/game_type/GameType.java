package com.kasa777.modal.kuber_starline.game_type;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameType implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("gameName")
    @Expose
    private String gameName;
    @SerializedName("gamePrice")
    @Expose
    private Double gamePrice;
    public final static Parcelable.Creator<GameType> CREATOR = new Creator<GameType>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GameType createFromParcel(Parcel in) {
            return new GameType(in);
        }

        public GameType[] newArray(int size) {
            return (new GameType[size]);
        }

    };

    protected GameType(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.gameName = ((String) in.readValue((String.class.getClassLoader())));
        this.gamePrice = ((Double) in.readValue((Integer.class.getClassLoader())));
    }

    public GameType() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(gameName);
        dest.writeValue(gamePrice);
    }

    public int describeContents() {
        return 0;
    }

}