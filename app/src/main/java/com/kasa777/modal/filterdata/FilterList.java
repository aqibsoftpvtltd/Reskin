package com.kasa777.modal.filterdata;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class FilterList{

	@SerializedName("data")
	public List<DataItem> data;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;
}