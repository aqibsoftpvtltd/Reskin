package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.jackpot_fragments.DigitBasedJodiFragment_Jackpot
import com.kasa77.ui.fragment.jackpot_fragments.GroupJodiFragment_Jackpot
import com.kasa77.ui.fragment.jackpot_fragments.JodiJackpotFragment_Jackpot
import com.kasa77.ui.fragment.jackpot_fragments.RedBracketFragment_Jackpot
import com.kasa77.ui.fragment.jackpot_fragments.modal.Result
import com.kasa77.utils.AppPreference
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.cart
import kotlinx.android.synthetic.main.toolbar.notificationCount
import kotlinx.android.synthetic.main.toolbar.toolbarTitle

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class JackpotGameActivity : BaseActivity() {
    private var gameProvider: Result? = null
    private var gParent = ""
    private var from = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_game)
        tvMenuTitle.isSelected = true

        from = intent.extras!!.getString("FROM").toString()
        gameProvider = intent.getParcelableExtra("PROVIDER")

        tvMenuTitle.text = "Jackpot ${gameProvider!!.providerName} - ($from)"
        toolbarTitle.text ="Jackpot ${gameProvider!!.providerName} - ($from)"

        val walletBal = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
        tvWalletCount.text = walletBal.toString()

        initFragment(from)

        notificationCount.visibility = View.GONE
        cart.setBackgroundResource(R.drawable.wallet_icon)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initFragment(from: String?) {
        when (from) {
            GameTypeNames.GroupJackpot -> openFragment(GroupJodiFragment_Jackpot())
            GameTypeNames.DigitBasedJackpot -> openFragment(DigitBasedJodiFragment_Jackpot())
            GameTypeNames.RedBracketJackpot -> openFragment(RedBracketFragment_Jackpot())
            GameTypeNames.JodiJackpot -> openFragment(JodiJackpotFragment_Jackpot())

        }
    }


    private fun openFragment(frag: Fragment) {
        val bundle = Bundle()
        bundle.putString("From", from)
        bundle.putParcelable("PROVIDER", gameProvider)
        frag.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout_id, frag).commit()
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        finish()
    }
}
