package com.kasa777.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.utils.Alerts
import com.kasa777.utils.AppPreference
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_sumit_idea.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class SumitIdeaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sumit_idea)

        et_idea.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_counter.text = p0!!.length.toString()+"/500"
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    fun onSubmitClick(view: View) {
        if(et_idea.text.isEmpty()){
           et_idea.setError("Please Enter Your Idea")
            et_idea.requestFocus()
        }else {

            val userLoginId =
                AppPreference.getStringPreference(this@SumitIdeaActivity, Constant.USER_LOGIN_ID)
            val userName =
                AppPreference.getStringPreference(
                    this@SumitIdeaActivity,
                    Constant.USER_LOGIN_USER_NAME
                )

            val jsonObject = JSONObject()
            jsonObject.put("userid", userLoginId)
            jsonObject.put("username", userName)
            jsonObject.put("idea", et_idea.text.trim().toString())

            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (jsonObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@SumitIdeaActivity),
                AuthHeaderRetrofitService.getRetrofit()!!.submitIdea(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
                            Log.e("OnResponse", responseObject.toString())
                            et_idea.setText("")
                            Alerts.AlertDialogSuccess(
                                this@SumitIdeaActivity,
                                responseObject.getString("message")
                            )

                        } catch (e: Exception) {

                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@SumitIdeaActivity, error.toString())
                    }
                })

        }
    }
}