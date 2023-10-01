package com.kasa77.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa77.R
import com.kasa77.adapter.game_rates_adapter.JackpotGameAdapter
import com.kasa77.adapter.game_rates_adapter.MorningGameRateAdapter
import com.kasa77.adapter.game_rates_adapter.StarGameRateAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.game_rates.ABgameRate
import com.kasa77.modal.game_rates.GameRate
import com.kasa77.modal.game_rates.GameRatesModal
import com.kasa77.modal.game_rates.StarGameRate
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.ConnectionDetector
import com.kasa77.utils.LogoutUser

import kotlinx.android.synthetic.main.fragment_game_rates.*


class GameRatesFragment : Fragment() {
    private var mContext : Context? = null
    private var cd : ConnectionDetector? = null
    private var retrofitApiClientAuth : RetrofitApiClient? = null
    private var rootView: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_game_rates,container,false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClientAuth = AuthHeaderRetrofitService.getRetrofit()
        tvMessage.visibility = View.GONE

        fetchGameRatesApi()
    }
    private fun fetchGameRatesApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.gameRatesData(
                Dialog(requireContext()),
                retrofitApiClientAuth!!.gameRates(),
                object : WebResponse {
                    override fun onResponseSuccess(result: retrofit2.Response<*>?) {
                        val mainModal: GameRatesModal =
                            result!!.body() as GameRatesModal
                        if (mainModal.status == 1) {
                            if (mainModal.data != null) {
                                if (mainModal.data.gameRates != null) {
                                gameRates(mainModal)
                                }
                                if (mainModal.data.starGameRates != null) {
                                starGameRates(mainModal)
                                }
                                if (mainModal.data.aBgameRates != null) {
                                jackpotGameRates(mainModal)
                                }
                            }else{
                                tvMessage.visibility = View.VISIBLE
                                tvMessage.text = "No data found"
                            }
                        } else {
                            logOutUser()
                            tvMessage.visibility = View.VISIBLE
                            tvMessage.text = mainModal.message.toString()                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = error.toString()
                        Alerts.serverError(context, error.toString())}

                })
        }

    }
    private fun  gameRates(mainModal: GameRatesModal) {
//        val resId = R.anim.layout_animation_left_to_right

        val gameRate: ArrayList<GameRate> =
            mainModal.data.gameRates as ArrayList<GameRate>
//        rvGameRates.setHasFixedSize(true)
        rvGameRates.layoutManager =
            LinearLayoutManager(mContext)
        val transactionAdapter = MorningGameRateAdapter(mContext, gameRate)
        rvGameRates.adapter = transactionAdapter
        transactionAdapter.notifyDataSetChanged()
    }
    private fun starGameRates(mainModal: GameRatesModal) {
//        val resId = R.anim.loyout_animation_right_to_left

        val gameRate: ArrayList<StarGameRate> =
            mainModal.data.starGameRates as ArrayList<StarGameRate>
        rvStarGameRates.setHasFixedSize(true)
        rvStarGameRates.layoutManager =
            LinearLayoutManager(mContext)
        val transactionAdapter = StarGameRateAdapter(mContext, gameRate)
        rvStarGameRates.adapter = transactionAdapter
        transactionAdapter.notifyDataSetChanged()
    }
    private fun jackpotGameRates(mainModal: GameRatesModal) {
//        val resId = R.anim.layout_animation_left_to_right

        val gameRate: ArrayList<ABgameRate> =
            mainModal.data.aBgameRates as ArrayList<ABgameRate>
        rvJackPotRates.setHasFixedSize(true)
        rvJackPotRates.layoutManager =
            LinearLayoutManager(mContext)
        val transactionAdapter = JackpotGameAdapter(mContext, gameRate)
        rvJackPotRates.adapter = transactionAdapter
        transactionAdapter.notifyDataSetChanged()
    }

    private fun logOutUser():Boolean {
        var isAvailable = true
        val loginTime = AppPreference.getLongPreference(mContext, Constant.USER_LOGIN_TIME)
        val currentTime = System.currentTimeMillis()
        val difference = currentTime-loginTime
        isAvailable = false
        val logoutUser = LogoutUser(requireContext(), requireActivity())
        logoutUser.logout()

        return isAvailable
    }


}