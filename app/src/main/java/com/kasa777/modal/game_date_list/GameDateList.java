package com.kasa777.modal.game_date_list;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GameDateList{

	@SerializedName("date")
	public List<DateObject> date;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;
}