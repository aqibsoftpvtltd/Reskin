package com.kasa777.modal.filterdata;


import com.google.gson.annotations.SerializedName;


public class DataItem{

	@SerializedName("resultStatus")
	public int resultStatus;

	@SerializedName("providerResult")
	public String providerResult;

	@SerializedName("modifiedAt")
	public String modifiedAt;

	@SerializedName("_id")
	public String id;

	@SerializedName("providerName")
	public String providerName;

	public boolean checked;
}