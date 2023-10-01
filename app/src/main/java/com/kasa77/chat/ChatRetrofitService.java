package com.kasa77.chat;


import android.app.Dialog;
import android.content.Context;

import com.kasa77.BuildConfig;
import com.kasa77.constant.Constant;
import com.kasa77.retrofit_provider.WebResponse;
import com.kasa77.retrofit_provider.WebServiceResponse;
import com.kasa77.utils.AppPreference;
import com.kasa77.utils.AppProgressDialog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatRetrofitService {

    public static RetrofitApiClient client;

    public static String BASE_URL = ChatConstants.CHAT_SERVER_URL;

    public ChatRetrofitService() {

        HttpLoggingInterceptor mHttpLoginInterceptor = new HttpLoggingInterceptor();

        mHttpLoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mOkClient = new OkHttpClient.Builder()
                .readTimeout(120000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS);

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("auth-token", AppPreference.getStringPreference(
                                ChatApplication.context,
                                Constant.USER_LOGIN_TOKEN
                        ))
                        .header("Authorization", AppPreference.getStringPreference(
                                ChatApplication.context,
                                Constant.DEVICE_ID
                        ))
                        .build();
                return chain.proceed(request);
            }
        };
        mOkClient.addInterceptor(interceptor);
        if (BuildConfig.DEBUG) {
            mOkClient.addInterceptor(mHttpLoginInterceptor);

        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkClient.build())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(RetrofitApiClient.class);
    }

    public static RetrofitApiClient getRetrofit() {
        if (client == null)
            new ChatRetrofitService();

        return client;
    }

    public static void getServerResponse(Context context, final Dialog dialog, final Call<ResponseBody> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                WebServiceResponse.handleResponse(context, response, webResponse);
                AppProgressDialog.hide(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }
}