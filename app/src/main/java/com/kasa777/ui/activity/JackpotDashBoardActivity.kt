package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasa777.R
import com.kasa777.adapter.jackpot_adapter.JackpotAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.jackpot_modal.gametype_id.GameTypeIdList
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.fragment.jackpot_fragments.modal.JackpotGames
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.activity_jackpot_dashboard.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class JackpotDashBoardActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jackpot_dashboard)






        btnJackNotification.isChecked =
            AppPreference.getBooleanPreference(mContext, Constant.andarBaharNotification)

        btnJackNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(4, true)

            } else {
                notificationOnOffApi(4, false)
            }

        }
        tabViewHistory.setOnClickListener {
            startActivity(
                Intent(this, BidsHistoryActivity::class.java)
                    .putExtra("from", "jackpotHistory")
            )
        }
        val backBtn: ImageView = findViewById(R.id.backbtn)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        loadGame()
        fetchStarlineGameId()
    }

    private fun loadGame() {
        if (cd.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@JackpotDashBoardActivity),
                retrofitApiClientAuth.kuberJackpotGameData(),
                object :
                    WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {

                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        val jackpotGames: JackpotGames = Gson().fromJson(
                            jsonresponse.toString(),
                            object : TypeToken<JackpotGames?>() {}.getType()
                        )
                        if (jackpotGames.status == 1) {
                            val resultarray = jackpotGames.result.values.toTypedArray();
                            val list = Arrays.asList(*resultarray)
                            Helper(mContext).getSortedListJackpot(list)

//                            val resId = R.anim.layout_animation_left_to_right
//                            val animation: LayoutAnimationController =
//                                android.view.animation.AnimationUtils.loadLayoutAnimation(
//                                    mContext,
//                                    resId
//                                )
//                            rvJackpotGameList.layoutAnimation = animation

                            rvJackpotGameList!!.setHasFixedSize(true)
                            rvJackpotGameList!!.layoutManager = GridLayoutManager(mContext,2)
                            val gameAdapter =
                                JackpotAdapter(
                                    list
                                )

                            rvJackpotGameList.adapter = gameAdapter
                            gameAdapter!!.notifyDataSetChanged()

                        }
                    }


                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@JackpotDashBoardActivity, error.toString())

                    }
                })
        }
    }



    private fun logOutUser(): Boolean {
        var isAvailable = true
        val loginTime = AppPreference.getLongPreference(mContext, Constant.USER_LOGIN_TIME)
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - loginTime
        isAvailable = false
        val logoutUser = LogoutUser(this@JackpotDashBoardActivity, this)
        logoutUser.logout()

        return isAvailable
    }

    private fun fetchStarlineGameId() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getJackpotGameTypeId(
                Dialog(this@JackpotDashBoardActivity),
                retrofitApiClientAuth!!.kuberJackpotGameTypeId(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val ksgModel = result!!.body() as GameTypeIdList
                        if (ksgModel.status == 1) {
                            if (ksgModel.gameTypes.size > 0) {

                                var gameTypeArray: ArrayList<com.kasa777.modal.jackpot_modal.gametype_id.GameType> =
                                    ksgModel.gameTypes as ArrayList<com.kasa777.modal.jackpot_modal.gametype_id.GameType>
                                gameTypeArray.forEach {
                                    if (it.gameName == "Jodi") {
                                        var gameTypePrice = it.gamePrice.toString()
                                        jodiDAta.text = "1 - $gameTypePrice"
                                    }
                                }
                            }
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@JackpotDashBoardActivity, error.toString())

                    }
                })
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


    override fun onBackPressed() {
        finish()
    }

    private fun notificationOnOffApi(notificationId: Int, notificationOnOff: Boolean) {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("notificationId", notificationId)
            mObject.put("notificationOnOff", notificationOnOff)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@JackpotDashBoardActivity),
                retrofitApiClientAuth!!.appNotificationOnOff(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: retrofit2.Response<*>?) {
                        val mainModal: ResponseBody = result!!.body() as ResponseBody
                        try {
                            val jsonObject = JSONObject(mainModal.string())
                            if (jsonObject.getInt("status") == 1) {
                                AppPreference.setBooleanPreference(
                                    mContext,
                                    Constant.andarBaharNotification,
                                    notificationOnOff
                                )
                            }
                        } catch (e: Exception) {
                            dialogBoxMessage(e.toString())
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@JackpotDashBoardActivity, error.toString())
                    }

                })
        }
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

}
