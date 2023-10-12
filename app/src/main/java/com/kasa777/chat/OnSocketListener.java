package com.kasa777.chat;

import com.kasa777.chat.model.MessageModal;

public interface OnSocketListener {
    public void onReceiveMsg(MessageModal toString);
    public void onDeleteSingleMsg(String toString);
    public void onDeleteUserMsg(String toString);
    public void onDeleteAllMsg(String toString);
}
