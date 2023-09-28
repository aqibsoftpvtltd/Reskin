package com.kasa777.retrofit_provider;


import android.app.Dialog;
import android.content.Context;


import com.kasa777.chat.ChatApplication;
import com.kasa777.constant.Constant;
import com.kasa777.modal.game_rates.GameRatesModal;
import com.kasa777.modal.jackpot_modal.gametype_id.GameTypeIdList;
import com.kasa777.modal.history_data_modal.jackpotresulthistory.JackpotResultData;
import com.kasa777.modal.kuber_dashboard_games.GameTypeDashboardModal;
import com.kasa777.modal.kuber_starline.game_type.KsGameTypeModel;
import com.kasa777.modal.history_data_modal.starlineresulthistory.StarlineResultData;
import com.kasa777.modal.profile_details_modal.ProfileDetailsModal;
import com.kasa777.modal.static_data.howtoplay.HowToPlayData;
import com.kasa777.modal.static_data.news.NewsData;
import com.kasa777.modal.static_data.noticeboard.NoticeBoardData;
import com.kasa777.modal.static_data.notification.NotificationData;
import com.kasa777.modal.static_data.profilenote.ProfileNoteData;
import com.kasa777.modal.static_data.walletcontact.WalletContactData;
import com.kasa777.utils.AppPreference;
import com.kasa777.utils.AppProgressDialog;

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
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthHeaderRetrofitService {

    public static RetrofitApiClient client;
    public static RetrofitApiClient regClient;
    public static Context mContext;

    public static String BASE_URL = Constant.BASE_URL;

    public AuthHeaderRetrofitService() {
        String auth_token = AppPreference.getStringPreference(
                ChatApplication.context,
                Constant.USER_LOGIN_TOKEN
        );
        String authorization = AppPreference.getStringPreference(
                ChatApplication.context,
                Constant.DEVICE_ID
        );
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
                        .header("auth-token", auth_token)
                        .header("Authorization", authorization)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        };

        Interceptor interceptorReg = chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("x-api-key", Constant.X_API_KEY)
                    /*.method(original.method(), original.body())*/
                    .build();
            return chain.proceed(request);
        };

//        if (BuildConfig.DEBUG) {
//            mOkClient.addInterceptor(mHttpLoginInterceptor);
//            mOkClientReg.addInterceptor(mHttpLoginInterceptor);
//        }

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
        client = null;
        new AuthHeaderRetrofitService();

        return client;
    }

    public static RetrofitApiClient getRetrofitReg() {
        regClient = null;
        new AuthHeaderRetrofitService();

        return regClient;
    }

    public static void getServerResponse(final Dialog dialog, final Call<ResponseBody> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void getBankServerResponse(final Dialog dialog, final Call<ResponseBody> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.showBankFetching(dialog);

        method.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }


    public static void getKsGameTypeId(final Dialog dialog, final Call<KsGameTypeModel> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<KsGameTypeModel>() {
            @Override
            public void onResponse(Call<KsGameTypeModel> call, Response<KsGameTypeModel> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<KsGameTypeModel> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void getJackpotGameTypeId(final Dialog dialog, final Call<GameTypeIdList> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<GameTypeIdList>() {
            @Override
            public void onResponse(Call<GameTypeIdList> call, Response<GameTypeIdList> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<GameTypeIdList> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }


    public static void getKuberDashboardMainData(final Dialog dialog, final Call<ResponseBody> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }


    public static void getDashboardGameTypeData(final Dialog dialog, final Call<GameTypeDashboardModal> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<GameTypeDashboardModal>() {
            @Override
            public void onResponse(Call<GameTypeDashboardModal> call, Response<GameTypeDashboardModal> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<GameTypeDashboardModal> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }


    public static void howToPlay(final Dialog dialog, final Call<HowToPlayData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<HowToPlayData>() {
            @Override
            public void onResponse(Call<HowToPlayData> call, Response<HowToPlayData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<HowToPlayData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void noticeboard(final Dialog dialog, final Call<NoticeBoardData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<NoticeBoardData>() {
            @Override
            public void onResponse(Call<NoticeBoardData> call, Response<NoticeBoardData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<NoticeBoardData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void newsList(final Dialog dialog, final Call<NewsData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void notificationList(final Dialog dialog, final Call<NotificationData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<NotificationData>() {
            @Override
            public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<NotificationData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void walletContact(final Dialog dialog, final Call<WalletContactData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<WalletContactData>() {
            @Override
            public void onResponse(Call<WalletContactData> call, Response<WalletContactData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<WalletContactData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void profileNote(final Dialog dialog, final Call<ProfileNoteData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ProfileNoteData>() {
            @Override
            public void onResponse(Call<ProfileNoteData> call, Response<ProfileNoteData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<ProfileNoteData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }


    public static void starlineResultHistoryData(final Dialog dialog, final Call<StarlineResultData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<StarlineResultData>() {
            @Override
            public void onResponse(Call<StarlineResultData> call, Response<StarlineResultData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<StarlineResultData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void jackpotResultData(final Dialog dialog, final Call<JackpotResultData> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<JackpotResultData>() {
            @Override
            public void onResponse(Call<JackpotResultData> call, Response<JackpotResultData> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<JackpotResultData> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void gameRatesData(final Dialog dialog, final Call<GameRatesModal> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<GameRatesModal>() {
            @Override
            public void onResponse(Call<GameRatesModal> call, Response<GameRatesModal> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<GameRatesModal> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    public static void profileData(final Dialog dialog, final Call<ProfileDetailsModal> method, final WebResponse webResponse) {
        if (dialog != null)
            AppProgressDialog.show(dialog);

        method.enqueue(new Callback<ProfileDetailsModal>() {
            @Override
            public void onResponse(Call<ProfileDetailsModal> call, Response<ProfileDetailsModal> response) {
                AppProgressDialog.hide(dialog);
                WebServiceResponse.handleResponse(mContext, response, webResponse);
            }

            @Override
            public void onFailure(Call<ProfileDetailsModal> call, Throwable throwable) {
                AppProgressDialog.hide(dialog);
                webResponse.onResponseFailed(throwable.getMessage());
            }
        });
    }

    /*RequestBody body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(),
                            ((jsonObject)).toString());*/

}