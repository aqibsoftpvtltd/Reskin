package com.kasa77.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kasa77.R
import com.kasa77.modal.logindetails.new_device_old_account.ChangeDetailsItem
import com.kasa77.modal.logindetails.new_device_old_account.GetOldDeviceDetails
import com.kasa77.modal.logindetails.new_device_old_account.RequestNewDevice
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.Helper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.ui.login_signup_pages.LoginSignUpActivity
import com.kasa77.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_submit_otp_udpate_device.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.layout_otp_register.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SubmitOtpUdpateDeviceActivity : AppCompatActivity() {


    private var getOldDeviceDetails: GetOldDeviceDetails? = null
    private lateinit var token: String
    private var uMobile = ""
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var mContext: Context? = null
    private lateinit var countDownTimer: CountDownTimer
    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_otp_udpate_device)
        cd = ConnectionDetector(this)
        mContext = this
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofitReg()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaseMessageService", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                token = task.result!!.token

                Log.d("FirebaseMessageService", token)
            })

        val intent = getIntent();
        getOldDeviceDetails = intent.getParcelableExtra<GetOldDeviceDetails>("DATA")
         uMobile = getOldDeviceDetails!!.data.mobileNumber
        numbers.text = Html.fromHtml(uMobile)

        tabVerifyOtp.setOnClickListener {
            if (getOldDeviceDetails != null && !TextUtils.isEmpty(etUserVerificationCode.text.toString()))
                verifyOTP(getOldDeviceDetails!!)
            else {
                Alerts.show(this@SubmitOtpUdpateDeviceActivity, "Please enter OTP")
            }
        }
        ivBack.setOnClickListener{
            val intent = Intent(this, LoginSignUpActivity::class.java)
            startActivity(intent)
           overridePendingTransition(0,0)
          finish()
        }

        code_sent_texts.setOnClickListener{
            resendOtp()
        }
    }

    private fun resendOtp() {
        when {
            cd!!.isNetworkAvailable -> {
                val jsonObject = JSONObject()
                jsonObject.put("mobile", uMobile)
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
                )
                AuthHeaderRetrofitService.getServerResponse(Dialog(this),
                    retrofitApiClient!!.getOtp(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            val response = result!!.body() as ResponseBody
                            try {
                                val responseObject = JSONObject(response.string())

                                if(responseObject.getInt("status") == 0)
                                {
                                    Alerts.show(mContext, responseObject.getString("message"))
                                }
                                else {

                                    if (responseObject.getInt("status") == 1) {
                                        Alerts.show(
                                            mContext, "OTP successfully sent to your Mobile Number"
                                        )

                                        timer()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("sendOtp error", e.toString())
                                dialogBoxMessage(e.toString())
                            }
                        }

                        override fun onResponseFailed(error: String?) {
                            Alerts.serverError(this@SubmitOtpUdpateDeviceActivity, error.toString())
                        }
                    })
            }
        }


    }

    private fun timer()
    {
        // Set the initial time (00:50) in milliseconds
        val initialTimeMillis: Long = 50 * 1000

        countDownTimer = object : CountDownTimer(initialTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Convert milliseconds to minutes and seconds
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60

                code_sent_texts.text = "Code Sent. Resend Code in "

                // Format the time as "mm:ss"
                val time = String.format("%02d:%02d", minutes, remainingSeconds)

                // Update the TextView with the remaining time
                timer_times.text = time
            }

            override fun onFinish() {
                try {
                    code_sent_texts.text = getString(R.string.resend_otp)
                    timer_times.text = ""
                } catch (e: Exception) {
                    timer!!.cancel()
                }

            }
        }

        // Start the countdown timer
        countDownTimer.start()
    }


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

    private fun verifyOTP(testModel: GetOldDeviceDetails) {

        val requestNewDevice = RequestNewDevice()
//       val changeDetailsItem= testModel.data.changeDetails;
        val changeDetailsItem = ChangeDetailsItem()

        val timeStamp: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
        changeDetailsItem.changeOn = timeStamp
        changeDetailsItem.oldDeviceName = testModel.data.oldDeviceName
        changeDetailsItem.olddeviceId = testModel.data.oldDeviceId

        testModel.data.changeDetails.add(changeDetailsItem)

        requestNewDevice.userId = testModel.data.userId
        requestNewDevice.olddeviceId = testModel.data.oldDeviceId
        requestNewDevice.oldDeviceName = testModel.data.oldDeviceName
        requestNewDevice.mobileNumber = testModel.data.mobileNumber
        requestNewDevice.oTP = etUserVerificationCode.text.toString()
        requestNewDevice.deviceId = testModel.data.newDeviceId
        requestNewDevice.deviceName = Helper.getDeviceName()
        requestNewDevice.firebaseToken = token
        requestNewDevice.changeDetails = testModel.data.changeDetails

        try {
            val jsonObject = JSONObject()
            var gson = Gson()
            var jsonString = gson.toJson(requestNewDevice)

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (jsonString).toString()
            )
            RetrofitService.getServerResponse(this@SubmitOtpUdpateDeviceActivity,
                Dialog(this@SubmitOtpUdpateDeviceActivity!!),
                AuthHeaderRetrofitService.getRetrofit()!!.deviceChange(body),
                object : WebResponse {
                    override fun onResponseFailed(error: String?) {
                        print("RES : " + error.toString())
                        Alerts.serverError(this@SubmitOtpUdpateDeviceActivity, error.toString())
                    }

                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        print("RES : " + response.toString())
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {
                            Alerts.show(
                                this@SubmitOtpUdpateDeviceActivity,
                                "Your Account Details Are Updated Successfully. Please Login Again :)"
                            )
                            startActivity(
                                Intent(
                                    this@SubmitOtpUdpateDeviceActivity,
                                    ChangeMpinActivity::class.java
                                )
                                    .putExtra("otp", etUserVerificationCode.text.toString())
                                    .putExtra("from", "forgotMpin")
                            )
                        } else if (responseObject.getInt("status") == 3) {
                            Alerts.AlertDialogWarning(
                                this@SubmitOtpUdpateDeviceActivity,
                                responseObject.getString("message")
                                ,"")

                        } else {
                            Alerts.show(
                                this@SubmitOtpUdpateDeviceActivity,
                                responseObject.getString("message")
                            )
                            finish()
                        }
                    }
                })
        } catch (e: Exception) {
            e.message
        }
    }
}
