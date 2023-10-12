package com.kasa777.modal.history_pagination;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class BidHistory{

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