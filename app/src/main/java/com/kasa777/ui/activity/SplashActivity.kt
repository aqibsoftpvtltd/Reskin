package com.kasa777.ui.activity

import android.Manifest.permission.*
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.kasa777.BuildConfig
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.RetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.login_signup_pages.LoginSignUpActivity
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException


class SplashActivity : AppCompatActivity() {


    private var dialog: Dialog? = null
    private val requestMultiplePermission = 123
    private var token = ""
    var cd: ConnectionDetector? = null
    var retrofitApiClient: RetrofitApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        hideNavigation()
        cd = ConnectionDetector(this@SplashActivity)
        retrofitApiClient = RetrofitService.getRetrofit()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaseMessageService", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                token = task.result!!.token

                Log.e("FirebaseMessageService", token)
                if (AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN).isEmpty()) {
                    AppPreference.setStringPreference(this, Constant.FIREBASE_TOKEN, token)
                }

                checkAndroidVersion()
            })


    }

    private fun init() {
//        Handler().postDelayed({
        if (AppPreference.getBooleanPreference(this, Constant.IS_LOGIN)) {
            AppPreference.setBooleanPreference(this@SplashActivity, Constant.IS_LOGIN, false);

        }
        val intent=Intent(this, LoginSignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

//        }, 2000)
    }

    private fun checkAndroidVersion() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkPermission()
//
//        } else {
            createRequestBody()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this@SplashActivity, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
             ContextCompat.checkSelfPermission(this@SplashActivity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
              ContextCompat.checkSelfPermission(
                this@SplashActivity,
                WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
        ) {


            dialogPermission()


        } else {
            createRequestBody()

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            requestMultiplePermission -> if (grantResults.isNotEmpty()) {
                val readPhoneState = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeRecordAudio = grantResults[2] == PackageManager.PERMISSION_GRANTED
                val writeExternalStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED

                if (readPhoneState && writeRecordAudio && writeExternalStorage) {
                    if (dialog != null) {
                        dialog!!.dismiss()
                    }
                    createRequestBody()
                    // init()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Please Grant Permissions !",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("ENABLE") {
                        requestPermissions(
                            arrayOf(
                                READ_PHONE_STATE,
                                READ_EXTERNAL_STORAGE,
                                RECORD_AUDIO,
                                WRITE_EXTERNAL_STORAGE
                            ),
                            requestMultiplePermission
                        )
                    }.show()
                }
            }
        }
    }

    private fun createRequestBody() {
        val deviceImei = Helper(this@SplashActivity).deviceId;
        AppPreference.setStringPreference(
            this@SplashActivity,
            Constant.DEVICE_ID,
            deviceImei
        )
        Log.e(
            "newDeviceID",
            deviceImei + "  " + AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN)
        )
        val mObject = JSONObject()
        try {
            mObject.put("deviceId", deviceImei)
            mObject.put("appVersion", BuildConfig.VERSION_CODE)
            mObject.put(
                "token", AppPreference.getStringPreference(this, Constant.FIREBASE_TOKEN)
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            mObject.toString()
        )
        tokenApi(body)
    }


    private fun tokenApi(body: RequestBody) {

        // status : 0 : Sing Up
        // status : 1 : Login
        // status : 5 : maintainance
        // status : 4 : App Upodate

        if (cd!!.isNetworkAvailable) {
            RetrofitService.getServerResponse(this,
                null,
                retrofitApiClient!!.updateFirebaseToken(body),
                object :
                    WebResponse {
                    override fun onResponseSuccess(result: Response<*>) {
                        /*{"status":1,"message":"Token Updated Successfully","data":{"n":0,"nModified":0,"ok":1}}*/
                        val responseBody = result.body() as ResponseBody?
                        try {
                            val jsonObject = JSONObject(responseBody!!.string())
                            val jsonresponse = JSONObject(jsonObject.toString())
                            Log.e("OnResponse", jsonresponse.toString())
                            if (jsonObject.getInt("status") == 1)
                            {

                                val userToken = jsonObject.getString("token")
                                val name = jsonObject.optString("name")
                                val mpinGen = jsonObject.optInt("mpinGen")





                                if (mpinGen == 0) {
                                    AppPreference.setBooleanPreference(
                                        this@SplashActivity,
                                        Constant.IsMpinGenerate,
                                        true
                                    )

                                    AppPreference.setBooleanPreference(
                                        this@SplashActivity,
                                        Constant.isLoginWithMpin,
                                        true
                                    )
                                    if (AppPreference.getBooleanPreference(
                                            this@SplashActivity,
                                            Constant.isLoginWithUsername
                                        )
                                    ) {
                                        AppPreference.setBooleanPreference(
                                            this@SplashActivity,
                                            Constant.isLoginWithMpin,
                                            false
                                        )
                                    }
                                } else {
                                    AppPreference.setBooleanPreference(
                                        this@SplashActivity,
                                        Constant.IsMpinGenerate,
                                        false
                                    )
                                    AppPreference.setBooleanPreference(
                                        this@SplashActivity,
                                        Constant.isLoginWithMpin,
                                        false
                                    )
                                }
                                AppPreference.setStringPreference(
                                    this@SplashActivity,
                                    Constant.userLoginUserName,
                                    name
                                )
                                AppPreference.setStringPreference(
                                    this@SplashActivity,
                                    Constant.USER_LOGIN_TOKEN,
                                    userToken
                                )
                                Constant.USER_TOKEN = userToken

                                AppPreference.setBooleanPreference(
                                    this@SplashActivity,
                                    Constant.IS_PROFILE_CREATED,
                                    true
                                )
                                init()

                            }
                            else if (jsonObject.getInt("status") == 4)
                            {
//                                  update
                                Alerts.AlertDialogUpdateApp(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    jsonObject.optString("message"),
                                    jsonObject.optString("appLink"),
                                    jsonObject.optBoolean("forceStatus"),
                                    object : View.OnClickListener {
                                        override fun onClick(v: View?) {
                                            Alerts.getDialog().dismiss()

                                            val name = jsonObject.optString("name")
                                            val mpinGen = jsonObject.optInt("mpinGen")

                                            if (name != null && mpinGen != null) {
                                                if (mpinGen == 0) {
                                                    AppPreference.setBooleanPreference(
                                                        this@SplashActivity,
                                                        Constant.IsMpinGenerate,
                                                        true
                                                    )

                                                    AppPreference.setBooleanPreference(
                                                        this@SplashActivity,
                                                        Constant.isLoginWithMpin,
                                                        true
                                                    )
                                                    if (AppPreference.getBooleanPreference(
                                                            this@SplashActivity,
                                                            Constant.isLoginWithUsername
                                                        )
                                                    ) {
                                                        AppPreference.setBooleanPreference(
                                                            this@SplashActivity,
                                                            Constant.isLoginWithMpin,
                                                            false
                                                        )
                                                    }
                                                } else {
                                                    AppPreference.setBooleanPreference(
                                                        this@SplashActivity,
                                                        Constant.IsMpinGenerate,
                                                        false
                                                    )
                                                    AppPreference.setBooleanPreference(
                                                        this@SplashActivity,
                                                        Constant.isLoginWithMpin,
                                                        false
                                                    )
                                                }

                                                AppPreference.setStringPreference(
                                                    this@SplashActivity,
                                                    Constant.userLoginUserName,
                                                    name
                                                )


                                                AppPreference.setBooleanPreference(
                                                    this@SplashActivity,
                                                    Constant.IS_PROFILE_CREATED,
                                                    true
                                                )
                                                init()
                                            } else {
                                                AppPreference.setBooleanPreference(
                                                    this@SplashActivity,
                                                    Constant.IS_PROFILE_CREATED,
                                                    false
                                                )
                                                init()
                                            }

                                        }
                                    }
                                )
                            }
                            else if (jsonObject.getInt("status") == 0)
                            {
                                AppPreference.setBooleanPreference(
                                    this@SplashActivity,
                                    Constant.IS_PROFILE_CREATED,
                                    false
                                )
                                init()

                            }
                            else
                            {
                                Alerts.AlertDialogFailAutoClose(
                                    this@SplashActivity,
                                    this@SplashActivity,
                                    "Application Is Under Maintainance\nPlease Comeback After Sometime\nThank You :)"
                                )
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onResponseFailed(error: String) {
                        Alerts.serverError(this@SplashActivity, error.toString())
                    }
                })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 108) {
            if (resultCode == Activity.RESULT_OK) {
                createRequestBody()
            }

        }
    }

    private fun dialogPermission() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivityForResult(Intent(this, PermissionA::class.java), 108)
        },2000)
    }


    // this will hide the bottom mobile navigation controll
    fun hideNavigation() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
    }


}
