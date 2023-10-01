package com.kasa77.modal.game_session_list;


import com.google.gson.annotations.SerializedName;


public class GameSession{

	@SerializedName("data")
	public DaySession daySession;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;
}