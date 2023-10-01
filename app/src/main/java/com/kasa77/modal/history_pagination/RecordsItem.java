package com.kasa77.modal.history_pagination;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("gameTypeId")
	public String gameTypeId;

	@SerializedName("bidDigit")
	public String bidDigit;

	@SerializedName("winStatus")
	public int winStatus;

	@SerializedName("gameDate")
	public String gameDate;

	@SerializedName("mobileNumber")
	public String mobileNumber;

	@SerializedName("userName")
	public String userName;

	@SerializedName("userId")
	public String userId;

	@SerializedName("gameTypePrice")
	public Double gameTypePrice;

	@SerializedName("biddingPoints")
	public int biddingPoints;

	@SerializedName("gameWinPoints")
	public Double gameWinPoints;

	@SerializedName("createdAt")
	public String createdAt;

	@SerializedName("providerId")
	public String providerId;

	@SerializedName("gameTypeName")
	public String gameTypeName;

	@SerializedName("_id")
	public String id;

	@SerializedName("providerName")
	public String providerName;

	@SerializedName("gameSession")
	public String gameSession;

	@SerializedName("updatedAt")
	public String updatedAt;
}