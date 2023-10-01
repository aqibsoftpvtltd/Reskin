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
import com.kasa77.ui.fragment.jackpot_fragments.modal.Result
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game_list.rvGameDashboard
import kotlinx.android.synthetic.main.activity_jackpot_game_list.tv_dashboard

class JackpotGameListActivity : BaseActivity() {

    var gameProvider: Result? = null
    private lateinit var gameDashboardAdapter: GameDashboardAdapter
    private val gameList=ArrayList<GameDashboardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jackpot_game_list)

        gameProvider = intent.getParcelableExtra("PROVIDER")
        tv_dashboard.text = "Jackpot Dashboard - "+gameProvider!!.providerName
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
        gameList.add(GameDashboardModel(GameTypeNames.JodiJackpot,R.drawable.ractengle_stroke_round_edge_dicegreen,R.drawable.ic_double_dice))
        gameList.add(GameDashboardModel(GameTypeNames.RedBracketJackpot,R.drawable.ractengle_stroke_round_edge_groupred,R.drawable.ic_red_bracket))
        gameList.add(GameDashboardModel(GameTypeNames.DigitBasedJackpot,R.drawable.ractengle_stroke_round_edge_panayellow,R.drawable.ic_odd_even))
        gameList.add(GameDashboardModel(GameTypeNames.GroupJackpot,R.drawable.ractengle_stroke_round_edge_groupred,R.drawable.ic_group_jodi))
    }

    private fun openActivity(from: String?) {
        startActivity(
            Intent(this, JackpotGameActivity::class.java)
                .putExtra("FROM", from)
                .putExtra("PROVIDER", gameProvider)
        )
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
