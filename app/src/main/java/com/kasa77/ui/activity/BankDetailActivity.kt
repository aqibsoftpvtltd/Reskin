package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.modal.ifsc_check.IFSCDetail
import com.kasa77.modal.profile_details_modal.Data
import com.kasa77.modal.profile_details_modal.ProfileDetailsModal
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.AuthHeaderRetrofitServiceForBankDetail
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.BaseActivity
import com.kasa77.utils.Helper
import kotlinx.android.synthetic.main.activity_bank_detail.*
import kotlinx.android.synthetic.main.activity_bank_detail.tvUserName
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_update_paytm_number.view.*
import kotlinx.android.synthetic.main.dialog_withdraw_bank_confirmation.view.*
import kotlinx.android.synthetic.main.dialog_withdraw_bank_confirmation.view.btn_cancel
import kotlinx.android.synthetic.main.layout_content_home.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankDetailActivity : BaseActivity() {
    private var bankNameOnly: String? = ""
    private var bankNameFull: String? = ""
    private var existingAccountNumber: String? = ""
    private var existingPaytmNumber: String? = ""

    private var profileData: Data? = null
    private var isIFSCValid: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_detail)

        toolbarTitle.setText("Profile")
        profileDetailApi()

        backBtn.setOnClickListener { onBackClick() }


/*
        dialogAlertSuccess(
            "uAccountNo",
            "uBankName",
            "uIFSCode",
            "",
            ""
        )
*/


        etIFSCCode!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    val ifsc = p0.toString()
                    checkIFCS(ifsc.toString(), etIFSCCode!!, etBankName)
                }
            }
        })


        btn_save.setOnClickListener { //api_call
            checkWithdrawRequest()

        }
        btn_add_paytm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //api_call
                val uPaytmNo = etPaytmNumber.text.toString().trim()
                if (uPaytmNo.isEmpty() || uPaytmNo.length < 10) {
                    Alerts.show(mContext, "Please enter a valid Paytm Number!")
                } else if (uPaytmNo.equals(existingPaytmNumber!!)) {
                    Alerts.show(mContext, "Cannot Update Existing Paytm Number!")
                } else {
                    addPaytmDetail(uPaytmNo, "add")
                }
            }
        })
//        ivProfile.controller=
//            Helper.frescoImageLoad("",R.drawable.ic_user_sec_placeholder,ivProfile)
        tvUserName.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        tvPhoneNumber1.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
        userWalletCheck()
        getNotificationCounter()
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
                Dialog(this@BankDetailActivity),
                retrofitApiClientAuth?.userWalletBalance(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")

                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonObject = JSONObject(response.string())
                        if (jsonObject.getInt("status") == 1) {
                            val data = jsonObject.getJSONObject("data")
                            val walletBalance: Double = data.getDouble("wallet_balance")
                          //  tvWalletAmount1.text = Helper.getSuffix(""+walletBalance.toString())
                            println("lagana pesa")
                            AppPreference.setIntegerPreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE,
                                walletBalance.toInt()
                            )

                            AppPreference.setDoublePreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE_FLOAT,
                                walletBalance
                            )

                        } else {
//                            Alerts.show(mContext,jsonObject.getString("message"))
                            Alerts.SessionLogout(
                                this@BankDetailActivity,
                                this@BankDetailActivity
                            )
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(mContext, error.toString())
                    }
                })

        }
    }

    private fun addPaytmDetail(
        uPaytmNo: String,
        from: String
    ) {
        if (cd!!.isNetworkAvailable) {
            var oldPaytmNumber = ""
            if (from.equals("update")) {
                oldPaytmNumber = profileData!!.paytmNumber
            } else {
                oldPaytmNumber = ""
            }
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val userName =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)

            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("username", userName)
            mObject.put("paytm_number", uPaytmNo)
            mObject.put("old_paytm_number", oldPaytmNumber)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@BankDetailActivity),
                retrofitApiClient?.updateProfileContactDetail(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                existingPaytmNumber = uPaytmNo
                            }
                            Alerts.show(mContext, responseObject.getString("message"))
                        } catch (e: Exception) {
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(mContext, error.toString())
                    }
                })
        }
    }


    private fun addbankAPI() {
        val uAccountNo = etAccountNo.text.toString().trim()
        val uConfirmAccountNo = etConfirmAccountNo.text.toString().trim()
        val uBankName = etBankName.text.toString().trim()
        val uIFSCode = etIFSCCode.text.toString().trim()
        val uAHolderName = etAccountantHolderName.text.toString().trim()
        when {
            uIFSCode.isEmpty() -> {

                etIFSCCode.requestFocus()
                Alerts.show(this@BankDetailActivity, "IFSC Code should not be empty!")
            }

            isIFSCValid == false -> {

                Alerts.show(this@BankDetailActivity, "IFSC Code invalid!")
                etIFSCCode.requestFocus()
            }

            uAccountNo.isEmpty() -> {
                etAccountNo.error = "Account no should not be empty!"
                etAccountNo.requestFocus()
            }

            uAccountNo.length < 9 || uAccountNo.length > 18 -> {
                etAccountNo.setError("Account no invalid!")
                etAccountNo.requestFocus()
            }

            ! uConfirmAccountNo.equals(uAccountNo)->{
                etConfirmAccountNo.setError("Account no is not same!");
                etConfirmAccountNo.requestFocus()
            }

            uBankName.isEmpty() -> {
                etBankName.setError("Bank Name should not be empty!")
                etBankName.requestFocus()
            }


            uAHolderName.isEmpty() -> {
                etAccountantHolderName.setError("Account Holder Name should not be empty!")
                etAccountantHolderName.requestFocus()
            }

            uAHolderName.length > 30 -> {
                etAccountantHolderName.setError("Account Holder Name should be less 30 characters")
                etAccountantHolderName.requestFocus()
            }
            existingAccountNumber.equals(uAccountNo) -> {
                etAccountNo.setError("Cannot Update Existing Account Number!")
                etAccountNo.requestFocus()
            }
            else -> {
                checkAccountNumber(
                    uAccountNo,
                    bankNameOnly!!.trim(),
                    uIFSCode,
                    uAHolderName,
                    bankNameFull
                )

            }
        }
    }

    fun onBackClick() {
        onBackPressed()
    }

    private fun profileDetailApi() {


        val userLoginId =
            AppPreference.getStringPreference(this@BankDetailActivity, Constant.USER_LOGIN_ID)
        val mObject = JSONObject()
        mObject.put("id", userLoginId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.profileData(
            Dialog(this@BankDetailActivity),
            AuthHeaderRetrofitService.getRetrofit()?.profileData(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ProfileDetailsModal
                    if (response.status == 1) {


                        if (response.data != null) {
                            profileData = response.data
                            if (profileData!!.accountNo != null && profileData!!.accountNo != "empty") {

                                ll_getbank.visibility = View.VISIBLE
                                cv_updatebank.visibility = View.GONE

                                tvAccountHolder.text = profileData!!.accountHolderName
                                tvBank.text = profileData!!.bankName
                                tvAccountNo.text = profileData!!.accountNo
                                tvIFSC.text = profileData!!.ifscCode
                                existingAccountNumber = profileData!!.accountNo

                            }
                            if (profileData!!.paytmNumber != null && profileData!!.paytmNumber != "empty") {

                                etPaytmNumber.setText(profileData!!.paytmNumber)
                                existingPaytmNumber = profileData!!.paytmNumber
                            }
                        } else {
                            ll_getbank.visibility = View.GONE
                            cv_updatebank.visibility = View.VISIBLE
                        }

                    } else {
                        Alerts.AlertDialogWarning(this@BankDetailActivity, response.message,"")
                        ll_getbank.visibility = View.GONE
                        cv_updatebank.visibility = View.VISIBLE
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@BankDetailActivity, error.toString())
                }
            })


    }

    private fun checkWithdrawRequest() {


        val userLoginId =
            AppPreference.getStringPreference(this@BankDetailActivity, Constant.USER_LOGIN_ID)
        val mObject = JSONObject()
        mObject.put("userid", userLoginId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(
            Dialog(this@BankDetailActivity),
            AuthHeaderRetrofitService.getRetrofit()?.checkWithdrawRequest(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {
                            addbankAPI()
                        }else{
                            Alerts.AlertDialogWarning(this@BankDetailActivity, responseObject.getString("message"),"")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@BankDetailActivity, error.toString())
                }
            })


    }


    private fun checkAccountNumber(
        uAccountNo: String,
        uBankName: String,
        uIFSCode: String,
        uAHolderName: String,
        bankNameFull: String?
    ) {

        val userLoginId =
            AppPreference.getStringPreference(this@BankDetailActivity, Constant.USER_LOGIN_ID)

        val mObject = JSONObject()

        mObject.put("userid", userLoginId)
        mObject.put("account_number", uAccountNo)
        mObject.put("bank_name", uBankName)
        mObject.put("ifsc_code", uIFSCode)
        mObject.put("account_name", uAHolderName)
        if(existingAccountNumber!!.isNotEmpty())
        mObject.put("firstTime", true)
        else
        mObject.put("firstTime", false)


        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getBankServerResponse(
            Dialog(this@BankDetailActivity),
            AuthHeaderRetrofitService.getRetrofit()?.checkAccountNumber(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        Log.e("Response",responseObject.toString())
                        if (responseObject.getInt("status") == 1) {
                            val resp = responseObject.getJSONObject("resp")
                            val data = resp.getJSONObject("data")
                           val beneficiaryName =  data.getString("beneficiary_name")
                            dialogAlertSuccess(
                                uAccountNo,
                                uBankName,
                                uIFSCode,
                                beneficiaryName,
                                bankNameFull
                            )


                        } else  if (responseObject.getInt("status") == 0){
                            val resp = responseObject.getJSONObject("resp")
                            val data = resp.getJSONObject("data")
                            val beneficiaryName =  data.getString("beneficiary_name")
                            dialogAlertFailed(
                                uAccountNo,
                                uBankName,
                                uIFSCode,
                                beneficiaryName
                            )
                        }else{
                            dialogAlertFailed(
                                "XXXXXXXXXX",
                                "XXXXXXXXXX",
                                "XXXXXXXXXX",
                               "XXXXXXXXXX"
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@BankDetailActivity, error.toString())
                }
            })

    }

    private fun addBankDetail(
        uAccountNo: String,
        uBankName: String,
        uIFSCode: String,
        uAHolderName: String,
        bankNameFull: String
    ) {

        val userLoginId =
            AppPreference.getStringPreference(this@BankDetailActivity, Constant.USER_LOGIN_ID)
        val userName =
            AppPreference.getStringPreference(
                this@BankDetailActivity,
                Constant.USER_LOGIN_USER_NAME
            )
        val mObject = JSONObject()
   /*     if (from.equals("update")) {
            if (uAHolderName.equals(profileData!!.accountHolderName) &&
                uAccountNo.equals(profileData!!.accountNo) && uBankName.equals(profileData!!.bankName)
                && uIFSCode.equals(profileData!!.ifscCode)
            ) {
                Alerts.AlertDialogSuccess(
                    this@BankDetailActivity,
                    "Bank Details Updated Successfully!!!"
                )
            } else {
                mObject.put("userId", userLoginId)
                mObject.put("username", userName)
                mObject.put("account_no", uAccountNo)
                mObject.put("bank_name", bankNameFull)
                mObject.put("ifsc_code", uIFSCode)
                mObject.put("account_holder_name", uAHolderName)
            }

        } else {*/
            mObject.put("userId", userLoginId)
            mObject.put("username", userName)
            mObject.put("account_no", uAccountNo)
            mObject.put("bank_name", bankNameFull)
            mObject.put("ifsc_code", uIFSCode)
            mObject.put("account_holder_name", uAHolderName)
//        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(
            Dialog(this@BankDetailActivity),
            AuthHeaderRetrofitService.getRetrofit()?.updateProfileBankDetail(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {

                            existingAccountNumber = uAccountNo

profileDetailApi()
                        }
                        Alerts.AlertDialogSuccess(
                            this@BankDetailActivity,
                            responseObject.getString("message")
                        )
                    } catch (e: Exception) {
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@BankDetailActivity, error.toString())
                }
            })

    }

    private fun checkIFCS(
        ifsc_code: String,
        etIFSCCode: EditText,
        etBankName: EditText
    ) {


        AuthHeaderRetrofitServiceForBankDetail.getRetrofit()!!.checkIFSC(ifsc_code.toUpperCase())
            .enqueue(object : Callback<IFSCDetail?> {


                override fun onFailure(
                    call: Call<IFSCDetail?>,
                    throwable: Throwable
                ) {
                    isIFSCValid = false;
                    etIFSCCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0)
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<IFSCDetail?>, response: Response<IFSCDetail?>) {


                    if (response.code() == 404) {
                        try {
                            etIFSCCode.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.error,
                                0
                            )
                            etBankName.setText("")
                            isIFSCValid = false
                        } catch (e: Exception) {
                        }
                    } else if (response.code() == 200) {
                        try {
                            etIFSCCode.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.success,
                                0
                            )
                            bankNameOnly = response.body()!!.getBank()
                            bankNameFull = response.body()!!.getBank() + " " + response.body()!!.getBranch() + " " + response.body()!!.getCity()
                            etBankName.setText(
                                response.body()!!.getBank() + " " + response.body()!!
                                    .getBranch() + " " + response.body()!!.getCity()
                            )
                            isIFSCValid = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            })
    }

    fun onChangeBankClick(view: View) {
        dialogAlert5Rupee()


    }


    private fun dialogAlert5Rupee(

    ) {
        val dialogBuilder = AlertDialog.Builder(this@BankDetailActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_alert_5_rupee, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.btn_cancel
        val btnOk = dialogView.btn_ok




        btnOk.setOnClickListener {
            alertDialog.dismiss()
            userWalletCheck()
            showEditBank()

        }


        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    private fun showEditBank(){
        ll_getbank.visibility = View.GONE
        cv_updatebank.visibility = View.VISIBLE
       etAccountantHolderName.setText(tvAccountHolder.text)
       etBankName.setText(tvBank.text)
        etAccountNo.setText(tvAccountNo.text)
        etConfirmAccountNo.setText(tvAccountNo.text)
       etIFSCCode.setText(tvIFSC.text)
        btn_save.setText("Update Bank");
    }
    private fun dialogAlertSuccess(
        uAccountNo: String,
        uBankName: String,
        uIFSCode: String,
        uAHolderName: String,
        bankNameFull: String?
    ) {
        val dialogBuilder = AlertDialog.Builder(this@BankDetailActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_alert_bank_success, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(false)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)


        val btnOk = dialogView.btn_ok
        dialogView.tvAccountHolder.text = uAHolderName
        dialogView.tvBank.text = uBankName
        dialogView.tvIFSC.text = uIFSCode
        dialogView.tvAccountNo.text = uAccountNo



        btnOk.setOnClickListener {
            alertDialog.dismiss()
            addBankDetail(
                uAccountNo,
                uBankName,
                uIFSCode,
                uAHolderName,
                bankNameFull!!.trim()
            )
        }




        alertDialog.show()
    }

    private fun dialogAlertFailed(
        uAccountNo: String,
        uBankName: String,
        uIFSCode: String,
        uAHolderName: String
    ) {
        val dialogBuilder = AlertDialog.Builder(this@BankDetailActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_alert_bank_failed, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(false)

        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)


        val btnOk = dialogView.btn_ok

        dialogView.tvAccountHolder.text = uAHolderName
        dialogView.tvBank.text = uBankName
        dialogView.tvIFSC.text = uIFSCode
        dialogView.tvAccountNo.text = uAccountNo



        btnOk.setOnClickListener {
            alertDialog.dismiss()

        }




        alertDialog.show()
    }
    private fun getNotificationCounter() {
        val mObject = JSONObject()
        mObject.put(
            "id", AppPreference.getStringPreference(
                mContext,
                Constant.API_Notification_id
            )
        )
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(null,
            retrofitApiClientAuth.getNotificationCounter(body),
            object : WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {
                            if (responseObject.getInt("data") > 0) {
                               // tv_notification_counter1.visibility = View.VISIBLE
                                Constant.NotificationCounter =
                                    Constant.NotificationCounter + responseObject.getInt("data")
                               // tv_notification_counter1.text = Constant.NotificationCounter.toString()
                                if (responseObject.getInt("data") > 9) {
                                   // tv_notification_counter1.text = "9+"
                                }

                                gameTitle.text = resources.getString(R.string.notification)
                                tabHomePageView.visibility=View.GONE
//                                fragmentUtils!!.replaceFragment(
//                                    NotificationFragment(),
//                                    Constant.NotificationFragment, R.id.home_frame
//                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@BankDetailActivity, error.toString())
                }
            })
    }


}