package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kasa777.R
import com.kasa777.retrofit_provider.*
import com.kasa777.utils.Alerts
import com.kasa777.utils.ConnectionDetector
import com.kasa777.utils.Helper
import `in`.aabhasjindal.otptextview.OTPListener
//import com.brand_name.utils.sms_retriever.MySMSBroadcastReceiver
//import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.auth.api.phone.SmsRetriever
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_forgot_password_otp_verification.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ForgotPasswordOtpVerifyActivity : AppCompatActivity(), View.OnClickListener{
    val DISCONNECT_TIMEOUT: Long = 120000
    var retrofitApiClient: RetrofitApiClient? = null
    var retrofitApiClientAuthReg: RetrofitApiClient? = null
    var retrofitApiClientAuth: RetrofitApiClient? = null
    var retrofitApiClientFile: RetrofitApiClient? = null
    var cd: ConnectionDetector? = null
    var mContext: Context? = null

    private var mobile = ""
    private var from = ""
    private var changefor: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_otp_verification)
        mContext = this
        cd = ConnectionDetector(mContext)
        retrofitApiClient = RetrofitService.getRetrofit()
        retrofitApiClientAuthReg = AuthHeaderRetrofitService.getRetrofitReg()
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit()
        retrofitApiClientFile = RetrofitServiceFileUpload.getRetrofit()
        val intent: Intent = intent
        from = intent.getStringExtra("from").toString()


        if (from == "forgotMpin" || from == "forgotMpinLoginTime") {
            tvTitle.text="Reset MPIN"
            text_reset.text="Enter 4 digit OTP to Set a New MPIN"
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tvResendOTP.layoutParams = params
            changefor = 2
            mobile = intent.getStringExtra("mobileNumber").toString()
        } else {
            tvTitle.text="Reset Password"
            text_reset.text="Enter 4 digit OTP to Set a New Password"
            changefor = 1
            mobile = intent.getStringExtra("mobileNumber").toString()
        }

      //  etOTP.requestFocus()

        etOTP.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                hideKeyboard()
                submitOtp(otp)
            }
        }


/*
        etOTP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charLength = s.toString()
                if (charLength.isNotEmpty()) {
                    val cl = charLength.length
                    if (cl == 6) {
                        hideKeyboard()
                        submitOtp(charLength)
                    }
                }
            }

        })
*/


       /* mCredentialsApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .enableAutoManage(this, this)
            .addApi(Auth.CREDENTIALS_API)
            .build()

        //GIir0P5ipLh  sms retriver key

        startSMSListener()

        smsBroadcast.initOTPListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)


        applicationContext.registerReceiver(smsBroadcast, intentFilter)*/

        init()
    }

    private fun hideKeyboard() {
        try {
            (this@ForgotPasswordOtpVerifyActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                this@ForgotPasswordOtpVerifyActivity.currentFocus!!.windowToken, 2
            )
        } catch (e: Exception) {
        }
    }


    private fun init() {
        tabOTPContinue.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        tvResendOTP.setOnClickListener(this)
        otpTimerStart()
    }

    /*private fun startSMSListener() {

        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            // ...
            //otpTxtView.text = "Waiting for the OTP"
            //Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
        }

        task.addOnFailureListener {
            //otpTxtView.text = "Cannot Start SMS Retriever"
            //Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }
    }*/

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tabOTPContinue -> {
                val userCode = etOTP.otp
                if (userCode.length!=6) {
                    Alerts.show(this, "Please enter a valid OTP")
                } else {
                    submitOtp(userCode)
                }
            }

            R.id.tvResendOTP -> {
                if (tvResendOTP.text.toString() == getString(R.string.resend_otp)) {
                    sendOtp()
                } else {
                    Alerts.show(this, "Wait for OTP timer End!!!")
                }
            }
            R.id.ivBack ->{
                onBackPressed()
            }
        }
    }


    private fun submitOtp(userOtp: String) {
        if (cd!!.isNetworkAvailable) {
            val deviceId = Helper(this@ForgotPasswordOtpVerifyActivity).deviceId;
            val jsonObject = JSONObject()
            jsonObject.put("mobileNumber", mobile)
            jsonObject.put("OTP", userOtp)
            jsonObject.put("deviceId", deviceId)
            jsonObject.put("changeFor", changefor)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (jsonObject).toString()
            )
            RetrofitService.getServerResponse(this,
                Dialog(this@ForgotPasswordOtpVerifyActivity),
                retrofitApiClient!!.verifyOtp(body),
                object :
                    WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            val data = responseObject.getJSONObject("data")
                            if (data.getString("type") == "success") {
                                val imm: InputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
                                imm.hideSoftInputFromWindow(scMain.windowToken, 0)

                                when (from) {
                                    "forgotMpin" -> {
                                        startActivity(
                                            Intent(mContext, ChangeMpinActivity::class.java)
                                                .putExtra("otp", userOtp)
                                                .putExtra("from", "forgotMpin")
                                        )
//                                        finish()
                                    }
                                    "forgotMpinLoginTime" -> {
                                        startActivity(
                                            Intent(mContext, ChangeMpinActivity::class.java)
                                                .putExtra("otp", userOtp)
                                                .putExtra("from", "forgotMpinLoginTime")
                                        )
//                                        finish()
                                    }
                                    else -> {
                                        startActivity(
                                            Intent(mContext, ChangePasswordActivity::class.java)
                                                .putExtra("otp", userOtp)
                                        )
//                                        finish()
                                    }
                                }
                            } else {
                                dialogBoxMessage("Enter a valid OTP!!!")
                            }
                        } catch (e: Exception) {
                              Alerts.show(mContext, e.toString())
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(mContext, error)
                    }
                })
        }
    }

    private fun sendOtp() {
        val deviceId = Helper(this@ForgotPasswordOtpVerifyActivity).deviceId;
        val jsonData = JSONObject()
        jsonData.put("deviceId", deviceId)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (jsonData).toString()
        )
        RetrofitService.getServerResponse(this,
           Dialog(this),
            retrofitApiClient!!.getForgotPasswordOtp(body),
            object :
                WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val responseBody: ResponseBody = result!!.body() as ResponseBody
                    try {
                        val jsonObject = JSONObject(responseBody.string())
                        if (jsonObject.getInt("status") == 1) {
                            otpTimerStart()
                            Alerts.show(
                                this@ForgotPasswordOtpVerifyActivity,
                                "Otp successfully sent to your mobile Number"
                            )
                            mobile = jsonObject.getString("mobileNumber")
                        } else {
                            Alerts.AlertDialogWarning(
                                mContext,

                                jsonObject.getString("message"),""
                            )
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.show(this@ForgotPasswordOtpVerifyActivity, error.toString())
                }

            })
    }

    private fun otpTimerStart() {
        object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvResendOTP.text = " " + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                codeSent.visibility=View.GONE
                tvResendOTP.text = getString(R.string.resend_otp)
            }

        }.start()
    }


  /*  override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onOTPReceived(otp: String) {
        if (smsBroadcast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcast)
        }
        etOTP.setText(extractOtpFromSms(otp))
        //  submitUser()
        //Toast.makeText(this, extractOtpFromSms(otp), Toast.LENGTH_LONG).show()
        //otpTxtView.text = "Your OTP is: $otp"
        //Log.e("OTP Received", otp)    }
    }

    override fun onOTPTimeOut() {
    }
*/
    private fun extractOtpFromSms(msg: String): String {
        val pattern = Pattern.compile("(\\d{6})")
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

    @SuppressLint("InflateParams")
    private fun dialogBoxMessage(string: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_view_toast_message, null)
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
