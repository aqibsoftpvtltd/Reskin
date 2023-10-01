package com.kasa77.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa77.R
import com.kasa77.adapter.static_data_adapter.NotificationAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.static_data.notification.Datum
import com.kasa77.modal.static_data.notification.NotificationData
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.ConnectionDetector
import kotlinx.android.synthetic.main.fragment_news_notification.*
import retrofit2.Response

class NotificationFragment : Fragment() {

    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_news_notification, container, false)
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

//        tvMessage.visibility = View.VISIBLE
//        tvFragment.text = "Notification"
        Constant.NotificationCounter = 0
        notificationListApi()
    }

    override fun onPause() {
        super.onPause()
        Constant.NotificationCounter = 0
    }

    private fun notificationListApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.notificationList(
                mContext?.let { Dialog(it) },
                retrofitApiClient!!.notificationData(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: NotificationData = result?.body() as NotificationData
                        if (responseBody.status == 1) {
                            if (responseBody.data.size > 0) {
                                val data: ArrayList<Datum> = responseBody.data as ArrayList<Datum>

                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.API_Notification_id,
                                    data.get(0).id
                                )
                                rvNewsNotification!!.setHasFixedSize(true)
                                rvNewsNotification!!.layoutManager = LinearLayoutManager(mContext)
                                val dashboardAdaper = NotificationAdapter(mContext, data)
                                rvNewsNotification!!.adapter = dashboardAdaper
                                dashboardAdaper.notifyDataSetChanged()
                            } else {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = responseBody.message
                            }
                        } else {

                            tvMessage.visibility = View.VISIBLE
                            tvMessage.text = responseBody.message.toString()
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        Alerts.serverError(context, error.toString())
                    }

                })
        }
    }


}