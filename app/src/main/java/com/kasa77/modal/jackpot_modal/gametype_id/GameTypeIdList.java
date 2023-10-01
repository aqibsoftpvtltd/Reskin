package com.kasa77.modal.jackpot_modal.gametype_id;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameTypeIdList implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("gameTypes")
    @Expose
    private List<GameType> gameTypes = null;
    public final static Parcelable.Creator<GameTypeIdList> CREATOR = new Creator<GameTypeIdList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GameTypeIdList createFromParcel(Parcel in) {
            return new GameTypeIdList(in);
        }

        public GameTypeIdList[] newArray(int size) {
            return (new GameTypeIdList[size]);
        }

    }
            ;

    protected GameTypeIdList(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.gameTypes, (com.kasa77.modal.jackpot_modal.gametype_id.GameType.class.getClassLoader()));
    }

    public GameTypeIdList() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GameType> getGameTypes() {
        return gameTypes;
    }

    public void setGameTypes(List<GameType> gameTypes) {
        this.gameTypes = gameTypes;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(gameTypes);
    }

    public int describeContents() {
        return 0;
    }

}