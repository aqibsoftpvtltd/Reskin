package com.kasa77.modal;


import com.google.gson.annotations.SerializedName;


public class WithdrawText{

	@SerializedName("data")
	public Data data;

	@SerializedName("message")
	public String message;

	@SerializedName("status")
	public int status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}