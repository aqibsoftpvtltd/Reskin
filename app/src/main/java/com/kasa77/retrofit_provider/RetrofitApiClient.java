package com.kasa77.retrofit_provider;

import com.kasa77.chat.model.MediaFileModel;
import com.kasa77.modal.game_rates.GameRatesModal;
import com.kasa77.modal.history_data_modal.debithistory.credithistory.CreditHistoryData;
import com.kasa77.modal.history_data_modal.debithistory.DebitHistoryData;
import com.kasa77.modal.fund_request_modal.request_history.FundRequestMainModal;
import com.kasa77.modal.history_data_modal.jackpotbidhistory.JackpotHistoryData;
import com.kasa77.modal.history_data_modal.morningdashboardbidhistory.MorningDashboardData;
import com.kasa77.modal.history_data_modal.starlinebidshistory.StarlineBidsData;
import com.kasa77.modal.ifsc_check.IFSCDetail;
import com.kasa77.modal.jackpot_modal.gametype_id.GameTypeIdList;
import com.kasa77.modal.history_data_modal.jackpotresulthistory.JackpotResultData;
import com.kasa77.modal.kuber_dashboard_games.GameTypeDashboardModal;
import com.kasa77.modal.kuber_starline.game_type.KsGameTypeModel;
import com.kasa77.modal.history_data_modal.starlineresulthistory.StarlineResultData;
import com.kasa77.modal.profile_details_modal.ProfileDetailsModal;
import com.kasa77.modal.static_data.howtoplay.HowToPlayData;
import com.kasa77.modal.static_data.news.NewsData;
import com.kasa77.modal.static_data.noticeboard.NoticeBoardData;
import com.kasa77.modal.static_data.notification.NotificationData;
import com.kasa77.modal.static_data.profilenote.ProfileNoteData;
import com.kasa77.modal.static_data.walletcontact.WalletContactData;
import com.kasa77.modal.transaction_statement.TransactionStatementModal;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitApiClient {

    /********************************
     *  user login apis
     *******************************/
    @Headers("Content-Type: application/json")//AuthReg
    @POST("api/user/register")
    Call<ResponseBody> registerUser(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/login")
    Call<ResponseBody> loginUser(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/sendOTP")
    Call<ResponseBody> sendOTP(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/deviceChnage")
    Call<ResponseBody> deviceChange(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user")
    Call<ResponseBody> getOtp(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/verifyMobile")
    Call<ResponseBody> verifyMobile(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/mpinLogin")
    Call<ResponseBody> loginWithMpin(@Body RequestBody body);


    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/checkUsername")
    Call<ResponseBody> checkUsername(@Body RequestBody body);

    @Headers("Content-Type: application/json")//No Auth
    @POST("api/user/logout")
    Call<ResponseBody> userLogout(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/firebaseUpdate")
    Call<ResponseBody> updateFirebaseToken(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/ideas")
    Call<ResponseBody> submitIdea(@Body RequestBody body);

    /********************************
     *  user mpin apis
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("MPIN/setMpin")
    Call<ResponseBody> setMpin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("MPIN")
    Call<ResponseBody> sendMpinOTP(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("MPIN/changeMpin")
    Call<ResponseBody> changeMpin(@Body RequestBody body);

    /********************************
     *  user profile update apis
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("profile/addAddress")
    Call<ResponseBody> updateProfileAddress(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("profile/bankdetails")
    Call<ResponseBody> updateProfileBankDetail(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("profile/checkRequest")
    Call<ResponseBody> checkWithdrawRequest(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("profile/checkAccountNumber")
    Call<ResponseBody> checkAccountNumber(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("profile/phoneNumber")
    Call<ResponseBody> updateProfileContactDetail(@Body RequestBody body);

    /********************************
     *  fund request apis
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("fundreq/addFund")
    Call<ResponseBody> requestAddFund(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("fundreq/withdrawFund")
    Call<ResponseBody> requestWithdrawFund(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("fundreq/idsUpi")
    Call<ResponseBody> getUPI(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("fundreq/newAutoPaymentUpi")
    Call<ResponseBody> autoPaymentUpi(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("fundreq/withdrawText")
    Call<ResponseBody> withdrawText(@Body RequestBody body);

    /*********************************************************
     *          history apis
     *
     * *****************************************************************/

    @Headers("Content-Type: application/json")//Auth
    @POST("history/fundRequestHistory")
    Call<FundRequestMainModal> fundRequestHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/stalineBids")
    Call<StarlineBidsData> starlineBidsHistory(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/abBids")
    Call<JackpotHistoryData> jackpotBidHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/gameBids")
    Call<MorningDashboardData> morningDashboardBidHistory(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("history/andarBaharRequestHistory")
    Call<JackpotResultData> jackpotResultHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("history/starlineHistory")
    Call<StarlineResultData> starlineResultHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("history/creditRequestHistory")
    Call<CreditHistoryData> creditRequestHistory(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("history/debitRequestHistory")
    Call<DebitHistoryData> debitRequestHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/walletHistory")
    Call<TransactionStatementModal> transactionHistory(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/gameRates")
    Call<GameRatesModal> gameRates();


    /*************************************************************
     *      Get method apj how to playnn1 , profile note, noticeboard,  notification,news data,wallet contact .
     * ***************************************************************/
    @GET("htp")
    Call<HowToPlayData> howToPlayData();

    @GET("htp/noticeBoard")
    Call<NoticeBoardData> noticeBoardDate();

    @GET("htp/profileNote")
    Call<ProfileNoteData> profileNote();

    @GET("htp/walletContact")
    Call<WalletContactData> walletContact();

    @GET("nnData/notification")
    Call<NotificationData> notificationData();

    @GET("nnData/news")
    Call<NewsData> newsData();

    /********************************
     *  Kuber starline game   and Id's
     *******************************/
//    @GET("result/starLineResult")
//    Call<KuberStarlineGameModel> getStarLineGameTimeList();
    @GET("result/starLineResultTest")
    Call<ResponseBody> getStarLineGameTimeList();

    @GET("gameTypes/starLineGameType")
    Call<KsGameTypeModel> kuberStarlineGameTypeId();

    /********************************
     *  SubmitBids
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("Bids/starLineBids")
    Call<ResponseBody> submitKSBids(@Body RequestBody body);


    /*******************************************
     *    Kuber jackpot games  and Id's
     * *******************************************/
    @GET("result/ABgameResultTest")
    Call<ResponseBody> kuberJackpotGameData();

//    @GET("result/ABgameResult")
//    Call<JackpotGameList> kuberJackpotGameData();

    @GET("gameTypes/ABgameGameType")
    Call<GameTypeIdList> kuberJackpotGameTypeId();

    /********************************
     *  Submit Jackpot Bids
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("Bids/abBids")
    Call<ResponseBody> submitJackpotBids(@Body RequestBody body);


    /*******************************************
     *    Kuber morning dashboard games and Id's
     * *******************************************/

//    @GET("result/gameResult")
//    Call<KuberDashboardMainModal> kuberMorningDashboardData();
    @GET("result/gameResultTest")
    Call<ResponseBody> kuberMorningDashboardData();

    @POST("result/daysGameBids")
    Call<ResponseBody> getDayGameBids(@Body RequestBody body);


    @POST("result/daySession")
    Call<ResponseBody> getDaySession(@Body RequestBody body);

    @GET("gameTypes/gameType")
    Call<GameTypeDashboardModal> kuberMorningDashboardGameId();


    /********************************
     *  Submit kuber morning Bids
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("Bids/game_bids")
    Call<ResponseBody> submitKuberMorningGame(@Body RequestBody body);

    /********************************
     *  Submit kuber morning Bids
     *******************************/
    @Headers("Content-Type: application/json")//Auth
    @POST("profile/userProfile")
    Call<ProfileDetailsModal> profileData(@Body RequestBody body);


    /**************************************************
     *         forgot password and mpin api
     * ****************************************************/

    @Headers("Content-Type: application/json")//Auth
    @POST("recovery/sendOTP")
    Call<ResponseBody> getForgotPasswordOtp(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("recovery/verifyOTP")
    Call<ResponseBody> verifyOtp(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("recovery/resetPassword")
    Call<ResponseBody> resetPassword(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("recovery/mpinReset")
    Call<ResponseBody> resetMPIN(@Body RequestBody body);

    /**********************************************************
     *      Check UserWallet Balance
     * **********************************************************/

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/getBal")
    Call<ResponseBody> userWalletBalance(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/appNotification")
    Call<ResponseBody> appNotificationOnOff(@Body RequestBody body);

    /**********************************************************
     *      File Upload
     * **********************************************************/

    @Headers("Content-Type: application/json")
    @POST("sendImage")
    Call<ResponseBody> uploadImage(@Body RequestBody body);


    @Headers("Content-Type: application/json")
    @POST("nnData/notiCounter")
    Call<ResponseBody> getNotificationCounter(@Body RequestBody body);

    @Multipart
    @POST("sendImageAndroid/upload")
    Call<MediaFileModel> uploadMediaFile(@Part MultipartBody.Part productImage);

    @GET("{ifcs}")
    Call<IFSCDetail> checkIFSC(@Path("ifcs") String ifcs);

    @POST("nnData/appBanner")
    Call<ResponseBody> getAppBanner();

    //history pagination api
    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/gameBidsPagination")
    Call<ResponseBody> getBidHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/stalineBidsPagination")
    Call<ResponseBody> getStarlineBidHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/abBidsPagination")
    Call<ResponseBody> getJackportBidHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("history/fundHistoryPagination")
    Call<ResponseBody> getFundHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("history/creditHistoryPagination")
    Call<ResponseBody> getCreditHistoryPaginatin(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("history/debitHistoryPagination")
    Call<ResponseBody> getDebitHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/walletHistoryPaginatoion")
    Call<ResponseBody> getTranscationHistoryPaginatin(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/getBid")
    Call<ResponseBody> getPassBookDetail(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/walletMinMax")
    Call<ResponseBody> getMinMaxFunding(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/providers")
    Call<ResponseBody> getFilterList(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/gameBidsFilter")
    Call<ResponseBody> getBidFilterList(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/abBidsFilter")
    Call<ResponseBody> getJackpotFilterList(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("bidsHistory/stalineBidsFilter")
    Call<ResponseBody> getStalineFilterList(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("recovery/getUsername")
    Call<ResponseBody> getUsername(@Body RequestBody body);

    @Headers("Content-Type: application/json")//Auth
    @POST("oldBroadcastList")
    Call<ResponseBody> getBroadCast(@Body RequestBody body);


    @Headers("Content-Type: application/json")//Auth
    @POST("nnData/allMode")
    Call<ResponseBody> getPaymentMode();

    @Headers("Content-Type: application/json")
    @GET("{pinCode}")
    Call<ResponseBody> getCityFromPinCode(@Path("pinCode") String pinCode);

}
