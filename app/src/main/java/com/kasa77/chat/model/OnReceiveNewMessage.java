package com.kasa77.chat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnReceiveNewMessage {
    @SerializedName("messageNew")
    @Expose
    public MessageModal messageNew = new MessageModal();

}
