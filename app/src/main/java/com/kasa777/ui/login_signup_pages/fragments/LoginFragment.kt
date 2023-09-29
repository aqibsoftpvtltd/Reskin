package com.kasa777.ui.login_signup_pages.fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.google.gson.Gson
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.modal.logindetails.new_device_old_account.GetOldDeviceDetails
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.NavigationMainActivity
import com.kasa777.ui.activity.ForgetUserNameActivity
import com.kasa777.ui.activity.ForgotPasswordOtpVerifyActivity
import com.kasa777.ui.activity.SubmitOtpUdpateDeviceActivity
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.fragment_login_page.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener {

    private var isMpinGenerated: Boolean? = false
    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_login_page, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = RetrofitService.getRetrofit()

        bindProgressButton(tvLogin)
        tvLogin.attachTextChangeAnimator()

        var userName = AppPreference.getStringPreference(mContext, Constant.userLoginUserName)

        etUserName.setText(userName);



        btnForgotPassword.setOnClickListener(this)
        btnForgotUsername.setOnClickListener(this)
        tabLogin.setOnClickListener(this)
        tabMPINLogin.setOnClickListener(this)
        tabSignup.setOnClickListener(this)
        isMpinGenerated = AppPreference.getBooleanPreference(mContext, Constant.IsMpinGenerate)
        if (isMpinGenerated == true) {
            tabMPINLogin.visibility == View.VISIBLE
        } else {
            tabMPINLogin.visibility = View.GONE
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tabLogin -> {
                // showProgressRight(tabLogin)
                doUserSignIn()
            }
            R.id.btnForgotPassword -> {
                forgotPassword()
            }

            R.id.forgotPassword -> {
                val intent = Intent(
                    context,
                    ForgetUserNameActivity::class.java
                )
                startActivity(intent)
            }
            R.id.tabMPINLogin -> {
                requireFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_left)
                    .replace(
                        R.id.frameLogin,
                        MpinLoginFragment(),
                        Constant.MpinLoginFragment
                    )
                    .addToBackStack(null)
                    .commit()
            }
            R.id.tabSignup -> {
                requireFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_left)
                    .replace(
                        R.id.frameLogin,
                        CreateProfileFragment(),
                        Constant.MpinLoginFragment
                    )
                    .addToBackStack(null)
                    .commit()
            }

        }
    }


    private fun isValidUserName(mUserName: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        /*RE for Any alphanumeric combination start with
          an alphabet and should be length 5 or more*/
        val emailPattern = "^[a-zA-Z][a-zA-Z0-9]{5,}$"
        pattern = Pattern.compile(emailPattern)
        matcher = pattern.matcher(mUserName)
        return matcher.matches()
    }


    private fun doUserSignIn() {
        val deviceId = Helper(context).deviceId;
        val uName = etUserName.text.toString().trim()
        val uPass = etUserPass.text.toString().trim()
        if (!isValidUserName(uName)) {
            Alerts.show(mContext, "You entered a wrong User Name")
        } else if (uPass.length < 5) {
            Alerts.show(mContext, " Password length must be minimum 5 characters!!!")
        } else {
            if (cd!!.isNetworkAvailable) {
                val jsonObject = JSONObject()
                jsonObject.put("username", uName)
                jsonObject.put("password", uPass)
                jsonObject.put("deviceId", deviceId)
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    (jsonObject).toString()
                )

                RetrofitService.getServerResponse(context,
                    Dialog(requireActivity()),
                    retrofitApiClient!!.loginUser(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            val response = result?.body() as ResponseBody
                            val jsonresponse = JSONObject(response.string())
                            Log.e("OnResponse", jsonresponse.toString())
                            try {
                                val responseObject = jsonresponse
                                if (responseObject.getInt("status") == 1) {

                                    getLoginDetailsIfDeviceisValid(responseObject)
                                } else if (responseObject.getInt("status") == 2) {
                                    getDeviceChangeDetails(responseObject.toString());
                                } else {
                                    Alerts.AlertDialogWarning(mContext, responseObject.getString("message"))
                                }

                            } catch (e: Exception) {
                                Alerts.show(mContext, e.toString())
                            }
                        }


                        override fun onResponseFailed(error: String?) {
                            Alerts.serverError(context, error.toString())
                        }
                    })
            }
        }
    }

    private fun getDeviceChangeDetails(response: String) {
        var gson = Gson()
        val jsonString = response.toString()
        var testModel = gson.fromJson(jsonString, GetOldDeviceDetails::class.java)
        Constant.USER_TOKEN = testModel.data.token
        AppPreference.setStringPreference(
            context,
            Constant.USER_LOGIN_TOKEN,
            testModel.data.token
        )
        Alerts.AlertDialogDeviceUpdate(context, testModel.data.userName, View.OnClickListener {
            sendOtpToDevice(testModel)
            if (Alerts.getDialog().isShowing)
                Alerts.getDialog().dismiss()
        })


    }

    private fun sendOtpToDevice(testModel: GetOldDeviceDetails) {
        val jsonObject = JSONObject()
        jsonObject.put("mobile", testModel.data.mobileNumber)
        jsonObject.put("reqType", false)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (jsonObject).toString()
        )
        RetrofitService.getServerResponse(context,
            Dialog(requireActivity()),
            AuthHeaderRetrofitService.getRetrofit()!!.sendOTP(body),
            object : WebResponse {
                override fun onResponseFailed(error: String?) {
                    print("RES : " + error.toString())
                    Alerts.serverError(context, error.toString())
                }

                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result?.body() as ResponseBody
                    print("RES : " + response.toString())
                    Log.i("RES: -> ", response.toString())
                    val responseObject = JSONObject(response.string())
                    if (responseObject.getInt("status") == 1) {
                        val intent = Intent(activity, SubmitOtpUdpateDeviceActivity::class.java)
                        intent.putExtra("DATA", testModel)
                        startActivity(intent)
                    } else {
                        Alerts.show(mContext, responseObject.getString("message"))
                    }
                }

            })
    }

    private fun getLoginDetailsIfDeviceisValid(responseObject: JSONObject) {
        if (responseObject.getInt("status") == 1) {
            val dataObject = responseObject.getJSONObject("data")

            val userToken = dataObject.getString("token")
            val userLoginMobile = dataObject.getString("mobile")
            val userLoginUserName = dataObject.getString("username")
            val userWalletBalance = dataObject.getInt("wallet_balance")
            val userLoginId = dataObject.getString("userId")
            val userNickName = dataObject.getString("name")
            val mainNotification = dataObject.getBoolean("mainNotification")
            val gameNotification = dataObject.getBoolean("gameNotification")
            val starLineNotification =
                dataObject.getBoolean("starLineNotification")
            val andarBaharNotification =
                dataObject.getBoolean("andarBaharNotification")

            val currentTime = System.currentTimeMillis()

            val mpinGenerated = responseObject.getInt("mpinGenerated")
            if (mpinGenerated == 1) {
                AppPreference.setBooleanPreference(mContext, Constant.IsMpinGenerate, true)
            } else {
                AppPreference.setBooleanPreference(mContext, Constant.IsMpinGenerate, false)
            }


            AppPreference.setLongPreference(mContext, Constant.USER_LOGIN_TIME, currentTime)
            AppPreference.setStringPreference(
                mContext,
                Constant.userLoginUserName,
                userLoginUserName
            )
            AppPreference.setStringPreference(mContext, Constant.USER_LOGIN_TOKEN, userToken)
            Constant.USER_TOKEN = userToken
            AppPreference.setStringPreference(mContext, Constant.USER_LOGIN_MOBILE, userLoginMobile)
            AppPreference.setStringPreference(
                mContext,
                Constant.USER_LOGIN_USER_NAME,
                userLoginUserName
            )
            AppPreference.setIntegerPreference(
                mContext,
                Constant.USER_WALLET_BALANCE,
                userWalletBalance
            )
            AppPreference.setStringPreference(mContext, Constant.USER_LOGIN_ID, userLoginId)
            AppPreference.setStringPreference(mContext, Constant.USER_LOGIN_FULL_NAME, userNickName)
            AppPreference.setBooleanPreference(
                mContext,
                Constant.IS_LOGIN,
                true
            )


            AppPreference.setBooleanPreference(
                mContext,
                Constant.mainNotification,
                mainNotification
            )
            AppPreference.setBooleanPreference(
                mContext,
                Constant.gameNotification,
                gameNotification
            )
            AppPreference.setBooleanPreference(
                mContext,
                Constant.starLineNotification,
                starLineNotification
            )
            AppPreference.setBooleanPreference(
                mContext,
                Constant.andarBaharNotification,
                andarBaharNotification
            )

            val intent =
                Intent(mContext, NavigationMainActivity::class.java)
                    .putExtra("from", "")

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
//            activity!!.finish()
        } else {
            Alerts.show(mContext, responseObject.getString("message"))
        }
    }


    private fun forgotPassword() {
        val deviceId = Helper(context).deviceId;
        val jsonData = JSONObject()
        jsonData.put("deviceId", deviceId)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (jsonData).toString()
        )

        RetrofitService.getServerResponse(context,
            Dialog(requireActivity()),
            retrofitApiClient!!.getForgotPasswordOtp(body),
            object :
                WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val responseBody: ResponseBody = result!!.body() as ResponseBody
                    try {
                        val jsonObject = JSONObject(responseBody.string())
                        if (jsonObject.getInt("status") == 1) {
                            val mobile = jsonObject.getString("mobileNumber")
                            startActivity(
                                Intent(mContext, ForgotPasswordOtpVerifyActivity::class.java)
                                    .putExtra("mobileNumber", mobile)
                                    .putExtra("from", "login")
                            )
                        } else {
                            Alerts.show(mContext, jsonObject.getString("message"))
                        }
                    } catch (e: Exception) {
                        Alerts.show(mContext, e.toString())
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(context, error.toString())
                }

            })
    }


}