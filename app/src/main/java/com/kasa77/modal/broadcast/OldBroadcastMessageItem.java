package com.kasa77.modal.broadcast;


import com.google.gson.annotations.SerializedName;


public class OldBroadcastMessageItem{

	@SerializedName("dateTime")
	public String dateTime;

	@SerializedName("_id")
	public String id;

	@SerializedName("message")
	public String message;

	@SerializedName("dateTimestamp")
	public String dateTimestamp;
}