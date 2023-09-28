package com.kasa777.retrofit_provider;


import android.content.Context;

import com.kasa777.BuildConfig;
import com.kasa777.chat.ChatConstants;
import com.kasa777.chat.model.MediaFileModel;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceFileUpload {

    public static RetrofitApiClient client;

    public static String BASE_URL = ChatConstants.mediaBaseUrl;

    public RetrofitServiceFileUpload() {

        HttpLoggingInterceptor mHttpLoginInterceptor = new HttpLoggingInterceptor();

        mHttpLoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mOkClient = new OkHttpClient.Builder()
                .readTimeout(120000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            mOkClient.addInterceptor(mHttpLoginInterceptor);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(RetrofitApiClient.class);
    }

    public static RetrofitApiClient getRetrofit() {
        if (client == null)
            new RetrofitServiceFileUpload();

        return client;
    }

    public static void getServerResponse(final Context context, final Call<ResponseBody> method, final WebResponse webResponse) {
//        if (dialog != null)
//            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                WebServiceResponse.handleResponse(context,response, webResponse);
//                AppProgressDialog.hide(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void fileUploadResponse(final Context context, final Call<MediaFileModel> method, final WebResponse webResponse) {
//        if (dialog != null)
//            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<MediaFileModel>() {
            @Override
            public void onResponse(Call<MediaFileModel> call, Response<MediaFileModel> response) {

                WebServiceResponse.handleResponse(context,response, webResponse);
//                AppProgressDialog.hide(dialog);
            }

            @Override
            public void onFailure(Call<MediaFileModel> call, Throwable throwable) {
//                AppProgressDialog.hide(dialog);
//                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

}