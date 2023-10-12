package com.kasa777.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.utils.AppPreference
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.ConnectionDetector
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_setting.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject


class SettingFragment : Fragment() {
    private var rootview: View? = null
    private var mContext: Context? = null
    private var retrofitApiClientAuth: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview =
            inflater.inflate(com.kasa777.R.layout.fragment_setting, container, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit()

        if (AppPreference.getBooleanPreference(mContext, Constant.isLoginWithMpin)) {
            rbLoginUsername.isChecked = false
            rbLoginMpin.isChecked = true
        } else {
            rbLoginUsername.isChecked = true
            rbLoginMpin.isChecked = false
        }

        var isMpinGenerated = AppPreference.getBooleanPreference(mContext, Constant.IsMpinGenerate)
        if (isMpinGenerated == true) {
            rbLoginMpin.visibility == View.VISIBLE
        } else {
            rbLoginMpin.visibility = View.GONE
        }

        switchMainNotification.isChecked = AppPreference.getBooleanPreference(mContext,Constant.mainNotification)
        switchGameNotification.isChecked = AppPreference.getBooleanPreference(mContext,Constant.gameNotification)
        switchStarlineNotification.isChecked = AppPreference.getBooleanPreference(mContext,Constant.starLineNotification)
        switchJackpotNotification.isChecked = AppPreference.getBooleanPreference(mContext,Constant.andarBaharNotification)

        rgLoginPreference.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = rootview!!.findViewById(checkedId)
            if (radio.id == R.id.rbLoginMpin) {
                AppPreference.setBooleanPreference(mContext, Constant.isLoginWithMpin, true)
                AppPreference.setBooleanPreference(mContext, Constant.isLoginWithUsername, false)
            } else {
                AppPreference.setBooleanPreference(mContext, Constant.isLoginWithMpin, false)
                AppPreference.setBooleanPreference(mContext, Constant.isLoginWithUsername, true)
            }
        }

        switchMainNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(1,true)

            } else {
                notificationOnOffApi(1,false)
            }

        }
        switchGameNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(2,true)

            } else {
                notificationOnOffApi(2,false)
            }

        }
        switchStarlineNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(3,true)

            } else {
                notificationOnOffApi(3,false)
            }
        }
        switchJackpotNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(4,true)

            } else {
                notificationOnOffApi(4,false)
            }

        }

    }

    private fun notificationOnOffApi(notificationId :Int,notificationOnOff :Boolean) {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("notificationId", notificationId)
            mObject.put("notificationOnOff", notificationOnOff)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireContext()),
                retrofitApiClientAuth!!.appNotificationOnOff(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: retrofit2.Response<*>?) {
                        val mainModal: ResponseBody = result!!.body() as ResponseBody
                        try {
                            val jsonObject  = JSONObject(mainModal.string())
                            if (jsonObject.getInt("status") ==1){
                              //  Alerts.show(mContext,jsonObject.getString("message"))

                                if (notificationId == 1){
                                    AppPreference.setBooleanPreference(mContext, Constant.mainNotification, notificationOnOff)
                                }
                                if (notificationId == 2){
                                    AppPreference.setBooleanPreference(mContext, Constant.gameNotification, notificationOnOff)
                                }
                                if (notificationId == 3){
                                    AppPreference.setBooleanPreference(mContext, Constant.starLineNotification, notificationOnOff)
                                }
                                if (notificationId == 4){
                                    AppPreference.setBooleanPreference(mContext, Constant.andarBaharNotification, notificationOnOff)
                                }
                            }
                        } catch (e: Exception) {
                            dialogBoxMessage(e.toString())
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }

                })
        }
    }

    @SuppressLint("InflateParams")
    private fun dialogBoxMessage(string: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView =
            inflater.inflate(R.layout.dialog_view_toast_message, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.txtMessage.text = string
        val btnSubmit = dialogView.btnOk
        btnSubmit.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}