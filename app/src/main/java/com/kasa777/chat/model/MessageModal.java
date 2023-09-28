package com.kasa777.chat.model;


import com.google.gson.annotations.SerializedName;


public class MessageModal{

	@SerializedName("local_id")
	public int local_id;

	@SerializedName("dateTime")
	public String dateTime;

	@SerializedName("messStatus")
	public int messStatus;

	@SerializedName("images")
	public String images;

	@SerializedName("audios")
	public String audios;

	@SerializedName("_id")
	public String id;

	@SerializedName("message")
	public String message;

	@SerializedName("userName")
	public String userName;

	@SerializedName("dateTimestamp")
	public String dateTimestamp;

	@SerializedName("userId")
	public String userId;

	@SerializedName("androidMessage")
	public String androidMessage;

	@SerializedName("replyName")
	public String replyName ="";
	@SerializedName("replyMessage")
	public String replyMessage="";

	@SerializedName("videos")
	public String videos;

	@SerializedName("messType")
	public int messType;

	public int msgContentType; // TODO: 28-12-2019 1 text,  2 image 3 image+text 4 audio

	public int messAudioDuration; // manage localy
	public String  msgFile; // manage localy
	public String  messFileUrl; // manage localy
}