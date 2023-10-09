package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.modal.dashboard_gamelist.Result
import com.kasa77.modal.kuber_dashboard_games.OpenCloseTime
import com.kasa77.ui.fragment.dashboard_games_fragment.*
import com.kasa77.ui.fragment.dashboard_games_fragment.jodi_games.JodiDigitFragmentDashBoard
import com.kasa77.ui.fragment.dashboard_games_fragment.jodi_games.*
import com.kasa77.utils.AppPreference
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.cart
import kotlinx.android.synthetic.main.toolbar.notificationCount
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import kotlinx.android.synthetic.main.toolbar.view.cart
import kotlinx.android.synthetic.main.toolbar.view.notificationCount
import kotlin.collections.ArrayList

class MainGameFromListActivity : BaseActivity() {
    private var providerResultData: Result? = null
    private var from = ""
    var gameProvider: com.kasa77.modal.kuber_dashboard_games.Provider? = null
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
            toolbarTitle.text = providerResultData!!.providerName + " - " + from + " Board"
        }

        notificationCount.visibility = View.GONE
        cart.setImageResource(R.drawable.wallet_icon)


        cart.setOnClickListener{
            startActivity(Intent(mContext, FundActivity_new::class.java))
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
        val walletBal = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
        tvWalletCount.text = walletBal.toString()
        initFragment(from)

    }

    private fun initFragment(from: String?) {
        when (from) {
            "Single Digit" -> openFragment(SingleDigitFragmentDashBoard()) //Done
            "Single Pana" -> openFragment(SinglePanaFragmentDashBoard()) //Done
            "Double Pana" -> openFragment(DoublePannaFragmentDashBoard()) //Done
            "Triple Pana" -> openFragment(TriplePannaFragmentDashBoard()) //Done
            "SP Motor" -> openFragment(SPMotorFragmentDashBoard()) //Done
            "DP Motor" -> openFragment(DPMotorFragmentDashBoard()) //Done
            "SP DP TP" -> openFragment(SpDpTpFragmentDashBoard())
            "Two Digit Panel" -> {
                openFragment(TwoDigitPanelFragmentDashBoard()) //Done
            }
            "Odd Even" -> {
                openFragment(OddEvenFragmentDashBoard()) //Done
            }
            "Jodi Digits" -> {
                openFragment(JodiDigitFragmentDashBoard()) //Done
            }
            "Half Sangam Digits" -> {
                openFragment(HalfSangamDigitsFragmentDashBoard()) //Done
            }
            "Full Sangam Digits" -> {
                openFragment(FullSangamDigitsFragmentDashBoard()) //Done
            }
            "Red Bracket" -> {
                openFragment(RedBracketFragmentDashBoard()) //Done
            }
            "Group Jodi" -> {
                openFragment(GroupJodiFragmentDashBoard()) //Done
            }
            "Digit Based Jodi" -> {
                openFragment(DigitBasedJodiFragmentDashBoard()) //Done
            }
            "Panel Group" -> {
                openFragment(PanelGroupFragmentDashBoard()) //Done
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
