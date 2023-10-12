package com.kasa777.retrofit_provider;


import android.content.Context;

import com.kasa777.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthHeaderRetrofitServiceForBankDetail {

    public static RetrofitApiClient client;
    public static RetrofitApiClient regClient;
    public static Context mContext;

//    public static String BASE_URL = "http://ifsc.datayuge.com/";
    public static String BASE_URL = "https://ifsc.razorpay.com/";
    public AuthHeaderRetrofitServiceForBankDetail() {

        HttpLoggingInterceptor mHttpLoginInterceptor = new HttpLoggingInterceptor();

        mHttpLoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mOkClient = new OkHttpClient.Builder()
                .readTimeout(120000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS);

        OkHttpClient.Builder mOkClientReg = new OkHttpClient.Builder()
                .readTimeout(120000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS);

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
//                        .header("DY-X-Authorization", Constant.BANK_DETAIL_TOKEN)
                        /*.method(original.method(), original.body())*/
                        .build();
              return chain.proceed(request);
            }
        };

        Interceptor interceptorReg = chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
//                    .header("x-api-key", Constant.X_API_KEY)
                    /*.method(original.method(), original.body())*/
                    .build();
            return chain.proceed(request);
        };

        if (BuildConfig.DEBUG) {
            mOkClient.addInterceptor(mHttpLoginInterceptor);
            mOkClientReg.addInterceptor(mHttpLoginInterceptor);
        }

        mOkClient.addInterceptor(interceptor);
        mOkClientReg.addInterceptor(interceptorReg);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(RetrofitApiClient.class);

        Retrofit retrofitReg = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkClientReg.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        regClient = retrofitReg.create(RetrofitApiClient.class);
    }

    public static RetrofitApiClient getRetrofit() {
        if (client == null)
            new AuthHeaderRetrofitServiceForBankDetail();

        return client;
    }




}