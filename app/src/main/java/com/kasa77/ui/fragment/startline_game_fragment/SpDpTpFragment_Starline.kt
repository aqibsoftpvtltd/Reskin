package com.kasa77.ui.fragment.startline_game_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasa77.R
import com.kasa77.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa77.adapter.bid_list_submit.BidListToSubmitAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.game_bid_data.BidData
import com.kasa77.modal.kuber_starline.game_type.GameType
import com.kasa77.modal.kuber_starline.game_type.KsGameTypeModel
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.fragment.GameConstantMessages
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.OnSubmitBid
import com.kasa77.ui.fragment.OnSubmitBidManager
import com.kasa77.utils.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_sp_dp_tp.*
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList

class SpDpTpFragment_Starline : Fragment(), View.OnClickListener {

    private var singlePanaId = ""
    private var doublePanaId = ""
    private var triplePanaId = ""
    private var singleDigitId = ""
    private var singlePanaPrice = "0"
    private var doublePanaPrice = "0"
    private var triplePanaPrice = "0"
    private var singleDigitPrice = "0"
    private var singlePanaGame = ""
    private var doublePanaGame = ""
    private var triplePanaGame = ""
    private var singleDigitGame = ""

    private var singlePannaZeros =
        arrayOf("127", "136", "145", "190", "235", "280", "370", "389", "460", "479", "569", "578")
    private var singlePannaOnes =
        arrayOf("128", "137", "146", "236", "245", "290", "380", "470", "489", "560", "579", "678")
    private var singlePannaTwos =
        arrayOf("129", "138", "147", "156", "237", "246", "345", "390", "480", "570", "589", "679")
    private var singlePannaThrees =
        arrayOf("120", "139", "148", "157", "238", "247", "256", "346", "490", "580", "670", "689")
    private var singlePannaFours =
        arrayOf("130", "149", "158", "167", "239", "248", "257", "347", "356", "590", "680", "789")
    private var singlePannaFives =
        arrayOf("140", "159", "168", "230", "249", "258", "267", "348", "357", "456", "690", "780")
    private var singlePannaSixes =
        arrayOf("123", "150", "169", "178", "240", "259", "268", "349", "358", "367", "457", "790")
    private var singlePannaSevens =
        arrayOf("124", "160", "278", "179", "250", "269", "340", "359", "368", "458", "467", "890")
    private var singlePannaEights =
        arrayOf("125", "134", "170", "189", "260", "279", "350", "369", "468", "378", "459", "567")
    private var singlePannaNines =
        arrayOf("126", "135", "180", "234", "270", "289", "360", "379", "450", "469", "478", "568")

    private var doublePannaZeros =
        arrayOf("118", "226", "244", "299", "334", "488", "550", "668", "677")
    private var doublePannaOnes =
        arrayOf("100", "119", "155", "227", "335", "344", "399", "588", "669")
    private var doublePannaTwos =
        arrayOf("110", "200", "228", "255", "336", "499", "660", "688", "778")
    private var doublePannaThrees =
        arrayOf("166", "229", "300", "337", "355", "445", "599", "779", "788")
    private var doublePannaFours =
        arrayOf("112", "220", "266", "338", "400", "446", "455", "699", "770")
    private var doublePannaFives =
        arrayOf("113", "122", "177", "339", "366", "447", "500", "799", "889", "555")
    private var doublePannaSixes =
        arrayOf("600", "114", "277", "330", "448", "466", "556", "880", "899")
    private var doublePannaSevens =
        arrayOf("115", "133", "188", "223", "377", "449", "557", "566", "700")
    private var doublePannaEights =
        arrayOf("116", "224", "233", "288", "440", "477", "558", "800", "990")
    private var doublePannaNines =
        arrayOf("117", "144", "199", "225", "388", "559", "577", "667", "900")

    private var t0 = "000"
    private var t1 = "777"
    private var t2 = "444"
    private var t3 = "111"
    private var t4 = "888"
    private var t5 = "555"
    private var t6 = "222"
    private var t7 = "999"
    private var t8 = "666"
    private var t9 = "333"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sp_dp_tp, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from = requireArguments().getString("FROM")!!
        if (from == GameTypeNames.SP_DP_TP) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }

        initViews()

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.SP_DP_TP)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }

    }

    private fun createBid() {
        hideKeyboard()
        val stDigits: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

        if (stDigits.isEmpty()) {
            actDigits!!.error = "Please Bid Digit!!!"
            actDigits!!.requestFocus()
        } else if (stPoints.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        } else if (stPoints.startsWith("0")) {
            dialogBoxMessage("Please enter valid point", "")
        } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints.toInt()) {
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        } else if (strGameSession.isEmpty()) {
            tvGameSession!!.error=GameConstantMessages.SelectGameType
tvGameSession!!.requestFocus()
            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
        } else if (providerResultData!!.gameDate.isEmpty()) {
            dialogBoxMessage("Select Date", "cancel")
        } else {

            try {
                bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                rvBidList!!.adapter = bidAdapter
                bidAdapter!!.notifyDataSetChanged()
                generateSpMotor(stDigits, stPoints)
            } catch (e: Exception) {
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun generateSpMotor(stDigits: String, stPoints: String) {
        bidItems!!.clear()

        if (cbSp.isChecked || cbDp.isChecked || cbTp.isChecked) {

            when (stDigits) {
                "0" -> {

                    if (cbSp.isChecked) {
                        singlePannaZeros.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaZeros.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + gameTypeName)
                            bidItems!!.add(bidItem)
                        }

                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t0,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t0 + " : " + gameTypeName)
                        bidItems!!.add(bidItem)
                    }
                }
                "1" -> {

                    if (cbSp.isChecked) {
                        singlePannaOnes.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaOnes.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t1,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t1 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "2" -> {

                    if (cbSp.isChecked) {
                        singlePannaTwos.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaTwos.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t2,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t2 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "3" -> {

                    if (cbSp.isChecked) {
                        singlePannaThrees.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaThrees.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t3,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t3 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "4" -> {

                    if (cbSp.isChecked) {
                        singlePannaFours.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaFours.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t4,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t4 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "5" -> {

                    if (cbSp.isChecked) {
                        singlePannaFives.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaFives.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t5,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t5 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "6" -> {

                    if (cbSp.isChecked) {
                        singlePannaSixes.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaSixes.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t6,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t6 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "7" -> {

                    if (cbSp.isChecked) {
                        singlePannaSevens.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaSevens.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t7,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t7 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "8" -> {

                    if (cbSp.isChecked) {
                        singlePannaEights.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaEights.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t8,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t8 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
                "9" -> {

                    if (cbSp.isChecked) {
                        singlePannaNines.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                singlePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                singlePanaPrice,
                                singlePanaId
                            )
                            Log.e("BID : ", it + " : " + singlePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbDp.isChecked) {
                        doublePannaNines.forEach {
                            val bidItem = BidData(
                                it,
                                stPoints,
                                doublePanaGame,
                                strGameSession,
                                providerResultData!!.gameDate,
                                doublePanaPrice,
                                doublePanaId
                            )
                            Log.e("BID : ", it + " : " + doublePanaGame)
                            bidItems!!.add(bidItem)
                        }
                    }
                    if (cbTp.isChecked) {
                        val bidItem = BidData(
                            t9,
                            stPoints,
                            triplePanaGame,
                            strGameSession,
                            providerResultData!!.gameDate,
                            triplePanaPrice,
                            triplePanaId
                        )
                        Log.e("BID : ", t9 + " : " + triplePanaGame)
                        bidItems!!.add(bidItem)
                    }
                }
            }
            bidAdapter!!.notifyDataSetChanged()
            etPoints!!.setText("")
            actDigits!!.setText("")
            if (bidItems!!.size == 0) {
                bidAdapter!!.notifyDataSetChanged()
                etPoints!!.setText("")
                actDigits!!.setText("")
                dialogBoxMessage("Please enter valid digits", "")
            }
            if (bidItems!!.size > 0) {
                val bCnt = "${bidItems!!.size}"
                dbPnt = bCnt
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
        } else {
            Alerts.AlertDialogWarning(context, "Please Select SP, DP or TP CheckBox")
        }

    }


    private fun initViews() {
        tabAddBid = rootView!!.findViewById<FrameLayout>(R.id.tabAddBid)
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        actDigits = rootView!!.findViewById(R.id.actDigits)
        etPoints = rootView!!.findViewById(R.id.etPoints)
        rvBidList = rootView!!.findViewById(R.id.rvBidList)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        tabGameSession = rootView!!.findViewById(R.id.tabGameSession)
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameSession = rootView!!.findViewById(R.id.tvGameSession)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        tvPannaCount = rootView!!.findViewById(R.id.tvPannaCount)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.SinglePana

        tvGameDate!!.setText(DateFormatToDisplay().parseDateToddMMyyyy(providerResultData!!.gameDate))
        tvGameSession!!.setText(providerResultData!!.providerName)

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
                            Alerts.AlertDialogWarning(mContext, GameConstantMessages.MaxPoint)
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


    /*new code modifications */
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var from = ""
    private var providerResultData: com.kasa77.ui.fragment.startline_game_fragment.modal.Result? = null
    private var tabGameDate:RelativeLayout?=null
    private var tabGameSession:RelativeLayout?=null
    private var tvGameSession: TextView? = null
    private var tvGameDate: TextView? = null
    private var tvTotalBids: TextView? = null
    private var tvTotalPoints: TextView? = null
    private var tvPannaCount: TextView? = null
    private var strGameSession = "Open"
    private var bidItems: ArrayList<BidData>? = ArrayList()
    private var bidAdapter: BidListToSubmitAdapter? = null
    private var submitBidData: BidListFinalAdapter? = null
    private var actDigits: AutoCompleteTextView? = null
    private var etPoints: EditText? = null
    private var rvBidList: RecyclerView? = null
    private var gameId = ""
    private var gameTypeName = ""
    private var gameTypePrice = 0
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
            "Starline " + from + " " + providerResultData!!.providerName

        tvTotalBid.text = dbCnt
        tvTotalBidAmount.text = dbPnt
        tvBeforeWalletBalance.text = walletBal.toString()

        val pntDeduct = dbPnt.toInt()
        val afterDeductWallet = walletBal - pntDeduct
        tvWalletAfterDeduct.text = afterDeductWallet.toString()

        val walletBalDouble = AppPreference.getDoublePreference(mContext, Constant.USER_WALLET_BALANCE_FLOAT)
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
                OnSubmitBidManager().submitStarLineBids(
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
                                actDigits!!.setText("")
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

    private fun fetchStarlineGameId() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getKsGameTypeId(
                Dialog(requireActivity()),
                retrofitApiClient!!.kuberStarlineGameTypeId(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val ksgModel = result!!.body() as KsGameTypeModel
                        if (ksgModel.status == 1) {
                            if (ksgModel.gameTypes.size > 0) {
                                var gameTypeArray =
                                    ksgModel.gameTypes as ArrayList<GameType>
                                gameTypeArray.forEach {
                                    if (it.gameName.equals(GameTypeNames.SinglePana)) {
                                        singlePanaId = it.id
                                        singlePanaPrice = it.gamePrice.toString()
                                        singlePanaGame = it.gameName
                                    }
                                    if (it.gameName.equals(GameTypeNames.DoublePana)) {
                                        doublePanaId = it.id
                                        doublePanaPrice = it.gamePrice.toString()
                                        doublePanaGame = it.gameName

                                    }
                                    if (it.gameName.equals(GameTypeNames.TriplePana)) {
                                        triplePanaId = it.id
                                        triplePanaPrice = it.gamePrice.toString()
                                        triplePanaGame = it.gameName

                                    }
                                    if (it.gameName.equals(GameTypeNames.SingleDigit)) {
                                        singleDigitId = it.id
                                        singleDigitPrice = it.gamePrice.toString()
                                        singleDigitGame = it.gameName
                                    }
                                }
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

    private fun hideKeyboard() {
        try {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken, 2
            )
        } catch (e: Exception) {
        }
    }

    @SuppressLint("SetTextI18n")
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
