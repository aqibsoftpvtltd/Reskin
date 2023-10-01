package com.kasa77.modal.fund_pagination;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class FundHistory{

	@SerializedName("current")
	public int current;

	@SerializedName("pages")
	public int pages;

	@SerializedName("records")
	public List<RecordsItem> records;

	@SerializedName("totalBids")
	public int totalBids;

	@SerializedName("status")
	public int status;
}