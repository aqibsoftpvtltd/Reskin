package com.kasa777.ui


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager

import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.kasa777.R
import com.kasa777.adapter.NavigationItemAdapter
import com.kasa777.chat.*
import com.kasa777.chat.model.GetOldMessages
import com.kasa777.chat.model.MessageModal
import com.kasa777.chat.model.OnReceiveNewMessage
import com.kasa777.chat.ui.ChatBoardActivity
import com.kasa777.chat.ui.ChatBoardActivity_old
import com.kasa777.constant.Constant
import com.kasa777.interfaces.AdapterClickListener
import com.kasa777.modal.NavigationItemModal
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.activity.*
import com.kasa777.ui.fragment.*
import com.kasa777.utils.*
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat_board.*
import kotlinx.android.synthetic.main.activity_navigation_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.layout_content_home.*
import kotlinx.android.synthetic.main.layout_content_home.ivProfile
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.navBtn
import kotlinx.android.synthetic.main.toolbar.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
public class NavigationMainActivity : BaseActivity(), View.OnClickListener,
    HomePageFragment.UpdateWalletAmountListener {
    private var welcome_message: String? = null
    private var mSocket: Socket? = null
    private var onSocketListener: OnSocketListener? = null
    private var navigationItemAdapter: NavigationItemAdapter? = null
    private val navModalList = ArrayList<NavigationItemModal>()
    private var fragmentUtils: FragmentUtils? = null
    private var doubleBackToExitPressedOnce = false
    private val imageList = ArrayList<SlideModel>() // Create image list
    private var dashboardstring: String = "orange"
    private var currentIndex: Int = 0
    private var previousIndex: Int = 0
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val type = intent!!.getStringExtra("TYPE")
            if (type.equals("Wallet")) {
                userWalletCheck()
            }
            if (type.equals("Notification")) {
                tv_notification_counter.visibility = View.VISIBLE
                Constant.NotificationCounter = Constant.NotificationCounter + 1
                tv_notification_counter.text = Constant.NotificationCounter.toString()
            }
            if (type.equals("broadcast")) {
                getuserOldMessageCounterOnly()

            }


        }
    }

    override fun onResume() {
        super.onResume()
        fragmentUtils = FragmentUtils(supportFragmentManager)
        manageChatCounter()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            (mMessageReceiver),
            IntentFilter("Notification")
        );

        userWalletCheck()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mMessageReceiver)
    }

    override fun onStart() {
        super.onStart()
//        Helper(this@NavigationMainActivity).deviceId;
        val authtoken = AppPreference.getStringPreference(
            ChatApplication.context,
            Constant.USER_LOGIN_TOKEN
        )
        Log.e("token", authtoken)
    }

    override fun onDestroy() {
        stopSocketConnections()
//        LocalBroadcastManager.getInstance(this)
//            .unregisterReceiver(mMessageReceiver)
        super.onDestroy()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_main)
        fragmentUtils = FragmentUtils(supportFragmentManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondaryColor);
        }

        initSocketSetup()
        Constant.NotificationCounter = 0
        Constant.BroadCastCounter = 0
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = false
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navBtn.setOnClickListener { drawer_layout.openDrawer(Gravity.LEFT); }


        matka.setOnClickListener {
            fragmentUtils?.replaceFragment(
                HomePageFragment(),
                Constant.LiveResultsFragment,
                R.id.home_frame
            )
            matka.setImageResource(R.drawable.matka_selected_orange)
            starline.setImageResource(R.drawable.starline_unselected)
            jackpot.setImageResource(R.drawable.jackpot_unselected)

            matkaDashboardSelected()

            dashboardstring = "orange"

        }

        starline.setOnClickListener {
            fragmentUtils?.replaceFragment(
                StarlineDashboardActivity(),
                Constant.StarlineDashboarFragment,
                R.id.home_frame
            )
            matka.setImageResource(R.drawable.matka_unselected)
            starline.setImageResource(R.drawable.starline_selected)
            jackpot.setImageResource(R.drawable.jackpot_unselected)
            starLineDashboardSelected()
            dashboardstring = "pink"

        }


        jackpot.setOnClickListener {

            fragmentUtils?.replaceFragment(
                JackpotDashBoardActivity(),
                Constant.StarlineDashboarFragment,
                R.id.home_frame
            )
            matka.setImageResource(R.drawable.matka_unselected)
            starline.setImageResource(R.drawable.starline_unselected)
            jackpot.setImageResource(R.drawable.jackpot_selected)
            jackpotDashboardSelected()
            dashboardstring = "green"
        }
        tabWalletAmount.setOnClickListener {
            //            Helper(mContext).showNotification(this@NavigationMainActivity)
            userWalletCheck()
        }


        var from = ""


        val intent = intent
        if (intent != null) {
            from = intent.getStringExtra("from").toString()
            welcome_message = intent.getStringExtra("welcome_message")
            if (from == "youTubePlay") {
                // gameTitle.text = resources.getString(R.string.notification)
                // tabHomePageView.visibility = View.GONE
                //  gamedesc.visibility = View.GONE
                //topMenu.visibility=View.GONE
                /*fragmentUtils!!.replaceFragment(
                    NotificationFragment(),
                    Constant.NotificationFragment, R.id.home_frame
                )*/
                startActivity(Intent(mContext, NotifcationActivity::class.java))
                //  changeBackgroundColorNotification()
            } else {
                gameTitle.text = resources.getString(R.string.app_name_full)
                tabHomePageView.visibility = View.VISIBLE
                gamedesc.visibility = View.VISIBLE
                fragmentUtils!!.replaceFragment(
                    HomePageFragment(),
                    Constant.LiveResultsFragment,
                    R.id.home_frame
                )
            }

            if (welcome_message != null)
                if (!TextUtils.isEmpty(welcome_message)) {
                    notifyWelcomeMessage(welcome_message)
                }
        }


        init()


        bottomNavHomeSelected()
        homeSmallLyt.setOnClickListener { bottomNavHomeSelected() }
        bidHistorySmallLyt.setOnClickListener { bottomNavBidHistorySelected() }
        notificationSmallLyt.setOnClickListener { bottomNavNotificationSelected() }
        chatSmallLyt.setOnClickListener { bottomNavChatSelected() }

        homeBigLyt.setOnClickListener { bottomNavHomeSelected() }
        bidHistoryBigLyt.setOnClickListener { bottomNavBidHistorySelected() }
        notificationsBigLyt.setOnClickListener { bottomNavNotificationSelected() }
        chatBigLyt.setOnClickListener { bottomNavChatSelected() }




        customSwitch.setOnCheckedChangeListener { buttonView, isChecked -> }
    }


    private fun init() {


        imageList.add(SlideModel(R.drawable.slider_image))
        imageList.add(SlideModel(R.drawable.slider_image))
        imageList.add(SlideModel(R.drawable.slider_image))

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)

        navigationItemValue()

        backBtn.visibility = View.GONE
        navBtn.visibility = View.VISIBLE

        cartLyt.setOnClickListener {
            startActivity(Intent(mContext, NotifcationActivity::class.java))
        }
        navNotiLyt.setOnClickListener {
            startActivity(Intent(mContext, NotifcationActivity::class.java))
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        rvNavigationContent.layoutManager = LinearLayoutManager(this)
        navigationItemAdapter =
            NavigationItemAdapter(navModalList, { position, view ->
                Log.e("click", position.toString() + "")
                fragmentUtils = FragmentUtils(supportFragmentManager)
                when (position) {
                    0 -> {

                        topMenu.visibility = View.GONE
                        toolbarTitle.text = "My Bids"
                        fragmentUtils?.replaceFragment(
                            MyHistoryFragment(),
                            Constant.MyHistoryFragment,
                            R.id.home_frame
                        )
                        drawer_layout.closeDrawer(GravityCompat.START)

                    }

                    1 -> {
                        startActivity(
                            Intent(
                                mContext,
                                BidsHistoryActivity::class.java
                            ).putExtra("from", "creditHistory")
                        )
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }

                    2 -> {
                        toolbarTitle.text = "Account Statements"
                        topMenu.visibility = View.GONE
                        fragmentUtils?.replaceFragment(
                            AccountStatementFragment(),
                            Constant.AccountStatementFragment,
                            R.id.home_frame
                        )
                        drawer_layout.closeDrawer(GravityCompat.START)

                    }

                    3 -> {
                        toolbarTitle.text = "Terms & Conditions"
                        topMenu.visibility = View.GONE
                        fragmentUtils?.replaceFragment(
                            NoticeBoardFragment(),
                            Constant.NoticeBoardFragment,
                            R.id.home_frame
                        )
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }

                    4 -> {
                        toolbarTitle.text = "Game Rates"
                        topMenu.visibility = View.GONE
                        fragmentUtils!!.replaceFragment(
                            GameRatesFragment(),
                            Constant.GameRatesFragment,
                            R.id.home_frame
                        )
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }

                    5 -> {
                        val intent = Intent(NavigationMainActivity@ this, FaqsActivity::class.java)
                        startActivity(intent)
                    }

                }
            })


        tvEdit.setOnClickListener {
            startActivity(Intent(mContext, BankDetailActivity::class.java))
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        notificationLyt.setOnClickListener {
            startActivity(Intent(mContext, NotifcationActivity::class.java))
            //  drawer_layout.closeDrawer(GravityCompat.START)
        }



        logoutLyt.setOnClickListener {
            doLogout()
            drawer_layout.closeDrawer(GravityCompat.START)
        }


        chatLyt.setOnClickListener {
            startActivity(Intent(mContext, ChatBoardActivity_old::class.java))

            //drawer_layout.closeDrawer(GravityCompat.START)
        }






        rvNavigationContent.adapter = navigationItemAdapter
        navigationItemAdapter!!.notifyDataSetChanged()



        ivProfile.controller =
            Helper.frescoImageLoad("", R.drawable.ic_user_round_placeholder, ivProfile)
        tvUserName.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        tvUserName.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_USER_NAME)
        tvPhoneNumber.text = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_MOBILE)
    }

    private fun navigationItemValue() {

        imgNotification1.setOnClickListener(this)
        ivBroadcast.setOnClickListener(this)
        navBack.setOnClickListener(this)


        navModalList.add(
            NavigationItemModal(
                R.drawable.my_bid_nav,
                "My Bids",
                "View your Current and previous bids"
            )
        )
        navModalList.add(
            NavigationItemModal(
                R.drawable.credit_history_nav,
                "Credit History",
                "Check all your deposits and winnings"
            )
        )
        navModalList.add(
            NavigationItemModal(
                R.drawable.account_statement_nav,
                "Account Statement",
                "Check your account statement & balance"
            )
        )
        navModalList.add(
            NavigationItemModal(
                R.drawable.terms_and_conditions_nav,
                "Terms and Conditions",
                "Read rules and terms and conditios"
            )
        )
        navModalList.add(
            NavigationItemModal(
                R.drawable.star_nav,
                "Game Rates",
                "Get an idea of the pricing of the game"
            )
        )
        navModalList.add(
            NavigationItemModal(
                R.drawable.star_nav,
                "FAQs",
                "Read how to play our games"
            )
        )


        /* navModalList.add(NavigationItemModal(R.drawable.menu_games_rate, mContext.getString(R.string.game_rates)))
         navModalList.add(NavigationItemModal(R.drawable.menu_how_to_play, mContext.getString(R.string.how_to_play)))
 //        navModalList.add(NavigationItemModal(R.drawable.menu_passbook, mContext.getString(R.string.passbook)))
 //        navModalList.add(NavigationItemModal(R.drawable.menu_chat, mContext.getString(R.string.chat)))
 //        navModalList.add(NavigationItemModal(R.drawable.menu_notification, mContext.getString(R.string.notification)))
 //        navModalList.add(NavigationItemModal(R.drawable.menu_notice,  mContext.getString(R.string.notice_board_rules)))
 //        navModalList.add(NavigationItemModal(R.drawable.idea,  mContext.getString(R.string.submit_your_ideas)))
 //        navModalList.add(NavigationItemModal(R.drawable.menu_setting,  mContext.getString(R.string.setting)))
         navModalList.add(NavigationItemModal(R.drawable.menu_logout,  mContext.getString(R.string.logout)))*/

    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        fragmentUtils = FragmentUtils(supportFragmentManager)
        when (p0?.id) {

            R.id.imgNotification1 -> {
                tv_notification_counter.visibility = View.GONE
                Constant.NotificationCounter = 0
                tv_notification_counter.text = Constant.NotificationCounter.toString()

                startActivity(Intent(mContext, NotifcationActivity::class.java))

            }

            R.id.ivBroadcast -> {
                tv_bro_counter.visibility = View.GONE
                Constant.BroadCastCounter = 0
                tv_bro_counter.text = Constant.BroadCastCounter.toString()
                gameTitle.text = "BroadCast"
                tabHomePageView.visibility = View.GONE
                gamedesc.visibility = View.GONE
                fragmentUtils!!.replaceFragment(
                    BroadCastFragment(),
                    Constant.BroadCastFragment, R.id.home_frame
                )
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.navBack -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }


        }
    }


    public fun userWalletCheck() {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(this@NavigationMainActivity),
                retrofitApiClientAuth?.userWalletBalance(body),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")

                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonObject = JSONObject(response.string())
                        if (jsonObject.getInt("status") == 1) {
                            val data = jsonObject.getJSONObject("data")
                            val walletBalance: Double = data.getDouble("wallet_balance")
                            tvWalletAmount.text = Helper.getSuffix("" + walletBalance.toString())
                            println("lagana pesa")
                            AppPreference.setIntegerPreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE,
                                walletBalance.toInt()
                            )

                            AppPreference.setDoublePreference(
                                mContext,
                                Constant.USER_WALLET_BALANCE_FLOAT,
                                walletBalance
                            )

                        } else {
//                            Alerts.show(mContext,jsonObject.getString("message"))
                            Alerts.SessionLogout(
                                this@NavigationMainActivity,
                                this@NavigationMainActivity
                            )
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.show(mContext, error.toString())
                    }
                })

        }
    }

    private fun doLogout() {
        AlertDialog.Builder(mContext)
            .setTitle("Logout")
            .setMessage("Are you sure want to Logout ?")
            .setPositiveButton("YES") { _, _ ->
                val logoutUser = LogoutUser(mContext, this@NavigationMainActivity)
                logoutUser.logout()
            }
            .setNegativeButton("NO", null)
            .create()
            .show()
    }

    override fun onBackPressed() {
        if (this.drawer_layout.isDrawerOpen(GravityCompat.START)) {
            this.drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val home = supportFragmentManager.findFragmentByTag(Constant.LiveResultsFragment)
            if (home != null) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                Alerts.show(this, "Please click BACK again to exit")
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
                this.doubleBackToExitPressedOnce = true
            } else {
                bottomNavHomeSelected()
            }


        }
    }

    //socket connection methods


    private fun initSocketSetup() {
        try {
            dbHelper = DBHelper(mContext)
            mSocket = (application as? ChatApplication)?.socket
            manageChatCounter()
            getuserOldMessageCounterOnly()
            getNotificationCounter()


            if (!mSocket!!.connected()) {
                mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
                mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
                mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
//                mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
                mSocket!!.on(ChatConstants.KEY_USER_ONLINE_STATUS, sendCurrentUserOnlineStatus)
                mSocket!!.on(ChatConstants.KEY_RECEIVE_MESSAGE, onReceiveMsg);
//                mSocket!!.on(ChatConstants.KEY_FORCE_LOGOUT, forceLogout);
                mSocket!!.on(ChatConstants.KEY_DELETE_SINGLE_MSG, onDeleteSingleMsg);
                mSocket!!.on(ChatConstants.KEY_DELETE_USER_MSG, onDeleteuserMsg);
                mSocket!!.on(ChatConstants.KEY_DELETE_ALL_MSG, onDeleteAllMsg);
                mSocket!!.connect()

                manageChatCounter2()
            }
        } catch (e: URISyntaxException) {
            Log.e("SOCKET", "Error in init Socket")
            e.printStackTrace()
        }

    }


    private fun stopSocketConnections() {
        if (mSocket!!.connected()) {
            mSocket!!.disconnect();
            mSocket!!.off(Socket.EVENT_CONNECT, onConnect);
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//            mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket!!.off(ChatConstants.KEY_USER_ONLINE_STATUS, sendCurrentUserOnlineStatus);
            mSocket!!.off(ChatConstants.KEY_RECEIVE_MESSAGE, onReceiveMsg);
//            mSocket!!.off(ChatConstants.KEY_FORCE_LOGOUT, forceLogout);
            mSocket!!.off(ChatConstants.KEY_DELETE_SINGLE_MSG, onDeleteSingleMsg);
            mSocket!!.off(ChatConstants.KEY_DELETE_USER_MSG, onDeleteuserMsg);
            mSocket!!.off(ChatConstants.KEY_DELETE_ALL_MSG, onDeleteAllMsg);
        }

    }

    private var onConnect: Emitter.Listener? =
        Emitter.Listener { runOnUiThread { Log.e("SOCKET", "Connect") } }

    private val onDisconnect =
        Emitter.Listener { runOnUiThread { Log.e("SOCKET", "Disconnect") } }

    private val onConnectError =
        Emitter.Listener { runOnUiThread { Log.e("SOCKET", "Error connecting") } }


    private val sendCurrentUserOnlineStatus = Emitter.Listener { args ->
        runOnUiThread {
            try {
                Log.e("SOCKET", "Updating... My Status")
                val data = args[0] as String
                var userId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                if (data.equals(userId)) {
                    val jsonObject = JSONObject()
                    jsonObject.put("userId", userId)
                    jsonObject.put("status", ChatConstants.MY_ONLINE_STATUS)
                    mSocket!!.emit(ChatConstants.KEY_USER_ONLINE_STATUS_RETURN, jsonObject)
                }
                Log.e("SOCKET", "Update My Status " + ChatConstants.MY_ONLINE_STATUS)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "Update My Status : " + e.message)
            }
        }
    }

    private var dbHelper: DBHelper? = null

    private val onReceiveMsg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val my_userid = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                Log.e("SOCKET", "Receive Message")
                Log.e("SOCKET", args[0].toString())
                var gson = Gson()
                val jsonString = args[0].toString()
                val finalJsonObject = JSONObject(jsonString);

                var messageModallist = gson.fromJson(jsonString, OnReceiveNewMessage::class.java)


                val msgData = messageModallist.messageNew
                if (msgData.userId.equals(my_userid)) {
                    if (!msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty()) {
                        Log.e("SOCKET", "image+text message")
                        msgData.msgContentType = 3; // image+text message
                    } else if (!msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty()) {
                        Log.e("SOCKET", "image")
                        msgData.msgContentType = 2; // image
                    } else if (msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty() && msgData!!.videos.isNullOrEmpty()) {
                        Log.e("SOCKET", "text message")
                        msgData.msgContentType = 1; // text
                    } else if (msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && !msgData!!.audios.isEmpty()) {
                        Log.e("SOCKET", "audio message")
                        msgData.msgContentType = 4; // audio
                    } else if (!msgData!!.videos.isNullOrEmpty()) {
                        Log.e("SOCKET", "videos message")
                        msgData.msgContentType = 6; // videos
                    }


                    dbHelper!!.addMessageToDb(msgData)
                    if ((application as? ChatApplication)?.onSocketListener != null) {
                        onSocketListener = (application as? ChatApplication)?.onSocketListener
                        onSocketListener!!.onReceiveMsg(msgData)
                    }
                    if (msgData.messType == 2) {
                        ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
                        manageChatCounter()
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "Receive Message : " + e.message)
            }
        }
    }

    private fun currentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a");

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        return formatter.format(calendar.time)
    }

    fun String.encodeToB64(): String {
        return android.util.Base64.encodeToString(
            this.toByteArray(charset("UTF-8")),
            android.util.Base64.DEFAULT
        )
    }

    private fun notifyBroadcastMessage(oldBroadcastMessage: List<MessageModal>) {
        for (i in 0..oldBroadcastMessage.size - 1) {
            runOnUiThread {
                try {
                    val my_userid =
                        AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                    Log.e("SOCKET", "Receive Message")
                    Log.e("SOCKET", oldBroadcastMessage.get(i).id)
//                    {"_id":"5e4d1f37e46d280018b3dfc5","message":"tsetsete","dateTime":"2020-02-19 05:12 pm","dateTimestamp":"1582112567"}
                    var messageModallist = OnReceiveNewMessage()
                    val msgData = messageModallist!!.messageNew

                    if (!oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(i)!!.message.isEmpty()) {
                        Log.e("SOCKET", "image+text message")
                        msgData.msgContentType = 3; // image+text message
                    } else if (!oldBroadcastMessage.get(i)!!.images.isEmpty() && oldBroadcastMessage.get(
                            i
                        )!!.message.isEmpty() && oldBroadcastMessage.get(i)!!.audios.isEmpty()
                    ) {
                        Log.e("SOCKET", "image")
                        msgData.msgContentType = 2; // image
                    } else if (oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(
                            i
                        )!!.message.isEmpty() && oldBroadcastMessage.get(i)!!.audios.isEmpty() && oldBroadcastMessage.get(
                            i
                        )!!.videos.isNullOrEmpty()
                    ) {
                        Log.e("SOCKET", "text message")
                        msgData.msgContentType = 1; // text
                    } else if (oldBroadcastMessage.get(i)!!.images.isEmpty() && !oldBroadcastMessage.get(
                            i
                        )!!.audios.isEmpty()
                    ) {
                        Log.e("SOCKET", "audio message")
                        msgData.msgContentType = 4; // audio
                    } else if (!msgData!!.videos.isNullOrEmpty()) {
                        Log.e("SOCKET", "videos message")
                        msgData.msgContentType = 6; // videos
                    }

                    if (!oldBroadcastMessage.get(i)!!.audios.isEmpty()) {
                        msgData.msgContentType = 4;
                    }
                    msgData.id = oldBroadcastMessage.get(i).id
                    msgData.userId = my_userid
                    msgData.userName = "Brand Name"
                    msgData.messStatus = 0
                    msgData.messType = 5
                    msgData.androidMessage = oldBroadcastMessage.get(i).message!!.encodeToB64()
                    msgData.dateTime = oldBroadcastMessage.get(i).dateTime
                    msgData.dateTimestamp = oldBroadcastMessage.get(i).dateTimestamp
                    msgData.msgFile = null
                    msgData.message = oldBroadcastMessage.get(i).message
                    msgData.audios = oldBroadcastMessage.get(i).audios
                    msgData.images = oldBroadcastMessage.get(i).images
                    msgData.videos = oldBroadcastMessage.get(i).videos
                    msgData.replyMessage = ""
                    msgData.replyName = ""
                    msgData.messAudioDuration = 0
//                    msgData.msgContentType = 1
                    msgData.messFileUrl = ""
                    if (msgData.userId.equals(my_userid)) {
                        dbHelper!!.addMessageToDb(msgData)
                        if ((application as? ChatApplication)?.onSocketListener != null) {
                            onSocketListener = (application as? ChatApplication)?.onSocketListener
                            onSocketListener!!.onReceiveMsg(msgData)
                        }
                        if (msgData.messType == 5) {
                            ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
                            manageChatCounter()
                        }
                    }


                    if (i == oldBroadcastMessage.size - 1) {
                        startActivity(Intent(mContext, ChatBoardActivity::class.java))
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("SOCKET", "Welcome Message : " + e.message)
                }
            }
        }


    }

    private fun notifyWelcomeMessage(welcomeMessage: String?) {
        runOnUiThread {
            try {
                val my_userid = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                Log.e("SOCKET", "Receive Message")
                Log.e("SOCKET", welcomeMessage.toString())
                var gson = Gson()

                var messageModallist = OnReceiveNewMessage()
                val msgData = messageModallist!!.messageNew
                msgData.id = ""
                msgData.userId = my_userid
                msgData.userName = "Brand Name"
                msgData.messStatus = 0
                msgData.messType = 2
                msgData.androidMessage = welcomeMessage!!.encodeToB64()
                msgData.dateTime = currentDateTime()
                msgData.dateTimestamp = (System.currentTimeMillis() / 1000).toString()
                msgData.msgFile = ""
                msgData.message = welcomeMessage
                msgData.audios = ""
                msgData.images = ""
                msgData.replyMessage = ""
                msgData.replyName = ""
                msgData.messAudioDuration = 0
                msgData.msgContentType = 0
                msgData.messFileUrl = ""






                if (msgData.userId.equals(my_userid)) {
                    if (!msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty()) {
                        Log.e("SOCKET", "image+text message")
                        msgData.msgContentType = 3; // image+text message
                    } else if (!msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty()) {
                        Log.e("SOCKET", "image")
                        msgData.msgContentType = 2; // image
                    } else if (msgData!!.images.isEmpty() && !msgData!!.androidMessage.isEmpty() && msgData!!.audios.isEmpty()) {
                        Log.e("SOCKET", "text message")
                        msgData.msgContentType = 1; // text
                    } else if (msgData!!.images.isEmpty() && msgData!!.androidMessage.isEmpty() && !msgData!!.audios.isEmpty()) {
                        Log.e("SOCKET", "audio message")
                        msgData.msgContentType = 4; // audio
                    }
                    dbHelper!!.addMessageToDb(msgData)
                    if ((application as? ChatApplication)?.onSocketListener != null) {
                        onSocketListener = (application as? ChatApplication)?.onSocketListener
                        onSocketListener!!.onReceiveMsg(msgData)
                    }
                    if (msgData.messType == 2) {
                        ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
                        manageChatCounter()
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "Welcome Message : " + e.message)
            }
        }

    }

    private val onDeleteSingleMsg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val my_userid = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                Log.e("SOCKET", "onDeleteSingleMsg Message")
                Log.e(
                    "SOCKET",
                    args[0].toString()
                ) //{"messId":"5e158b039ef56b00181549b7","userId":"5e07316bea11f30018d5fe19"}
                val jsonString = args[0].toString()
                val json = JSONObject(jsonString)


                if (json.optString("userId").equals(my_userid)) {
                    val chatMessageList = dbHelper!!.getAllMessageFromDB()
                    for (i in 0..chatMessageList.size) {
                        if (json.optString("messId").equals(chatMessageList.get(i).id)) {
                            chatMessageList.get(i).messStatus = 3
                            dbHelper!!.updateMessageInDbById(chatMessageList.get(i))
                            Log.e("SOCKET", "Update message to delte in db")

                            if ((application as? ChatApplication)?.onSocketListener != null) {
                                onSocketListener =
                                    (application as? ChatApplication)?.onSocketListener
                                onSocketListener!!.onDeleteSingleMsg(json.optString("messId"))
                            }
                        }
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "onDeleteSingleMsg Message : " + e.message)
            }
        }
    }


    private val onDeleteAllMsg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                Log.e("SOCKET", "onDeleteAllMsg Message")
                Log.e("SOCKET", args[0].toString())
                dbHelper!!.deleteAllChatFromDb()
                if ((application as? ChatApplication)?.onSocketListener != null) {
                    onSocketListener = (application as? ChatApplication)?.onSocketListener
                    onSocketListener!!.onDeleteAllMsg(args[0].toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "onDeleteAllMsg Message : " + e.message)
            }
        }
    }


    private val onDeleteuserMsg = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val my_userid = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
                Log.e("SOCKET", "onDeleteuserMsg Message")
                Log.e("SOCKET", args[0].toString())
                if (args[0].toString().equals(my_userid)) {
                    dbHelper!!.deleteAllChatFromDb()
                    if ((application as? ChatApplication)?.onSocketListener != null) {
                        onSocketListener = (application as? ChatApplication)?.onSocketListener
                        onSocketListener!!.onDeleteUserMsg(args[0].toString())
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SOCKET", "onDeleteuserMsg Message : " + e.message)
            }
        }
    }

    public fun manageChatCounter() {
        if (ChatConstants.CHAT_COUNTER == 0) {
            tv_chat_counter.visibility = View.GONE
        } else {
            tv_chat_counter.visibility = View.VISIBLE
            if (ChatConstants.CHAT_COUNTER <= 9) {
                tv_chat_counter.text = ChatConstants.CHAT_COUNTER.toString()
            } else {
                tv_chat_counter.text = "9+"
            }

        }
    }

    public fun manageChatCounter2() {
        if (ChatConstants.CHAT_COUNTER == 0) {
            tv_chat_counter.visibility = View.GONE
        } else {
            tv_chat_counter.visibility = View.VISIBLE
            if (ChatConstants.CHAT_COUNTER <= 9) {
                tv_chat_counter.text = ChatConstants.CHAT_COUNTER.toString()
            } else {
                tv_chat_counter.text = "9+"
            }
            startActivity(Intent(mContext, ChatBoardActivity::class.java))
        }
    }

    private fun getuserOldMessageCounterOnly() {
        dbHelper = DBHelper(mContext)
        var broMessId = ""
        var lastmessage_id = ""

        val my_userid = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)

        var chatMessageList = dbHelper!!.getAllMessageFromDB()
        if (chatMessageList != null)
            if (chatMessageList.size > 0) {
                for (i in 0..chatMessageList.size - 1) {
                    if (chatMessageList.get(i).messType == 5) {
                        broMessId = chatMessageList.get(i).id
                    } else {
                        lastmessage_id = chatMessageList.get(i).id
                    }


//                lastmessage_id = chatMessageList.get(chatMessageList.size - 1).id

                }

                getUsersOldMessage(lastmessage_id, my_userid, broMessId)

            }
        getUsersOldMessage(lastmessage_id, my_userid, broMessId)
    }

    private fun getUsersOldMessage(lastmessageId: String, myUserid: String, broMessId: String) {
        val mObject = JSONObject()
        mObject.put("messId", lastmessageId)
        mObject.put("broMessId", broMessId)
        mObject.put("userId", myUserid)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        ChatRetrofitService.getServerResponse(
            this@NavigationMainActivity,
            null,
            ChatRetrofitService.getRetrofit()?.getOldMessages(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")

                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    val jsonObject = JSONObject(response.string())
                    println(response.toString())
                    var gson = Gson()
                    try {
                        var messageModallist =
                            gson.fromJson(jsonObject.toString(), GetOldMessages::class.java)

                        if (jsonObject.getBoolean("status")) {
                            for (i in 0 until messageModallist.messageList.size) {
                                if (messageModallist.messageList[i].messStatus != 3) {
                                    ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
                                    manageChatCounter()
                                }
                            }
                            if (messageModallist.messageList.isNotEmpty()) {
//                                if (tv_chat_counter.visibility == View.VISIBLE) {
                                startActivity(Intent(mContext, ChatBoardActivity::class.java))
//                                }
                            }
                            for (msgData in messageModallist.deleteMessageList) {
                                dbHelper = DBHelper(this@NavigationMainActivity)
                                val chatMessageList = dbHelper!!.getAllMessageFromDB()


                                for (i in 0..chatMessageList.size - 1) {
                                    if (msgData.id.equals(chatMessageList.get(i).id)) {
                                        msgData.messStatus = 3;
                                        chatMessageList.set(i, msgData)
                                        dbHelper!!.updateMessageInDbById(msgData)
                                    }
                                }
                            }

//                            for (msgData in messageModallist.oldBroadcastMessage) {
//                                ChatConstants.CHAT_COUNTER = ChatConstants.CHAT_COUNTER + 1;
//                                manageChatCounter()
                            notifyBroadcastMessage(messageModallist.oldBroadcastMessage)
//                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onResponseFailed(error: String?) {
                }
            })
    }


    override fun onUpdateWallet() {
        userWalletCheck()
    }


    private fun getNotificationCounter() {
        val mObject = JSONObject()
        mObject.put(
            "id", AppPreference.getStringPreference(
                mContext,
                Constant.API_Notification_id
            )
        )
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(null,
            retrofitApiClientAuth.getNotificationCounter(body),
            object : WebResponse {
                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    try {
                        val responseObject = JSONObject(response.string())
                        if (responseObject.getInt("status") == 1) {
                            if (responseObject.getInt("data") > 0) {
                                tv_notification_counter.visibility = View.VISIBLE
                                Constant.NotificationCounter =
                                    Constant.NotificationCounter + responseObject.getInt("data")
                                tv_notification_counter.text =
                                    Constant.NotificationCounter.toString()
                                if (responseObject.getInt("data") > 9) {
                                    tv_notification_counter.text =
                                        "9+"
                                }

//                                gameTitle.text = resources.getString(R.string.notification)
//                                tabHomePageView.visibility=View.GONE
//                                gamedesc.visibility=View.GONE
//                                fragmentUtils!!.replaceFragment(
//                                    NotificationFragment(),
//                                    Constant.NotificationFragment, R.id.home_frame
//                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@NavigationMainActivity, error.toString())
                }
            })
    }


    fun onChattingClick(view: View) {
        startActivity(Intent(mContext, ChatBoardActivity::class.java))

    }

    fun onHistoryClick(view: View) {
        gameTitle.text = "History"
        tabHomePageView.visibility = View.GONE
        gamedesc.visibility = View.GONE
        fragmentUtils?.replaceFragment(
            MyHistoryFragment(),
            Constant.MyHistoryFragment,
            R.id.home_frame
        )
    }

    fun onHomeClick(view: View) {
        gameTitle.text = resources.getString(R.string.app_name_full)
        tabHomePageView.visibility = View.VISIBLE
        gamedesc.visibility = View.GONE
        fragmentUtils?.replaceFragment(
            HomePageFragment(),
            Constant.LiveResultsFragment,
            R.id.home_frame
        )


    }

    fun onPassbookClick(view: View) {
        startActivity(Intent(this, PassbookActivity::class.java))

    }

    fun onFundClick(view: View) {
        gameTitle.text = "Funds"
        tabHomePageView.visibility = View.GONE
        gamedesc.visibility = View.GONE
        fragmentUtils?.replaceFragment(
            FundsFragment(),
            Constant.FundsFragment,
            R.id.home_frame
        )

    }
        private fun changeDrawableColor(context: Context, view: Button, color: Int) {
            val drawables: Array<Drawable> = view.compoundDrawables
            for (drawable in drawables) {
                drawable?.setColorFilter(
                    resources.getColor(color),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            view.setTextColor(resources.getColor(color));
        }


        private fun bottomNavHomeSelected() {




            currentIndex = 1


            when (dashboardstring) {
                "orange" -> {
                    topMenu.visibility = View.VISIBLE
                    toolbarTitle.text = resources.getString(R.string.app_name_full)


                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(HomePageFragment(), Constant.LiveResultsFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(HomePageFragment(), Constant.LiveResultsFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }

                    else{
                        fragmentUtils?.replaceFragment(HomePageFragment(), Constant.LiveResultsFragment, R.id.home_frame)

                    }

                }

                "pink" -> {
                    topMenu.visibility = View.VISIBLE
                    toolbarTitle.text = resources.getString(R.string.app_name_full)


                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(StarlineDashboardActivity(), Constant.LiveResultsFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(StarlineDashboardActivity(), Constant.LiveResultsFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }

                    else{
                        fragmentUtils?.replaceFragment(StarlineDashboardActivity(), Constant.LiveResultsFragment, R.id.home_frame)

                    }
                }

                "green" -> {
                    topMenu.visibility = View.VISIBLE
                    toolbarTitle.text = resources.getString(R.string.app_name_full)
                    fragmentUtils?.replaceFragment(
                        JackpotDashBoardActivity(),
                        Constant.LiveResultsFragment,
                        R.id.home_frame
                    )

                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(JackpotDashBoardActivity(), Constant.LiveResultsFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(JackpotDashBoardActivity(), Constant.LiveResultsFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }

                    else{
                        fragmentUtils?.replaceFragment(JackpotDashBoardActivity(), Constant.LiveResultsFragment, R.id.home_frame)

                    }


                }
            }

            homeBigLyt.visibility = View.VISIBLE
            bidHistoryBigLyt.visibility = View.INVISIBLE
            notificationsBigLyt.visibility = View.INVISIBLE
            chatBigLyt.visibility = View.INVISIBLE

            homeSmallLyt.visibility = View.INVISIBLE
            bidHistorySmallLyt.visibility = View.VISIBLE
            notificationSmallLyt.visibility = View.VISIBLE
            chatSmallLyt.visibility = View.VISIBLE


        }


        private fun bottomNavBidHistorySelected() {



            topMenu.visibility = View.GONE
            toolbarTitle.text = "Bid History"

            currentIndex = 2

            if (currentIndex < previousIndex) {
                fragmentUtils?.replaceFragment(MyHistoryFragment(), Constant.MyHistoryFragment, R.id.home_frame,"left")
                previousIndex=currentIndex
            }
            else if(currentIndex > previousIndex){
                fragmentUtils?.replaceFragment(MyHistoryFragment(), Constant.MyHistoryFragment, R.id.home_frame,"right")
                previousIndex=currentIndex
            }

            else{
                fragmentUtils?.replaceFragment(MyHistoryFragment(), Constant.MyHistoryFragment, R.id.home_frame)

            }

            homeBigLyt.visibility = View.INVISIBLE
            bidHistoryBigLyt.visibility = View.VISIBLE
            notificationsBigLyt.visibility = View.INVISIBLE
            chatBigLyt.visibility = View.INVISIBLE

            homeSmallLyt.visibility = View.VISIBLE
            bidHistorySmallLyt.visibility = View.INVISIBLE
            notificationSmallLyt.visibility = View.VISIBLE
            chatSmallLyt.visibility = View.VISIBLE

        }


        private fun bottomNavNotificationSelected() {



            currentIndex = 3

            when (dashboardstring) {
                "orange" -> {
                    topMenu.visibility = View.GONE
                    toolbarTitle.text = "Notifications"

                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(NotificationFragment(), Constant.NotificationFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(NotificationFragment(), Constant.NotificationFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }

                    else{
                        fragmentUtils?.replaceFragment(NotificationFragment(), Constant.NotificationFragment, R.id.home_frame)

                    }
                }

                "pink" -> {
                    topMenu.visibility = View.GONE
                    toolbarTitle.text = "Wallet"


                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }

                    else{
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame)

                    }

                }

                "green" -> {

                    topMenu.visibility = View.GONE
                    toolbarTitle.text = "Wallet"
                    if (currentIndex < previousIndex) {
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame,"left")
                        previousIndex=currentIndex
                    }
                    else if(currentIndex > previousIndex){
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame,"right")
                        previousIndex=currentIndex
                    }
                    else{
                        fragmentUtils?.replaceFragment(FundsFragment(), Constant.FundsFragment, R.id.home_frame)

                    }

                }
            }
            homeBigLyt.visibility = View.INVISIBLE
            bidHistoryBigLyt.visibility = View.INVISIBLE
            notificationsBigLyt.visibility = View.VISIBLE
            chatBigLyt.visibility = View.INVISIBLE

            homeSmallLyt.visibility = View.VISIBLE
            bidHistorySmallLyt.visibility = View.VISIBLE
            notificationSmallLyt.visibility = View.INVISIBLE
            chatSmallLyt.visibility = View.VISIBLE

        }

        private fun bottomNavChatSelected() {





            topMenu.visibility = View.GONE
            toolbarTitle.text = "Support Chat"

            currentIndex = 4


            if (currentIndex < previousIndex) {
                fragmentUtils?.replaceFragment(ChatBoardActivity(), Constant.ChatFragment, R.id.home_frame,"left")
                previousIndex=currentIndex
            }
            else if(currentIndex > previousIndex){
                fragmentUtils?.replaceFragment(ChatBoardActivity(), Constant.ChatFragment, R.id.home_frame,"right")
                previousIndex=currentIndex
            }

            else{
                fragmentUtils?.replaceFragment(ChatBoardActivity(), Constant.ChatFragment, R.id.home_frame)

            }

            homeBigLyt.visibility = View.INVISIBLE
            bidHistoryBigLyt.visibility = View.INVISIBLE
            notificationsBigLyt.visibility = View.INVISIBLE
            chatBigLyt.visibility = View.VISIBLE

            homeSmallLyt.visibility = View.VISIBLE
            bidHistorySmallLyt.visibility = View.VISIBLE
            notificationSmallLyt.visibility = View.VISIBLE
            chatSmallLyt.visibility = View.INVISIBLE



        }

        private fun matkaDashboardSelected() {
            notificationSelectedText.text = "Notification"
            notificationsUnselected.setImageResource(R.drawable.notification_with_indicator)
            homeSelected.setImageResource(R.drawable.home_selected);
            bidHistorySelected.setImageResource(R.drawable.bid_history_selected);
            notificationsSelected.setImageResource(R.drawable.notification_selected);
            chatSelected.setImageResource(R.drawable.chat_selected);
        }

        private fun starLineDashboardSelected() {
            notificationSelectedText.text = "Wallet"
            notificationsUnselected.setImageResource(R.drawable.wallet_icon)
            homeSelected.setImageResource(R.drawable.home_selected_pink);
            bidHistorySelected.setImageResource(R.drawable.bid_history_selected_pink);
            notificationsSelected.setImageResource(R.drawable.wallet_selected_pink);
            chatSelected.setImageResource(R.drawable.chat_selected_pink);
        }

        private fun jackpotDashboardSelected() {
            notificationSelectedText.text = "Wallet"
            notificationsUnselected.setImageResource(R.drawable.wallet_icon)
            homeSelected.setImageResource(R.drawable.home_selected_green);
            bidHistorySelected.setImageResource(R.drawable.bid_history_selected_green);
            notificationsSelected.setImageResource(R.drawable.wallet_selected_green);
            chatSelected.setImageResource(R.drawable.chat_selected_green);
        }


        private fun fadeIn(view: View) {

            val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            view.startAnimation(fadeInAnimation)
            view.visibility = View.VISIBLE
        }

        private fun fadeOut(view: View) {
            val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            view.startAnimation(fadeOutAnimation)
            view.visibility = View.INVISIBLE // Make the view invisible
        }

    }

