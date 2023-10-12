package com.kasa777.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kasa777.R
import kotlinx.android.synthetic.main.fragment_funds.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*


class FundActivity_new : AppCompatActivity(), View.OnClickListener {

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fund_activity_new)

        mContext = this
        init()

        val toolbar = findViewById<View>(R.id.toobarsridevi)

        toolbar.cart.setOnClickListener{
            startActivity(Intent(mContext, NotifcationActivity::class.java))
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        cvAddFund.setOnClickListener(this)
        cvWithdrawFund.setOnClickListener(this)
        cvFundRequestHistory.setOnClickListener(this)
        cvFundRequestHistory.setOnClickListener(this)
        card_bank.setOnClickListener(this)
        card_accountstatements.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cvAddFund -> {
                startActivity(
                    Intent(mContext, FundsActivity::class.java)
                    .putExtra("from", "addFund"))
            }
            R.id.cvWithdrawFund -> {
                startActivity(Intent(mContext, FundsActivity::class.java)
                    .putExtra("from", "withdrawFund"))
            }
            R.id.cvFundRequestHistory -> {
                startActivity(Intent(mContext, FundsActivity::class.java)
                    .putExtra("from", "requestHistory"))
            }

            R.id.card_bank -> {
                startActivity(Intent(mContext, BankDetailActivity::class.java))
            }

            R.id.card_accountstatements -> {
                startActivity(Intent(mContext, AccountStatementActivity::class.java))
            }
        }
    }
}
