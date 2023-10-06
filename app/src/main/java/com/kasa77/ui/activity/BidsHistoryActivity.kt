package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kasa77.R
import com.kasa77.adapter.FundHistoryPaginationAdapter
import com.kasa77.adapter.bids_history_adapter.*
import com.kasa77.adapter.history_adapter_pagination.BidHistoryPaginationAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.filterdata.RequestBodyFilter
import com.kasa77.modal.fund_pagination.FundHistory
import com.kasa77.modal.history_data_modal.jackpotresulthistory.JackpotResultData
import com.kasa77.modal.history_data_modal.starlineresulthistory.StarlineResultData
import com.kasa77.modal.history_pagination.BidHistory
import com.kasa77.modal.history_pagination.RecordsItem
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.fragment.FilterBottomSheetFragment
import com.kasa77.utils.*

import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BidsHistoryActivity : BaseActivity(), View.OnClickListener,
    DatePickerFragment.DateDialogListener, FilterBottomSheetFragment.OnApplyFilter {

    private var newwinTypeList: java.util.ArrayList<Int>? = ArrayList()
    private var newgameTypeList: java.util.ArrayList<String>? = ArrayList()
    private var fundHistory: FundHistory? = null
    private var layoutManger: LinearLayoutManager? = null
    private var bidHistoryPaginationAdapter: BidHistoryPaginationAdapter? = null
    private var fundHistoryPaginationAdapter: FundHistoryPaginationAdapter? = null
    private var bidHistory: BidHistory? = null
    private var todayDate = ""
    private var strFrom = ""
    private var walletAmount = 0
    private var caseValue = "1"
    private var starlineResultAdapter: StarlineResultAdapter? = null
    private var jackPotResultAdapter: AndarBaharHistoryAdapter? = null

    private var andarBaharList: ArrayList<com.kasa77.modal.history_data_modal.jackpotresulthistory.Datum> =
        ArrayList()
    private var starLineResultList: ArrayList<com.kasa77.modal.history_data_modal.starlineresulthistory.Datum> =
        ArrayList()

    var filterCheckedIds: java.util.ArrayList<String>? = ArrayList();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        strFrom = intent.getStringExtra("from").toString()

        todayDate = todayDate()
        btnSelectedDate.text = todayDate
        tvMessage.visibility = View.GONE
        init()


        tabFilter.setOnClickListener {
            val ft = getSupportFragmentManager().beginTransaction()
            val newFragment = FilterBottomSheetFragment.newInstance(caseValue)
            newFragment.show(ft, "dialog")
        }
        /**
         * add scroll listener while user reach in bottom load more will call
         */

        layoutManger = LinearLayoutManager(this@BidsHistoryActivity)
        rvHistory.layoutManager = layoutManger


        tabNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var page: String = " "
                if (bidHistory != null) {
                    bidHistory!!.current = bidHistory!!.current + 1
                    page = bidHistory!!.current.toString()
                    Log.e("PAGE", page + "/" + bidHistory!!.totalPage)
                }
                if (fundHistory != null) {
                    fundHistory!!.current = fundHistory!!.current + 1
                    page = fundHistory!!.current.toString()
                    Log.e("PAGE", page + "/" + fundHistory!!.pages)

                }

                when (strFrom) {

                    "starlineBidHistory" -> {
                        if (filterCheckedIds!!.size == 0 && newgameTypeList!!.size == 0 && newwinTypeList!!.size == 0) {
                            starlinebidHistorypaginationApi(page.toString())
                        } else {
                            filterStarlinebidHistorypaginationApi(page.toString())
                        }
                    }
                    "jackpotHistory" -> {
                        if (filterCheckedIds!!.size == 0 && newgameTypeList!!.size == 0 && newwinTypeList!!.size == 0) {
                            jackpotBidHistorypaginationApi(page.toString())
                        } else {
                            filterJackpotBidHistorypaginationApi(page.toString())
                        }
                    }
                    "morningDashboardHistory" -> {
                        if (filterCheckedIds!!.size == 0 && newgameTypeList!!.size == 0 && newwinTypeList!!.size == 0) {
                            bidHistorypaginationApi(page.toString())
                        } else
                            filterBidHistorypaginationApi(page.toString())
                    }
                    "fundRequestHistory" -> {
                        fundRequestHistorypaginationApi(page.toString())
                    }
                    "creditHistory" -> {
                        creditRequestHistorypaginationApi(page.toString())
                    }
                    "debitHistory" -> {
                        debitRequestHistorypaginationApi(page.toString())
                    }

                }
            }
        })
    }


    private fun init() {
        val intent = intent
        strFrom = intent.getStringExtra("from").toString()
        when (strFrom) {
            "starlineHistory" -> {

                tabFilter.visibility = View.GONE
                tabNext.visibility = View.GONE
                toolbarTitle.text = "Starline Result History"
                val dialog = DatePickerFragment()
                dialog.show(supportFragmentManager, Constant.DIALOG_DATE)
                btnSelectedDate.visibility = View.VISIBLE
                llDate.visibility = View.VISIBLE
                tvText.visibility = View.VISIBLE
                tvText.text = "Starline Result By Date "
                todayDate = todayDate()
                btnSelectedDate.text = todayDate
                starlineResultHistoryApi(todayDate)

            }
            "starlineBidHistory" -> {
                caseValue = "3"
                tabFilter.visibility = View.VISIBLE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Starline Bid History"
                val recordlist: List<RecordsItem> = ArrayList()
                bidHistoryPaginationAdapter =
                    BidHistoryPaginationAdapter(
                        this@BidsHistoryActivity, recordlist

                    )
                rvHistory.adapter = bidHistoryPaginationAdapter
                starlinebidHistorypaginationApi("1")
            }
            "jackpotHistory" -> {
                caseValue = "2"
                tabFilter.visibility = View.VISIBLE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Jackpot Bid History"
                val recordlist: List<RecordsItem> = ArrayList()
                bidHistoryPaginationAdapter =
                    BidHistoryPaginationAdapter(
                        this@BidsHistoryActivity, recordlist

                    )
                rvHistory.adapter = bidHistoryPaginationAdapter
                jackpotBidHistorypaginationApi("1")
            }
            "morningDashboardHistory" -> {
                tabFilter.visibility = View.VISIBLE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Bid History"
                caseValue = "1"
                val recordlist: List<RecordsItem> = ArrayList()
                bidHistoryPaginationAdapter =
                    BidHistoryPaginationAdapter(
                        this@BidsHistoryActivity, recordlist
                    )
                rvHistory.adapter = bidHistoryPaginationAdapter
                bidHistorypaginationApi("1")

            }
            "fundRequestHistory" -> {
                tabFilter.visibility = View.GONE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Fund Request History"
                val recordlist: List<com.kasa77.modal.fund_pagination.RecordsItem> =
                    ArrayList()
                fundHistoryPaginationAdapter =
                    FundHistoryPaginationAdapter(
                       recordlist
                    )
                rvHistory.adapter = fundHistoryPaginationAdapter
                fundRequestHistorypaginationApi("1")
            }
            "creditHistory" -> {
                tabFilter.visibility = View.GONE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Approved Credit History"
                val recordlist: List<com.kasa77.modal.fund_pagination.RecordsItem> =
                    ArrayList()
                fundHistoryPaginationAdapter =
                    FundHistoryPaginationAdapter(
                        recordlist)
                rvHistory.adapter = fundHistoryPaginationAdapter
                creditRequestHistorypaginationApi("1")
            }
            "debitHistory" -> {
                tabFilter.visibility = View.GONE
                tabNext.visibility = View.VISIBLE
                toolbarTitle.text = "Approved Debit History"
                val recordlist: List<com.kasa77.modal.fund_pagination.RecordsItem> =
                    ArrayList()
                fundHistoryPaginationAdapter =
                    FundHistoryPaginationAdapter(
                        recordlist)
                rvHistory.adapter = fundHistoryPaginationAdapter
                debitRequestHistorypaginationApi("1")
            }
            "andarBaharHistory" -> {
                tabFilter.visibility = View.GONE
                tabNext.visibility = View.GONE
                toolbarTitle.text = "Jackpot Result History"
                val dialog = DatePickerFragment()
                dialog.show(supportFragmentManager, Constant.DIALOG_DATE)
                //  dialog.isCancelable = false
                btnSelectedDate.visibility = View.VISIBLE
                dateFilter.visibility=View.VISIBLE
                llDate.visibility = View.VISIBLE
                tvText.visibility = View.VISIBLE
                tvText.text = "Andar Bahar Result By Date "
                todayDate = todayDate()
                btnSelectedDate.text = todayDate
                jackpotResultHistoryApi(todayDate)
                //  openDatePickerForJackpotResult()
            }
        }

        btnSelectedDate.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, Constant.DIALOG_DATE)
            //  dialog.isCancelable = false
        }
        dateFilter.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, Constant.DIALOG_DATE)
            //  dialog.isCancelable = false
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onFinishDialog(date: Date) {
        tvMessage.visibility = View.GONE
        andarBaharList.clear()
        starLineResultList.clear()

        if (strFrom == "starlineHistory") {
            rvHistory.setHasFixedSize(true)
            rvHistory.layoutManager = LinearLayoutManager(this@BidsHistoryActivity)
            starlineResultAdapter =
                StarlineResultAdapter(
                    this@BidsHistoryActivity,
                    starLineResultList
                )
            rvHistory.adapter = starlineResultAdapter
            starlineResultAdapter!!.notifyDataSetChanged()
            todayDate = formatDate(date)
            btnSelectedDate.text = todayDate
            starlineResultHistoryApi(todayDate)
        } else if (strFrom == "andarBaharHistory") {
            rvHistory.setHasFixedSize(true)
            rvHistory.layoutManager =
                LinearLayoutManager(this@BidsHistoryActivity)
            jackPotResultAdapter =
                AndarBaharHistoryAdapter(this@BidsHistoryActivity, andarBaharList)
            rvHistory.adapter = jackPotResultAdapter
            jackPotResultAdapter!!.notifyDataSetChanged()
            todayDate = formatDate(date)
            btnSelectedDate.text = todayDate
            jackpotResultHistoryApi(todayDate)
        }

    }

    @SuppressLint("SimpleDateFormat")
    public fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val hireDate = sdf.format(date)
        return hireDate
    }

    override fun onClick(p0: View?) {
    }

    private fun todayDate(): String {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
        } catch (e: Exception) {
            "Error"
        }
    }


    // history apis


    private fun starlineResultHistoryApi(selectedDate: String) {
        if (cd!!.isNetworkAvailable) {
            val date = DateFormatToDisplay().parseDateToddMMyyyy(selectedDate)

            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("date", selectedDate)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.starlineResultHistoryData(
                Dialog(this@BidsHistoryActivity),
                retrofitApiClientAuth.starlineResultHistory(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val mainModal: StarlineResultData =
                            result!!.body() as StarlineResultData
                        starLineResultList.clear()
                        if (mainModal.status == 1) {
                            if (mainModal.data.size > 0) {
//                                val resId = R.anim.layout_animation_left_to_right
//                                val animation: LayoutAnimationController =
//                                    android.view.animation.AnimationUtils.loadLayoutAnimation(
//                                        this@BidsHistoryActivity,
//                                        resId
//                                    )
                                rvHistory.setHasFixedSize(true)
                                rvHistory.layoutManager =
                                    LinearLayoutManager(this@BidsHistoryActivity)
                                starlineResultAdapter =
                                    StarlineResultAdapter(
                                        this@BidsHistoryActivity,
                                        starLineResultList
                                    )
                                rvHistory.adapter = starlineResultAdapter
//                                rvHistory.layoutAnimation = animation
                                val list = mainModal.data;
                                Helper(mContext).getSortedListStarlineResult(list)

                                starLineResultList.addAll(list)
                                starlineResultAdapter!!.notifyDataSetChanged()
                            } else {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = "No data found"
                                Alerts.AlertDialogWarning(
                                    this@BidsHistoryActivity,
                                    mainModal.message,""
                                )
                            }
                        } else {
                            tvMessage.visibility = View.VISIBLE
                            tvMessage.text = mainModal.message.toString()
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun jackpotResultHistoryApi(selectedDate: String) {
        if (cd!!.isNetworkAvailable) {

            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("date", selectedDate)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.jackpotResultData(Dialog(this@BidsHistoryActivity),
                retrofitApiClientAuth.jackpotResultHistory(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val mainModal: JackpotResultData =
                            result!!.body() as JackpotResultData
                        andarBaharList.clear()
                        if (mainModal.status == 1) {
                            var fundRequestAdapter =
                                AndarBaharHistoryAdapter(
                                    this@BidsHistoryActivity,
                                    andarBaharList
                                )

                            if (mainModal.data.size > 0) {
//                                val resId = R.anim.layout_animation_left_to_right
//                                val animation: LayoutAnimationController =
//                                    android.view.animation.AnimationUtils.loadLayoutAnimation(
//                                        this@BidsHistoryActivity,
//                                        resId
//                                    )
                                rvHistory.setHasFixedSize(true)
                                rvHistory.layoutManager =
                                    LinearLayoutManager(this@BidsHistoryActivity)
                                jackPotResultAdapter =
                                    AndarBaharHistoryAdapter(
                                        this@BidsHistoryActivity,
                                        andarBaharList
                                    )
                                rvHistory.adapter = jackPotResultAdapter
                                val list = mainModal.data;
                                Helper(mContext).getSortedListJackpotResult(list)

                                andarBaharList.addAll(list)

                                jackPotResultAdapter!!.notifyDataSetChanged()
                            } else {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = "No data found"
                                Alerts.AlertDialogWarning(
                                    this@BidsHistoryActivity,
                                    mainModal.message,""
                                )

                            }
                            fundRequestAdapter.notifyDataSetChanged()
                        } else {
                            tvMessage.visibility = View.VISIBLE
                            tvMessage.text = mainModal.message.toString()
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }


    // Pagination new API
    private fun bidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getBidHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun starlinebidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getStarlineBidHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun jackpotBidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getJackportBidHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun fundRequestHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getFundHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            fundHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                FundHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + fundHistory!!.current + "/" + fundHistory!!.pages + ")"
                            fundHistoryPaginationAdapter!!.addItems(fundHistory!!.records);

                        } else {

                            if (fundHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun creditRequestHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getCreditHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            fundHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                FundHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + fundHistory!!.current + "/" + fundHistory!!.pages + ")"

                            fundHistoryPaginationAdapter!!.addItems(fundHistory!!.records);


                        } else {

                            if (fundHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun debitRequestHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("skipValue", skipValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getDebitHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            fundHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                FundHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + fundHistory!!.current + "/" + fundHistory!!.pages + ")"

                            fundHistoryPaginationAdapter!!.addItems(fundHistory!!.records);


                        } else {

                            if (fundHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onApply(
        checkedIds: java.util.ArrayList<String>,
        gameTypeList: java.util.ArrayList<String>?,
        winTypeList: java.util.ArrayList<Int>?,
        caseValue: String?
    ) {
//        Alerts.show(this@BidsHistoryActivity, checkedIds.size.toString())
        filterCheckedIds = checkedIds;
        newgameTypeList = gameTypeList;
        newwinTypeList = winTypeList;
        if (caseValue.equals("1")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            filterBidHistorypaginationApi("1")
        }
        if (caseValue.equals("2")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            filterJackpotBidHistorypaginationApi("1")

        }
        if (caseValue.equals("3")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            filterStarlinebidHistorypaginationApi("1")

        }
    }

    override fun onClear(caseValue: String?) {
        filterCheckedIds!!.clear();
        newgameTypeList!!.clear();
        newwinTypeList!!.clear();
        if (caseValue.equals("1")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            bidHistorypaginationApi("1")
        }
        if (caseValue.equals("2")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            jackpotBidHistorypaginationApi("1")

        }
        if (caseValue.equals("3")) {
            bidHistoryPaginationAdapter!!.clear()
            bidHistoryPaginationAdapter!!.notifyDataSetChanged()
            tvMessage.visibility = View.GONE
            starlinebidHistorypaginationApi("1")

        }
    }


    //filterApi
    private fun filterBidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)

            val objectReqeust = RequestBodyFilter()
            objectReqeust.userId = userLoginId
            objectReqeust.skipValue = Integer.parseInt(skipValue)
            objectReqeust.providerId = filterCheckedIds
            objectReqeust.session = newgameTypeList
            objectReqeust.status = newwinTypeList

            val json = Gson().toJson(objectReqeust);
//

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                json
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getBidFilterList(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun filterStarlinebidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val objectReqeust = RequestBodyFilter()
            objectReqeust.userId = userLoginId
            objectReqeust.skipValue = Integer.parseInt(skipValue)
            objectReqeust.providerId = filterCheckedIds
            objectReqeust.session = newgameTypeList
            objectReqeust.status = newwinTypeList
            val json = Gson().toJson(objectReqeust);
//

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                json
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getStalineFilterList(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

    private fun filterJackpotBidHistorypaginationApi(skipValue: String) {
        ll_loading.visibility = View.VISIBLE
        txt_loading.text = "Loading..."
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val objectReqeust = RequestBodyFilter()
            objectReqeust.userId = userLoginId
            objectReqeust.skipValue = Integer.parseInt(skipValue)
            objectReqeust.providerId = filterCheckedIds
            objectReqeust.session = newgameTypeList
            objectReqeust.status = newwinTypeList
            val json = Gson().toJson(objectReqeust);
//

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                json
            )
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClientAuth.getJackpotFilterList(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            bidHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                BidHistory::class.java
                            )
                            tvNext.text =
                                "Next (" + bidHistory!!.current + "/" + bidHistory!!.totalPage + ")"
                            bidHistoryPaginationAdapter!!.addItems(bidHistory!!.records);


                        } else {
                            if (bidHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@BidsHistoryActivity,
                                jsonresponse.optString("message"),""
                            )

                        }


                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        ll_loading.visibility = View.GONE
                        Alerts.serverError(this@BidsHistoryActivity, error.toString())
                    }

                })
        }
    }

}
