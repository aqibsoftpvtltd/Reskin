package com.kasa777.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kasa777.R
import com.kasa777.ui.activity.BidsHistoryActivity
import kotlinx.android.synthetic.main.fragment_history.*

class MyHistoryFragment : Fragment(), View.OnClickListener {
    private lateinit var mContext: Context
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_history, container, false)
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        cvStarlineHistory.setOnClickListener(this)
        cvStarlineBidHistory.setOnClickListener(this)
        cvJackpotHistory.setOnClickListener(this)
        cvMorningDashboardHistory.setOnClickListener(this)
        cvAndarBaharHistory.setOnClickListener(this)
        cvFundRequestHistoryH.setOnClickListener(this)
        cvApprovedCreditHistory.setOnClickListener(this)
        cvDebitHistory.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cvStarlineHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "starlineHistory")
                )
            }
            R.id.cvStarlineBidHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "starlineBidHistory")
                )
            }
            R.id.cvJackpotHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "jackpotHistory")
                )
            }
            R.id.cvMorningDashboardHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "morningDashboardHistory")
                )
            }
            R.id.cvFundRequestHistoryH -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "fundRequestHistory")
                )
            }
            R.id.cvAndarBaharHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "andarBaharHistory")
                )
            }
            R.id.cvApprovedCreditHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "creditHistory")
                )
            }
            R.id.cvDebitHistory -> {
                startActivity(
                    Intent(mContext, BidsHistoryActivity::class.java)
                        .putExtra("from", "debitHistory")
                )
            }
        }
    }

}