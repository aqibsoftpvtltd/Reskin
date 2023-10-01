package com.kasa77.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasa77.R
import com.kasa77.adapter.PassBookAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.passbookDetail.PassBookDetails
import com.kasa77.modal.transaction_statement.AccountHistory
import com.kasa77.modal.transaction_statement.RecordsItem
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.BaseActivity
import com.kasa77.utils.DateFormatToDisplay
import com.google.gson.Gson
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import kotlinx.android.synthetic.main.activity_passbook.*
import kotlinx.android.synthetic.main.layout_target.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class PassbookActivity : BaseActivity(), PassBookAdapter.OnDropActionListner {

    var isLoading = false
    var pageNumber = 1
    private var totalPage: Int = 0;

    private lateinit var paginationAdapter: PassBookAdapter
    private var accountHistory: AccountHistory? = null
    val recordlist: ArrayList<RecordsItem?> =
        ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passbook)

        paginationAdapter = PassBookAdapter(this@PassbookActivity, recordlist, this)
        recyclerViewPassBook.layoutManager = LinearLayoutManager(this)
        recyclerViewPassBook.adapter = paginationAdapter
        accountHistorypaginationApi("1")
        initScrollListener()
    }

    private fun spotLightEffect() {
        val targets = ArrayList<Target>()
        // first target
        val firstRoot = FrameLayout(this@PassbookActivity)
        val first = layoutInflater.inflate(R.layout.layout_target, firstRoot)

        val firstTarget = Target.Builder()
            .setAnchor(tabRotate)
            .setShape(Circle(150f))
            .setOverlay(first)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    print("\"first target is started\",")
                }

                override fun onEnded() {
                    print("first target is ended")

                }
            })
            .build()

        targets.add(firstTarget)
        // create spotlight
        val spotlight = Spotlight.Builder(this@PassbookActivity)
            .setTargets(targets)
            .setBackgroundColor(R.color.spotlightBackground)
            .setDuration(500L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {
                    print("spotlight is started")

                }

                override fun onEnded() {
                    print("spotlight is end")

                }
            })
            .build()

        spotlight.start()

        first.findViewById<Button>(R.id.btnOk).setOnClickListener {
            spotlight.finish()
        }
//
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    private fun initScrollListener() {
        recyclerViewPassBook.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == recordlist.size - 1) {
                        //bottom of list!
                        pageNumber++
                        if (totalPage >= pageNumber) {

                            accountHistorypaginationApi(pageNumber.toString())
                            isLoading = true
                        }
                    }
                }
            }
        })
    }

    private fun accountHistorypaginationApi(skipValue: String) {
        Log.e("PAGE", "accountHistorypaginationApi: $skipValue ")
        if (skipValue != "1") {
            recordlist.add(null)
            recyclerViewPassBook.post {
                paginationAdapter.notifyItemInserted(recordlist!!.size!! - 1)
            }
        }

        val userLoginId =
            AppPreference.getStringPreference(this@PassbookActivity, Constant.USER_LOGIN_ID)
        val mObject = JSONObject()
        mObject.put("id", userLoginId)
        mObject.put("skipValue", skipValue)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(
            null,
            AuthHeaderRetrofitService.getRetrofit()!!.getTranscationHistoryPaginatin(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")


                override fun onResponseFailed(error: String?) {
                    Alerts.serverError(this@PassbookActivity, "")
                }

                override fun onResponseSuccess(result: Response<*>?) {
                    val response = result!!.body() as ResponseBody
                    val jsonresponse = JSONObject(response.string())
                    Log.e("OnResponse", jsonresponse.toString())
//              filterType      0: Bid Placed, 1: Bid Status Won, 2: Bid Status Loss, 3: Bid Status Refund, 4: Fund Credit, 5: Fund Debit, 6 : Fund Declined, 7: Account Update Charge
                    if (jsonresponse.optInt("status") == 1) {
                        accountHistory = Gson().fromJson(
                            jsonresponse.toString(),
                            AccountHistory::class.java
                        )

                        totalPage = accountHistory!!.totalPage

                        if (skipValue != "1") {
                            recordlist.removeAt(recordlist.size - 1)
                            val scrollPosition: Int = recordlist.size
                            paginationAdapter.notifyItemRemoved(scrollPosition)
                            paginationAdapter.notifyDataSetChanged()
                            isLoading = false
                        } else {
                            paginationAdapter.notifyDataSetChanged()
                        }


                        paginationAdapter!!.addItems(accountHistory!!.records);

                    } else {
                        if (paginationAdapter!!.itemCount == 0) {
//                            tvMessage.visibility = View.VISIBLE
//                            tvMessage.text = jsonresponse.optString("message")
                        }
                        Alerts.AlertDialogWarning(
                            this@PassbookActivity,
                            jsonresponse.optString("message")
                        )
                        if (skipValue != "1") {
                            recordlist.removeAt(recordlist.size - 1)
                            val scrollPosition: Int = recordlist.size
                            paginationAdapter.notifyItemRemoved(scrollPosition)
                            paginationAdapter.notifyDataSetChanged()
                            isLoading = false
                        }
                    }
                }

            })


    }

    override fun onBidPlaced(viewHolder: PassBookAdapter.MyViewHolder?, datum: RecordsItem?) {
        getPassbookDetails(viewHolder, datum)
    }

    override fun onBidWonLossRefund(
        viewHolder: PassBookAdapter.MyViewHolder?,
        datum: RecordsItem?
    ) {
        getPassbookDetails(viewHolder, datum)
    }

    override fun onAddFund(viewHolder: PassBookAdapter.MyViewHolder?, datum: RecordsItem?) {
        getPassbookDetails(viewHolder, datum)
    }

    override fun onWithdrawChangeBankFund(
        viewHolder: PassBookAdapter.MyViewHolder?,
        datum: RecordsItem?
    ) {
        getPassbookDetails(viewHolder, datum)
    }

    override fun onNoDataFound(holder: PassBookAdapter.MyViewHolder, datum: RecordsItem?) {
        holder!!.loading_spinner.visibility = View.GONE
        holder.content_bank.visibility = View.GONE
        holder.contentBidPlaced.visibility = View.GONE
        holder.contentBidWon.visibility = View.GONE
        holder.contentCredit.visibility = View.GONE
        holder.contentNoData.visibility = View.VISIBLE
    }


    private fun getPassbookDetails(holder: PassBookAdapter.MyViewHolder?, datum: RecordsItem?) {
        val mObject = JSONObject()
        mObject.put("id", datum!!.bidId)
        mObject.put("type", datum!!.reqType)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (mObject).toString()
        )
        AuthHeaderRetrofitService.getServerResponse(
            null,
            AuthHeaderRetrofitService.getRetrofit()?.getPassBookDetail(body),
            object : WebResponse {
                @SuppressLint("SetTextI18n")
                override fun onResponseSuccess(result: Response<*>?) {

                    val responseBody = result!!.body() as ResponseBody?
                    val jsonObject = JSONObject(responseBody!!.string())
                    val jsonresponse = JSONObject(jsonObject.toString())
                    Log.e("OnResponse", jsonresponse.toString())
                    holder!!.loading_spinner.visibility = View.GONE
                    if (jsonresponse.optInt("status") == 1) {
                        val passBookDetails = Gson().fromJson(
                            jsonresponse.toString(),
                            PassBookDetails::class.java
                        )
                        holder.content_bank.visibility = View.GONE
                        holder.contentBidPlaced.visibility = View.GONE
                        holder.contentBidWon.visibility = View.GONE
                        holder.contentCredit.visibility = View.GONE
                        holder.contentNoData.visibility = View.GONE

                        if (passBookDetails.data != null) {
                            when (datum.filterType) {
                                0 -> {//0: Bid Placed,,
                                    holder.contentBidPlaced.visibility = View.VISIBLE

                                    holder.tvBidPlacedType.text =
                                        passBookDetails!!.data!!.gameSession
                                    holder.tvBidPlacedGameName.text =
                                        passBookDetails!!.data!!.gameTypeName
                                    holder.tvBidPlacedProviderName.text =
                                        passBookDetails!!.data!!.providerName
                                    holder.tvBidPlacedDateTime.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy2(passBookDetails!!.data!!.createdAt)
                                    holder.tvBidPlacedPlayedFor.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(passBookDetails!!.data!!.gameDate)

                                    if (passBookDetails!!.data!!.winStatus == 0) {
                                        holder.tvBidPlacedStatus.setText("Bid Placed Successfully, Wait For The Result!!!")
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 1) {
                                        holder.tvBidPlacedStatus.setText("You Won Rs. " + passBookDetails!!.data!!.gameWinPoints.toString())
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 2) {
                                        holder.tvBidPlacedStatus.setText("Better Luck Next Time!!!")
                                    }
                                    if (passBookDetails.data!!.winStatus == 5) {
                                        holder.tvBidPlacedStatus.setText("Game Declined Payment Refunded!!!")
                                    }

                                }
                                1 -> {
// 1: Bid Status Won
                                    holder.contentBidWon.visibility = View.VISIBLE

                                    holder.tvBidWonType.text = passBookDetails!!.data!!.gameSession
                                    holder.tvBidWonGameName.text =
                                        passBookDetails!!.data!!.gameTypeName
                                    holder.tvBidWonProviderName.text =
                                        passBookDetails!!.data!!.providerName
                                    holder.tvBidWonDateTime.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy2(passBookDetails!!.data!!.createdAt)
                                    holder.tvBidWonPlayedFor.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(passBookDetails!!.data!!.gameDate)
                                    holder.tvBidWonWinRatio.text =
                                        "1*" + passBookDetails!!.data!!.gameTypePrice.toString()
                                    holder.tvBidWonBidAmount.text =
                                        passBookDetails!!.data!!.biddingPoints.toString() + "*" + passBookDetails!!.data!!.gameTypePrice.toString() + "=" + passBookDetails!!.data!!.gameWinPoints.toString()

                                    if (passBookDetails!!.data!!.winStatus == 0) {
                                        holder.tvBidWonStatus.setText("Bid Placed Successfully, Wait For The Result!!!")
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 1) {
                                        holder.tvBidWonStatus.setText("You Won Rs. " + passBookDetails!!.data!!.gameWinPoints.toString())
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 2) {
                                        holder.tvBidWonStatus.setText("Better Luck Next Time!!!")
                                    }
                                    if (passBookDetails.data!!.winStatus == 5) {
                                        holder.tvBidWonStatus.setText("Game Declined Payment Refunded!!!")
                                    }

                                }
                                2 -> {
                                    //2: Bid Status Loss,
                                    holder.contentBidPlaced.visibility = View.VISIBLE

                                    holder.tvBidPlacedType.text =
                                        passBookDetails!!.data!!.gameSession
                                    holder.tvBidPlacedGameName.text =
                                        passBookDetails!!.data!!.gameTypeName
                                    holder.tvBidPlacedProviderName.text =
                                        passBookDetails!!.data!!.providerName
                                    holder.tvBidPlacedDateTime.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy2(passBookDetails!!.data!!.createdAt)
                                    holder.tvBidPlacedPlayedFor.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(passBookDetails!!.data!!.gameDate)

                                    if (passBookDetails!!.data!!.winStatus == 0) {
                                        holder.tvBidPlacedStatus.setText("Bid Placed Successfully, Wait For The Result!!!")
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 1) {
                                        holder.tvBidPlacedStatus.setText("You Won Rs. " + passBookDetails!!.data!!.gameWinPoints.toString())
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 2) {
                                        holder.tvBidPlacedStatus.setText("Better Luck Next Time!!!")
                                    }
                                    if (passBookDetails.data!!.winStatus == 5) {
                                        holder.tvBidPlacedStatus.setText("Game Declined Payment Refunded!!!")
                                    }
                                }
                                3 -> {
                                    //3: Bid Status Refund,
                                    holder.contentBidPlaced.visibility = View.VISIBLE

                                    holder.tvBidPlacedType.text =
                                        passBookDetails!!.data!!.gameSession
                                    holder.tvBidPlacedGameName.text =
                                        passBookDetails!!.data!!.gameTypeName
                                    holder.tvBidPlacedProviderName.text =
                                        passBookDetails!!.data!!.providerName
                                    holder.tvBidPlacedDateTime.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy2(passBookDetails!!.data!!.createdAt)
                                    holder.tvBidPlacedPlayedFor.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(passBookDetails!!.data!!.gameDate)

                                    if (passBookDetails!!.data!!.winStatus == 0) {
                                        holder.tvBidPlacedStatus.setText("Bid Placed Successfully, Wait For The Result!!!")
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 1) {
                                        holder.tvBidPlacedStatus.setText("You Won Rs. " + passBookDetails!!.data!!.gameWinPoints.toString())
                                    }
                                    if (passBookDetails!!.data!!.winStatus == 2) {
                                        holder.tvBidPlacedStatus.setText("Better Luck Next Time!!!")
                                    }
                                    if (passBookDetails.data!!.winStatus == 5) {
                                        holder.tvBidPlacedStatus.setText("Game Declined Payment Refunded!!!")
                                    }

                                }
                                5 -> {
                                    //4: withraw by chat admin
                                    holder.contentCredit.visibility = View.VISIBLE

                                    holder.tvCreditFundMode.text =
                                        passBookDetails!!.data!!.withdrawalMode
                                    holder.tvCreditAmount.text =
                                        passBookDetails!!.data!!.reqAmount.toString()
                                    holder.tvCreditTransId.text = "--"

                                    holder.tvCreditDateTime.text =
                                        passBookDetails!!.data!!.reqDate + " " + passBookDetails!!.data!!.reqTime

                                    holder.tvCreditStatus.text = passBookDetails!!.data!!.reqStatus
                                }
                                4 -> {
                                    //4: Fund Credit,
                                    holder.contentCredit.visibility = View.VISIBLE

                                    holder.tvCreditFundMode.text =
                                        passBookDetails!!.data!!.withdrawalMode
                                    holder.tvCreditAmount.text =
                                        passBookDetails!!.data!!.reqAmount.toString()
                                    holder.tvCreditTransId.text = "--"

                                    holder.tvCreditDateTime.text =
                                        passBookDetails!!.data!!.reqDate + " " + passBookDetails!!.data!!.reqTime

                                    holder.tvCreditStatus.text = passBookDetails!!.data!!.reqStatus
                                }
                                9 -> {
                                    //5: Fund Debit,
                                    holder.content_bank.visibility = View.VISIBLE

                                    holder.tvAccountHolder.text =
                                        passBookDetails!!.data!!.toAccount!!.accName
                                    holder.tvAccountNo.text =
                                        passBookDetails!!.data!!.toAccount!!.accNumber
                                    holder.tvIFSC.text =
                                        passBookDetails!!.data!!.toAccount!!.ifscCode
                                    holder.tvBank.text =
                                        passBookDetails!!.data!!.toAccount!!.bankName

                                    holder.tvdatetime.text =
                                        passBookDetails!!.data!!.reqDate + " " + passBookDetails!!.data!!.reqTime

                                    holder.tvstatus.text = passBookDetails!!.data!!.reqStatus

                                }
                                6 -> {
                                    // 6 : Fund Declined,
                                    holder.content_bank.visibility = View.VISIBLE

                                    holder.tvAccountHolder.text =
                                        passBookDetails!!.data!!.toAccount!!.accName
                                    holder.tvAccountNo.text =
                                        passBookDetails!!.data!!.toAccount!!.accNumber
                                    holder.tvIFSC.text =
                                        passBookDetails!!.data!!.toAccount!!.ifscCode
                                    holder.tvBank.text =
                                        passBookDetails!!.data!!.toAccount!!.bankName

                                    holder.tvdatetime.text =
                                        passBookDetails!!.data!!.reqDate + " " + passBookDetails!!.data!!.reqTime

                                    holder.tvstatus.text = passBookDetails!!.data!!.reqStatus
                                }
                                7 -> {
                                    // 7: Account Update Charge
                                    holder.content_bank.visibility = View.VISIBLE

                                    holder.tvAccountHolder.text =
                                        passBookDetails!!.data!!.toAccount!!.accName
                                    holder.tvAccountNo.text =
                                        passBookDetails!!.data!!.toAccount!!.accNumber
                                    holder.tvIFSC.text =
                                        passBookDetails!!.data!!.toAccount!!.ifscCode
                                    holder.tvBank.text =
                                        passBookDetails!!.data!!.toAccount!!.bankName

                                    holder.tvdatetime.text =
                                        passBookDetails!!.data!!.reqDate + " " + passBookDetails!!.data!!.reqTime

                                    holder.tvstatus.text = passBookDetails!!.data!!.reqStatus
                                }
                                8 -> {
                                    //3: Bid Result Revert Refund,
                                    holder.contentBidPlaced.visibility = View.VISIBLE

                                    holder.tvBidPlacedType.text =
                                        passBookDetails!!.data!!.gameSession
                                    holder.tvBidPlacedGameName.text =
                                        passBookDetails!!.data!!.gameTypeName
                                    holder.tvBidPlacedProviderName.text =
                                        passBookDetails!!.data!!.providerName
                                    holder.tvBidPlacedDateTime.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy2(passBookDetails!!.data!!.createdAt)
                                    holder.tvBidPlacedPlayedFor.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(passBookDetails!!.data!!.gameDate)

                                    holder.tvBidPlacedStatus.setText("Amount Deducted For Result Changes")


                                }
                            }
                        } else {
                            onNoDataFound(holder, datum)
                        }
                    } else {
                        onNoDataFound(holder, datum)
                    }


                }

                override fun onResponseFailed(error: String?) {
                    holder!!.loading_spinner.visibility = View.GONE
                    Alerts.serverError(this@PassbookActivity, error.toString())
                }
            })


    }

    fun onRotateMobile(view: View) {
        val orientation = resources.configuration.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    fun onHelpClick(view: View) {

        spotLightEffect()
    }

}