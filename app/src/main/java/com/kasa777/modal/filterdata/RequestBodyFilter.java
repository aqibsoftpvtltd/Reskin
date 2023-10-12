package com.kasa777.modal.filterdata;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class RequestBodyFilter{



	@SerializedName("skipValue")
	public int skipValue;

	@SerializedName("session") // Open Close
	public List<String> session;

	@SerializedName("providerId")
	public List<String> providerId;

	@SerializedName("status") //0 pen 1 win 2 loss
	public List<Integer> status;

	@SerializedName("userId")
	public String userId;


}