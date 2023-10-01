package com.kasa77.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import com.kasa77.constant.Constant

import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import com.kasa77.ui.activity.SplashActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class LogoutUser(var mContext: Context, var mActivity: Activity?) {

    private var retrofitApiClientAuth: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null

    fun logout() {
        cd = ConnectionDetector(mContext)
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit()
        val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
        val mObject = JSONObject()
        mObject.put("userId", userLoginId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(mContext),
                retrofitApiClientAuth!!.userLogout(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                AppPreference.setBooleanPreference(mContext, Constant.IS_LOGIN,false);
                                val intent = Intent(mContext, SplashActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                mContext.startActivity(intent)

                            } else {
                                Alerts.show(mContext, responseObject.getString("message"))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Alerts.show(mContext, e.message)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(mContext, error.toString())
                    }
                })
        }
    }
}
