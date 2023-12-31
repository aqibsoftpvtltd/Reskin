package com.kasa777.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.kasa777.R
import com.kasa777.adapter.GameDashboardAdapter
import com.kasa777.interfaces.AdapterClickListener
import com.kasa777.modal.GameDashboardModel
import com.kasa777.ui.fragment.GameTypeNames
import com.kasa777.ui.fragment.startline_game_fragment.modal.Result
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game_list.rvGameDashboard
import kotlinx.android.synthetic.main.activity_starline_game_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*

class StarLineGameListActivity : BaseActivity() {

    var gameProvider : Result? = null
    private lateinit var gameDashboardAdapter: GameDashboardAdapter
    private val gameList=ArrayList<GameDashboardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starline_game_list)

        gameProvider = intent.getParcelableExtra("PROVIDER")

        toolbarTitle.text = "Starline Dashboard - "+gameProvider!!.providerName

        setupAdapter()




        notificationCount.visibility = View.GONE

        cart.setImageResource(R.drawable.wallet_icon)
        cart.setOnClickListener{
            startActivity(Intent(mContext, FundActivity_new::class.java))
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAdapter() {
        genrateList()
        val gridLayoutManager= GridLayoutManager(mContext,2)
        gridLayoutManager.orientation= GridLayoutManager.VERTICAL
        rvGameDashboard.layoutManager=gridLayoutManager
        gameDashboardAdapter= GameDashboardAdapter(gameList, AdapterClickListener { position, view ->
            openActivity(gameList.get(position).name)
        })
        rvGameDashboard.adapter=gameDashboardAdapter

//        val resId = R.anim.layout_animation_left_to_right
//        val animation: LayoutAnimationController =
//            android.view.animation.AnimationUtils.loadLayoutAnimation(
//                mContext,
//                resId
//            )
//        rvGameDashboard.layoutAnimation = animation
    }

    private fun genrateList() {
        gameList.clear()
        gameList.add(GameDashboardModel(GameTypeNames.SingleDigit,R.drawable.single_digit_back))
        gameList.add(GameDashboardModel(GameTypeNames.SinglePana,R.drawable.single_pana_back))
        gameList.add(GameDashboardModel(GameTypeNames.SP_DP_TP,R.drawable.sp_dp_back))
        gameList.add(GameDashboardModel(GameTypeNames.SPMOTOR,R.drawable.sp_motor_back))
        gameList.add(GameDashboardModel(GameTypeNames.DoublePana,R.drawable.double_pana_back))
        gameList.add(GameDashboardModel(GameTypeNames.TriplePana,R.drawable.triple_pana_back))
        gameList.add(GameDashboardModel(GameTypeNames.DPMOTOR,R.drawable.dp_motor_back))
        gameList.add(GameDashboardModel(GameTypeNames.TWO_DIGIT_PANEL,R.drawable.two_digit_back))

    }


    private fun openActivity(from: String?) {
        startActivity(Intent(this, StarlineGameActivity::class.java)
            .putExtra("PROVIDER", gameProvider)
            .putExtra("FROM", from))
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
