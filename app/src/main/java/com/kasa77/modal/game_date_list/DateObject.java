package com.kasa77.modal.game_date_list;


import com.google.gson.annotations.SerializedName;


public class DateObject{


	@SerializedName("date")
	public String date;

	@SerializedName("dayname")
	public String dayname;

	@SerializedName("bidClosed")
	public String bidClosed;

	@SerializedName("status")
	public int status;

	@SerializedName("gameSession")
	public String gameSession;

	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}