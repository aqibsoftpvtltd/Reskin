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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasa777.R
import com.kasa777.adapter.StarLineTimeGameAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.kuber_starline.Provider
import com.kasa777.modal.kuber_starline.game_type.GameType
import com.kasa777.modal.kuber_starline.game_type.KsGameTypeModel
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.fragment.startline_game_fragment.modal.StartLineTimeGames
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.activity_starline_dashboard.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class StarlineDashboardActivity : BaseActivity() {

    private var gameTypeArray: ArrayList<GameType> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext= this
        setContentView(R.layout.activity_starline_dashboard)


        loadGame()
        fetchStarlineGameId()

        btnStarNotification.isChecked =
            AppPreference.getBooleanPreference(mContext, Constant.starLineNotification)
        btnStarNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(3, true)

            } else {
                notificationOnOffApi(3, false)
            }
        }
        tabViewHistory.setOnClickListener {
            startActivity(
                Intent(this, BidsHistoryActivity::class.java)
                    .putExtra("from", "starlineBidHistory")
            )
        }
        val backBtn: ImageView = findViewById(R.id.backbtn)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadGame() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@StarlineDashboardActivity),
                AuthHeaderRetrofitService.getRetrofit()
                !!.getStarLineGameTimeList(),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        val startLineTimeGames: StartLineTimeGames = Gson().fromJson(
                            jsonresponse.toString(),
                            object : TypeToken<StartLineTimeGames?>() {}.getType()
                        )
                        if (startLineTimeGames.status == 1) {
                            val resultarray = startLineTimeGames.result.values.toTypedArray();
                            val list = Arrays.asList(*resultarray)
                            Helper(mContext).getSortedListStarline(list)

//                            val resId = R.anim.layout_animation_left_to_right
//                            val animation: LayoutAnimationController =
//                                android.view.animation.AnimationUtils.loadLayoutAnimation(
//                                    mContext,
//                                    resId
//                                )
//                            rvKsGameList.layoutAnimation = animation

                            rvKsGameList!!.setHasFixedSize(true)
                            rvKsGameList!!.layoutManager = LinearLayoutManager(mContext)
                            val gameAdapter =
                                StarLineTimeGameAdapter(list)
                            rvKsGameList.layoutManager = LinearLayoutManager(mContext)
                            rvKsGameList.adapter = gameAdapter
                            gameAdapter!!.notifyDataSetChanged()
                        }

                    }

                    override fun onResponseFailed(error: String?) {

                    }
                })
        }
    }


    private fun fetchStarlineGameId() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getKsGameTypeId(
                Dialog(this@StarlineDashboardActivity),
                retrofitApiClientAuth!!.kuberStarlineGameTypeId(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val ksgModel = result!!.body() as KsGameTypeModel
                        if (ksgModel.status == 1) {
                            if (ksgModel.gameTypes.size > 0) {
                                gameTypeArray = ksgModel.gameTypes as ArrayList<GameType>
                                tvSingleDigit.text =
                                    "1-" + gameTypeArray[3].gamePrice.toString()
                                tvDoublePana.text =
                                    "1-" + gameTypeArray[1].gamePrice.toString()
                                tvTriplePana.text =
                                    "1-" + gameTypeArray[2].gamePrice.toString()
                                tvSinglePanna.text =
                                    "1-" + gameTypeArray[0].gamePrice.toString()
                            }
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(this@StarlineDashboardActivity, error.toString())
                    }
                })
        }
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
                Dialog(this@StarlineDashboardActivity),
                retrofitApiClientAuth!!.appNotificationOnOff(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: retrofit2.Response<*>?) {
                        val mainModal: ResponseBody = result!!.body() as ResponseBody
                        try {
                            val jsonObject = JSONObject(mainModal.string())
                            if (jsonObject.getInt("status") == 1) {
                                AppPreference.setBooleanPreference(
                                    mContext,
                                    Constant.starLineNotification,
                                    notificationOnOff
                                )
                            }
                        } catch (e: Exception) {
                            dialogBoxMessage(e.toString())
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        dialogBoxMessage(error.toString())
                    }

                })
        }
    }

    private fun dialogBoxMessage(string: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView =
            inflater.inflate(R.layout.dialog_view_toast_message, null)
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


    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
