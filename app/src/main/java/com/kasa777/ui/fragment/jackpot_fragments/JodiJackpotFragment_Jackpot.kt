package com.kasa777.ui.fragment.jackpot_fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasa777.R
import com.kasa777.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa777.adapter.jackpot_adapter.JodiJackpotBidAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.game_bid_data.BidData
import com.kasa777.modal.jackpot_modal.gametype_id.GameTypeIdList
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.fragment.GameConstantMessages
import com.kasa777.ui.fragment.GameTypeNames
import com.kasa777.ui.fragment.OnSubmitBid
import com.kasa777.ui.fragment.OnSubmitBidManager
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import org.json.JSONObject
import retrofit2.Response


class JodiJackpotFragment_Jackpot : Fragment(), ItemOnclickListener,OnItemTextChangedListener  {


    private var tvGameProvider: TextView? = null
    private var rvJodiJackpot: RecyclerView? = null
    private var finalBidItem: ArrayList<BidData>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_jodi_jackpot, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from = requireArguments().getString("From")!!

        if (from == GameTypeNames.JodiJackpot) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }
        initViews()




        rvJodiJackpot = rootView!!.findViewById(R.id.rvJodiJackpot)


    }

    private fun addListBids() {
        for (x in 0 until 100) {
            var digit = x.toString()
            if (digit.length == 1) {
                digit = "0$digit"
                bidItems?.add(
                    BidData(
                        digit,
                        "",
                        gameTypeName,
                        strGameSession,
                        providerResultData!!.gameDate,
                        gameTypePrice,
                        gameId
                    )
                )
            } else {
                bidItems?.add(
                    BidData(
                        digit,
                        "",
                        gameTypeName,
                        strGameSession,
                        providerResultData!!.gameDate,
                        gameTypePrice,
                        gameId
                    )
                )
            }

        }

    }

    private fun initViews() {
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        tvGameProvider = rootView!!.findViewById(R.id.tvGameSession)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameDate!!.setText(DateFormatToDisplay().parseDateToddMMyyyy(providerResultData!!.gameDate) + " (" + providerResultData!!.providerName + ")")
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)



        tvFinalSubmit!!.setOnClickListener {
            finalBidItem!!.clear()

            var totalPoints = 0
            var totalbids = 0

            var isAllBidValid = true
            var currentpos = 0
            var invalidIndex = 0
            bidItems!!.forEach {
                if (it.points.isNotEmpty()) {

                    //check for bid point
                    if (it.points.toString().startsWith("0")) {
                        isAllBidValid = false
                        invalidIndex = currentpos
//                        dialogBoxMessage("Please enter valid point", "")
                    } else if (it.points.toInt() < GameConstantMessages.MinPointValue) {
                        isAllBidValid = false
                        invalidIndex = currentpos
//                        dialogBoxMessage(GameConstantMessages.MinPoint, "")
                    } else if (it.points.toInt() > GameConstantMessages.MaxPointValue) {
                        isAllBidValid = false
                        invalidIndex = currentpos
//                        dialogBoxMessage(GameConstantMessages.MaxPoint, "")
                    }


                }
                currentpos++
                if (currentpos == bidItems!!.size) {
                    if (isAllBidValid) {
                        var currentposnew = 0
                        bidItems!!.forEach {
                            if (it.points.isNotEmpty()) {
                                //if all correct
                                totalPoints += it.points.toInt()
                                totalbids++
                                finalBidItem!!.add(
                                    BidData(
                                        it.digits,
                                        it.points,
                                        it.gameType,
                                        strGameSession,
                                        providerResultData!!.gameDate,
                                        it.gameTypePrice,
                                        gameId
                                    )
                                )


                            }
                            currentposnew++
                            if (currentposnew == bidItems!!.size) {
                                dbPnt == totalPoints.toString()
                                dbCnt == totalbids.toString()
                                val walletAmount =
                                    AppPreference.getIntegerPreference(
                                        mContext,
                                        Constant.USER_WALLET_BALANCE
                                    )
                                if (totalPoints > 0) {
                                    if (walletAmount >= totalPoints) {
                                        dialogBitSubmit(GameTypeNames.JodiJackpot)
                                    } else {
                                        dialogBoxMessage("Insufficiant wallet balance", "cancel")
                                    }
                                } else {
                                    val message = "Please add a bid first!!!"
                                    dialogBoxMessage(message, "cancel")
                                }
                            }

                        }
                    } else {
                        if (rvJodiJackpot!!.findViewHolderForLayoutPosition(invalidIndex) is JodiJackpotBidAdapter.ViewHolder) {
                            var childHolder =
                                rvJodiJackpot!!.findViewHolderForLayoutPosition(invalidIndex) as JodiJackpotBidAdapter.ViewHolder

                            childHolder.etPoints.setError("Please enter valid point.\n" + " Min "+GameConstantMessages.MinPointValue+" & Max "+GameConstantMessages.MaxPointValue)
                        }
                        dialogBoxMessage("Please enter valid point.\n" + " Min "+GameConstantMessages.MinPointValue+" & Max "+GameConstantMessages.MaxPointValue, "")
                    }

                }

            }


        }


    }


    private fun fetchStarlineGameId() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getJackpotGameTypeId(
                Dialog(requireActivity()),
                retrofitApiClient!!.kuberJackpotGameTypeId(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val ksgModel = result!!.body() as GameTypeIdList
                        if (ksgModel.status == 1) {
                            if (ksgModel.gameTypes.size > 0) {
                                gameTypeArray =
                                    ksgModel.gameTypes as ArrayList<com.kasa777.modal.jackpot_modal.gametype_id.GameType>
                                gameTypeArray.forEach {
                                    if (it.gameName == "Jodi") {
                                        gameId = it.id
                                        gameTypePrice = it.gamePrice.toString()
                                        gameTypeName = it.gameName

                                        addListBids()
                                        bidAdapter = JodiJackpotBidAdapter(
                                            mContext,
                                            bidItems,
                                            this@JodiJackpotFragment_Jackpot,
                                            this@JodiJackpotFragment_Jackpot,
                                            activity
                                        )
                                        rvJodiJackpot!!.layoutManager =
                                            LinearLayoutManager(mContext)
                                        rvJodiJackpot!!.adapter = bidAdapter
                                        bidAdapter!!.notifyDataSetChanged()

                                    }
                                }
                                //     Alerts.show(mContext, "Size : ${gameTypeArray.size}")
                            }
                        } else {
                            Alerts.AlertDialogWarning(mContext, ksgModel.message)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }


    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var from = ""

    private var providerResultData: com.kasa777.ui.fragment.jackpot_fragments.modal.Result? = null
    private var tabGameDate: RelativeLayout?=null
    private var tvGameDate: TextView? = null
    private var tvTotalBids: TextView? = null
    private var tvTotalPoints: TextView? = null
    private var strGameSession = "Jodi"
    private var bidItems: ArrayList<BidData>? = ArrayList()
    private var bidAdapter: JodiJackpotBidAdapter? = null
    private var submitBidData: BidListFinalAdapter? = null
    private var actDigits: AutoCompleteTextView? = null
    private var etPoints: EditText? = null
    private var rvBidList: RecyclerView? = null
    private var gameId = ""
    private var gameTypeName = ""
    private var gameTypePrice = "0"
    private var tvFinalSubmit: TextView? = null
    private var rootView: View? = null
    private var dbCnt = ""
    private var dbPnt = ""
    private lateinit var gameTypeArray: java.util.ArrayList<com.kasa777.modal.jackpot_modal.gametype_id.GameType>


    private fun resetBidTableOnChangeDateOrSession() {
        if (bidItems != null) {
            bidItems!!.clear();
            if (bidAdapter != null)
                bidAdapter!!.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dialogBitSubmit(s: String) {

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_final_bid_summary)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );

        val tvCurrentTime = dialog.findViewById<TextView>(R.id.tvCurrentTime)
        val rvFinalBidListSingleDigit =
            dialog.findViewById<RecyclerView>(R.id.rvFinalBidListSingleDigit)
        val tvTotalBid = dialog.findViewById<TextView>(R.id.tvTotalBid)
        val tvTotalBidAmount = dialog.findViewById<TextView>(R.id.tvTotalBidAmount)
        val tvBeforeWalletBalance = dialog.findViewById<TextView>(R.id.tvBeforeWalletBalance)
        val tvWalletAfterDeduct = dialog.findViewById<TextView>(R.id.tvWalletAfterDeduct)
        val tabSubmit = dialog.findViewById<RelativeLayout>(R.id.tabSubmit)
        val tabCancel = dialog.findViewById<RelativeLayout>(R.id.tabCancel)
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)
        tvCurrentTime.text =
            "Jackpot " + providerResultData!!.providerName + " " + DateFormatToDisplay().parseDateToddMMyyyy(
                providerResultData!!.gameDate
            )


        if (bidItems!!.size > 0) {
            var totalPoints = 0
            var totalbids = 0
            bidItems!!.forEach {
                if (it.points.isNotEmpty()) {
                    totalPoints += it.points.toInt()
                    totalbids++
                }

            }
//            dbPnt == totalPoints.toString()
//            dbCnt == totalbids.toString()

            tvTotalBid.text = totalbids.toString()
            tvTotalBidAmount.text = totalPoints.toString()
            tvBeforeWalletBalance.text = walletBal.toString()

            val pntDeduct = totalPoints.toInt()
            val afterDeductWallet = walletBal - pntDeduct
            tvWalletAfterDeduct.text = afterDeductWallet.toString()

            val walletBalDouble = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
            tvBeforeWalletBalance.text = walletBalDouble.toString()
            val pntDeduct1 = totalPoints
            val afterDeductWallet1 = walletBalDouble - pntDeduct1
            tvWalletAfterDeduct.text = afterDeductWallet1.toString()

            val rvBidData = rvFinalBidListSingleDigit
            submitBidData = BidListFinalAdapter(mContext, finalBidItem)
            rvBidData!!.layoutManager = LinearLayoutManager(mContext)
            rvBidData.adapter = submitBidData
            submitBidData!!.notifyDataSetChanged()


            tabSubmit.setOnClickListener {
                tabSubmit.isEnabled =false
                if (walletBal < pntDeduct) {
                    dialog.dismiss()
                    val message = "You don't have required bid amount please add fund."
                    dialogBoxMessage(message, "cancel")
                } else {
                    OnSubmitBidManager()
                        .submitJackportBids(
                            requireContext(),
                            retrofitApiClient!!,
                            cd!!,
                            finalBidItem!!,
                            providerResultData!!,
                            providerResultData!!.gameDate,
                            strGameSession, object :
                                OnSubmitBid {
                                override fun onSuccess(response: String?) {
                                    val responseObject = JSONObject(response)
                                    if (responseObject.getInt("status") == 1) {

                                        bidItems!!.clear()
                                        finalBidItem!!.clear()
                                        addListBids()
                                        bidAdapter = JodiJackpotBidAdapter(
                                            mContext,
                                            bidItems,
                                            this@JodiJackpotFragment_Jackpot,
                                            this@JodiJackpotFragment_Jackpot,
                                            activity
                                        )
                                        rvJodiJackpot!!.layoutManager =
                                            LinearLayoutManager(mContext)
                                        rvJodiJackpot!!.adapter = bidAdapter
                                        bidAdapter!!.notifyDataSetChanged()

                                        val wallet = responseObject.getDouble("updatedWalletBal")
                                        AppPreference.setIntegerPreference(
                                            mContext,
                                            Constant.USER_WALLET_BALANCE,
                                            wallet.toInt()
                                        )
                                        AppPreference.setDoublePreference(
                                            mContext,
                                            Constant.USER_WALLET_BALANCE_FLOAT,
                                            wallet
                                        )
                                        dialog.dismiss()
                                        Alerts.AlertDialogSuccessAutoClose(
                                            context,
                                            activity,
                                            responseObject.getString("message")
                                        )
                                    } else {
                                        Alerts.AlertDialogWarning(
                                            context,
                                            responseObject.getString("message")
                                        )
                                    }
                                }

                                override fun onFail(response: String?) {
                                    Alerts.AlertDialogWarning(context, response)
                                }
                            }
                        )
                }
            }
            tabCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }


    private fun dialogBoxMessage(string: String, s: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_view_toast_message, null)
        dialogBuilder?.setView(dialogView)
        if (s == "submit") {
            dialogBuilder?.setCancelable(false)
        } else {
            dialogBuilder?.setCancelable(true)
        }
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogView.txtMessage.text = string
        val btnSubmit = dialogView.btnOk
        btnSubmit.setOnClickListener {
            if (s == "submit") {
                alertDialog.dismiss()
                requireActivity().finish()
            } else {
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }


    private fun hideKeyboard() {
        try {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken, 2
            )
        } catch (e: Exception) {
        }
    }

    override fun onItemClicked(v: View, position: Int) {
        when (v?.id) {
            R.id.btnDelete -> {
                val tag: Int = position

                bidItems?.let {
                    if (tag >= 0 && tag < it.size) {
                        it[tag].setPoints("");

                        // Update the points total by calculating the sum of all items' points
                        var bdPnt = 0
                        for (item in it) {
                            bdPnt += if (item.points.isEmpty()) 0 else item.points.toInt()
                        }

                        // Update the TextViews displaying the total bids and total points
                        val nonEmptyBidItems = bidItems!!.filter { it.points.isNotEmpty() }
                        val bCnt = nonEmptyBidItems.size
                        val bPnt = "$bdPnt"
                        dbCnt = it.size.toString()
                        dbPnt = bdPnt.toString()
                        tvTotalBids?.text = bCnt.toString()
                        tvTotalPoints?.text = bPnt
                        bidAdapter?.notifyDataSetChanged()
                        // Notify the adapter that an item has been removed
//                        bidAdapter?.notifyItemRemoved(tag)
//
//                        // Manually update the positions of remaining items in the RecyclerView
//                        for (i in tag until it.size) {
//                            bidAdapter?.notifyItemChanged(i)
//                        }
                    }
                }
            }
        }
    }



    override fun onItemTextChanged(position: Int, text: CharSequence?)  {
        if (bidItems!!.size > 0) {

            val count: Int = bidItems!!.size
            dbCnt = count.toString()
            bidItems!![position].setPoints(text.toString())
            val nonEmptyBidItems = bidItems!!.filter { it.points.isNotEmpty() }
            val bCnt = nonEmptyBidItems.size
            var bdPnt = 0
            bidItems!!.forEach {
                bdPnt += (if (it.points.isEmpty()) 0 else it.points.toInt())
            }
            val bPnt = "$bdPnt"
            dbPnt = bdPnt.toString()

            tvTotalBids!!.text = "${bCnt}"
            tvTotalPoints!!.text = "${bPnt}"
        } else {
            tvTotalBids!!.text = ""
            tvTotalPoints!!.text = ""
        }
//            bidAdapter!!.notifyDataSetChanged()
    }


}

