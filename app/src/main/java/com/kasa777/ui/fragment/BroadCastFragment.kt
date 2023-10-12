package com.kasa777.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa777.R
import com.kasa777.adapter.static_data_adapter.BroadCastAdapter
import com.kasa777.chat.BroadCastRetrofitService
import com.kasa777.chat.ChatRetrofitService
import com.kasa777.constant.Constant
import com.kasa777.modal.broadcast.BroadCast
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.ConnectionDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news_notification.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class BroadCastFragment : Fragment() {

    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_broadcast, container, false)
        return rootView
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        Constant.BroadCastCounter = 0
//        tvMessage.visibility = View.VISIBLE
//        tvFragment.text = "Notification"

        notificationListApi()
    }
    private fun notificationListApi() {
        if (cd!!.isNetworkAvailable) {
            BroadCastRetrofitService.getServerResponse(
                context,
                mContext?.let { Dialog(it) },
                ChatRetrofitService.getRetrofit()?.getBroadCast(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: ResponseBody = result?.body() as ResponseBody
                        val jsonObject = JSONObject(responseBody.string())
                        var broadCast = Gson().fromJson(jsonObject.toString(), BroadCast::class.java)
                        if (broadCast.status){
                            if (broadCast.oldBroadcastMessage.size>0){

                                rvNewsNotification!!.setHasFixedSize(true)
                                rvNewsNotification!!.layoutManager = LinearLayoutManager(mContext)
                                val  dashboardAdaper = BroadCastAdapter(mContext, broadCast.oldBroadcastMessage)
                                rvNewsNotification!!.adapter = dashboardAdaper
                                dashboardAdaper.notifyDataSetChanged()
                            }else{
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = "No BroadCasting Found"
                            }
                        }else{

                            tvMessage.visibility = View.VISIBLE
                            tvMessage.text = "No BroadCasting Found"
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