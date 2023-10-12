package com.kasa777.chat;

import com.kasa777.constant.Constant;

public class ChatConstants {
    public static final String CHAT_SERVER_URL = Constant.CHAT_URL;
    public static final String mediaBaseUrl = Constant.CHAT_URL;

    public static String MY_ONLINE_STATUS = "Offline";
    public static int CHAT_COUNTER = 0;

    public static final String KEY_USER_ONLINE_STATUS = "currentUserOnlineStatus";
    public static final String KEY_USER_ONLINE_STATUS_RETURN = "'userOnlineStatus'"; // TODO: 28-12-2019 json (status, userId
    public static final String KEY_RECEIVE_MESSAGE = "receivedMessageData";
    public static final String KEY_FORCE_LOGOUT = "logoutKarDoSabkoAppSe";
    public static final String KEY_ADMIN_ONLINE_STATUS = "currentAdminOnlineStatus"; // TODO: 28-12-2019 request userid
    public static final String KEY_ADMIN_ONLINE_STATUS_RETURN = "adminOnlineStatus";
    public static final String KEY_DELETE_SINGLE_MSG = "deleteMessId";
    public static final String KEY_DELETE_USER_MSG = "deleteUserMess";
    public static final String KEY_DELETE_ALL_MSG = "deleteAllMess";
    public static final String KEY_SEND_MSG = "sendMessageData";
    public static final String KEY_USER_OLD_MSG = "usersOldMessage"; // TODO: 28-12-2019 json ("messId":"5e073b3beaf6050f3006a62a","userId":"5dfb804f4289f90011a31299"


}
