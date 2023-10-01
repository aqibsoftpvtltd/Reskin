package com.kasa77.modal.broadcast;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class BroadCast{

	@SerializedName("oldBroadcastMessage")
	public List<OldBroadcastMessageItem> oldBroadcastMessage;

	@SerializedName("status")
	public boolean status;
}