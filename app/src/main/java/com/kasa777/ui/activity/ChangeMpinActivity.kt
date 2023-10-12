package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kasa777.R
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.login_signup_pages.LoginSignUpActivity
import com.kasa777.ui.onboarding.OnboardingActivity
import com.kasa777.utils.Alerts
import com.kasa777.utils.ConnectionDetector
import com.kasa777.utils.Helper
import kotlinx.android.synthetic.main.acvitiy_change_mpin.*
import kotlinx.android.synthetic.main.acvitiy_change_mpin.etConfirmMpin
import kotlinx.android.synthetic.main.layout_mpin.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChangeMpinActivity : AppCompatActivity(), View.OnClickListener {
    private var uPass = ""
    var retrofitApiClient: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null
    private var mContext: Context? = null
    private var otp = ""
    private var from = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acvitiy_change_mpin)

        mContext = this
        retrofitApiClient = RetrofitService.getRetrofit()
        cd = ConnectionDetector(this)

        val intent: Intent = intent
        from = intent.getStringExtra("from").toString()
        otp = intent.getStringExtra("otp").toString()
        init()
    }

    private fun init() {
        tabChangePassword.setOnClickListener(this)
        bck_new.setOnClickListener(this)
    }

    private fun isValidPassword(): Boolean {
        uPass = etEnterPassword.text.toString()
        val cPass = etConfirmMpin.text.toString()

        var isValid = false
        when {
            uPass.isEmpty() -> Alerts.showSnack(
                etEnterPassword,
                "Your MPIN should not be empty!"
            )
            cPass != uPass -> Alerts.showSnack(
                etConfirmMpin,
                "Your both MPIN must be same!"
            )
            uPass.length < 3 -> {
                Alerts.show(this, "MPIN length must be four character !!!")
            }
            else -> isValid = true
        }
        return isValid
    }

    override fun onClick(view: View?) {
        when (view!!.id)
        {
            R.id.bck_new -> {
                val intent = Intent(mContext, LoginSignUpActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
            }
            R.id.tabChangePassword -> {
                changePassword()
            }
        }


    }



    private fun changePassword() {
        if (isValidPassword()) {
            val deviceId = Helper(this@ChangeMpinActivity).deviceId;
            val mpin = uPass + deviceId

            var userObject = JSONObject()
            userObject.put("deviceId", deviceId)
            userObject.put("mpin", mpin)


            if (cd!!.isNetworkAvailable) {
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    (userObject).toString()
                )
                RetrofitService.getServerResponse(this,
                    Dialog(this@ChangeMpinActivity), retrofitApiClient!!.resetMPIN(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            val msg: ResponseBody = result!!.body() as ResponseBody
                            try {
                                val responseObject = JSONObject(msg.string())
                                if (responseObject.getInt("status") == 1) {

                                    val imm: InputMethodManager =
                                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(scCp.windowToken, 0)

                                    Alerts.show(
                                        this@ChangeMpinActivity, responseObject.getString("message")
                                    )
                                    if (from == "forgotMpin") {

                                        val intent = Intent(
                                            this@ChangeMpinActivity,
                                            OnboardingActivity::class.java
                                        )
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY

                                        startActivity(intent)
//                                        finish()
                                    } else {
                                        val intent = Intent(
                                            this@ChangeMpinActivity,
                                            OnboardingActivity::class.java
                                        )
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY

                                        startActivity(intent)
//                                        finish()
                                    }

                                    finish()
                                } else {
//                                    Alerts.show(this@ChangeMpinActivity,responseObject.getString("message"))
                                    Alerts.AlertDialogWarning(
                                        this@ChangeMpinActivity,
                                        responseObject.getString("message"),""
                                    )
                                }
                            } catch (e: Exception) {
                                Alerts.AlertDialogWarning(
                                    this@ChangeMpinActivity,
                                    e.message,""
                                )
                            }
                        }

                        override fun onResponseFailed(error: String?) {
                            Alerts.AlertDialogWarning(
                                this@ChangeMpinActivity,
                               error,""
                            )
                        }
                    })
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getUniqueIMEIId(): String {
        try {
            val telephonyManager =
               getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                  this@ChangeMpinActivity,
                    android.Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ""
            }
            val imei: String
            imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                telephonyManager.deviceId
            }
            Log.v("IMEI", "=$imei")
            return if (imei != null && imei.isNotEmpty()) {
                imei
            } else {
                Build.SERIAL
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "not_found"
    }

}
