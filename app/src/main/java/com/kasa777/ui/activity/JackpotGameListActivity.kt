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
import com.kasa777.ui.fragment.jackpot_fragments.modal.Result
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_game_list.rvGameDashboard
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.cart
import kotlinx.android.synthetic.main.toolbar.notificationCount
import kotlinx.android.synthetic.main.toolbar.toolbarTitle
import kotlinx.android.synthetic.main.toolbar.view.*

class JackpotGameListActivity : BaseActivity() {

    var gameProvider: Result? = null
    private lateinit var gameDashboardAdapter: GameDashboardAdapter
    private val gameList=ArrayList<GameDashboardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jackpot_game_list)

        gameProvider = intent.getParcelableExtra("PROVIDER")
        toolbarTitle.text = "Jackpot Dashboard - "+gameProvider!!.providerName

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
        gameList.add(GameDashboardModel(GameTypeNames.JodiJackpot,R.drawable.jodi_jackpot_back/*,R.drawable.ic_double_dice*/))
        gameList.add(GameDashboardModel(GameTypeNames.RedBracketJackpot,R.drawable.red_bracket_jackport_back/*,R.drawable.ic_red_bracket*/))
        gameList.add(GameDashboardModel(GameTypeNames.DigitBasedJackpot,R.drawable.digit_based_jodi_jackpot_back/*,R.drawable.ic_odd_even*/))
        gameList.add(GameDashboardModel(GameTypeNames.GroupJackpot,R.drawable.group_jodi_jackpot_back/*,R.drawable.ic_group_jodi*/))
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
