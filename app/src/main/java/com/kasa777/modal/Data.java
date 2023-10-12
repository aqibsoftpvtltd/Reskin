package com.kasa777.modal;


import com.google.gson.annotations.SerializedName;


public class Data{

	@SerializedName("Number")
	public String number;

	@SerializedName("textSecondry")
	public String textSecondry;

	@SerializedName("Timing")
	public String timing;

	@SerializedName("textMain")
	public String textMain;

	public String getNumber(){
		return number;
	}

	public String getTextSecondry(){
		return textSecondry;
	}

	public String getTiming(){
		return timing;
	}

	public String getTextMain(){
		return textMain;
	}
}