package com.kasa77.ui.fragment.jackpot_fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasa77.R
import com.kasa77.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa77.adapter.bid_list_submit.BidListToSubmitAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.game_bid_data.BidData
import com.kasa77.modal.game_date_list.GameDateList
import com.kasa77.modal.jackpot_modal.gametype_id.GameTypeIdList
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.fragment.GameConstantMessages
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.OnSubmitBid
import com.kasa77.ui.fragment.OnSubmitBidManager
import com.kasa77.utils.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.layout_bid_action_bottom_bar.*
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList

class DigitBasedJodiFragment_Jackpot : Fragment(), View.OnClickListener {

    private lateinit var gameTypeArray: java.util.ArrayList<com.kasa77.modal.jackpot_modal.gametype_id.GameType>
    private var isLeft = true

    private var jodiDigitPannaArray = arrayOf(
        "00",
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "60",
        "61",
        "62",
        "63",
        "64",
        "65",
        "66",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75",
        "76",
        "77",
        "78",
        "79",
        "80",
        "81",
        "82",
        "83",
        "84",
        "85",
        "86",
        "87",
        "88",
        "89",
        "90",
        "91",
        "92",
        "93",
        "94",
        "95",
        "96",
        "97",
        "98",
        "99"
    )


    private var etLeftDigit: EditText? = null
    private var etRightDigit: EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_digit_based_jodi, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        from = requireArguments().getString("From")!!

        if (from == GameTypeNames.DigitBasedJackpot) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }
        initViews()

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.DigitBasedJackpot)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }
        etLeftDigit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    etRightDigit!!.setText("")
                    isLeft = true
                }
            }
        })

        etRightDigit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    etLeftDigit!!.setText("")
                    isLeft = false
                }
            }
        })
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
                                    ksgModel.gameTypes as ArrayList<com.kasa77.modal.jackpot_modal.gametype_id.GameType>
                                gameTypeArray.forEach {
                                    if (it.gameName == "Jodi") {
                                        gameId = it.id
                                        gameTypePrice = it.gamePrice.toString()
                                        gameTypeName = it.gameName
                                    }
                                }
                                //   Alerts.show(mContext, "Size : ${gameTypeArray.size}")
                            }
                        } else {

                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun initViews() {
        etLeftDigit = rootView!!.findViewById(R.id.etLeftDigit)
        etRightDigit = rootView!!.findViewById(R.id.etRightDigit)
        tabAddBid = rootView!!.findViewById<FrameLayout>(R.id.tabAddBid)
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        etPoints = rootView!!.findViewById(R.id.etPoints)
        rvBidList = rootView!!.findViewById(R.id.rvBidList)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        etLeftDigit!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        etRightDigit!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))


        tvGameDate!!.setText(DateFormatToDisplay().parseDateToddMMyyyy(providerResultData!!.gameDate) + " (" + providerResultData!!.providerName + ")")

        tabAddBid!!.setOnClickListener {
            createBid()
        }

        etPoints!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    val tCP = p0.toString()
                    if (tCP.isNotEmpty()) {
                        if (tCP.toInt() > GameConstantMessages.MaxPointValue) {
                            Alerts.show(mContext, GameConstantMessages.MaxPoint)
                        }

                    }
                }
            }
        })

        etPoints!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createBid()
            }
            false
        }
    }


    @SuppressLint("SetTextI18n")
    private fun createBid() {
        hideKeyboard()
        bidItems!!.clear()
        bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
        rvBidList!!.layoutManager = LinearLayoutManager(mContext)
        rvBidList!!.adapter = bidAdapter
        bidAdapter!!.notifyDataSetChanged()
        if (isLeft) {
            val leftDigits: String = etLeftDigit!!.text.toString().trim()
            val stPoints: String = etPoints!!.text.toString().trim()
            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)
            if (leftDigits!!.isEmpty()) {
                etLeftDigit!!.error = "Please Bid Digit!!!"
                etLeftDigit!!.requestFocus()
            } else if (stPoints!!.isEmpty()) {
                etPoints!!.error = "Please Enter Point!!!"
                etPoints!!.requestFocus()
            } else if (stPoints!!.startsWith("0")) {
                dialogBoxMessage("Please enter valid point", "")
            } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
                dialogBoxMessage(GameConstantMessages.MinPoint, "")
            } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
                dialogBoxMessage(GameConstantMessages.MaxPoint, "")
            } else if (walletBal < stPoints.toInt()) {
                val message = "You don't have required bid amount please add fund."
                dialogBoxMessage(message, "cancel")
            } else if (strGameSession.isEmpty()) {
                Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
            } else if (providerResultData!!.gameDate.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            } else {
                try {

                    bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                    rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                    rvBidList!!.adapter = bidAdapter
                    bidAdapter!!.notifyDataSetChanged()
                    jodiDigitPannaArray.forEach {
                        if (it[0].toString() == leftDigits) {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                gameTypeName,
                                strGameSession,
                                providerResultData!!.gameDate,
                                gameTypePrice,
                                gameId
                            )
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (bidItems!!.size == 0) {
                        etLeftDigit!!.setText("")
                        etRightDigit!!.setText("")
                        etPoints!!.setText("")
                        dialogBoxMessage("Please enter valid digits", "Cancel")
                    }
                } catch (e: Exception) {
                }
            }


        } else {
            val rightDigits: String = etRightDigit!!.text.toString().trim()
            val stPoints: String = etPoints!!.text.toString().trim()
            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

            if (rightDigits!!.isEmpty()) {
                etRightDigit!!.error = "Please Bid Digit!!!"
                etRightDigit!!.requestFocus()
            } else if (stPoints!!.isEmpty()) {
                etPoints!!.error = "Please Enter Point!!!"
                etPoints!!.requestFocus()
            } else if (stPoints!!.startsWith("0")) {
                dialogBoxMessage("Please enter valid point", "")
            } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
                dialogBoxMessage(GameConstantMessages.MinPoint, "")
            } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
                dialogBoxMessage(GameConstantMessages.MaxPoint, "")
            } else if (walletBal < stPoints.toInt()) {
                val message = "You don't have required bid amount please add fund."
                dialogBoxMessage(message, "cancel")
            } else if (strGameSession.isEmpty()) {
                Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
            } else if (providerResultData!!.gameDate.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            } else {
                try {

                    bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                    rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                    rvBidList!!.adapter = bidAdapter
                    bidAdapter!!.notifyDataSetChanged()

                    jodiDigitPannaArray.forEach {
                        if (it[1].toString() == rightDigits) {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                gameTypeName,
                                strGameSession,
                                providerResultData!!.gameDate,
                                gameTypePrice,
                                gameId
                            )
                            bidItems!!.add(bidItem)
                        }
                    }
                    etLeftDigit!!.setText("")
                    etRightDigit!!.setText("")
                    etPoints!!.setText("")
                    if (bidItems!!.size == 0) {
                        etLeftDigit!!.setText("")
                        etRightDigit!!.setText("")
                        etPoints!!.setText("")
                        dialogBoxMessage("Please enter valid digits", "Cancel")
                    }
                } catch (e: Exception) {
                }
            }

        }

        if (bidItems!!.size > 0) {
            val bCnt = "${bidItems!!.size}"
            val count: Int = bidItems!!.size
            dbCnt = count.toString()

            var bdPnt = 0
            bidItems!!.forEach {
                bdPnt += (it.points.toInt())
            }
            val bPnt = "$bdPnt"
            dbPnt = bdPnt.toString()

            tvTotalBids!!.text = "${bCnt}"
            tvTotalPoints!!.text = "${bPnt}"
        } else {
            tvTotalBids!!.text = ""
            tvTotalPoints!!.text = ""
        }
    }

    /*new code modifications */
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var from = ""
    private var gameDateList: GameDateList? = null
    private var providerResultData: com.kasa77.ui.fragment.jackpot_fragments.modal.Result? = null
    private var tabGameDate: RelativeLayout?=null
    private var tvGameDate: TextView? = null
    private var tvTotalBids: TextView? = null
    private var tvTotalPoints: TextView? = null
    private var strGameSession = "Jodi"
    private var bidItems: ArrayList<BidData>? = ArrayList()
    private var bidAdapter: BidListToSubmitAdapter? = null
    private var submitBidData: BidListFinalAdapter? = null
    private var actDigits: AutoCompleteTextView? = null
    private var etPoints: EditText? = null
    private var rvBidList: RecyclerView? = null
    private var gameId = ""
    private var gameTypeName = ""
    private var gameTypePrice = "0"
    private var tvFinalSubmit: TextView? = null
    private var tabAddBid: FrameLayout? = null
    private var rootView: View? = null
    private var dbCnt = ""
    private var dbPnt = ""


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
        tvTotalBid.text = dbCnt
        tvTotalBidAmount.text = dbPnt
        tvBeforeWalletBalance.text = walletBal.toString()

        val pntDeduct = dbPnt.toInt()
        val afterDeductWallet = walletBal - pntDeduct
        tvWalletAfterDeduct.text = afterDeductWallet.toString()

        val walletBalDouble =
            AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
        tvBeforeWalletBalance.text = walletBalDouble.toString()
        val pntDeduct1 = dbPnt.toInt()
        val afterDeductWallet1 = walletBalDouble - pntDeduct1
        tvWalletAfterDeduct.text = afterDeductWallet1.toString()

        val rvBidData = rvFinalBidListSingleDigit
        submitBidData = BidListFinalAdapter(mContext, bidItems)
        rvBidData!!.layoutManager = LinearLayoutManager(mContext)
        rvBidData.adapter = submitBidData
        submitBidData!!.notifyDataSetChanged()


        tabSubmit.setOnClickListener {
            tabSubmit.isEnabled = false
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
                        bidItems!!,
                        providerResultData!!,
                        providerResultData!!.gameDate,
                        strGameSession, object :
                            OnSubmitBid {
                            override fun onSuccess(response: String?) {
                                val responseObject = JSONObject(response)
                                if (responseObject.getInt("status") == 1) {
                                    etLeftDigit!!.setText("")
                                    etRightDigit!!.setText("")
                                    etPoints!!.setText("")
                                    bidItems!!.clear()
                                    tvTotalBids!!.text = ""
                                    tvTotalPoints!!.text = ""
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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDelete -> {
                val tag: Int = v.tag as Int
                bidItems!!.removeAt(tag)
                bidAdapter!!.notifyDataSetChanged()
                if (bidItems!!.size > 0) {
                    val bCnt = "${bidItems!!.size}"
                    val count: Int = bidItems!!.size
                    dbCnt = count.toString()

                    var bdPnt = 0
                    bidItems!!.forEach {
                        bdPnt += (it.points.toInt())
                    }
                    val bPnt = "$bdPnt"
                    dbPnt = bdPnt.toString()

                    tvTotalBids!!.text = "${bCnt}"
                    tvTotalPoints!!.text = "${bPnt}"
                } else {
                    tvTotalBids!!.text = ""
                    tvTotalPoints!!.text = ""
                }
            }


        }
    }


}
