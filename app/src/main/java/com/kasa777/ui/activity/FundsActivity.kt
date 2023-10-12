package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa777.R
import com.kasa777.adapter.FundHistoryPaginationAdapter
import com.kasa777.chat.ChatApplication.context
import com.kasa777.constant.Constant
import com.kasa777.modal.PaymentMode
import com.kasa777.modal.TransactionDetails
import com.kasa777.modal.WithdrawText
import com.kasa777.modal.fund_pagination.FundHistory
import com.kasa777.modal.profile_details_modal.Data
import com.kasa777.modal.profile_details_modal.ProfileDetailsModal
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.BaseActivity
import com.kasa777.utils.UpiBottomSheet
import com.google.gson.Gson
import com.kasa777.utils.Helper
import kotlinx.android.synthetic.main.activity_funds.*
import kotlinx.android.synthetic.main.activity_funds.tvWalletAmount
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_withdraw_bank_confirmation.view.*

import kotlinx.android.synthetic.main.dialog_payment_mode.view.*

import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.btnOk
import kotlinx.android.synthetic.main.dialog_withdraw_request.view.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.layout_content_home.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import kotlinx.android.synthetic.main.toolbar.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FundsActivity : BaseActivity(), View.OnClickListener {
    private var selectedApp: String = ""
    private var adminUpiID: String = ""
    private var fundHistory: FundHistory? = null
    private var fundHistoryPaginationAdapter: FundHistoryPaginationAdapter? = null
    private lateinit var layoutManger: LinearLayoutManager
    private var strFrom = ""
    private var walletAmount: Double? = null
    var rp: Int = 0
    var profileData: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funds)


        val recordlist: List<com.kasa777.modal.fund_pagination.RecordsItem> = ArrayList()
        fundHistoryPaginationAdapter = FundHistoryPaginationAdapter(recordlist)

        backBtn.setOnClickListener { onBackClick() }


       // val toolbar = findViewById<View>(R.id.toobarsridevi)

       /* toolbar.*/cart.setOnClickListener{
            startActivity(Intent(mContext, NotifcationActivity::class.java))
        }

        UserName.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        PhoneNumber.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)

        strFrom = intent!!.getStringExtra("from").toString()
        layoutManger = LinearLayoutManager(this@FundsActivity)
        rvFundRequestes.layoutManager = layoutManger
        tabNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var page: String = " "
                if (fundHistory != null) {
                    fundHistory!!.current = fundHistory!!.current + 1
                    page = fundHistory!!.current.toString()
                    Log.e("PAGE", page + "/" + fundHistory!!.pages)

                }
                when (strFrom) {

                    "requestHistory" -> {
                        fundRequestHistorypaginationApi(page.toString())
                    }

                }
            }
        });

        hundered.setOnClickListener {
            etRequestPoints.setText(hundered.text.toString())
        }
        fiveHundered.setOnClickListener {
            etRequestPoints.setText(fiveHundered.text.toString())
        }
        twentyFiveHundered.setOnClickListener {
            etRequestPoints.setText(twentyFiveHundered.text.toString())
        }
        thousand.setOnClickListener {
            etRequestPoints.setText(thousand.text.toString())
        }
        fiveThousand.setOnClickListener {
            etRequestPoints.setText(fiveThousand.text.toString())
        }
        tenThousand.setOnClickListener {
            etRequestPoints.setText(tenThousand.text.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        init()
    }


    public fun userWalletCheck() {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@FundsActivity),
                retrofitApiClientAuth?.userWalletBalance(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")

                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonObject = JSONObject(response.string())
                        if (jsonObject.getInt("status") == 1) {
                            val data = jsonObject.getJSONObject("data")
                            walletAmount = data.getDouble("wallet_balance")
                            tvWalletAmount.text = "₹${Helper.getSuffix("${walletAmount}")}"
                            AppPreference.setIntegerPreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE,
                                walletAmount!!.toInt()
                            )
                            AppPreference.setDoublePreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE_FLOAT,
                                walletAmount!!
                            )
                        } else {
                            Alerts.show(mContext, jsonObject.getString("message"))

                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(mContext, error.toString())
                    }
                })

        }
    }

    @SuppressLint("SetTextI18n")
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun init() {
        val intent = intent
        strFrom = intent.getStringExtra("from").toString()
        userWalletCheck()

//            AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
//            AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)


        when (strFrom) {
            "addFund" -> {
                getUpiFromAdmin()
                toolbarTitle.text = mContext.getString(R.string.add_fund)
                //tvSubTitle.text = mContext.getString(R.string.add_fund_request)
                tabNote.visibility=View.GONE
                tabTelegram.visibility=View.GONE
                tabSelection.visibility=View.VISIBLE
                tabHistoryFund.visibility=View.GONE
            }
            "withdrawFund" -> {
                toolbarTitle.text = mContext.getString(R.string.withdraw_fund)
                //tvSubTitle.text = mContext.getString(R.string.total_payment)
               // tabNote.visibility=View.VISIBLE
              //  tabTelegram.visibility=View.VISIBLE
                tabSelection.visibility=View.VISIBLE
                tabHistoryFund.visibility=View.GONE
                getWithdrawTextDetails();
            }
            "requestHistory" -> {
                toolbarTitle.text = mContext.getString(R.string.fund_request_history)
                tabNote.visibility=View.GONE
                tabTelegram.visibility=View.GONE
                tabSelection.visibility=View.GONE
                rvFundRequestes.adapter = fundHistoryPaginationAdapter
                tabHistoryFund.visibility=View.VISIBLE
                fundRequestHistorypaginationApi("1")
            }
        }


//        btnWithdrawpopup.setOnClickListener { dialogWithdrawRequest() }


//COMMENTED CALL FEATURE
//        llCall.setOnClickListener(this)

        tabSendRequest.setOnClickListener {
            when (strFrom) {
                "addFund" -> {
                    getMinMaxFundingAmount(etRequestPoints.text.toString().trim())
                }

                "withdrawFund" -> {
                    validateWithdrawRequestNew(etRequestPoints.text.toString().trim())
                }

                "requestHistory" -> {

                }
            }
        }
    }

    private fun getWithdrawTextDetails() {
        val mObject = JSONObject()
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(Dialog(this@FundsActivity),
            retrofitApiClientAuth.withdrawText(body),
            object : WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {
                            val withdrawText = Gson().fromJson(
                                responseObject.toString(),
                                WithdrawText::class.java)
                            tvNote.setText(withdrawText.data.textMain)
                        } else if (responseObject.getInt("status") == 0) {
                            Alerts.show(this@FundsActivity, responseObject.getString("message"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@FundsActivity, error.toString())
                }
            })
    }

    private fun validateWithdrawRequestNew(amount: String) {
        var requestPoints = 0
        try {
            requestPoints = amount.toInt()
        } catch (e: Exception) {
        }
        val currentTime = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()

        if (requestPoints > walletAmount!!) {
            dialogBoxMessage("Insufficient Wallet Balance.", "cancel")
        } else if (requestPoints < 100 || requestPoints > 100000) {
            dialogBoxMessage(
                "Minimum Withdrawal Amount is 100 & Maximum Withdrawal Amount is 100000.",
                "cancel"
            )
        } else if (currentTime >= 10) {
            profileDetailApi(requestPoints)
        } else {
            dialogBoxMessage("Withdrawal Requesting Time is 10:00 AM to 02:00 PM.", "cancel")
        }
    }

    private fun validateWithdrawRequest(amount: String, alertDialog: AlertDialog) {

        var requestPoints = 0
        try {
            requestPoints = amount.toInt()
        } catch (e: Exception) {
        }
        val currentTime = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()

        if (requestPoints > walletAmount!!) {
            dialogBoxMessage("Insufficient Wallet Balance.", "cancel")
        } else if (requestPoints < 100 || requestPoints > 100000) {
            dialogBoxMessage(
                "Minimum Withdrawal Amount is 100 & Maximum Withdrawal Amount is 100000.",
                "cancel"
            )

        } else if (currentTime >= 10) {
            alertDialog.dismiss()
            profileDetailApi(requestPoints)

        } else {
            dialogBoxMessage("Withdrawal Requesting Time is 10:00 AM to 02:00 PM.", "cancel")
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
//            COMMENTED CALL FEATURE
//            R.id.llCall -> {
//                try {
//                    val callIntent = Intent(Intent.ACTION_DIAL)
//                    callIntent.data = Uri.parse("tel:${txtNumber.text}")
//                    mContext.startActivity(callIntent)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    dialogBoxMessage("No SIM Found", "cancel")
//                }
//            }
        }

    }

    private fun sendWithdrawRequest(
        requestPoints: Int,
        alertDialog: AlertDialog,
        paymentType: String,
        profileData: Data
    ) {
        alertDialog.dismiss()
        if (cd.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val userFullName =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_FULL_NAME)
            val userLoginUserName =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
            val userLoginMobile =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
            val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            val mObject = JSONObject()
            mObject.put("user_id", userLoginId)
            mObject.put("fullname", userFullName)
            mObject.put("username", userLoginUserName)
            mObject.put("mobile", userLoginMobile)
            mObject.put("req_amount", requestPoints)
            mObject.put("req_date", currentDate)
            mObject.put("req_time", currentTime)
            mObject.put("withdrawalMode", paymentType)

            mObject.put("accNumber", profileData.accountNo)
            mObject.put("ifscCode", profileData.ifscCode)
            mObject.put("bankName", profileData.bankName)
            mObject.put("accName", profileData.accountHolderName)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(Dialog(this@FundsActivity),
                retrofitApiClientAuth.requestWithdrawFund(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {

                                if (alertDialog.isShowing)
                                    alertDialog.dismiss()
                                Alerts. AlertDialogSuccessWithdraw(
                                    this@FundsActivity,
                                    responseObject.getString("message")
                                )
                            } else if (responseObject.getInt("status") == 0) {
                                Alerts.AlertDialogWarning(
                                    this@FundsActivity,
                                    responseObject.getString("message"),""
                                )
                            }
//                            else
//                                Alerts.SessionLogout(this@FundsActivity, this@FundsActivity)

                        } catch (e: Exception) {
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        if (alertDialog.isShowing)
                            alertDialog.dismiss()
                        Alerts.serverError(this@FundsActivity, error.toString())
                    }
                })
        }
    }

    private fun dialogBankConfiramtion(
        requestPoints: Int,
        profileData: Data
    ) {
        val dialogBuilder = AlertDialog.Builder(this@FundsActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_withdraw_bank_confirmation, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.btn_cancel
        val btnOk = dialogView.btn_ok

        dialogView.tvAccountHolder.text = profileData!!.accountHolderName
        dialogView.tvBank.text = profileData!!.bankName
        dialogView.tvAccountNo.text = profileData!!.accountNo
        dialogView.tvIFSC.text = profileData!!.ifscCode


        btnOk.setOnClickListener {
            sendWithdrawRequest(requestPoints, alertDialog, "Bank", profileData)
        }


        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun profileDetailApi(requestPoints: Int) {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.profileData(
                Dialog(this@FundsActivity),
                retrofitApiClientAuth?.profileData(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ProfileDetailsModal
                        if (response.status == 1) {
                            if (response.data != null) {
                                profileData = response.data
                                if (profileData!!.accountNo != null) { // || profileData!!.paytmNumber != null
                                    dialogBankConfiramtion(requestPoints, profileData!!)
                                } else {
                                    dialogBoxMessage("Please Add Your Bank Details", "")

                                }
                            } else {
                                dialogBoxMessage("Please Add Your Bank Details", "")

                            }

                        } else if (response.status == 0) {
                            Alerts.show(mContext, response.message)

                        } else {
                            Alerts.SessionLogout(this@FundsActivity, this@FundsActivity);
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@FundsActivity, error.toString())
                    }
                })

        }
    }

    private fun fundRequestHistorypaginationApi(skipValue: String) {
        ll_loading2.visibility = View.VISIBLE
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
                retrofitApiClientAuth.getFundHistoryPaginatin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        ll_loading2.visibility = View.GONE
                        if (jsonresponse.optInt("status") == 1) {
                            fundHistory = Gson().fromJson(
                                jsonresponse.toString(),
                                FundHistory::class.java)

                            tabNext.text = "${mContext.getString(R.string.next)} (" + fundHistory!!.current + "/" + fundHistory!!.pages + ")"

                            fundHistoryPaginationAdapter!!.addItems(fundHistory!!.records);


                        } else {
                            if (fundHistoryPaginationAdapter!!.itemCount == 0) {
                                tvMessage2!!.visibility = View.VISIBLE
                                tvMessage2!!.text = jsonresponse.optString("message")
                            }
                            Alerts.AlertDialogWarning(
                                this@FundsActivity,
                                jsonresponse.optString("message"),""
                            )
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage2!!.visibility = View.VISIBLE
                        tvMessage2!!.text = error.toString()
                        ll_loading2.visibility = View.GONE
                        Alerts.serverError(this@FundsActivity, error.toString())
                    }

                })
        }
    }


    private fun dialogBoxMessage(string: String, s: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_view_toast_message, null)
        dialogBuilder?.setView(dialogView)
        if (s == "submit") {
            dialogBuilder?.setCancelable(false)
        } else {
            dialogBuilder?.setCancelable(true)
        }
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.txtMessage.text = string
        val btnSubmit = dialogView.btnOk
        btnSubmit.setOnClickListener {
            if (s == "submit") {
                alertDialog.dismiss()
                finish()
            } else {
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("tr", "")
            .appendQueryParameter("tid", "")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        val appNames = arrayListOf<String>()

        val appList = context.packageManager.queryIntentActivities(upiPayIntent, 0)

        for (i in appList) {
            if (i.loadLabel(context.packageManager).toString().toLowerCase() == "google pay")
                appNames.add(i.loadLabel(context.packageManager).toString().toLowerCase())
        }

        if (!appNames.isEmpty())
            startUpiBottomSheet(uri, appNames)
        else {
            Alerts.AlertDialogUPIApp(
                this@FundsActivity!!,
                "No UPI app found,\nPlease Download Google-Pay app to continue"
            )
        }


    }

    private fun startUpiBottomSheet(
        uri: Uri,
        appNames: ArrayList<String>
    ) {

        val bundle = Bundle()
        bundle.putString("uri", uri.toString())
        bundle.putStringArrayList("ARG_UPI_APPS_LIST", appNames)

        val upiBottomSheet = UpiBottomSheet()
        upiBottomSheet.arguments = bundle
        upiBottomSheet.setListener(object : UpiBottomSheet.OnUpiTypeSelectedListener {
            override fun onUpiAppClosed() {

            }

            override fun onUpiAppSelected(data: ResolveInfo) {
                selectedApp = data.loadLabel(packageManager).toString().toLowerCase()
                val paymentIntent = Intent(Intent.ACTION_VIEW)
                paymentIntent.data = uri
                paymentIntent.setPackage(data.activityInfo.packageName)
                startActivityForResult(paymentIntent, UPI_PAYMENT)
            }
        })
        upiBottomSheet.isCancelable = false
        upiBottomSheet.show(supportFragmentManager, UpiBottomSheet::class.java.simpleName)
    }

    internal val UPI_PAYMENT = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> {
                if (data != null) {
                    // Get Response from activity intent
                    val response = data.getStringExtra("response")
                    if (response == null) {
                        Alerts.AlertDialogWarning(
                            this@FundsActivity,
                            "Payment cancelled by user.",""
                        )
                        Log.d(
                            "UPI",
                            "Response is null"
                        )
                    } else {
                        val transactionDetails =
                            getTransactionDetails(response)
                        // Transaction Completed
                        Log.d("UPI", transactionDetails.toString())
                        //Update Listener onTransactionCompleted()
                        //Check if success, submitted or failed
                        try {
                            if (transactionDetails!!.status!!.toLowerCase().equals("success")) {
                                if (transactionDetails.approvalRefNo != null) {
                                    autoPaymentApi(
                                        transactionDetails.approvalRefNo!!,
                                        transactionDetails.approvalRefNo!!,
                                        etRequestPoints.text.toString().trim(), "Approved"
                                    )
                                } else {
                                    autoPaymentApi(
                                        transactionDetails.transactionRefId!!,
                                        transactionDetails.transactionRefId!!,
                                        etRequestPoints.text.toString().trim(), "Approved"
                                    )
                                }
                            } else if (transactionDetails.status!!.toLowerCase()
                                    .equals("submitted")
                            ) {
                                autoPaymentApi(
                                    transactionDetails.transactionRefId!!,
                                    transactionDetails.transactionRefId!!,
                                    etRequestPoints.text.toString().trim(), "submitted"
                                )
                            } else if (transactionDetails.status!!.toLowerCase()
                                    .equals("pending")
                            ) {
                                autoPaymentApi(
                                    transactionDetails.transactionRefId!!,
                                    transactionDetails.transactionRefId!!,
                                    etRequestPoints.text.toString().trim(), "submitted"
                                )
                            } else {
                                Alerts.AlertDialogWarning(
                                    this@FundsActivity,
                                    "Transaction failed.Please try again",""
                                )
                            }
                        } catch (e: java.lang.Exception) {
                            Log.e(
                                "UPI",
                                e.message.toString()
                            )
                            Alerts.AlertDialogWarning(
                                this@FundsActivity,
                                "Transaction failed.Please try again",""
                            )
                        }
                    }
                } else {
                    Log.e(
                        "UPI",
                        "Intent Data is null. User cancelled"
                    )
                    Alerts.AlertDialogWarning(
                        this@FundsActivity,
                        "Payment cancelled by user.",""
                    )
                }
            }

            1000 -> {
                try {
                    val response = data!!.getStringExtra("response")
                    if (response.equals("success", true)) {
                        Alerts.AlertDialogWarning(
                            this@FundsActivity,
                            "Transaction Success.\nPoints Added To Your Wallet",""
                        )
                    } else if (response.equals("failure", true)) {
                        Alerts.AlertDialogWarning(
                            this@FundsActivity,
                            "Transaction failed.Please try again",""
                        )
                    } else {
                        Alerts.AlertDialogWarning(
                            this@FundsActivity,
                            "Transaction Cancelled By User",""
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }
    }

    private fun getQueryString(url: String): Map<String, String>? {
        val params = url.split("&").toTypedArray()
        val map: MutableMap<String, String> =
            HashMap()
        for (param in params) {
            val name = param.split("=").toTypedArray()[0]
            val value = param.split("=").toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    // Make TransactionDetails object from response string
    private fun getTransactionDetails(response: String): TransactionDetails? {
        val map = getQueryString(response)
        val transactionId = map!!["txnId"]
        val responseCode = map["responseCode"]
        val approvalRefNo = map["ApprovalRefNo"]
        val status = map["Status"]
        val transactionRefId = map["txnRef"]
        return TransactionDetails(
            transactionId,
            responseCode,
            approvalRefNo,
            status,
            transactionRefId
        )
    }

    companion object {
        const val UPI_PAYMENT_REQUEST_CODE = 201
        const val ARG_UPI_APPS_LIST = "upi.apps.list"
    }

    private fun getUpiFromAdmin() {
        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val upiLastId = AppPreference.getStringPreference(mContext, Constant.UPI_LAST_ID)
            val mObject = JSONObject()
            mObject.put("last_id", upiLastId)

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@FundsActivity),
                retrofitApiClientAuth.getUPI(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())

                        if (jsonresponse.optInt("status") == 1) {
                            adminUpiID = jsonresponse.optString("data")
                            val id = jsonresponse.optString("id")
                            AppPreference.setStringPreference(
                                mContext,
                                Constant.UPI_LAST_ID,
                                id
                            )
                            AppPreference.setStringPreference(
                                mContext,
                                Constant.UPI_Name,
                                adminUpiID
                            )
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(this@FundsActivity, "Server Error")
                    }

                })
        }
    }

    private fun getPaymentMode() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@FundsActivity),
                retrofitApiClientAuth.paymentMode,
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())

                        if (jsonresponse.optInt("status") == 1) {

                            val paymentMode = Gson().fromJson(
                                jsonresponse.toString(),
                                PaymentMode::class.java
                            )

                            if (paymentMode.data.size == 2) {
                                dialogPaymentMode(paymentMode)

                            } else if (paymentMode.data.size == 1) {
                                for (paymentTypes in 0 until paymentMode.data.size) {
                                    if (paymentMode.data.get(paymentTypes).getMode()
                                            .contains("upi", true)
                                    ) {
                                        payUsingUpi(
                                            etRequestPoints.text.toString().trim(),
                                            adminUpiID,
                                            "Brand Name",
                                            "Fund Request"
                                        )
                                    }
                                    if (paymentMode.data.get(paymentTypes).getMode()
                                            .contains("net banking", true)
                                    ) {
                                        val url =
                                            paymentMode.data.get(paymentTypes).redirectURL.toString()
                                        startActivityForResult(
                                            Intent(
                                                this@FundsActivity,
                                                NetBankingActivity::class.java
                                            ).putExtra("URL", url), 1000
                                        )

                                    }
                                }

                            } else {
                                Alerts.AlertDialogWarning(
                                    this@FundsActivity,
                                    jsonresponse.optString("message"),""
                                )
                            }


                        } else {
                            Alerts.AlertDialogWarning(
                                this@FundsActivity,
                                jsonresponse.optString("message"),""
                            )

                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(this@FundsActivity, "Server Error" + error!!)
                    }

                })
        }
    }

    private fun autoPaymentApi(
        transactiondId: String,
        reference_id: String,
        amount: String,
        paymentStatus: String
    ) {

        if (cd!!.isNetworkAvailable) {
            val userLoginId =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("transactiondId", transactiondId)
            mObject.put("reference_id", reference_id)
            mObject.put("amount", amount)
            mObject.put("transaction_mode", "UPI")
            mObject.put("payment_status", paymentStatus)
            mObject.put("app", selectedApp)
            mObject.put(
                "UPI_name",
                AppPreference.getStringPreference(mContext, Constant.UPI_Name)
            )
            mObject.put(
                "UPI_id",
                AppPreference.getStringPreference(mContext, Constant.UPI_LAST_ID)
            )
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@FundsActivity),
                retrofitApiClientAuth.autoPaymentUpi(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        Alerts.AlertDialogSuccess(
                            this@FundsActivity,
                            jsonresponse.optString("message")
                        )
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@FundsActivity, error.toString())
                    }
                })
        }
    }

    fun onBackClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getMinMaxFundingAmount(amount: String) {

//        getPaymentMode()
        val userObject = JSONObject()
        userObject.put("amount", amount)
        if (cd!!.isNetworkAvailable) {
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (userObject).toString()
            )
            RetrofitService.getServerResponse(this,
                Dialog(this), retrofitApiClient!!.getMinMaxFunding(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        var msg: ResponseBody = result!!.body() as ResponseBody

                        try {
                            val responseObject = JSONObject(msg.string())
                            if (responseObject.getInt("status") == 1) {

                                getPaymentMode()


                            } else
                                Alerts.AlertDialogWarning(
                                    this@FundsActivity,

                                    responseObject.getString("message"),""
                                )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@FundsActivity, error.toString())
                    }
                })
        }
    }

    private fun dialogPaymentMode(paymentmode: PaymentMode) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_payment_mode, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val ll_netbanking = dialogView.rb_netbanking
        val ll_upi = dialogView.rb_upi
        val iv_close = dialogView.iv_cancel

        ll_upi.visibility = View.GONE
        ll_netbanking.visibility = View.GONE

        var url = " "
        for (paymentTypes in 0 until paymentmode.data.size) {
            if (paymentmode.data.get(paymentTypes).getMode().contains("upi", true)) {
                ll_upi.visibility = View.VISIBLE
            }
            if (paymentmode.data.get(paymentTypes).getMode().contains("net banking", true)) {
                url = paymentmode.data.get(paymentTypes).redirectURL.toString()
                ll_netbanking.visibility = View.VISIBLE
            }
        }


        ll_upi.setOnClickListener {
            if (ll_upi.isChecked) {
                alertDialog.dismiss()
                payUsingUpi(
                    etRequestPoints.text.toString().trim(),
                    adminUpiID,
                    "Brand Name",
                    "Fund Request"
                )
            }
        }
        ll_netbanking.setOnClickListener {
            if (ll_netbanking.isChecked) {
                alertDialog.dismiss()
                startActivityForResult(
                    Intent(
                        this@FundsActivity,
                        NetBankingActivity::class.java
                    ).putExtra("URL", url), 1000
                )
            }
        }

        iv_close.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
