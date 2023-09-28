package com.kasa777.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.modal.dashboard_gamelist.Result
import com.kasa777.modal.kuber_dashboard_games.OpenCloseTime
import com.kasa777.ui.fragment.dashboard_games_fragment.*
import com.kasa777.ui.fragment.dashboard_games_fragment.jodi_games.JodiDigitFragmentDashBoard
import com.kasa777.ui.fragment.dashboard_games_fragment.jodi_games.*
import com.kasa777.utils.AppPreference
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game.*
import kotlin.collections.ArrayList

class MainGameFromListActivity : BaseActivity() {
    private var providerResultData: Result? = null
    private var from = ""
    var gameProvider: com.kasa777.modal.kuber_dashboard_games.Provider? = null
    private var timeList: ArrayList<OpenCloseTime>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_game)

        tvMenuTitle.isSelected = true
        val intent: Intent = intent
        if (getIntent() != null) {

            providerResultData = intent.getParcelableExtra("PROVIDER")
            from = intent.extras!!.getString("FROM").toString()
            tvMenuTitle.text = providerResultData!!.providerName + " - " + from + " Board"
        }
        val backBtn: ImageView = findViewById(R.id.backbtn)

        backBtn.setOnClickListener {
            onBackPressed()
        }
        val walletBal = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
        tvWalletCount.text = walletBal.toString()
        initFragment(from)

    }

    private fun initFragment(from: String?) {
        when (from) {
            "Single Digit" -> openFragment(SingleDigitFragmentDashBoard())
            "Single Pana" -> openFragment(SinglePanaFragmentDashBoard())
            "Double Pana" -> openFragment(DoublePannaFragmentDashBoard())
            "Triple Pana" -> openFragment(TriplePannaFragmentDashBoard())
            "SP Motor" -> openFragment(SPMotorFragmentDashBoard())
            "DP Motor" -> openFragment(DPMotorFragmentDashBoard())
            "SP DP TP" -> openFragment(SpDpTpFragmentDashBoard())
            "Two Digit Panel" -> {
                openFragment(TwoDigitPanelFragmentDashBoard())
            }
            "Odd Even" -> {
                openFragment(OddEvenFragmentDashBoard())
            }
            "Jodi Digits" -> {
                openFragment(JodiDigitFragmentDashBoard())
            }
            "Half Sangam Digits" -> {
                openFragment(HalfSangamDigitsFragmentDashBoard())
            }
            "Full Sangam Digits" -> {
                openFragment(FullSangamDigitsFragmentDashBoard())
            }
            "Red Bracket" -> {
                openFragment(RedBracketFragmentDashBoard())
            }
            "Group Jodi" -> {
                openFragment(GroupJodiFragmentDashBoard())
            }
            "Digit Based Jodi" -> {
                openFragment(DigitBasedJodiFragmentDashBoard())
            }
            "Panel Group" -> {
                openFragment(PanelGroupFragmentDashBoard())
            }
            "Choice Panna SP DP" -> {
                openFragment(ChoicePannaSpDpFragmentDashBoard())
            }
        }
    }


    private fun openFragment(frag: Fragment) {
        val bundle = Bundle()
        bundle.putString("From", "KuberDashboard")
        bundle.putParcelable("PROVIDER", providerResultData)
        frag.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout_id, frag).commit()
    }

//    fun onBackClick(view: View) {
//        onBackPressed()
//    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
