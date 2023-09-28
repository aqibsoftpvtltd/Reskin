package com.kasa777.ui.login_signup_pages.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
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
import com.kasa777.retrofit_provider.RetrofitServicePinCode
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.NavigationMainActivity
import com.kasa777.ui.activity.SubmitOtpUdpateDeviceActivity
import com.kasa777.ui.login_signup_pages.LoginSignUpActivity
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.activity_starline_dashboard.*
import kotlinx.android.synthetic.main.activity_submit_otp_udpate_device.*
import kotlinx.android.synthetic.main.activity_sumit_idea.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_signup_pages.*
import kotlinx.android.synthetic.main.layout_mobile_register.*
import kotlinx.android.synthetic.main.layout_mpin.*
import kotlinx.android.synthetic.main.layout_otp_register.*
import kotlinx.android.synthetic.main.layout_register_form.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class CreateProfileFragment : Fragment(), View.OnClickListener {

    private var uMpin = ""
    private var timer: CountDownTimer? = null
    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var strlayout: String? = "mobile"
    private var uLastName = ""
    private var uFirstName = ""
    private var uAge = ""
    private var uCity = ""
    private var uPinCode = ""
    private var uName = ""
    private var uPass = ""
    private var uMobile = ""
    private var uOTP = ""
    private var otpId = ""
    private var uMobileNumber = ""
    private var firebaseToken = ""

    //    var mCredentialsApiClient: GoogleApiClient? = null
//    private val RC_HINT = 2
//    val smsBroadcast = MySMSBroadcastReceiver()
    var out: Animation? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_signup_pages, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        val inA = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left)
        out = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right)
//        viewFlipper.inAnimation = inA
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofitReg()
        bindProgressButton(tvContinueMPIN)
        bindProgressButton(tvSendOTP)
        bindProgressButton(tvOTPContinue)
        bindProgressButton(tvContinueSignup)
        tabContinueMPIN.setOnClickListener(this)
        tabSendOTP.setOnClickListener(this)
        tabOTPContinue.setOnClickListener(this)
        tabContinueSignup.setOnClickListener(this)

        tvResendOTP.setOnClickListener(this)
        tabLogin.setOnClickListener(this)
        ivMainBack.setOnClickListener(this)

        tvContinueMPIN.attachTextChangeAnimator()
        tvSendOTP.attachTextChangeAnimator()
        tvOTPContinue.attachTextChangeAnimator()
        tvContinueSignup.attachTextChangeAnimator()

        etPincode.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ("$s".length == 6) {
                    getCityFromPinCode(s.toString())
                } else {
                    etCity.setText("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })


//        ## "Welcome"
//        ## "India's best Satta Matka Application Welcomes You!!!"
        flipView("mobile", llMoblie, true)
    }

    fun getCityFromPinCode(s: String?) {
        if (cd!!.isNetworkAvailable) {
            RetrofitServicePinCode.getServerResponse(context,
                null,
                RetrofitServicePinCode.getRetrofit()!!.getCityFromPinCode(s),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getString("Status") == "Success") {
                            val firstPostOffice =
                                JSONObject(responseObject.getJSONArray("PostOffice")[0].toString())
                            etCity.setText(firstPostOffice.getString("Circle"))
                        } else {
                            etCity.setText("")
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                    }
                })
        }


    }

    fun flipView(id: String, layout: View, initial: Boolean) {
        try {
            strlayout = id
            when (id) {
                "mobile" -> {
                    llMoblie.visibility = View.VISIBLE
                    llForm.visibility = View.GONE
                    llMpin.visibility = View.GONE
                    llOtp.visibility = View.GONE
                }
                "form" -> {
                    llMoblie.visibility = View.GONE
                    llForm.visibility = View.VISIBLE
                    llMpin.visibility = View.GONE
                    llOtp.visibility = View.GONE
                }
                "mpin" -> {
                    llMoblie.visibility = View.GONE
                    llForm.visibility = View.GONE
                    llMpin.visibility = View.VISIBLE
                    llOtp.visibility = View.GONE
                }
                "otp" -> {
                    llMoblie.visibility = View.GONE
                    llForm.visibility = View.GONE
                    llMpin.visibility = View.GONE
                    llOtp.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            Log.e("ERRORRRRRRR ID", id)
            Log.e("ERRORRRRRRR", e.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        strlayout?.let { Log.i("STR_LAYOUT", it) }
        when (p0!!.id) {
            R.id.tabContinueMPIN->{
                if (isValidMpin()) {
//                            ## "OTP Verification"
//                            ## Html.fromHtml("Verification code has been sent to your mobile number <b>+91$uMobile</b>. please check and enter OTP.")
                    flipView("otp", llOtp, false)
                    initUserMobileContact()
                }
            }
            R.id.tabSendOTP->{
                uMobile = etMobileNumber.text.toString()

                if (uMobile.isEmpty()) {
                    Alerts.show(mContext, "Your Mobile should not be empty!")
                } else if (uMobile.length < 10) {
                    Alerts.show(mContext, "You entered a wrong Mobile Number!")
                } else {

                    checkForMobileNumber(uMobile)

                }
            }
            R.id.tabOTPContinue->{
                when {
                    strlayout.equals("otp") -> {
                        strlayout = "done"
//                        initUserMobileContact()
                    }
                    strlayout.equals("done") -> {
                        submitUser()

                    }
                }
            }
            R.id.tabContinueSignup->{
                initUserBasicContact()


            }
            R.id.tvResendOTP -> {
                if (tvResendOTP.text.toString() == requireActivity().getString(R.string.resend_otp)) {
                    resendOtp()
                } else {
                    Alerts.show(mContext, "Please wait until otp time is end!!")
                }
            }
//            R.id.txtChangeMobile -> {
//                viewFlipper.animation = out
//                flipView("otp", llOtp, false)
//            }
            R.id.ivMainBack -> {
                    requireActivity().onBackPressed()
            }
            R.id.tabLogin -> {
                requireFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_left, 0, 0, R.anim.exit_to_right)
                    .replace(
                        R.id.frameLogin, LoginFragment(), Constant.LoginFragment
                    ).commit()
            }
        }

    }


    private fun checkForMobileNumber(uMobile: String) {
        val deviceId = Helper(context).deviceId;
        val jsonObject = JSONObject()
        jsonObject.put("mobile", "+91" + uMobile)
        jsonObject.put("deviceId", deviceId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(Dialog(requireActivity()),
            retrofitApiClient!!.verifyMobile(body),
            object : WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 0) {
//                            ## "Create Your New Account"
//                            ## "Please enter your details to create new account"
                            flipView("form", llForm, false)
                        } else {
                            var gson = Gson()

                            var testModel = gson.fromJson(
                                responseObject.toString(), GetOldDeviceDetails::class.java
                            )
                            Alerts.AlertDialogDeviceUpdate(context,
                                testModel.data.userName,
                                object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        sendOtpToDevice(testModel)
                                        if (Alerts.getDialog().isShowing) Alerts.getDialog()
                                            .dismiss()
                                    }
                                })
                        }
                    } catch (e: Exception) {
                        dialogBoxMessage(e.toString())
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(context, error.toString())
                }
            })

    }



    private fun otpTimerStart() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        timer = object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                try {
                    tvResendOTP.text = requireActivity().getString(R.string.seconds_remaining_)+" " + millisUntilFinished / 1000
                } catch (e: Exception) {
                }
                //here you can have your logic to set text to edittext
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                try {
                    tvResendOTP.text = requireActivity().getString(R.string.resend_otp)
                } catch (e: Exception) {
                    timer!!.cancel()
                }
            }

        }
        timer!!.start()

    }

    private fun isValidMpin(): Boolean {
        uMpin = etMpin.text.toString()
        val cPass = etConfirmMpin.text.toString()

        var isValid = false
        when {
            uMpin.isEmpty() -> Alerts.showSnack(
                etMpin, "Your MPIN should not be empty!"
            )
            uMpin.length < 3 -> {
                Alerts.show(context, "MPIN is 4 digit number !!!")
            }
            cPass != uMpin -> Alerts.showSnack(
                etConfirmPassword, "Your both MPIN must be same!"
            )

            else -> isValid = true
        }
        return isValid
    }

    private fun initUserBasicContact() {
        uLastName = etLastName.text.toString()
        uName = etUserName.text.toString()
        uPass = etUserPassword.text.toString()

        uFirstName = etFirstName.text.toString()
        uAge = etAge.text.toString()
        uCity = etCity.text.toString().trim()
        uPinCode = etPincode.text.toString()

        val cPass = etConfirmPassword.text.toString()

//        var isValid = false
        when {
            uName.isEmpty() -> Alerts.showSnack(etUserName, "Your User Name should not be empty!")
            uFirstName.isEmpty() -> Alerts.showSnack(
                etUserName, "Your First Name should not be empty!"
            )
            uLastName.isEmpty() -> Alerts.showSnack(
                etLastName, "Your Last Name should not be empty!"
            )
            uAge.isEmpty() -> Alerts.showSnack(etUserName, "Your Age should not be empty!")
            uCity.isEmpty() -> Alerts.showSnack(etUserName, "Your City should not be empty!")
            uPinCode.isEmpty() -> Alerts.showSnack(etUserName, "Your Pin Code should not be empty!")

            !isValidUserName(uName) -> Alerts.showSnack(
                etUserName, "Username too small minimum 5 character required"
            )
            uPass.isEmpty() -> Alerts.showSnack(
                etUserPassword, "Your Password should not be empty!"
            )
            cPass != uPass -> Alerts.showSnack(
                etConfirmPassword, "Your both Password must be same!"
            )
            else -> {
                checkUserName(uName)

            }

        }
//        return isValid
    }

    private fun checkUserName(uName: String) {

        val jsonObject = JSONObject()

        jsonObject.put("username", uName)


        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(Dialog(requireActivity()),
            AuthHeaderRetrofitService.getRetrofit()!!.checkUsername(body),
            object : WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        Log.e("OnResponse", responseObject.toString())
                        if (responseObject.getInt("status") == 0) {

                            Alerts.AlertDialogWarning(
                                requireContext(), responseObject.getString("message")
                            )

                        } else {
//                             isValid = true
                            flipView("mpin", llMpin, false)
//                            ## "MPIN Registration"
//                            ## "Provide a 4 digit numeric MPIN to protect your account against unauthorized access"
                        }

                    } catch (e: Exception) {

                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(requireContext(), error.toString())
                }
            })


    }

    private fun initUserMobileContact() {
        uMobile = etMobileNumber.text.toString()
        when {
            uMobile.isEmpty() -> Alerts.show(mContext, "Your Mobile should not be empty!")
            uMobile.length < 10 -> Alerts.show(mContext, "You entered a wrong Mobile Number!")
            cd!!.isNetworkAvailable -> {
                val jsonObject = JSONObject()
                jsonObject.put("mobile", "+91$uMobile")
                jsonObject.put("reqType", true)
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
                )
                AuthHeaderRetrofitService.getServerResponse(Dialog(requireActivity()),
                    retrofitApiClient!!.sendOTP(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            val response = result!!.body() as ResponseBody
                            try {
                                val responseObject = JSONObject(response.string())
                                Log.e("OnResponse : hello", responseObject.toString())
                                otpId = responseObject.getJSONObject("data").getString("id")
                                if (responseObject.getInt("status") == 1) {
                                    Alerts.show(
                                        mContext,
                                        "OTP successfully sent to your entered Mobile Number +91$uMobile"
                                    )
                                    uMobileNumber = "+91$uMobile"
                                    flipView("otp", llOtp, false)
//                                    ## "Fetching OTP"
                                    otpTimerStart()
                                    strlayout = "done"
                                    tvOTPContinue.text = requireActivity().getString(R.string.submit)

                                } else {
                                    Alerts.show(mContext, responseObject.getString("message"))
                                }
                            } catch (e: Exception) {
                                Log.e("sendOtp error", e.toString())
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

    private fun resendOtp() {
        when {
            uMobileNumber.isEmpty() -> Alerts.show(mContext, "Your Mobile should not be empty!")
            cd!!.isNetworkAvailable -> {
                val jsonObject = JSONObject()
                jsonObject.put("mobile", uMobileNumber)
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
                )
                AuthHeaderRetrofitService.getServerResponse(Dialog(requireActivity()),
                    retrofitApiClient!!.getOtp(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            val response = result!!.body() as ResponseBody
                            try {
                                val responseObject = JSONObject(response.string())
                                if (responseObject.getInt("status") == 1) {
                                    Alerts.show(
                                        mContext, "OTP successfully sent to your Mobile Number"
                                    )
                                    otpTimerStart()
                                }
                            } catch (e: Exception) {
                                Log.e("sendOtp error", e.toString())
                                dialogBoxMessage(e.toString())
                            }
                        }

                        override fun onResponseFailed(error: String?) {
                            Alerts.serverError(context, error.toString())
                        }
                    })
            }
        }


    }

    private fun initUserOTP() {

    }

    private fun isValidEmailId(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val emailPattern =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(emailPattern)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidOTP(mOtp: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        /*RE for any combination of only
          numeric value of length 4*/
        val emailPattern = "^[0-9]{6}$"
        pattern = Pattern.compile(emailPattern)
        matcher = pattern.matcher(mOtp)
        return matcher.matches()
    }

    private fun isValidUserName(mUserName: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        /*RE for Any alphanumeric combination start with
          an alphabet and should be length 5 or more*/
        val emailPattern = "^[a-zA-Z0-9]{5,}$"
        pattern = Pattern.compile(emailPattern)
        matcher = pattern.matcher(mUserName)
        return matcher.matches()
    }

    /******************************************
     *
     * <#> Your otp is 2782. Please do not share it with anybody
    GIir0P5ipLh

     * SMS FORMAT ==>
     * <#> OTP MESSAGE CONTENT
    GIir0P5ipLh
     ****************************************/


//    private fun startSMSListener() {
//
//        val client = SmsRetriever.getClient(mContext!!)
//        val task = client.startSmsRetriever()
//        task.addOnSuccessListener {
//            // Successfully started retriever, expect broadcast intent
//            // ...
//            //otpTxtView.text = "Waiting for the OTP"
//            //Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
//        }
//
//        task.addOnFailureListener {
//            //otpTxtView.text = "Cannot Start SMS Retriever"
//            //Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
//        }
//    }

    /*  override fun onConnected(p0: Bundle?) {

      }

      override fun onConnectionSuspended(p0: Int) {

      }

      override fun onConnectionFailed(p0: ConnectionResult) {

      }

      override fun onOTPReceived(otp: String) {
          if (smsBroadcast != null) {
              LocalBroadcastManager.getInstance(mContext!!).unregisterReceiver(smsBroadcast)
          }
          etOTP.setText(extractOtpFromSms(otp))
          submitUser()
          //Toast.makeText(this, extractOtpFromSms(otp), Toast.LENGTH_LONG).show()
          //otpTxtView.text = "Your OTP is: $otp"
          //Log.e("OTP Received", otp)
      }

      override fun onOTPTimeOut() {

      }*/

    private fun extractOtpFromSms(msg: String): String {
        val pattern = Pattern.compile("(\\d{4})")
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


    private fun submitUser() {
        uOTP = etOTP.text.toString()

        firebaseToken = AppPreference.getStringPreference(mContext, Constant.FIREBASE_TOKEN)

        if (uOTP.isEmpty()) {
            Alerts.show(mContext, "OTP should not be empty!")
        } else if (!isValidOTP(uOTP)) {
            Alerts.show(mContext, "You entered a wrong OTP!")
        } else {
            val deviceName = Build.MODEL
            val deviceId = Helper(context).deviceId;
            val regitstrationThrough = "1"

            var userObject = JSONObject()
            userObject.put("name", uFirstName)
            userObject.put("mpin", uMpin + Helper(context).deviceId)
            userObject.put("password", uPass)
            userObject.put("username", uName)
            userObject.put("mobile", "+91$uMobile")
            userObject.put("firebaseId", firebaseToken)
            userObject.put("deviceName", deviceName)
            userObject.put("deviceId", deviceId)
            userObject.put("deviceVeriOTP", uOTP)
            userObject.put("register_via", regitstrationThrough)
            userObject.put("id", otpId)

            userObject.put("firstName", uFirstName)
            userObject.put("lastName", uLastName)
            userObject.put("age", uAge)
            userObject.put("city", uCity)
            userObject.put("pinCode", uPinCode)
            Log.i("Params", (userObject).toString())
            if (cd!!.isNetworkAvailable) {
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(), (userObject).toString()
                )
                AuthHeaderRetrofitService.getServerResponse(Dialog(requireActivity()),
                    retrofitApiClient!!.registerUser(body),
                    object : WebResponse {
                        override fun onResponseSuccess(result: Response<*>?) {
                            var msg: ResponseBody = result!!.body() as ResponseBody

                            try {
                                val responseObject = JSONObject(msg.string())
                                Log.e("OnResponse", responseObject.toString())
                                if (responseObject.getInt("status") == 1) {

                                    val dataObject = responseObject.getJSONObject("data")
                                    val welcome_message =
                                        responseObject.optString("welcome_message")
                                    val userToken = dataObject.getString("token")
                                    val userLoginMobile = dataObject.getString("mobile")
                                    val userLoginUserName = dataObject.getString("username")
                                    val userWalletBalance = dataObject.getInt("wallet_balance")
                                    val userLoginId = dataObject.getString("userId")
                                    val userNickName = dataObject.getString("name")
                                    val userFirstName = dataObject.getString("firstName")
                                    val userLastName = dataObject.getString("lastName")
                                    val mainNotification = dataObject.getBoolean("mainNotification")
                                    val gameNotification = dataObject.getBoolean("gameNotification")
                                    val starLineNotification =
                                        dataObject.getBoolean("starLineNotification")
                                    val andarBaharNotification =
                                        dataObject.getBoolean("andarBaharNotification")

                                    val currentTime = System.currentTimeMillis()

                                    /*if(AppMainPreference.getStringPreference(mContext,Constant.FIREBASE_TOKEN).isEmpty()){
                                        AppMainPreference.setStringPreference(mContext, Constant.FIREBASE_TOKEN, firebaseToken)
                                    }*/
                                    AppPreference.setLongPreference(
                                        mContext, Constant.USER_LOGIN_TIME, currentTime
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.userLoginUserName, userLoginUserName
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LOGIN_TOKEN, userToken
                                    )
                                    Constant.USER_TOKEN = userToken
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LOGIN_MOBILE, userLoginMobile
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LOGIN_USER_NAME, userLoginUserName
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_FIRST_NAME, userFirstName
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LAST_NAME, userLastName
                                    )
                                    AppPreference.setIntegerPreference(
                                        mContext, Constant.USER_WALLET_BALANCE, userWalletBalance
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LOGIN_ID, userLoginId
                                    )
                                    AppPreference.setStringPreference(
                                        mContext, Constant.USER_LOGIN_FULL_NAME, userNickName
                                    )
                                    AppPreference.setBooleanPreference(
                                        mContext, Constant.IS_LOGIN, true
                                    )

                                    AppPreference.setBooleanPreference(
                                        mContext, Constant.mainNotification, mainNotification
                                    )
                                    AppPreference.setBooleanPreference(
                                        mContext, Constant.gameNotification, gameNotification
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
                                    AppPreference.setBooleanPreference(
                                        mContext, Constant.IS_PROFILE_CREATED, true
                                    )

                                    val intent = Intent(
                                        context, NavigationMainActivity::class.java
                                    ).putExtra("from", "")
                                        .putExtra("welcome_message", welcome_message)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                                    startActivity(intent)
                                    Alerts.show(mContext, responseObject.getString("message"))
//                                    activity!!.finish()
                                } else {
                                    Alerts.AlertDialogWarning(
                                        mContext, responseObject.getString("message")
                                    )
                                }

                            } catch (e: Exception) {
                                Log.e("sendOtp error", e.toString())
                                dialogBoxMessage(e.toString())
                            }
                        }

                        override fun onResponseFailed(error: String?) {
                            Alerts.serverError(context, error.toString())
                        }
                    })
            }
        }
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


    private fun sendOtpToDevice(testModel: GetOldDeviceDetails) {


        val jsonObject = JSONObject()
        jsonObject.put("mobile", testModel.data.mobileNumber)
        jsonObject.put("reqType", true)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), (jsonObject).toString()
        )
        Log.i("SendOTP text: SendOTP", (jsonObject).toString())
        RetrofitService.getServerResponse(context,
            Dialog(requireActivity()),
            AuthHeaderRetrofitService.getRetrofit()!!.sendOTP(body),
            object : WebResponse {
                override fun onResponseFailed(error: String?) {
                    print("RES : SendOTP " + error.toString())
                    Alerts.serverError(context, error.toString())
                }

                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result?.body() as ResponseBody
                    print("RES : SendOTP " + response.toString())
                    val responseObject = JSONObject(response.string())
                    otpId = responseObject.getJSONObject("data").getString("id")
                    Log.i("RES: SendOTP -> ", responseObject.toString())
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

}