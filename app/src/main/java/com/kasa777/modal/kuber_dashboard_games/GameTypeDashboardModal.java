package com.kasa777.modal.kuber_dashboard_games;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameTypeDashboardModal implements Parcelable
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
    public final static Parcelable.Creator<GameTypeDashboardModal> CREATOR = new Creator<GameTypeDashboardModal>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GameTypeDashboardModal createFromParcel(Parcel in) {
            return new GameTypeDashboardModal(in);
        }

        public GameTypeDashboardModal[] newArray(int size) {
            return (new GameTypeDashboardModal[size]);
        }

    }
            ;

    protected GameTypeDashboardModal(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.gameTypes, (com.kasa777.modal.kuber_dashboard_games.GameType.class.getClassLoader()));
    }

    public GameTypeDashboardModal() {
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
