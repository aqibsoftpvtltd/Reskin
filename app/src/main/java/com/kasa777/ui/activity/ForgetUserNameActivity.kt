package com.kasa777.ui.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kasa777.R
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
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

    }


    fun onBackClick(view: View) {
        onBackPressed()
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

                                Alerts.AlertDialogSuccessAutoClose(this@ForgetUserNameActivity,this@ForgetUserNameActivity,jsonObject.getString("message"))
                            } else {
                                Alerts.AlertDialogWarning(this@ForgetUserNameActivity,jsonObject.getString("message"))

                            }
                        } else {
                            Alerts.AlertDialogWarning(this@ForgetUserNameActivity,jsonObject.getString("message"))

                        }
                    } catch (e: Exception) {
                        Alerts.AlertDialogWarning(this@ForgetUserNameActivity,e.message)

                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@ForgetUserNameActivity, error.toString())
                }

            })
    }
}
