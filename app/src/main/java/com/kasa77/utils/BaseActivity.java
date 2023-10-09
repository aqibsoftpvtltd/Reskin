package com.kasa77.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.kasa77.chat.ChatApplication;
import com.kasa77.chat.ChatConstants;
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService;
import com.kasa77.retrofit_provider.RetrofitApiClient;
import com.kasa77.retrofit_provider.RetrofitService;
import com.kasa77.retrofit_provider.RetrofitServiceFileUpload;
import com.kasa77.retrofit_provider.RetrofitServicePinCode;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class BaseActivity extends AppCompatActivity {

    public static final long DISCONNECT_TIMEOUT = 12000000;
    public RetrofitApiClient retrofitApiClient;
    public RetrofitApiClient retrofitApiClientAuthReg;
    public RetrofitApiClient retrofitApiClientAuth;
    public RetrofitApiClient retrofitApiClientFile;
    public RetrofitApiClient retrofitApiClientPinCode;
    public ConnectionDetector cd;
    public Context mContext;
    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        retrofitApiClientAuthReg = AuthHeaderRetrofitService.getRetrofitReg();
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit();
        retrofitApiClientFile = RetrofitServiceFileUpload.getRetrofit();
        retrofitApiClientPinCode = RetrofitServicePinCode.getRetrofit();

        resetDisconnectTimer();

        ChatApplication chatApplication = (ChatApplication) getApplicationContext();
        mSocket = chatApplication.getSocket();
        mSocket.on(ChatConstants.KEY_FORCE_LOGOUT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("SOCKET", "forceLogout");
                            Alerts.SessionLogout(BaseActivity.this, BaseActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("SOCKET", "forceLogout Message : " + e.getMessage());
                        }

                    }
                }, 1000);


            }

        });

    }


    private static Handler disconnectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // todo
            return true;
        }
    });

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {

            Alerts.SessionLogout(BaseActivity.this, BaseActivity.this);
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        Log.e("EVENt", "event");
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        resetDisconnectTimer();

        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();

    }

    @Override
    protected void onDestroy() {
        stopDisconnectTimer();


        super.onDestroy();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}