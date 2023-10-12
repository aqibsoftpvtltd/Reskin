package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa777.R
import com.kasa777.adapter.static_data_adapter.NotificationAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.static_data.notification.Datum
import com.kasa777.modal.static_data.notification.NotificationData
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.ConnectionDetector
import kotlinx.android.synthetic.main.fragment_news_notification.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import retrofit2.Response
import java.util.ArrayList

class NotifcationActivity : AppCompatActivity() {

    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifcation_activity_new)

        mContext = this
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        // Set up the UI elements and load notification data
        setupUI()
        notificationListApi()

        val toolbar = findViewById<View>(R.id.toobarsridevi)
        toolbar.toolbarTitle.text = "Notification"
        toolbar.cart.visibility = View.GONE
        toolbar.notificationCount.visibility = View.GONE

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        // Set any UI elements and initialization logic you had in onViewCreated

        // Example: tvMessage.visibility = View.VISIBLE
        // Example: tvFragment.text = "Notification"
        Constant.NotificationCounter = 0

        // Initialize your RecyclerView and adapter here
        rvNewsNotification.setHasFixedSize(true)
        rvNewsNotification.layoutManager = LinearLayoutManager(mContext)
    }

    private fun notificationListApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.notificationList(
                Dialog(applicationContext), retrofitApiClient!!.notificationData(),
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
                                    data[0].id
                                )

                                // Set up your RecyclerView adapter with the data
                                val dashboardAdapter = NotificationAdapter(mContext, data)
                                rvNewsNotification.adapter = dashboardAdapter
                                dashboardAdapter.notifyDataSetChanged()
                            } else {
                                tvMessage.visibility = View.VISIBLE
                                tvDay.visibility = View.GONE
                                tvMessage.text = responseBody.message
                            }
                        } else {
                            tvMessage.visibility = View.VISIBLE
                            tvDay.visibility = View.GONE
                            tvMessage.text = responseBody.message.toString()
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvDay.visibility = View.GONE
                        tvMessage.text = error.toString()
                        Alerts.serverError(mContext, error.toString())
                    }
                })
        }
    }
}
