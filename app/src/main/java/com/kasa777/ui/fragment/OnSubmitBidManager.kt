package com.kasa777.ui.fragment

import android.app.Dialog
import android.content.Context
import android.util.Log
import com.kasa777.constant.Constant
import com.kasa777.modal.dashboard_gamelist.Result
import com.kasa777.modal.game_bid_data.BidData
import com.kasa777.modal.game_date_list.DateObject
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.AppPreference
import com.kasa777.utils.ConnectionDetector
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


public class OnSubmitBidManager {


    public fun submitBids(
        mContext: Context,
        retrofitApiClient: RetrofitApiClient,
        cd: ConnectionDetector,
        bidItems: ArrayList<BidData>,
        providerResultData: Result,
        selectedDateObject: DateObject,
        strGameSession: String,
        onSubmitBid: OnSubmitBid
    ) {
        val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
        val userName =
            AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
        var currentTime: String = ""
        try {
            currentTime = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH).format(Date())
        } catch (e: Exception) {

        }

        val formatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        // you can change format of date
        // you can change format of date
        val date: Date = formatter.parse(bidItems!!.get(0).gameDate)
        val timeStampDate = Timestamp(date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date2 = format.parse(timeStampDate.toString())
        val timestamp:Int = (date2.time/1000).toInt()


        val jsonArray = JSONArray()
        bidItems!!.forEach {





            val jsonObject = JSONObject()
            jsonObject.put("userId", userLoginId)
            jsonObject.put("providerId", providerResultData!!.providerId)
            jsonObject.put("gameTypeId", it.gameTypeId)
            jsonObject.put("providerName", providerResultData!!.providerName)
            jsonObject.put("gameTypeName", it.gameType)
            jsonObject.put("gameTypePrice", it.gameTypePrice)
            jsonObject.put("userName", userName)
            jsonObject.put("mobileNumber", userMobile)
            jsonObject.put("bidDigit", it.digits)
            jsonObject.put("biddingPoints", it.points)
            jsonObject.put("gameSession", it.gemeSession)
            jsonObject.put("winStatus", 0)
            jsonObject.put("gameWinPoints", "0")
            jsonObject.put("gameDate", it.gameDate)
            jsonObject.put("updatedAt", "--:--:----")
            jsonObject.put("createdAt", currentTime)
            jsonObject.put("dateStamp", timestamp)
            jsonArray.put(jsonObject)
        }

        var bidSum = 0
        bidItems!!.forEach {
            bidSum += it.points.toInt()
        }

        val finalObject = JSONObject()
        finalObject.put("userId", userLoginId)
        finalObject.put("bidSum", bidSum)
        finalObject.put("providerId", providerResultData!!.providerId)
        finalObject.put("gameDate", selectedDateObject!!.date)
        finalObject.put("gameSession", strGameSession)
        finalObject.put("bidData", jsonArray)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        Log.e("OnRequest", finalObject.toString())
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(mContext),
                retrofitApiClient!!.submitKuberMorningGame(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            Log.e("OnResponse", responseObject.toString())
                            onSubmitBid.onSuccess(responseObject.toString())

                        } catch (e: Exception) {
                            onSubmitBid.onFail(e.message)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        onSubmitBid.onFail(error)
                    }
                })

        }
    }


    public fun submitStarLineBids(
        mContext: Context,
        retrofitApiClient: RetrofitApiClient,
        cd: ConnectionDetector,
        bidItems: ArrayList<BidData>,
        providerResultData: com.kasa777.ui.fragment.startline_game_fragment.modal.Result,
        selectedDateObject: String,
        strGameSession: String,
        onSubmitBid: OnSubmitBid
    ) {
        val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
        val userName =
            AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
        var currentTime: String = ""
        try {
            currentTime = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH).format(Date())
        } catch (e: Exception) {

        }

        val formatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        // you can change format of date
        // you can change format of date
        val date: Date = formatter.parse(bidItems!!.get(0).gameDate)
        val timeStampDate = Timestamp(date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date2 = format.parse(timeStampDate.toString())
        val timestamp:Int = (date2.time/1000).toInt()


        val jsonArray = JSONArray()
        bidItems!!.forEach {
            val jsonObject = JSONObject()
            jsonObject.put("userId", userLoginId)
            jsonObject.put("providerId", providerResultData!!.providerId)
            jsonObject.put("gameTypeId", it.gameTypeId)
            jsonObject.put("providerName", providerResultData!!.providerName)
            jsonObject.put("gameTypeName", it.gameType)
            jsonObject.put("gameTypePrice", it.gameTypePrice)
            jsonObject.put("userName", userName)
            jsonObject.put("mobileNumber", userMobile)
            jsonObject.put("bidDigit", it.digits)
            jsonObject.put("biddingPoints", it.points)
            jsonObject.put("gameSession", it.gemeSession)
            jsonObject.put("winStatus", 0)
            jsonObject.put("gameWinPoints", "0")
            jsonObject.put("gameDate", it.gameDate)
            jsonObject.put("updatedAt", "--:--:----")
            jsonObject.put("createdAt", currentTime)
            jsonObject.put("dateStamp", timestamp)
            jsonArray.put(jsonObject)
        }

        var bidSum = 0
        bidItems!!.forEach {
            bidSum += it.points.toInt()
        }

        val finalObject = JSONObject()
        finalObject.put("userId", userLoginId)
        finalObject.put("bidSum", bidSum)
        finalObject.put("providerId", providerResultData!!.providerId)
        finalObject.put("gameDate", selectedDateObject)
        finalObject.put("gameSession", strGameSession)
        finalObject.put("bidData", jsonArray)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        Log.e("OnRequest", finalObject.toString())
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(mContext),
                retrofitApiClient!!.submitKSBids(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            Log.e("OnResponse", responseObject.toString())
                            onSubmitBid.onSuccess(responseObject.toString())

                        } catch (e: Exception) {
                            onSubmitBid.onFail(e.message)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        onSubmitBid.onFail(error)
                    }
                })

        }
    }


    public fun submitJackportBids(
        mContext: Context,
        retrofitApiClient: RetrofitApiClient,
        cd: ConnectionDetector,
        bidItems: ArrayList<BidData>,
        providerResultData:com.kasa777.ui.fragment.jackpot_fragments.modal.Result,
        selectedDateObject: String,
        strGameSession: String,
        onSubmitBid: OnSubmitBid
    ) {
        val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
        val userName =
            AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
        var currentTime: String = ""
        try {
            currentTime = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH).format(Date())
        } catch (e: Exception) {

        }

        val formatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        // you can change format of date
        // you can change format of date
        val date: Date = formatter.parse(bidItems!!.get(0).gameDate)
        val timeStampDate = Timestamp(date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date2 = format.parse(timeStampDate.toString())
        val timestamp:Int = (date2.time/1000).toInt()

        val jsonArray = JSONArray()
        bidItems!!.forEach {
            val jsonObject = JSONObject()
            jsonObject.put("userId", userLoginId)
            jsonObject.put("providerId", providerResultData!!.providerId)
            jsonObject.put("gameTypeId", it.gameTypeId)
            jsonObject.put("providerName", providerResultData!!.providerName)
            jsonObject.put("gameTypeName", it.gameType)
            jsonObject.put("gameTypePrice", it.gameTypePrice)
            jsonObject.put("userName", userName)
            jsonObject.put("mobileNumber", userMobile)
            jsonObject.put("bidDigit", it.digits)
            jsonObject.put("biddingPoints", it.points)
            jsonObject.put("gameSession", it.gemeSession)
            jsonObject.put("winStatus", 0)
            jsonObject.put("gameWinPoints", "0")
            jsonObject.put("gameDate", it.gameDate)
            jsonObject.put("updatedAt", "--:--:----")
            jsonObject.put("createdAt", currentTime)
            jsonObject.put("dateStamp", timestamp)
            jsonArray.put(jsonObject)
        }

        var bidSum = 0
        bidItems!!.forEach {
            bidSum += it.points.toInt()
        }

        val finalObject = JSONObject()
        finalObject.put("userId", userLoginId)
        finalObject.put("bidSum", bidSum)
        finalObject.put("providerId", providerResultData!!.providerId)
        finalObject.put("gameDate", selectedDateObject)
        finalObject.put("gameSession", strGameSession)
        finalObject.put("bidData", jsonArray)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        Log.e("OnRequest", finalObject.toString())
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(mContext),
                retrofitApiClient!!.submitJackpotBids(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            Log.e("OnResponse", responseObject.toString())
                            onSubmitBid.onSuccess(responseObject.toString())

                        } catch (e: Exception) {
                            onSubmitBid.onFail(e.message)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        onSubmitBid.onFail(error)
                    }
                })

        }
    }


}