package com.kasa777.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa777.R
import com.kasa777.adapter.static_data_adapter.ProfileNoteAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.ifsc_check.IFSCDetail
import com.kasa777.modal.profile_details_modal.Data
import com.kasa777.modal.profile_details_modal.ProfileDetailsModal
import com.kasa777.modal.static_data.profilenote.Datum
import com.kasa777.modal.static_data.profilenote.ProfileNoteData
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.AuthHeaderRetrofitServiceForBankDetail
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.ConnectionDetector
import kotlinx.android.synthetic.main.dialog_bank_details.view.*
import kotlinx.android.synthetic.main.dialog_update_address.view.*
import kotlinx.android.synthetic.main.dialog_update_paytm_number.view.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {
    private var existingPaytmNumber: String? = ""
    private var existingAccountNumber: String? = ""
    private var rootView: View? = null
    private var mContext: Context? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null
    private var profileData: Data? = null
    private var isIFSCValid: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_my_profile, container, false)
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        cd = ConnectionDetector(mContext)
        val userName = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        val mobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
//        rootView!!.tvUserName.text = userName

        init()
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

                override fun onResponse(call: Call<IFSCDetail?>, response: Response<IFSCDetail?>) {


                        if (response.code()==404) {
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
                        }
                        else if (response.code()==200) {
                            try {
                                etIFSCCode.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.success,
                                    0
                                )
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

    private fun init() {

//        card_address.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                profileDetailApi("address")
//
//            }
//        })
//        card_paytm.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                profileDetailApi("paytm")
//            }
//        })
//        card_bank.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                profileDetailApi("bank")
//
//            }
//        })



//        profileNoteApi()

    }

    /* @SuppressLint("SetTextI18n")
     override fun onClick(p0: View?) {
         when (p0?.id) {
             R.id.btnAddAddre -> {
                 val uAddress = rootView!!.etAddress.text.toString().trim()
                 val uCity = rootView!!.etCity.text.toString().trim()
                 val uZipCode = rootView!!.etZipCode.text.toString().trim()
                 when {
                     uAddress.isEmpty() -> Alerts.show(mContext, "Address should not be empty!")
                     uCity.isEmpty() -> Alerts.show(mContext, "City should not be empty!")
                     uZipCode.isEmpty() || uZipCode.length < 6 -> Alerts.show(
                         mContext,
                         "Please enter a valid Pin Code!"
                     )
                     else -> addUserAddress(uAddress, uCity, uZipCode)
                 }
             }
             R.id.btnAddBankDetail -> {
                 val uAccountNo = rootView!!.etAccountNo.text.toString().trim()
                 val uCAccountNO = rootView!!.etConfirmAccountNo.text.toString().trim()
                 val uBankName = rootView!!.etBankName.text.toString().trim()
                 val uIFSCode = rootView!!.etIFSCCode.text.toString().trim()
                 val uAHolderName = rootView!!.etAccountantHolderName.text.toString().trim()
                 when {
                     uIFSCode.isEmpty() -> {

                         etIFSCCode.requestFocus()
                         Alerts.show(context, "IFSC Code should not be empty!")
                     }

                     isIFSCValid == false -> {

                         Alerts.show(context, "IFSC Code invalid!")
                         etIFSCCode.requestFocus()
                     }

                     uAccountNo.isEmpty() -> {
                         etAccountNo.setError("Account no should not be empty!")
                         etAccountNo.requestFocus()
                     }

                     uAccountNo.length < 9 || uAccountNo.length > 18 -> {
                         etAccountNo.setError("Account no invalid!")
                         etAccountNo.requestFocus()
                     }


                     uCAccountNO.isEmpty() -> {
                         etConfirmAccountNo.setError("Re-Enter Your Account No. Should Not Be Empty!")
                         etConfirmAccountNo.requestFocus()
                     }
                     uAccountNo != uCAccountNO -> {
                         etConfirmAccountNo.setError("Entered Account Number Does Not Match!!!")
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
                     existingAccountNumber.equals(uAccountNo) -> {
                         etAccountNo.setError("Cannot Update Existing Account Number!")
                         etAccountNo.requestFocus()
                     }
                     else -> addBankDetail(uAccountNo, uBankName, uIFSCode, uAHolderName, "addBank")
                 }
             }
             R.id.btnAddPaytm -> {
                 val uPaytmNo = rootView!!.etPaytmNumber.text.toString().trim()
                 if (uPaytmNo.isEmpty() || uPaytmNo.length < 10) {
                     Alerts.show(mContext, "Please enter a valid Paytm Number!")
                 } else if (uPaytmNo.equals(existingPaytmNumber!!)) {
                     Alerts.show(mContext, "Cannot Update Existing Paytm Number!")
                 } else {
                     addPaytmDetail(uPaytmNo, "add")
                 }
             }
             R.id.imgEditAddress -> {
                 llAddressDetail.visibility = View.GONE
                 llEditAddressDetail.visibility = View.VISIBLE
                 btnAddAddre.text = "Update"
             }
             R.id.imgEditAcDetail -> {
                 llViewBankDetail.visibility = View.GONE
                 llEditBankDetail.visibility = View.VISIBLE
                 btnAddBankDetail.text = "Update"
             }
             R.id.imgEditPaytmNumber -> {
                 detailPaytm.visibility = View.GONE
                 llEditPaytm.visibility = View.VISIBLE
                 btnAddPaytm.text = "Update"
             }
             R.id.btnCancelAddAddre -> {
                 llEditAddressDetail.visibility = View.GONE
                 llAddressDetail.visibility = View.VISIBLE
             }
             R.id.btnCancelBankDetail -> {
                 llEditBankDetail.visibility = View.GONE
                 llViewBankDetail.visibility = View.VISIBLE
             }
             R.id.btnCancelPaytm -> {
                 llEditPaytm.visibility = View.GONE
                 detailPaytm.visibility = View.VISIBLE
             }
         }
     }*/

    private fun profileDetailApi(type: String) {

        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.profileData(
                Dialog(requireActivity()),
                retrofitApiClient?.profileData(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ProfileDetailsModal
                        if (response.status == 1) {


//                            if (response.data != null) {
                            profileData = response.data

                            if (type.equals("address")) {
                                dialogAdress(response)
                            }
                            if (type.equals("paytm")) {
                                dialogPaytm(response)
                            }
                            if (type.equals("bank")) {
                                dialogBank(response)
                            }

//
//                            } else {
//                                Alerts.AlertDialogWarning(context, response.message);
//                            }

                        } else {
                            Alerts.AlertDialogWarning(context, response.message);
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })

        }
    }

    private fun addPaytmDetail(
        uPaytmNo: String,
        from: String,
        alertDialog: AlertDialog
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
                Dialog(requireActivity()),
                retrofitApiClient?.updateProfileContactDetail(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                existingPaytmNumber = uPaytmNo
                                alertDialog.dismiss()
                            }
                            Alerts.show(mContext, responseObject.getString("message"))
                        } catch (e: Exception) {
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun addUserAddress(
        uAddress: String,
        uCity: String,
        uZipCode: String,
        alertDialog: AlertDialog
    ) {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val userName =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
            val mObject = JSONObject()
            mObject.put("userId", userLoginId)
            mObject.put("username", userName)
            mObject.put("address", uAddress)
            mObject.put("city", uCity)
            mObject.put("pincode", uZipCode)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient?.updateProfileAddress(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                Alerts.show(mContext, responseObject.getString("message"))
                                alertDialog.dismiss()
                            }

                        } catch (e: Exception) {
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun addBankDetail(
        uAccountNo: String,
        uBankName: String,
        uIFSCode: String,
        uAHolderName: String,
        from: String,
        alertDialog: AlertDialog,
        dialogView: View
    ) {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val userName =
                AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
            val mObject = JSONObject()
            if (from.equals("update")) {
                if (uAHolderName.equals(profileData!!.accountHolderName) &&
                    uAccountNo.equals(profileData!!.accountNo) && uBankName.equals(profileData!!.bankName)
                    && uIFSCode.equals(profileData!!.ifscCode)
                ) {
                    Alerts.AlertDialogSuccess(context, "Bank Details Updated Successfully!!!")
                } else {
                    mObject.put("userId", userLoginId)
                    mObject.put("username", userName)
                    mObject.put("account_no", uAccountNo)
                    mObject.put("bank_name", uBankName)
                    mObject.put("ifsc_code", uIFSCode)
                    mObject.put("account_holder_name", uAHolderName)
                }

            } else {
                mObject.put("userId", userLoginId)
                mObject.put("username", userName)
                mObject.put("account_no", uAccountNo)
                mObject.put("bank_name", uBankName)
                mObject.put("ifsc_code", uIFSCode)
                mObject.put("account_holder_name", uAHolderName)
            }

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient?.updateProfileBankDetail(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {

                                existingAccountNumber = uAccountNo


                                alertDialog.dismiss()
                            }
                            Alerts.show(mContext, responseObject.getString("message"))
                        } catch (e: Exception) {
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }


    private fun profileNoteApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.profileNote(
                mContext?.let { Dialog(it) },
                retrofitApiClient!!.profileNote(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: ProfileNoteData = result?.body() as ProfileNoteData
                        if (responseBody.status == 1) {
                            if (responseBody.data.size > 0) {
                                cvProfileNote.visibility = View.VISIBLE
                                val data: ArrayList<Datum> = responseBody.data as ArrayList<Datum>
                                rvProfileNote!!.setHasFixedSize(true)
                                rvProfileNote!!.layoutManager = LinearLayoutManager(mContext)
                                val dashboardAdaper = ProfileNoteAdapter(mContext, data)
                                rvProfileNote!!.adapter = dashboardAdaper
                                dashboardAdaper.notifyDataSetChanged()
                            }
                        } else {
                            Alerts.AlertDialogWarning(context, responseBody.message)
                        }

                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }

                })
        }
    }


    private fun dialogAdress(response: ProfileDetailsModal) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update_address, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )




        if (response.data != null) {
            profileData = response.data
            if (profileData!!.address != null && profileData!!.address != "") {
                dialogView.etAddress.setText(profileData!!.address)
                dialogView.etCity.setText(profileData!!.city)
                dialogView.etZipCode.setText(profileData!!.pincode)
            }
        }
        dialogView.btn_cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                alertDialog.dismiss()
            }
        })
        dialogView.btnAddAddress.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //api_call
                val uAddress = dialogView.etAddress.text.toString().trim()
                val uCity = dialogView.etCity.text.toString().trim()
                val uZipCode = dialogView.etZipCode.text.toString().trim()
                when {
                    uAddress.isEmpty() -> Alerts.show(mContext, "Address should not be empty!")
                    uCity.isEmpty() -> Alerts.show(mContext, "City should not be empty!")
                    uZipCode.isEmpty() || uZipCode.length < 6 -> Alerts.show(
                        mContext,
                        "Please enter a valid Pin Code!"
                    )
                    else -> addUserAddress(uAddress, uCity, uZipCode, alertDialog)
                }
            }
        })



        alertDialog.show()
    }

    private fun dialogBank(response: ProfileDetailsModal) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it,android.R.style.Theme_Black_NoTitleBar_Fullscreen) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_bank_details, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.white)

        alertDialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )



        dialogView.etIFSCCode!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    val ifsc = p0.toString()
//                    if (ifsc.isNotEmpty()) {
                    checkIFCS(ifsc.toString(), dialogView.etIFSCCode!!, dialogView.etBankName)
//                    }
                }
            }
        })

        dialogView.iv_back.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })

        if (response.data != null) {
            profileData = response.data
            if (profileData!!.accountNo != null && profileData!!.accountNo != "empty") {

                dialogView.etAccountNo.setText(profileData!!.accountNo)
                dialogView.etBankName.setText(profileData!!.bankName)
                dialogView.etIFSCCode.setText(profileData!!.ifscCode)
                dialogView.etAccountantHolderName.setText(profileData!!.accountHolderName)

                existingAccountNumber=profileData!!.accountNo

            }
        }
        dialogView.btn_cancel_bank!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                alertDialog.dismiss()
            }
        })
        dialogView.btn_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {                //api_call
                val uAccountNo = dialogView.etAccountNo.text.toString().trim()
                val uBankName = dialogView.etBankName.text.toString().trim()
                val uIFSCode = dialogView.etIFSCCode.text.toString().trim()
                val uAHolderName = dialogView.etAccountantHolderName.text.toString().trim()
                when {
                    uIFSCode.isEmpty() -> {

                        dialogView.etIFSCCode.requestFocus()
                        Alerts.show(context, "IFSC Code should not be empty!")
                    }

                    isIFSCValid == false -> {

                        Alerts.show(context, "IFSC Code invalid!")
                        dialogView.etIFSCCode.requestFocus()
                    }

                    uAccountNo.isEmpty() -> {
                        dialogView.etAccountNo.setError("Account no should not be empty!")
                        dialogView.etAccountNo.requestFocus()
                    }

                    uAccountNo.length < 9 || uAccountNo.length > 18 -> {
                        dialogView.etAccountNo.setError("Account no invalid!")
                        dialogView.etAccountNo.requestFocus()
                    }


                    uBankName.isEmpty() -> {
                        dialogView.etBankName.setError("Bank Name should not be empty!")
                        dialogView.etBankName.requestFocus()
                    }


                    uAHolderName.isEmpty() -> {
                        dialogView.etAccountantHolderName.setError("Account Holder Name should not be empty!")
                        dialogView.etAccountantHolderName.requestFocus()
                    }

                    uAHolderName.length > 30 -> {
                        dialogView.etAccountantHolderName.setError("Account Holder Name should be less 30 characters")
                        dialogView.etAccountantHolderName.requestFocus()
                    }
                    existingAccountNumber.equals(uAccountNo) -> {
                        dialogView.etAccountNo.setError("Cannot Update Existing Account Number!")
                        dialogView.etAccountNo.requestFocus()
                    }
                    else -> addBankDetail(
                        uAccountNo,
                        uBankName,
                        uIFSCode,
                        uAHolderName,
                        "addBank",
                        alertDialog, dialogView
                    )
                }
            }


        })
        alertDialog.show()
    }


    private fun dialogPaytm(response: ProfileDetailsModal) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it,android.R.style.Theme_Black_NoTitleBar_Fullscreen) }

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update_paytm_number, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (response.data != null) {
            profileData = response.data
            if (profileData!!.paytmNumber != null && profileData!!.paytmNumber != "empty") {

                dialogView.etPaytmNumber.setText(profileData!!.paytmNumber)
                existingPaytmNumber = profileData!!.paytmNumber
            }
        }

        dialogView.iv_back_paytm.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })

        dialogView.btn_cancel_paytm!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                alertDialog.dismiss()
            }
        })
        dialogView.btn_add_paytm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //api_call
                val uPaytmNo = dialogView.etPaytmNumber.text.toString().trim()
                if (uPaytmNo.isEmpty() || uPaytmNo.length < 10) {
                    Alerts.show(mContext, "Please enter a valid Paytm Number!")
                } else if (uPaytmNo.equals(existingPaytmNumber!!)) {
                    Alerts.show(mContext, "Cannot Update Existing Paytm Number!")
                } else {
                    addPaytmDetail(uPaytmNo, "add", alertDialog)
                }
            }
        })

        alertDialog.show()
    }


}