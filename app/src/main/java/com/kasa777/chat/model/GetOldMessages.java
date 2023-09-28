package com.kasa777.chat.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class GetOldMessages{

	@SerializedName("deleteMessageList")
	public List<MessageModal> deleteMessageList;

	@SerializedName("messageList")
	public List<MessageModal> messageList;

	@SerializedName("oldBroadcastMessage")
	public List<MessageModal> oldBroadcastMessage;

	@SerializedName("status")
	public boolean status;
	
	@SerializedName("allMessageListStatus")
	public boolean allMessageListStatus;

	public void setDeleteMessageList(List<MessageModal> deleteMessageList){
		this.deleteMessageList = deleteMessageList;
	}

	public List<MessageModal> getDeleteMessageList(){
		return deleteMessageList;
	}

	public void setMessageList(List<MessageModal> messageList){
		this.messageList = messageList;
	}

	public List<MessageModal> getMessageList(){
		return messageList;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"GetOldMessages{" + 
			"deleteMessageList = '" + deleteMessageList + '\'' + 
			",messageList = '" + messageList + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}