package com.kasa777.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kasa777.chat.model.MessageModal;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "ChatDB";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CHAT = "MessageTable";

    private static final String local_Id = "local_Id";
    private static final String dateTime = "dateTime";
    private static final String msgStatus = "messStatus";
    private static final String images = "images";
    private static final String audios = "audios";
    private static final String msg_id = "id";
    private static final String message = "message";
    private static final String userName = "userName";
    private static final String dateTimestamp = "dateTimestamp";
    private static final String userId = "userId";
    private static final String androidMessage = "androidMessage";
    private static final String messType = "messType";
    private static final String msgContentType = "msgContentType";
    private static final String messAudioDuration = "messAudioDuration";
    private static final String msgFile = "msgFile";
    private static final String messFileUrl = "messFileUrl";
    private static final String replyMessage = "replyMessage";
    private static final String replyName = "replyName";
    private static final String videos = "videos";

    private static final String CREATE_TABLE_CHAT = "CREATE TABLE "
            + TABLE_CHAT + "(" + local_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + msg_id + " TEXT,"
            + userId + " TEXT,"
            + userName + " TEXT,"
            + msgStatus + " INTEGER,"
            + messType + " INTEGER,"
            + androidMessage + " TEXT,"
            + dateTime + " TEXT,"
            + dateTimestamp + " INTEGER,"
            + msgFile + " TEXT,"
            + message + " TEXT,"
            + audios + " TEXT,"
            + videos + " TEXT,"
            + images + " TEXT,"
            + replyMessage + " TEXT,"
            + replyName + " TEXT,"
            + messFileUrl + " TEXT,"
            + messAudioDuration + " INTEGER,"
            + msgContentType + " INTEGER );";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CHAT + "'");
        onCreate(db);
    }

    /*************************************
     * Manage Database Operation
     ****************************************/
    public void addMessageToDb(MessageModal chatModal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(msg_id, chatModal.id);
        values.put(userId, chatModal.userId);
        values.put(userName, chatModal.userName);
        values.put(msgStatus, chatModal.messStatus);
        values.put(messType, chatModal.messType);
        values.put(androidMessage, chatModal.androidMessage);
        values.put(dateTime, chatModal.dateTime);
        values.put(dateTimestamp, chatModal.dateTimestamp);
        values.put(msgFile, chatModal.msgFile);
        values.put(message, chatModal.message);
        values.put(audios, chatModal.audios);
        values.put(videos, chatModal.videos);
        values.put(images, chatModal.images);
        values.put(replyMessage, chatModal.replyMessage);
        values.put(replyName, chatModal.replyName);
        values.put(messAudioDuration, chatModal.messAudioDuration);
        values.put(msgContentType, chatModal.msgContentType);
        values.put(messFileUrl, chatModal.messFileUrl);
        db.insert(TABLE_CHAT, null, values);
        db.close();
        System.out.println("SOCKET : add to db" +chatModal.id);
    }

    public ArrayList<MessageModal> getAllMessageFromDB() {
        ArrayList<MessageModal> chatMessageList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                MessageModal chatModal = new MessageModal();
                chatModal.local_id = (c.getInt(c.getColumnIndex(local_Id)));
                chatModal.id = (c.getString(c.getColumnIndex(msg_id)));
                chatModal.userId = (c.getString(c.getColumnIndex(userId)));
                chatModal.userName = (c.getString(c.getColumnIndex(userName)));
                chatModal.messStatus = (c.getInt(c.getColumnIndex(msgStatus)));
                chatModal.messType = (c.getInt(c.getColumnIndex(messType)));
                chatModal.message= (c.getString(c.getColumnIndex(message)));
                chatModal.dateTime =(c.getString(c.getColumnIndex(dateTime)));
                chatModal.dateTimestamp =(c.getString(c.getColumnIndex(dateTimestamp)));
                chatModal.androidMessage = (c.getString(c.getColumnIndex(androidMessage)));
                chatModal.audios = (c.getString(c.getColumnIndex(audios)));
                chatModal.images = (c.getString(c.getColumnIndex(images)));
                chatModal.videos = (c.getString(c.getColumnIndex(videos)));
                chatModal.replyName = (c.getString(c.getColumnIndex(replyName)));
                chatModal.replyMessage = (c.getString(c.getColumnIndex(replyMessage)));
                chatModal.msgFile = (c.getString(c.getColumnIndex(msgFile)));
                chatModal.messAudioDuration = (c.getInt(c.getColumnIndex(messAudioDuration)));
                chatModal.msgContentType = (c.getInt(c.getColumnIndex(msgContentType)));
                chatModal.messFileUrl = (c.getString(c.getColumnIndex(messFileUrl)));
                chatMessageList.add(chatModal);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return chatMessageList;
    }

    public void updateMessageInDbById(MessageModal chatModal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(msg_id, chatModal.id);
        values.put(userId, chatModal.userId);
        values.put(userName, chatModal.userName);
        values.put(msgStatus, chatModal.messStatus);
        values.put(messType, chatModal.messType);
        values.put(androidMessage, chatModal.androidMessage);
        values.put(dateTime, chatModal.dateTime);
        values.put(dateTimestamp, chatModal.dateTimestamp);
        values.put(msgFile, chatModal.msgFile);
        values.put(message, chatModal.message);
        values.put(audios, chatModal.audios);
        values.put(images, chatModal.images);
        values.put(videos, chatModal.videos);
        values.put(replyName, chatModal.replyName);
        values.put(replyMessage, chatModal.replyMessage);
        values.put(messAudioDuration, chatModal.messAudioDuration);
        values.put(msgContentType, chatModal.msgContentType);
        values.put(messFileUrl, chatModal.messFileUrl);
        db.update(TABLE_CHAT, values,
                msg_id + " = ? ",
                new String[]{String.valueOf(chatModal.id)});

        db.close();
    }

/*    public void updateMessageInDbByTimeStamp(MessageModal chatModal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESS_ID, chatModal.getMessId());
        values.put(USER_ID, chatModal.getUserId());
        values.put(USER_NAME, chatModal.getUserName());
        values.put(MESS_STATUS, chatModal.getMessStatus());
        values.put(MESS_TYPE, chatModal.getMessType());
        values.put(MESS_TEXT, chatModal.getMessText());
        values.put(MESS_DATE, chatModal.getMessDate());
        //values.put(MESS_TIME_STAMP, chatModal.getMessTimeStamp());
        //values.put(MESS_FILE, chatModal.getMessFile());
        values.put(MESS_FILE_URL, chatModal.getMessFileUrl());
        values.put(MESS_FILE_TYPE, chatModal.getMessFileType());
        values.put(MESS_AUDIO_DURATION, chatModal.getMessAudioDuration());
        values.put(MESS_CONTENT_TYPE, chatModal.getMessContentType());
        db.update(TABLE_CHAT, values,
                MESS_TIME_STAMP + " = ? ",
                new String[]{String.valueOf(chatModal.getMessTimeStamp())});

        db.close();
    }*/

    public void deleteAllChatFromDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT, null, null);
        db.close();
    }


}
