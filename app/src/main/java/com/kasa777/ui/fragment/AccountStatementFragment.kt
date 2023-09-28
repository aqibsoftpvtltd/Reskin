package com.kasa777.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kasa777.R
import com.kasa777.adapter.AccountPaginationAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.transaction_statement.AccountHistory
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.ConnectionDetector
import kotlinx.android.synthetic.main.fragment_account_statement.*
import kotlinx.android.synthetic.main.fragment_account_statement.btn_next
import kotlinx.android.synthetic.main.fragment_account_statement.tvMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response


class AccountStatementFragment : Fragment() {



    private lateinit var paginationAdapter: AccountPaginationAdapter
    private var accountHistory: AccountHistory? = null
    private lateinit var layoutManger: LinearLayoutManager
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClientAuth: RetrofitApiClient? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_account_statement, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit()
        tvMessage.visibility = View.GONE

        val recordlist: List<com.kasa777.modal.transaction_statement.RecordsItem> =
            ArrayList()
        paginationAdapter =
            AccountPaginationAdapter(
                mContext, recordlist

            )
        rvAccountStatement.adapter = paginationAdapter
        layoutManger = LinearLayoutManager(mContext)
        rvAccountStatement.layoutManager = layoutManger


        btn_next.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var page: String = " "
                if (accountHistory != null) {
                    accountHistory!!.current = accountHistory!!.current + 1
                    page = accountHistory!!.current.toString()
                    Log.e("PAGE", page + "/" + accountHistory!!.totalPage)
                }
                accountHistorypaginationApi(page.toString())

            }});
        accountHistorypaginationApi("1")
    }


    // Pagination new API
    private fun accountHistorypaginationApi(skipValue: String) {
        ll_loading1.visibility = View.VISIBLE
        txt_loading1.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth!!.getTranscationHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        try {
                            ll_loading1!!.visibility = View.GONE
                        } catch (e: Exception) {
                        }
                        if (jsonresponse.optInt("status") == 1) {
                            accountHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                AccountHistory::class.java
                            )
                            paginationAdapter!!.addItems(accountHistory!!.records);
                            btn_next.text =
                                "Next (" + accountHistory!!.current + "/" + accountHistory!!.totalPage + ")"
                        } else {
                            if (paginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(context,jsonresponse.optString("message"))
                        }

                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        try {
                            ll_loading1.visibility = View.GONE
                            Alerts.serverError(context, error.toString())
                        } catch (e: Exception) {
                        }
                    }

                })
        }
    }

}