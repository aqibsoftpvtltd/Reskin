package com.kasa777.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kasa777.R
import com.kasa777.modal.logindetails.new_device_old_account.ChangeDetailsItem
import com.kasa777.modal.logindetails.new_device_old_account.GetOldDeviceDetails
import com.kasa777.modal.logindetails.new_device_old_account.RequestNewDevice
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.Helper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_submit_otp_udpate_device.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_otp_udpate_device)
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
        val uMobile = getOldDeviceDetails!!.data.mobileNumber
        tvOtpSubTitle.text = Html.fromHtml("Verification code has been sent to your mobile number <b>$uMobile</b>. please check and enter OTP.")

        tabVerifyOtp.setOnClickListener {
            if (getOldDeviceDetails != null && !TextUtils.isEmpty(etUserVerificationCode.text.toString()))
                verifyOTP(getOldDeviceDetails!!)
            else {
                Alerts.show(this@SubmitOtpUdpateDeviceActivity, "Please enter OTP")
            }
        }
        ivBack.setOnClickListener{
            onBackPressed()
        }
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
                            )

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
