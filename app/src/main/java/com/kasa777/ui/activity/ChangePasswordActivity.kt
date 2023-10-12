package com.kasa777.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.kasa777.R
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.ConnectionDetector
import com.kasa777.utils.Helper
import kotlinx.android.synthetic.main.activity_change_password.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {
    private var uPass = ""
    var retrofitApiClient: RetrofitApiClient? = null
    private var cd: ConnectionDetector? = null
    private var mContext: Context? = null
    private var otp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        mContext = this
        retrofitApiClient = RetrofitService.getRetrofit()
        cd = ConnectionDetector(this)

        val intent: Intent = intent
        otp = intent.getStringExtra("otp").toString()
        init()
    }

    private fun init() {
        btnChangePassword.setOnClickListener(this)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun isValidPassword(): Boolean {
        uPass = etEnterPassword.text.toString().trim()
        val cPass = etConfirmPassword.text.toString().trim()

        var isValid = false
        when {
            uPass.isEmpty() -> Alerts.showSnack(
                etEnterPassword,
                "Your Password should not be empty!"
            )
            cPass != uPass -> Alerts.showSnack(
                etConfirmPassword,
                "Your both Password must be same!"
            )
            uPass.length < 5 -> {
                Alerts.show(this, "Password length must be minimum 5 characters!!!")
            }
            else -> isValid = true
        }
        return isValid
    }

    override fun onClick(view: View?) {
        changePassword()
    }

    private fun changePassword() {
        if (isValidPassword()) {
            val deviceId = Helper(this@ChangePasswordActivity).deviceId;

            val userObject = JSONObject()
            userObject.put("deviceId", deviceId)
            userObject.put("password", uPass)
            userObject.put("OTP", otp.toInt())

            if (cd!!.isNetworkAvailable) {
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    (userObject).toString()
                )
                RetrofitService.getServerResponse(this,
                    Dialog(this), retrofitApiClient!!.resetPassword(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            var msg: ResponseBody = result!!.body() as ResponseBody

                            try {
                                val responseObject = JSONObject(msg.string())
                                if (responseObject.getInt("status") == 1) {

                                    val imm: InputMethodManager =
                                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
                                    imm.hideSoftInputFromWindow(scCp.windowToken, 0)
                                    /*                  scCp.visibility = View.GONE
                                                      gifSuccess.visibility = View.VISIBLE
                                              Glide.with(this@ChangePasswordActivity).load(R.drawable.git_su).into(gifSuccess)*/

                                    startActivity(
                                        Intent(
                                            this@ChangePasswordActivity,
                                            MpinPasswordChangedSuccessfullyActivity::class.java
                                        )
                                            .putExtra("from", "changePassword")
                                    )
                                    Alerts.show(
                                        this@ChangePasswordActivity,
                                        responseObject.getString("message")
                                    )
                                    finish()
                                } else
                                    Alerts.AlertDialogWarning(
                                        this@ChangePasswordActivity,

                                        responseObject.getString("message"),""
                                    )
                            } catch (e: Exception) {
                            }
                        }

                        override fun onResponseFailed(error: String?) {
                            Alerts.show(this@ChangePasswordActivity, error)
                        }
                    })
            }
        }
    }



    fun onBackClick(view: View) {
        onBackPressed()
    }
}
