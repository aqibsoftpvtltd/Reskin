package com.kasa77.modal.fund_pagination;


import com.google.gson.annotations.SerializedName;


public class RecordsItem{

	@SerializedName("withdrawalMode")
	public String withdrawalMode;

	@SerializedName("mobile")
	public String mobile;

	@SerializedName("reqTime")
	public String reqTime;

	@SerializedName("userId")
	public String userId;

	@SerializedName("UpdatedBy")
	public String updatedBy;

	@SerializedName("reqStatus")
	public String reqStatus;

	@SerializedName("reqDate")
	public String reqDate;

	@SerializedName("reqUpdatedAt")
	public String reqUpdatedAt;

	@SerializedName("reqType")
	public String reqType;

	@SerializedName("reqAmount")
	public Double reqAmount;

	@SerializedName("_id")
	public String id;

	@SerializedName("fullname")
	public String fullname;

	@SerializedName("username")
	public String username;
}