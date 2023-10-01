package com.kasa77.chat;


import android.app.Application;
import android.content.Context;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.kasa77.utils.ImagePipelineConfigUtils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApplication extends Application {

    private Socket mSocket;

    public static Context context;



    {
        try {
            mSocket = IO.socket(ChatConstants.CHAT_SERVER_URL);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));
        context = getApplicationContext();
    }

    private OnSocketListener onSocketListener = null;

    public OnSocketListener getOnSocketListener() {
        return onSocketListener;
    }

    public void setOnSocketListener(OnSocketListener onSocketListener) {
        this.onSocketListener = onSocketListener;
    }
}