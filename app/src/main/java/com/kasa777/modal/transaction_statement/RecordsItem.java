package com.kasa777.modal.transaction_statement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("_id")
	@Expose
	public String id;
	@SerializedName("userId")
	@Expose
	public String userId;
	@SerializedName("previous_amount")
	@Expose
	public Double previousAmount;
	@SerializedName("current_amount")
	@Expose
	public Double currentAmount;
	@SerializedName("transaction_amount")
	@Expose
	public Double transactionAmount;
	@SerializedName("username")
	@Expose
	public String username;
	@SerializedName("description")
	@Expose
	public String description;
	@SerializedName("transaction_date")
	@Expose
	public String transactionDate;
	@SerializedName("transaction_time")
	@Expose
	public String transactionTime;
	@SerializedName("transaction_status")
	@Expose
	public String transactionStatus;

	@SerializedName("bidId")
	@Expose
	public String bidId;
	@SerializedName("filterType")
	@Expose
	public Integer filterType;
	@SerializedName("reqType")
	@Expose
	public String reqType;

	public boolean isExpand = false;

	public void setExpand(boolean expand) {
		isExpand = expand;
	}

	public boolean isExpand() {
		return isExpand;
	}
}