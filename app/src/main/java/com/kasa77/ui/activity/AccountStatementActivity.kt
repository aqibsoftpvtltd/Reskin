package com.kasa77.ui.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.ui.fragment.AccountStatementFragment
import com.kasa77.utils.BaseActivity
import com.kasa77.utils.FragmentUtils
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.toolbarTitle

class AccountStatementActivity : BaseActivity() {
    private var fragmentUtils: FragmentUtils?=null
    private var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_statement)

        toolbarTitle.text="Account Statements"
        backBtn.setOnClickListener { onBackClick() }

        fragmentManager = supportFragmentManager
        fragmentUtils = FragmentUtils(fragmentManager)
        fragmentUtils?.replaceFragment(AccountStatementFragment(), Constant.AccountStatementFragment, R.id.home_frame)
    }

    fun onBackClick() {
        onBackPressed()
    }
}