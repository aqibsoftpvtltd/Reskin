package com.kasa77.modal.kuber_starline;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KuberStarlineGameModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("provider")
    @Expose
    private List<Provider> provider = null;
    public final static Parcelable.Creator<KuberStarlineGameModel> CREATOR = new Creator<KuberStarlineGameModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public KuberStarlineGameModel createFromParcel(Parcel in) {
            return new KuberStarlineGameModel(in);
        }

        public KuberStarlineGameModel[] newArray(int size) {
            return (new KuberStarlineGameModel[size]);
        }

    }
            ;

    protected KuberStarlineGameModel(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.provider, (com.kasa77.modal.kuber_starline.Provider.class.getClassLoader()));
    }

    public KuberStarlineGameModel() {
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

    public List<Provider> getProvider() {
        return provider;
    }

    public void setProvider(List<Provider> provider) {
        this.provider = provider;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(provider);
    }

    public int describeContents() {
        return 0;
    }

}