package com.kasa77.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
//import com.google.android.gms.auth.api.phone.SmsRetriever
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.RetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.activity.ForgotPasswordOtpVerifyActivity
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.ConnectionDetector
import com.kasa77.utils.Helper
import kotlinx.android.synthetic.main.dialog_generate_update_mpin.*
import kotlinx.android.synthetic.main.dialog_generate_update_mpin.view.*
import kotlinx.android.synthetic.main.fragment_mpin.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.regex.Pattern


class GenerateMpinFragment : Fragment(), View.OnClickListener

    {

    private var rootView: View? = null
    private var etUOTP: EditText? = null
    private var mContext: Context? = null
    private var isMpinGenerate = false
    private var retrofitApiClient: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_mpin, container, false)
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context
        mContext = activity
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        cd = ConnectionDetector(mContext)

//        startSMSListener()

//        smsBroadcast.initOTPListener(this)
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
//
//        activity?.applicationContext?.registerReceiver(smsBroadcast, intentFilter)

        isMpinGenerate = AppPreference.getBooleanPreference(mContext, Constant.IsMpinGenerate)
        if (isMpinGenerate) {
            tvGmpin.text = mContext?.getString(R.string.change_mpin)
            cvForgetMpin.visibility==View.VISIBLE
        }else{
            tvGmpin.text = mContext?.getString(R.string.generate_mpin)
            cvForgetMpin.visibility==View.GONE
        }
        init()
    }

    private fun init() {

        /*tvRequestOtp
        etOTP
        rlOtp*/

        //Alerts.show(mContext, Constant.USER_TOKEN)

        cvGenerateMpin.setOnClickListener(this)
        cvForgetMpin.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cvGenerateMpin -> {
                isMpinGenerate = AppPreference.getBooleanPreference(mContext, Constant.IsMpinGenerate)
                if (!isMpinGenerate) {
                    generateMpinDialog()
                } else {
                    tvGmpin.text = mContext?.getString(R.string.change_mpin)
                    updateMpinDialog()
                }
            }
            R.id.cvForgetMpin -> {
                forgotPassword()
            }
        }
    }

    private fun generateMpinDialog() {
        val dialogBox = AlertDialog.Builder(mContext)
        dialogBox.setCancelable(true)

        val li = LayoutInflater.from(mContext)
        val view = li.inflate(R.layout.dialog_generate_update_mpin, null)
        dialogBox.setView(view)
        val alertDialog = dialogBox.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        alertDialog.show()

        view.etOldMpin.visibility = View.GONE
        view.rlOtp.visibility = View.VISIBLE
        view.tvRequestOtp.setOnClickListener { requestOTP() }
        view.etEnterMpin.hint = getString(R.string.enter_mpin)
        view.etConfirmMpin.hint = getString(R.string.confirm_mpin)
        etUOTP = view.findViewById(R.id.etOTP)
        view.tabGenerate.setOnClickListener {
            val uOTP = etUOTP!!.text.toString().trim()
            val uMpin = view.etEnterMpin.text.toString().trim()
            val cMpin = view.etConfirmMpin.text.toString().trim()

            when {
                uOTP.isEmpty() || uOTP.length < 4 -> Alerts.show(
                    mContext,
                    "Please enter a valid OTP!"
                )
                uMpin.isEmpty() || uMpin.length < 4 -> Alerts.show(
                    mContext,
                    "Please enter a valid 4 digit MPIN!"
                )
                uMpin != cMpin -> Alerts.show(mContext, "Both MPIN must be same!")
                else -> generateMpin(uOTP, uMpin, alertDialog)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateMpinDialog() {
        val dialogBox = AlertDialog.Builder(mContext)
        dialogBox.setCancelable(true)

        val li = LayoutInflater.from(mContext)
        val view = li.inflate(R.layout.dialog_generate_update_mpin, null)
        dialogBox.setView(view)
        val alertDialog = dialogBox.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        alertDialog.show()

        alertDialog.etOldMpin.visibility = View.VISIBLE
        alertDialog.tvTitle.text = mContext?.getString(R.string.update_mpin)
        alertDialog.tvGenerate.text = mContext?.getString(R.string.update)
        etUOTP = view.findViewById(R.id.etOTP)
        view.tvRequestOtp.setOnClickListener { requestOTP() }
        view.tabGenerate.setOnClickListener {
            val uOTP = etUOTP!!.text.toString().trim()
            val oMpin = view.etOldMpin.text.toString().trim()
            val uMpin = view.etEnterMpin.text.toString().trim()
            val cMpin = view.etConfirmMpin.text.toString().trim()

            when {
                uOTP.isEmpty() || uOTP.length < 4 -> Alerts.show(
                    mContext,
                    "Please enter a valid OTP!"
                )
                oMpin.isEmpty() || oMpin.length < 4 -> Alerts.show(
                    mContext,
                    "Please enter a valid 4 digit old MPIN!"
                )
                uMpin.isEmpty() || uMpin.length < 4 -> Alerts.show(
                    mContext,
                    "Please enter a valid 4 digit new MPIN!"
                )
                uMpin != cMpin -> Alerts.show(mContext, "New & Confirm MPIN must be same!")
                else -> updateMpin(uOTP, uMpin, alertDialog, oMpin)
            }
        }
    }


    private fun forgotPassword(){
        val deviceId = Helper(context).deviceId;
        val jsonData  = JSONObject()
        jsonData.put("deviceId",deviceId)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (jsonData).toString()
        )

        RetrofitService.getServerResponse(context,Dialog(requireActivity()), retrofitApiClient!!.getForgotPasswordOtp(body),object :
            WebResponse {
            override fun onResponseSuccess(result: Response<*>?) {
                val responseBody :ResponseBody = result!!.body() as ResponseBody
                try {
                    val jsonObject = JSONObject(responseBody.string())
                    if(jsonObject.getInt("status") ==1){
                        val mobile = jsonObject.getString("mobileNumber")
                        startActivity(
                            Intent(mContext, ForgotPasswordOtpVerifyActivity::class.java)
                                .putExtra("mobileNumber",mobile)
                                .putExtra("from","forgotMpin"))
                        Alerts.show(mContext, jsonObject.getString("message"))
                    }else
                        Alerts.SessionLogout(context,activity)
                } catch (e: Exception) {
                }
            }
            override fun onResponseFailed(error: String?) {
                Alerts.serverError(context, error.toString())
            }

        })
    }
    private fun requestOTP() {
        if (cd!!.isNetworkAvailable) {
            val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
            val mObject = JSONObject()
            mObject.put("mobile", userMobile)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient?.sendMpinOTP(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                Alerts.show(
                                    mContext,
                                    "OTP successfully send to your entered Mobile Number"
                                )
                            } else {
                                Alerts.SessionLogout(context,activity)
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
    private fun generateMpin(uOTP: String, uMpin: String, alertDialog: AlertDialog) {
        if (cd!!.isNetworkAvailable) {
            val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val deviceId = Helper(context).deviceId;
            val mObject = JSONObject()
            mObject.put("deviceId", deviceId)
            mObject.put("mobile", userMobile)
            mObject.put("OTP", uOTP)
            mObject.put("MPIN", uMpin+deviceId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient?.setMpin(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                /**********************************************
                                 * Request ==> {"id":"5d987b8bf654090010664c95","mobile":"7000359280","OTP":"4043","MPIN":"1234"}
                                 * Response ==> {"status":1,"message":"MPIN Generated Successfully"}
                                 ***********************************************/
                                AppPreference.setBooleanPreference(mContext, Constant.IsMpinGenerate, true)
                                alertDialog.dismiss()
                                Alerts.show(mContext,responseObject.getString("message"))
                                tvGmpin.text = mContext?.getString(R.string.change_mpin)
                                cvForgetMpin.visibility==View.VISIBLE
                            }
                            else{
                                Alerts.SessionLogout(context,activity)
                            }
                        } catch (e: Exception) {
                            Alerts.SessionLogout(context,activity)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }
    private fun updateMpin(uOTP: String, uMpin: String, alertDialog: AlertDialog, oMpin: String) {
        if (cd!!.isNetworkAvailable) {
            val userMobile = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val deviceId = Helper(context).deviceId;
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("mobile", userMobile)
            mObject.put("OTP", uOTP)
            mObject.put("oldMpin", oMpin+deviceId)
            mObject.put("newMpin", uMpin+deviceId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient?.changeMpin(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            if (responseObject.getInt("status") == 1) {
                                /**********************************************
                                 * Request ==> {"id":"5d987b8bf654090010664c95","mobile":"7000359280","OTP":"3927","oldMpin":"1234","newMpin":"11115d987b8bf654090010664c95"}
                                 * Response ==> {"status":1,"message":"MPIN Changed Successfully"}
                                 ***********************************************/
                                AppPreference.setBooleanPreference(mContext, Constant.IsMpinGenerate, true)
                                Alerts.show(mContext,responseObject.getString("message"))
                                alertDialog.dismiss()
                            }else{
                                Alerts.SessionLogout(context,activity)
                            }
                        } catch (e: Exception) {
                            Alerts.SessionLogout(context,activity)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

//    private fun startSMSListener() {
//
//        val client = SmsRetriever.getClient(requireContext())
//        val task = client.startSmsRetriever()
//        task.addOnSuccessListener {
//
//        }
//
//        task.addOnFailureListener {
//
//        }
//    }

 /*   override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onOTPReceived(otp: String) {
        try {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(smsBroadcast)
            etUOTP!!.setText(extractOtpFromSms(otp))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

  /*  override fun onOTPTimeOut() {

    }*/

    private fun extractOtpFromSms(msg: String): String {
        val pattern = Pattern.compile("(\\d{4})")
        //   \d is for a digit
        //   {} is the number of digits here 4.
        var otp: String = ""
        try {
            val matcher = pattern.matcher(msg)
            otp = ""
            if (matcher.find()) {
                otp = matcher.group(0)  // 4 digit number
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return otp
    }



}