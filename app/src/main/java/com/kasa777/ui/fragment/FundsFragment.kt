package com.kasa777.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
/*import com.kasa777.AddFundNewActivity*/
import com.kasa777.R
import com.kasa777.ui.activity.AccountStatementActivity
import com.kasa777.ui.activity.BankDetailActivity
import com.kasa777.ui.activity.FundsActivity
import kotlinx.android.synthetic.main.fragment_funds.*


class FundsFragment : Fragment(),View.OnClickListener {



    private var rootView: View? = null
    private var mContext:Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_funds,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = context
        init()
    }
    private fun init(){
        cvAddFund.setOnClickListener(this)
        cvWithdrawFund.setOnClickListener(this)
        cvFundRequestHistory.setOnClickListener(this)
        cvFundRequestHistory.setOnClickListener(this)
        card_bank.setOnClickListener(this)
        card_accountstatements.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
/*
            R.id.cvAddFund->{
                startActivity(Intent(mContext,AddFundNewActivity::class.java))
//                startActivity(Intent(mContext,FundsActivity::class.java)
//                    .putExtra("from","addFund"))
            }
*/
            R.id.cvWithdrawFund->{
                startActivity(Intent(mContext,FundsActivity::class.java)
                    .putExtra("from","withdrawFund"))
            }
            R.id.cvFundRequestHistory->{
                startActivity(Intent(mContext,FundsActivity::class.java)
                    .putExtra("from","requestHistory"))
            }

            R.id.card_bank->{
                startActivity(Intent(mContext,BankDetailActivity::class.java))
//                profileDetailApi("bank")
            }

            R.id.card_accountstatements->{
                startActivity(Intent(mContext,AccountStatementActivity::class.java))

            }


        }
    }







}