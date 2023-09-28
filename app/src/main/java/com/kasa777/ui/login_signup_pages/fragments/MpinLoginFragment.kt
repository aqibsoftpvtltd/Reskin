package com.kasa777.ui.login_signup_pages.fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.NavigationMainActivity
import com.kasa777.ui.activity.ForgotPasswordOtpVerifyActivity
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.mpin_login_fragment.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MpinLoginFragment : Fragment(), View.OnClickListener {

    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.mpin_login_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        bindProgressButton(tvMpinSignIn)
        tabMpinSignIn.setOnClickListener(this)
        tabUsernameLogin.setOnClickListener(this)
        tvMpinSignIn.attachTextChangeAnimator()


        // Requesting cursor focus
        etUserMpin.requestFocus()

        etUserMpin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charLength = s.toString()
                if (charLength.isNotEmpty()) {
                    val cl = charLength.length
                    if (cl == 4) {
                        hideKeyboard()
                        loginWithMPIN(charLength)
                    }
                }
            }

        })


    }

    private fun hideKeyboard() {
        try {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken, 2
            )
        } catch (e: Exception) {
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tabMpinSignIn -> {
                val userPin = etUserMpin.text.toString()
                if (userPin.isEmpty() || userPin.length < 4) {
                    Alerts.show(mContext, "Please enter a valid MPIN")
                } else {
                    loginWithMPIN(userPin)
                }
            }
            R.id.tabUsernameLogin -> {
                requireFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_left, 0, 0, R.anim.exit_to_left)
                    .replace(
                        R.id.frameLogin,
                        LoginFragment(),
                        Constant.LoginFragment
                    )
                    .addToBackStack(null)
                    .commit()
            }
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
                                    .putExtra("from", "forgotMpinLoginTime")
                            )
                            Alerts.show(mContext, jsonObject.getString("message"))
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(context, error.toString())
                }

            })
    }

    private fun showProgressRight(button: Button) {
        button.showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.GREEN
        }
        button.isEnabled = false
        Handler().postDelayed({
            button.isEnabled = true
            button.hideProgress(R.string.sign_in)
        }, 3000)
    }


    private fun loginWithMPIN(userPin: String) {
        if (cd!!.isNetworkAvailable) {
            val deviceId = Helper(context).deviceId;
            val jsonObject = JSONObject()
            jsonObject.put("deviceId", deviceId)
            jsonObject.put("mpin", userPin + deviceId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (jsonObject).toString()
            )

            RetrofitService.getServerResponse(context,
                Dialog(requireActivity()),
                retrofitApiClient!!.loginWithMpin(body),
                object :
                    WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result?.body() as ResponseBody
                        try {
                            val responseObject = JSONObject(response.string())
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

                                AppPreference.setLongPreference(
                                    mContext,
                                    Constant.USER_LOGIN_TIME,
                                    currentTime
                                )

                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.USER_LOGIN_TOKEN,
                                    userToken
                                )
                                Constant.USER_TOKEN = userToken
                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.USER_LOGIN_MOBILE,
                                    userLoginMobile
                                )
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
                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.USER_LOGIN_ID,
                                    userLoginId
                                )
                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.USER_LOGIN_ID,
                                    userLoginId
                                )
                                AppPreference.setBooleanPreference(
                                    mContext,
                                    Constant.IS_LOGIN,
                                    true
                                )
                                AppPreference.setStringPreference(
                                    mContext,
                                    Constant.USER_LOGIN_FULL_NAME,
                                    userNickName
                                )

                                AppPreference.setBooleanPreference(
                                    mContext,
                                    Constant.IsMpinGenerate,
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
                                //  Alerts.show(mContext, responseObject.getString("message"))
                                val intent =
                                    Intent(mContext, NavigationMainActivity::class.java)
                                        .putExtra("from", "")

                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
//                                activity!!.finish()
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