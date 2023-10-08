package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasa77.R
import com.kasa77.adapter.StarLineTimeGameAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.kuber_starline.game_type.GameType
import com.kasa77.modal.kuber_starline.game_type.KsGameTypeModel
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.fragment.startline_game_fragment.modal.StartLineTimeGames
import com.kasa77.utils.*
import kotlinx.android.synthetic.main.activity_starline_dashboard.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class StarlineDashboardActivity : Fragment() {
    private var gameTypeArray: ArrayList<GameType> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_starline_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize your views and set click listeners here
        loadGame()
        fetchStarlineGameId()

        btnStarNotification.isChecked =
            AppPreference.getBooleanPreference(requireContext(), Constant.starLineNotification)

        btnStarNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notificationOnOffApi(3, true)
            } else {
                notificationOnOffApi(3, false)
            }
        }
    }



    private fun loadGame() {
        val baseActivity = requireActivity() as BaseActivity
        if (baseActivity.cd != null && baseActivity.cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireContext()),
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
                            Helper(requireContext()).getSortedListStarline(list)

//                            val resId = R.anim.layout_animation_left_to_right
//                            val animation: LayoutAnimationController =
//                                android.view.animation.AnimationUtils.loadLayoutAnimation(
//                                    mContext,
//                                    resId
//                                )
//                            rvKsGameList.layoutAnimation = animation

                            rvKsGameList!!.setHasFixedSize(true)
                            rvKsGameList!!.layoutManager = LinearLayoutManager(requireContext())
                            val gameAdapter =
                                StarLineTimeGameAdapter(list)
                            rvKsGameList.layoutManager = LinearLayoutManager(requireContext())
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
        val baseActivity = requireActivity() as BaseActivity
        if (baseActivity.cd != null && baseActivity.cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getKsGameTypeId(
                Dialog(requireContext()),
                baseActivity.retrofitApiClientAuth!!.kuberStarlineGameTypeId(),
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
                        Alerts.serverError(requireContext(), error.toString())
                    }
                })
        }
    }

    private fun notificationOnOffApi(notificationId: Int, notificationOnOff: Boolean) {
        val baseActivity = requireActivity() as BaseActivity
        if (baseActivity.cd != null && baseActivity.cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(requireContext(), Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            mObject.put("notificationId", notificationId)
            mObject.put("notificationOnOff", notificationOnOff)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireContext()),
                baseActivity.retrofitApiClientAuth!!.appNotificationOnOff(body),
                object : WebResponse {
                    override fun onResponseSuccess(result: retrofit2.Response<*>?) {
                        val mainModal: ResponseBody = result!!.body() as ResponseBody
                        try {
                            val jsonObject = JSONObject(mainModal.string())
                            if (jsonObject.getInt("status") == 1) {
                                AppPreference.setBooleanPreference(
                                    requireContext(),
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
        val dialogBuilder = requireContext().let { AlertDialog.Builder(it) }
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
        requireActivity().onBackPressed()
    }
}
