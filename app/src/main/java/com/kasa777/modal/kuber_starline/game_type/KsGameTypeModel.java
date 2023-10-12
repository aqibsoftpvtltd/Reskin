package com.kasa777.modal.kuber_starline.game_type;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KsGameTypeModel implements Parcelable
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
    public final static Parcelable.Creator<KsGameTypeModel> CREATOR = new Creator<KsGameTypeModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public KsGameTypeModel createFromParcel(Parcel in) {
            return new KsGameTypeModel(in);
        }

        public KsGameTypeModel[] newArray(int size) {
            return (new KsGameTypeModel[size]);
        }

    }
            ;

    protected KsGameTypeModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.gameTypes, (com.kasa777.modal.kuber_starline.game_type.GameType.class.getClassLoader()));
    }

    public KsGameTypeModel() {
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