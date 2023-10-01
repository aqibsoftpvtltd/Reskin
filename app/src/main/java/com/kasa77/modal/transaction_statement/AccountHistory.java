package com.kasa77.modal.transaction_statement;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AccountHistory {

	@SerializedName("current")
	public int current;

	@SerializedName("records")
	public List<RecordsItem> records;

	@SerializedName("totalBids")
	public int totalBids;

	@SerializedName("status")
	public int status;

	@SerializedName("pages")
	public int totalPage;
}