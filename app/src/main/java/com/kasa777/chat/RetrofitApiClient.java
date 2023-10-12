package com.kasa777.chat;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApiClient {

    @Headers("Content-Type: application/json")
    @POST("usersOldMessage")
    Call<ResponseBody> getOldMessages(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("oldBroadcastList")
    Call<ResponseBody> getBroadCast();

}
