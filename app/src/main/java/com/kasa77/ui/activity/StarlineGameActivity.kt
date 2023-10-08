package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.startline_game_fragment.*
import com.kasa77.ui.fragment.startline_game_fragment.modal.Result
import com.kasa77.utils.AppPreference
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.cart
import kotlinx.android.synthetic.main.toolbar.notificationCount
import kotlinx.android.synthetic.main.toolbar.toolbarTitle

class StarlineGameActivity : BaseActivity() {

    private var from: String? = null
    private var gameProvider: Result? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_game)
        tvMenuTitle.isSelected = true


        gameProvider = intent.getParcelableExtra("PROVIDER")
        from = intent.getStringExtra("FROM")

        tvMenuTitle.text = "Starline ${gameProvider!!.providerName} - ($from)"
        toolbarTitle.text = "Starline ${gameProvider!!.providerName} - ($from)"

        val walletBal = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)

        tvWalletCount.text = walletBal.toString()
        initFragment(from)
       // val backBtn: ImageView = findViewById(R.id.backbtn)

        notificationCount.visibility = View.GONE
        cart.setBackgroundResource(R.drawable.wallet_icon)
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initFragment(from: String?) {
        when (from) {
            GameTypeNames.SingleDigit -> openFragment(SingleDigitFragment_Starline())
            GameTypeNames.SinglePana -> openFragment(SinglePanaFragment_Starline())
            GameTypeNames.DoublePana -> openFragment(DoublePannaFragment_Starline())
            GameTypeNames.TriplePana -> openFragment(TriplePannaFragment_Starline())
            GameTypeNames.SPMOTOR -> openFragment(SPMotorFragment_Starline())
            GameTypeNames.DPMOTOR -> openFragment(DPMotorFragment_Starline())
            GameTypeNames.SP_DP_TP -> openFragment(SpDpTpFragment_Starline())
            GameTypeNames.TWO_DIGIT_PANEL -> openFragment(TwoDigitPanelFragment_Starline())

        }
    }

    private fun openFragment(frag: Fragment) {
        val bundle = Bundle()
        bundle.putString("FROM", from)
        bundle.putParcelable("PROVIDER", gameProvider)
        frag.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout_id, frag).commit()
    }

    fun onBackClick(view: View) {

    }


}
