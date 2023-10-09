package com.kasa77.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.kasa77.R
import com.kasa77.adapter.GameDashboardAdapter
import com.kasa77.constant.Constant
import com.kasa77.interfaces.AdapterClickListener
import com.kasa77.modal.GameDashboardModel
import com.kasa77.modal.dashboard_gamelist.Result
import com.kasa77.ui.fragment.FundsFragment
import com.kasa77.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game_list.*
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import kotlinx.android.synthetic.main.toolbar.view.*


class GameListFromDashboardActivity : BaseActivity() {


    private var providerResultData: Result? = null
    private lateinit var gameDashboardAdapter:GameDashboardAdapter
    private val gameList=ArrayList<GameDashboardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_game_list)

        val intent: Intent = intent
        if (getIntent() != null) {

            providerResultData = intent.getParcelableExtra("PROVIDER")
            toolbarTitle.text = providerResultData!!.providerName + " DASHBOARD"

        }
        setupAdapter()

        val toolbar = findViewById<View>(R.id.toobarsridevi)



        toolbar.notificationCount.visibility = View.GONE

        toolbar.cart.setImageResource(R.drawable.wallet_icon)


        toolbar.cart.setOnClickListener{
            startActivity(Intent(mContext, FundActivity_new::class.java))
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAdapter() {
        genrateList()
        val gridLayoutManager=GridLayoutManager(mContext,2)
        gridLayoutManager.orientation=GridLayoutManager.VERTICAL
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
        gameList.add(GameDashboardModel("Single Digit",R.drawable.single_digit_back/*,R.drawable.ic_single_dice*/))
        gameList.add(GameDashboardModel("Jodi Digits",R.drawable.jodi_digit_back/*,R.drawable.ic_double_dice*/))
        gameList.add(GameDashboardModel("Single Pana",R.drawable.single_pana_back/*,R.drawable.ic_single_pana*/))
        gameList.add(GameDashboardModel("Double Pana",R.drawable.double_pana_back/*,R.drawable.ic_double_pana*/))
        gameList.add(GameDashboardModel("SP Motor",R.drawable.sp_motor_back/*,R.drawable.ic_sp_motor*/))
        gameList.add(GameDashboardModel("DP Motor",R.drawable.dp_motor_back/*,R.drawable.ic_dp_motor*/))
        gameList.add(GameDashboardModel("Group Jodi",R.drawable.group_jodi/*,R.drawable.ic_group_jodi*/))
        gameList.add(GameDashboardModel("SP DP TP",R.drawable.sp_dp_back/*,R.drawable.ic_sp_dp_tp*/))
        gameList.add(GameDashboardModel("Odd Even",R.drawable.odd_even_bck/*,R.drawable.ic_odd_even*/))
        gameList.add(GameDashboardModel("Red Bracket",R.drawable.red_bracket_back/*,R.drawable.ic_red_bracket*/))
//        gameList.add(GameDashboardModel("Digit Based Jodi",R.drawable.ractengle_stroke_round_edge_dicegreen,R.drawable.ic_double_dice))
//        gameList.add(GameDashboardModel("Choice Panna SP DP",R.drawable.ractengle_stroke_round_edge_choicepana,R.drawable.newchoicepana))
//        gameList.add(GameDashboardModel("Panel Group",R.drawable.ractengle_stroke_round_edge_spdptpblack,R.drawable.new_panel_group))
//        gameList.add(GameDashboardModel("Two Digit Panel",R.drawable.ractengle_stroke_round_edge_spblue,R.drawable.ic_pannel))
        gameList.add(GameDashboardModel("Triple Pana",R.drawable.triple_pana_back/*,R.drawable.ic_triple*/))
        gameList.add(GameDashboardModel("Half Sangam Digits",R.drawable.half_sangam_digit/*,R.drawable.newhalf_sangam*/))
        gameList.add(GameDashboardModel("Full Sangam Digits",R.drawable.full_sangam_digit_back/*,R.drawable.newfull_sangam*/))
    }

    private fun openActivity(from: String?) {
        startActivity(
            Intent(this, MainGameFromListActivity::class.java)
                .putExtra("PROVIDER", providerResultData)
                .putExtra("FROM", from)
        )
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
