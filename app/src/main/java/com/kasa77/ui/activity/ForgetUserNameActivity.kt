package com.kasa77.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.RetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.login_signup_pages.LoginSignUpActivity
import com.kasa77.ui.login_signup_pages.fragments.LoginFragment
import com.kasa77.utils.Alerts
import kotlinx.android.synthetic.main.activity_forget_user_name.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class ForgetUserNameActivity : AppCompatActivity() {

    private var retrofitApiClient: RetrofitApiClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_user_name)
        retrofitApiClient = RetrofitService.getRetrofit()

        btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val uMobile = etMobileNumber.text.toString()

                if (uMobile.isEmpty()) {
                    Alerts.show(this@ForgetUserNameActivity, "Your Mobile should not be empty!")
                } else if (uMobile.length < 10) {
                    Alerts.show(this@ForgetUserNameActivity, "You entered a wrong Mobile Number!")
                } else {
                    forgotusername(uMobile)
                }
            }
        })

        /*bck_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?)
            {
                val intent = Intent(, LoginSignUpActivity::class.java)
                startActivity(intent)
               overridePendingTransition(0,0)
                finish()

            }
        })*/



    }


    fun onBackClick(view: View) {
     overridePendingTransition(0,0)
        finish()
    }

    private fun forgotusername(uMobile: String) {

        val jsonData = JSONObject()
        jsonData.put("mobile", "+91"+uMobile)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (jsonData).toString()
        )

        RetrofitService.getServerResponse(this@ForgetUserNameActivity,
            Dialog(this@ForgetUserNameActivity!!),
            retrofitApiClient!!.getUsername(body),
            object :
                WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val responseBody: ResponseBody = result!!.body() as ResponseBody
                    try {
                        val jsonObject = JSONObject(responseBody.string())
                        if (jsonObject.getInt("status") == 1) {

                            if (jsonObject.getInt("status") == 1) {

                                Alerts.AlertDialogSuccessAutoClose(this@ForgetUserNameActivity,this@ForgetUserNameActivity,jsonObject.getString("message"),"")
                            } else {
                                Alerts.AlertDialogWarning(this@ForgetUserNameActivity,jsonObject.getString("message"),"")

                            }
                        } else {
                            Alerts.AlertDialogWarning(this@ForgetUserNameActivity,jsonObject.getString("message"),"")

                        }
                    } catch (e: Exception) {
                        Alerts.AlertDialogWarning(this@ForgetUserNameActivity,e.message,"")

                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@ForgetUserNameActivity, error.toString())
                }

            })
    }
}
