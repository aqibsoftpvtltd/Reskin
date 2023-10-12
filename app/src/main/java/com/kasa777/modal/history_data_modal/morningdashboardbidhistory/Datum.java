package com.kasa777.modal.history_data_modal.morningdashboardbidhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("userId")
@Expose
private String userId;
@SerializedName("providerId")
@Expose
private String providerId;
@SerializedName("gameTypeId")
@Expose
private String gameTypeId;
@SerializedName("providerName")
@Expose
private String providerName;
@SerializedName("gameTypeName")
@Expose
private String gameTypeName;
@SerializedName("gameTypePrice")
@Expose
private Double gameTypePrice;
@SerializedName("userName")
@Expose
private String userName;
@SerializedName("bidDigit")
@Expose
private String bidDigit;
@SerializedName("biddingPoints")
@Expose
private Integer biddingPoints;
@SerializedName("gameSession")
@Expose
private String gameSession;
@SerializedName("winStatus")
@Expose
private String winStatus;
@SerializedName("gameWinPoints")
@Expose
private Integer gameWinPoints;
@SerializedName("gameDate")
@Expose
private String gameDate;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("createdAt")
@Expose
private String createdAt;
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
this.userId = ((String) in.readValue((String.class.getClassLoader())));
this.providerId = ((String) in.readValue((String.class.getClassLoader())));
this.gameTypeId = ((String) in.readValue((String.class.getClassLoader())));
this.providerName = ((String) in.readValue((String.class.getClassLoader())));
this.gameTypeName = ((String) in.readValue((String.class.getClassLoader())));
this.gameTypePrice = ((Double) in.readValue((Integer.class.getClassLoader())));
this.userName = ((String) in.readValue((String.class.getClassLoader())));
this.bidDigit = ((String) in.readValue((String.class.getClassLoader())));
this.biddingPoints = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.gameSession = ((String) in.readValue((String.class.getClassLoader())));
this.winStatus = ((String) in.readValue((String.class.getClassLoader())));
this.gameWinPoints = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.gameDate = ((String) in.readValue((String.class.getClassLoader())));
this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
}

public Datum() {
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getProviderId() {
return providerId;
}

public void setProviderId(String providerId) {
this.providerId = providerId;
}

public String getGameTypeId() {
return gameTypeId;
}

public void setGameTypeId(String gameTypeId) {
this.gameTypeId = gameTypeId;
}

public String getProviderName() {
return providerName;
}

public void setProviderName(String providerName) {
this.providerName = providerName;
}

public String getGameTypeName() {
return gameTypeName;
}

public void setGameTypeName(String gameTypeName) {
this.gameTypeName = gameTypeName;
}

public Double getGameTypePrice() {
return gameTypePrice;
}

public void setGameTypePrice(Double gameTypePrice) {
this.gameTypePrice = gameTypePrice;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getBidDigit() {
return bidDigit;
}

public void setBidDigit(String bidDigit) {
this.bidDigit = bidDigit;
}

public Integer getBiddingPoints() {
return biddingPoints;
}

public void setBiddingPoints(Integer biddingPoints) {
this.biddingPoints = biddingPoints;
}

public String getGameSession() {
return gameSession;
}

public void setGameSession(String gameSession) {
this.gameSession = gameSession;
}

public String getWinStatus() {
return winStatus;
}

public void setWinStatus(String winStatus) {
this.winStatus = winStatus;
}

public Integer getGameWinPoints() {
return gameWinPoints;
}

public void setGameWinPoints(Integer gameWinPoints) {
this.gameWinPoints = gameWinPoints;
}

public String getGameDate() {
return gameDate;
}

public void setGameDate(String gameDate) {
this.gameDate = gameDate;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(userId);
dest.writeValue(providerId);
dest.writeValue(gameTypeId);
dest.writeValue(providerName);
dest.writeValue(gameTypeName);
dest.writeValue(gameTypePrice);
dest.writeValue(userName);
dest.writeValue(bidDigit);
dest.writeValue(biddingPoints);
dest.writeValue(gameSession);
dest.writeValue(winStatus);
dest.writeValue(gameWinPoints);
dest.writeValue(gameDate);
dest.writeValue(updatedAt);
dest.writeValue(createdAt);
}

public int describeContents() {
return 0;
}

}