package com.kasa777.ui.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.kasa777.R
import com.kasa777.constant.Constant
import com.kasa777.ui.fragment.AccountStatementFragment
import com.kasa777.utils.BaseActivity
import com.kasa777.utils.FragmentUtils

class AccountStatementActivity : BaseActivity() {
    private var fragmentUtils: FragmentUtils?=null
    private var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_statement)


        fragmentManager = supportFragmentManager
        fragmentUtils = FragmentUtils(fragmentManager)
        fragmentUtils?.replaceFragment(
            AccountStatementFragment(),
            Constant.AccountStatementFragment,
            R.id.home_frame
        )
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }
}