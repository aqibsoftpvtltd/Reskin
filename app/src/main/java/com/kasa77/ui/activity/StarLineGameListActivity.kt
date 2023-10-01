package com.kasa77.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.kasa77.R
import com.kasa77.adapter.GameDashboardAdapter
import com.kasa77.interfaces.AdapterClickListener
import com.kasa77.modal.GameDashboardModel
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.startline_game_fragment.modal.Result
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game_list.rvGameDashboard
import kotlinx.android.synthetic.main.activity_starline_game_list.*

class StarLineGameListActivity : BaseActivity() {

    var gameProvider : Result? = null
    private lateinit var gameDashboardAdapter: GameDashboardAdapter
    private val gameList=ArrayList<GameDashboardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starline_game_list)

        gameProvider = intent.getParcelableExtra("PROVIDER")
        tv_dashboard.text = "Starline Dashboard - "+gameProvider!!.providerName

        setupAdapter()
        val backBtn: ImageView = findViewById(R.id.backbtn)

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
        gameList.add(GameDashboardModel(GameTypeNames.SingleDigit,R.drawable.ractengle_stroke_round_edge_diceblue,R.drawable.ic_single_dice))
        gameList.add(GameDashboardModel(GameTypeNames.SinglePana,R.drawable.ractengle_stroke_round_edge_panagreen,R.drawable.ic_single_pana))
        gameList.add(GameDashboardModel(GameTypeNames.SP_DP_TP,R.drawable.ractengle_stroke_round_edge_spdptpblack,R.drawable.ic_sp_dp_tp))
        gameList.add(GameDashboardModel(GameTypeNames.SPMOTOR,R.drawable.ractengle_stroke_round_edge_spblue,R.drawable.ic_sp_motor))
        gameList.add(GameDashboardModel(GameTypeNames.DoublePana,R.drawable.ractengle_stroke_round_edge_panayellow,R.drawable.ic_double_pana))
        gameList.add(GameDashboardModel(GameTypeNames.TriplePana,R.drawable.ractengle_stroke_round_edge_triplepurple,R.drawable.ic_triple))
        gameList.add(GameDashboardModel(GameTypeNames.DPMOTOR,R.drawable.ractengle_stroke_round_edge_dpred,R.drawable.ic_dp_motor))
        gameList.add(GameDashboardModel(GameTypeNames.TWO_DIGIT_PANEL,R.drawable.ractengle_stroke_round_edge_spblue,R.drawable.ic_pannel))

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