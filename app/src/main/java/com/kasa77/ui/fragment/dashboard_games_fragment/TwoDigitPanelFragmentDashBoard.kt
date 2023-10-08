package com.kasa77.ui.fragment.dashboard_games_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kasa77.R
import com.kasa77.adapter.GameDateAdapter
import com.kasa77.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa77.adapter.bid_list_submit.BidListToSubmitAdapter
import com.kasa77.constant.Constant
import com.kasa77.modal.dashboard_gamelist.Result
import com.kasa77.modal.game_bid_data.BidData
import com.kasa77.modal.game_date_list.DateObject
import com.kasa77.modal.game_date_list.GameDateList
import com.kasa77.modal.game_session_list.GameSession
import com.kasa77.modal.kuber_dashboard_games.GameType
import com.kasa77.modal.kuber_dashboard_games.GameTypeDashboardModal
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.ui.fragment.GameConstantMessages
import com.kasa77.ui.fragment.GameTypeNames
import com.kasa77.ui.fragment.OnSubmitBid
import com.kasa77.ui.fragment.OnSubmitBidManager
import com.kasa77.utils.*
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TwoDigitPanelFragmentDashBoard : Fragment(), View.OnClickListener {

    private var singlePannaArray = arrayOf(
        "127", "128", "129", "120", "130", "140", "123", "124",
        "125", "126", "136", "137", "138", "139", "149", "159", "150", "160", "134", "135", "145",
        "146", "147", "148", "158", "168", "169", "179", "170", "180", "190", "236", "156", "157",
        "167", "230", "178", "250", "189", "234", "235", "245", "237", "238", "239", "249", "240",
        "269", "260", "270", "280", "290", "246", "247", "248", "258", "259", "278", "279", "289",
        "370", "380", "345", "256", "257", "267", "268", "340", "350", "360", "389", "470", "390",
        "346", "347", "348", "349", "359", "369", "379", "460", "489", "480", "490", "356", "357",
        "358", "368", "378", "450", "479", "560", "570", "580", "590", "456", "367", "458", "459",
        "469", "569", "579", "589", "670", "680", "690", "457", "467", "468", "478", "578", "678",
        "679", "689", "789", "780", "790", "890", "567", "568"
    )
    private var doublePannaArray = arrayOf(
        "118", "100", "110", "166", "112", "113", "114", "115",
        "116", "117", "226", "119", "200", "229", "220", "122", "277", "133", "224", "144", "244",
        "155", "228", "300", "266", "177", "330", "188", "233", "199", "299", "227", "255", "337",
        "338", "339", "448", "223", "288", "225", "334", "335", "336", "355", "400", "366", "466",
        "377", "440", "388", "488", "344", "499", "445", "446", "447", "556", "449", "477", "559",
        "550", "399", "660", "599", "455", "500", "600", "557", "558", "577", "668", "588", "688",
        "779", "699", "799", "880", "566", "800", "667", "677", "669", "778", "788", "770", "889",
        "899", "700", "990", "900"
    )
    private var triplePannaArray = arrayOf(
        "000", "111", "222", "333", "444", "555", "666", "777", "888", "999"

    )
    private var singlePanaId = ""
    private var doublePanaId = ""
    private var triplePanaId = ""

    private var singlePanaPrice = "0"
    private var doublePanaPrice = "0"
    private var triplePanaPrice = "0"

    private var singlePanaGame = ""
    private var doublePanaGame = ""
    private var triplePanaGame = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_two_digit_panel, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        from = requireArguments().getString("From")!!

        if (from == "KuberDashboard") {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
            getGameDates()
        }

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.TDP)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }


    }


    @SuppressLint("SetTextI18n")
    private fun initViews() {
        tabAddBid = rootView!!.findViewById(R.id.tabAddBid)
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        actDigits = rootView!!.findViewById(R.id.actDigits)
        etPoints = rootView!!.findViewById(R.id.etPoints)
        rvBidList = rootView!!.findViewById(R.id.rvBidList)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        tabGameSession = rootView!!.findViewById(R.id.tabGameSession)
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameSession = rootView!!.findViewById(R.id.tvGameSession)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        actDigits!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
        tabGameSession!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (selectedDateObject != null)
                    getGameSession()
            }
        })

        tabGameDate!!.setOnClickListener(View.OnClickListener {
            dialogSelectBidInterval()
        })

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
/*
        val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.TDP*/


    }


    private fun createBid() {
        hideKeyboard()
        val stDigits: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

        if (stDigits.isEmpty()) {
            actDigits!!.error = "Please Enter Bid Digits!!!"
            actDigits!!.requestFocus()
        } else if (stPoints.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "cancel")
        } else if (stPoints.startsWith("0")) {
            dialogBoxMessage("Please Enter Valid Point", "cancel")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "cancel")
        } else if (walletBal < stPoints.toInt()) {
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        } else if (strGameSession.isEmpty()) {
            tvGameSession!!.error = GameConstantMessages.SelectGameType
//            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
            spotLightEffect()
        } else if (selectedDateObject!!.date.isEmpty()) {
            dialogBoxMessage("Please Select date", "cancel")

        } else {

            if (stDigits.length < 2) {
                actDigits!!.error = "Please Enter Two Digits!!!"
                actDigits!!.requestFocus()
            } else {
                val digits = stDigits.toCharArray()
                val firstDigit = Integer.parseInt(digits[0].toString())
                val secontDigit = Integer.parseInt(digits[1].toString())

                if (firstDigit >= 0 && secontDigit == 0) {
                    try {
                        generateSpMotor(stDigits, stPoints)
                    } catch (e: Exception) {
                    }
                } else {
                    if (firstDigit > secontDigit) {
                        actDigits!!.error = "Please Enter Valid Digits!!!"
                        actDigits!!.requestFocus()
                    } else if (firstDigit == 0) {
                        actDigits!!.error = "Please Enter Valid Digits!!!"
                        actDigits!!.requestFocus()
                    } else {
                        try {
                            generateSpMotor(stDigits, stPoints)
                        } catch (e: Exception) {
                        }
                    }
                }

            }
        }


    }


//    @SuppressLint("SetTextI18n")
//    private fun generateSpMotor(stDigits: String, stPoints: String) {
//        bidItems!!.clear()
//
//        singlePannaArray.forEach {
//            if (it.contains(stDigits)) {
//                val bidItem = BidData(
//                    it,
//                    stPoints,
//                    singlePanaGame,
//                    strGameSession,
//                    selectedDateObject!!.date,
//                    singlePanaPrice,
//                    singlePanaId
//                )
//                bidItems!!.add(bidItem)
//            }
//        }
//
//        doublePannaArray.forEach {
//            if (it.contains(stDigits)) {
//                val bidItem = BidData(
//                    it,
//                    stPoints,
//                    doublePanaGame,
//                    strGameSession,
//                    selectedDateObject!!.date,
//                    doublePanaPrice,
//                    doublePanaId
//                )
//                bidItems!!.add(bidItem)
//            }
//        }
//
//        if (bidItems!!.isEmpty()) {
//            actDigits!!.setText("")
//            etPoints!!.setText("")
//            dialogBoxMessage("Please Enter Valid Digit","Cancel")
//        }
//
//        bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
//        rvBidList!!.layoutManager = LinearLayoutManager(mContext)
//        rvBidList!!.adapter = bidAdapter
//        bidAdapter!!.notifyDataSetChanged()
//
//
//        etPoints!!.setText("")
//        actDigits!!.setText("")
//
//        if (bidItems!!.size > 0) {
//            val bCnt = "${bidItems!!.size}"
//
//            val count: Int = bidItems!!.size
//            dbCnt = count.toString()
//
//            var bdPnt = 0
//            bidItems!!.forEach {
//                bdPnt += (it.points.toInt())
//            }
//            val bPnt = "$bdPnt"
//            dbPnt = bdPnt.toString()
//
//            tvTotalBids!!.text = "${bCnt}"
//            tvTotalPoints!!.text = "${bPnt}"
//        } else {
//             tvTotalBids!!.text = ""
//             tvTotalPoints!!.text = ""
//        }
//
//    }

    @SuppressLint("SetTextI18n")
    private fun generateSpMotor(stDigits: String, stPoints: String) {
        bidItems!!.clear()
        val digitArray = stDigits.toCharArray()
        val totalDigits = stDigits.length
        val firstDigit = digitArray[0]
        val secontDigit = digitArray[1]

        val firstDigitInt = Integer.parseInt(digitArray[0].toString())
        val secontDigitInt = Integer.parseInt(digitArray[1].toString())


        val difference =
            Integer.parseInt(digitArray[1].toString()) - Integer.parseInt(digitArray[0].toString())

        //add 3rd digit
        val fixedfirstTwo = firstDigit + "" + secontDigit
        for (i in 0..9) {
            if (i >= secontDigitInt || i == 0) {
                val digit = fixedfirstTwo + "" + i
                if (singlePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        singlePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        singlePanaPrice,
                        singlePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (doublePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        doublePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        doublePanaPrice,
                        doublePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (triplePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        triplePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        triplePanaPrice,
                        triplePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
            }
        }

        if (firstDigitInt == 0 && secontDigitInt == 0) {
            for (j in 0..9) {
                val digit = "" + j + "" + fixedfirstTwo
                if (singlePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        singlePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        singlePanaPrice,
                        singlePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (doublePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        doublePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        doublePanaPrice,
                        doublePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (triplePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        triplePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        triplePanaPrice,
                        triplePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
            }
        }
        //add 1rd digit
        for (j in 1..9) {
            if (j <= firstDigitInt) {
                val digit = "" + j + "" + fixedfirstTwo
                if (singlePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        singlePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        singlePanaPrice,
                        singlePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (doublePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        doublePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        doublePanaPrice,
                        doublePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (triplePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        triplePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        triplePanaPrice,
                        triplePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }

            }
        }

        for (k in 0..9) {
            val middleNumber = firstDigitInt + k + 1
            if (firstDigitInt <= middleNumber) {
                val digit = "" + firstDigitInt + "" + middleNumber + "" + secontDigitInt
                if (singlePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        singlePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        singlePanaPrice,
                        singlePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (doublePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        doublePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        doublePanaPrice,
                        doublePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
                if (triplePannaArray.contains(digit)) {
                    val bidItem = BidData(
                        digit,
                        stPoints,
                        triplePanaGame,
                        strGameSession,
                        selectedDateObject!!.date,
                        triplePanaPrice,
                        triplePanaId
                    )
                    Log.e("BID : ", bidItem.digits + " : " + bidItem.gameType)
                    bidItems!!.add(bidItem)
                }
            }
        }


        val newList = bidItems!!.distinctBy { it.digits }
        bidItems!!.clear()
        bidItems!!.addAll(newList)


        etPoints!!.setText("")
        actDigits!!.setText("")

        if (bidItems!!.isEmpty()) {
            bidAdapter!!.notifyDataSetChanged()
            dialogBoxMessage("Please enter valid digits", "Cancel")
        }

        bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
        rvBidList!!.layoutManager = LinearLayoutManager(mContext)
        rvBidList!!.adapter = bidAdapter
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

    /*new code modifications */
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var from = ""
    private var gameSession: GameSession? = null
    private var selectedDateObject: DateObject? = null
    private var gameDateList: GameDateList? = null
    private var providerResultData: Result? = null
    private var tabGameDate:RelativeLayout?=null
    private var tabGameSession:RelativeLayout?=null
    private var tvGameSession: TextView? = null
    private var tvGameDate: TextView? = null
    private var tvTotalBids: TextView? = null
    private var tvTotalPoints: TextView? = null
    private var strGameSession = ""
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
    private var tabAddBid: TextView? = null
    private var rootView: View? = null
    private var dbCnt = ""
    private var dbPnt = ""


    private fun getGameSessionAutoFill() {
        val finalObject = JSONObject()
        finalObject.put("providerId", providerResultData!!.providerId)
        finalObject.put("gameDay", selectedDateObject!!.dayname)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient!!.getDaySession(body),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        if (jsonresponse.optInt("status") == 1) {
                            gameSession = Gson().fromJson(
                                jsonresponse.toString(),
                                GameSession::class.java
                            )
                            if (gameSession != null) {
                                resetBidTableOnChangeDateOrSession()
                                val openBidData = gameSession!!.daySession.openSession
                                val closeBidData = gameSession!!.daySession.closeSession
                                if (openBidData.equals("open", true) && closeBidData.equals(
                                        "open",
                                        true
                                    )
                                ) {

                                    strGameSession = "Open"
                                    tvGameSession!!.text =
                                        providerResultData!!.providerName + " " + strGameSession
                                }
                                if (openBidData.equals("close", true) && closeBidData.equals(
                                        "open",
                                        true
                                    )
                                ) {

//                                    strGameSession = "Close"
//                                    tvGameSession!!.text =
//                                        providerResultData!!.providerName + " " + strGameSession

                                }

                            }
                        } else {
                            Alerts.show(context, jsonresponse.optString("message"))
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun getGameDates() {
        val finalObject = JSONObject()
        finalObject.put("providerId", providerResultData!!.providerId)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient!!.getDayGameBids(body),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        if (jsonresponse.optInt("status") == 1) {
                            gameDateList = Gson().fromJson(
                                jsonresponse.toString(),
                                GameDateList::class.java
                            )

                            var isUpdateGameType = true
                            for (currentActivedate in gameDateList!!.date) {
                                if (currentActivedate!!.status == 3) {
                                    isUpdateGameType = false
                                }
                                if (currentActivedate!!.status == 1 || currentActivedate!!.status == 2) {
                                    selectedDateObject = currentActivedate;
                                    tvGameDate!!.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                                    break
                                }
                            }
//                            if (isUpdateGameType) {
                                getGameSessionAutoFill()
//                            }
                        } else {
                            Alerts.show(context, jsonresponse.optString("message"))
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun getGameSession() {
        val finalObject = JSONObject()
        finalObject.put("providerId", providerResultData!!.providerId)
        finalObject.put("gameDay", selectedDateObject!!.dayname)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (finalObject).toString()
        )
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                retrofitApiClient!!.getDaySession(body),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        if (jsonresponse.optInt("status") == 1) {
                            gameSession = Gson().fromJson(
                                jsonresponse.toString(),
                                GameSession::class.java
                            )
                            dialogSelectBidType(gameSession!!)
                        } else {
                            Alerts.show(context, jsonresponse.optString("message"))
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }

    private fun dialogSelectBidInterval() {

        if (gameDateList != null) {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.dialog_select_date_of_bid)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );

            val tabDone = dialog.findViewById<RelativeLayout>(R.id.tabDone)

            var selectedData: DateObject? =null
            val recylerView = dialog.findViewById<RecyclerView>(R.id.rv_game_date_list)
            recylerView.adapter = GameDateAdapter(
                context,
                gameDateList!!.date,
                object : GameDateAdapter.OnSelectDateFromGameDates{
                    override fun onSelectDate(position: Int) {
                        selectedData=gameDateList!!.date.get(position);

                        for (item:DateObject in gameDateList!!.date)
                        {
                            val index=gameDateList!!.date.indexOf(item)
                            item.isSelected=false
                            gameDateList!!.date.set(index,item)
                        }
                        selectedData!!.isSelected=true
                        gameDateList!!.date.set(position,selectedData)
                        recylerView.adapter!!.notifyDataSetChanged()
                    }

                })

            tabDone.setOnClickListener {
                if (selectedData!=null)
                {
                    if (selectedData!!.status == 3) {
                        Alerts.AlertDialogWarning(
                            dialog.context,

                            GameConstantMessages.BidClosedForDay,""
                        )
                    } else {
                        dialog.dismiss()
                        tvGameSession!!.text = ""
                        selectedDateObject = selectedData
                        tvGameDate!!.text =
                            DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                        resetBidTableOnChangeDateOrSession()
//                            if(dateObject.date.equals(gameDateList!!.date.get(0).date))
                        getGameSessionAutoFill()
                    }
                }
            }

            dialog.show()

        } else {
            Alerts.AlertDialogWarning(context, GameConstantMessages.NoDateForBid,"")
        }
    }

    private fun resetBidTableOnChangeDateOrSession() {
        strGameSession = ""
        if (bidItems != null) {
            bidItems!!.clear();
            if (bidAdapter != null)
                bidAdapter!!.notifyDataSetChanged()
        }
    }

    private fun dialogSelectBidType(gameSession: GameSession) {
        tvGameSession!!.error = null
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_select_bid)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );

        val tabOpenBid = dialog.findViewById<RelativeLayout>(R.id.tabOpenBid)
        val tabCloseBid = dialog.findViewById<RelativeLayout>(R.id.tabCloseBid)
        val tabDone = dialog.findViewById<RelativeLayout>(R.id.tabDone)
        val ivOpenBid = dialog.findViewById<ImageView>(R.id.ivOpenBid)
        val ivCloseBid = dialog.findViewById<ImageView>(R.id.ivCloseBid)
        val tv_openBid = dialog.findViewById<TextView>(R.id.tvOpen)
        val tv_closeBid = dialog.findViewById<TextView>(R.id.tvClose)


        tabOpenBid.visibility = View.GONE
        tabCloseBid.visibility = View.GONE

        val openBidData = gameSession.daySession.openSession
        val closeBidData = gameSession.daySession.closeSession



        if (openBidData.equals("open", true) && closeBidData.equals("open", true)) {
            tabOpenBid.visibility = View.VISIBLE
            tabCloseBid.visibility = View.VISIBLE
            tv_openBid.text = providerResultData!!.providerName + " OPEN"
            tv_closeBid.text = providerResultData!!.providerName + " CLOSE"
        }
        if (openBidData.equals("close", true) && closeBidData.equals("open", true)) {
            tabOpenBid.visibility = View.GONE
            tabCloseBid.visibility = View.VISIBLE
            tv_closeBid.text = providerResultData!!.providerName + " CLOSE"
            tv_openBid.text = ""
        }
        if (openBidData.equals("close", true) && closeBidData.equals("close", true)) {
            tabOpenBid.visibility = View.GONE
            tabCloseBid.visibility = View.GONE
            tv_openBid.text = ""
            tv_closeBid.text = ""
        }
        if (openBidData.equals("open", true) && closeBidData.equals("close", true)) {
            tabOpenBid.visibility = View.VISIBLE
            tabCloseBid.visibility = View.GONE
            tv_closeBid.text = providerResultData!!.providerName + " OPEN"
            tv_openBid.text = ""

        }


        var STRSESSION=strGameSession
        var TITLESTRSESSION=providerResultData!!.providerName + " " + STRSESSION
        tabOpenBid.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                STRSESSION = "Open"
                TITLESTRSESSION= providerResultData!!.providerName + " " + STRSESSION
                ivOpenBid.visibility=View.VISIBLE
                ivCloseBid.visibility=View.GONE

            }
        })
        tabCloseBid.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                STRSESSION = "Close"
                TITLESTRSESSION = providerResultData!!.providerName + " " + STRSESSION
                ivOpenBid.visibility=View.GONE
                ivCloseBid.visibility=View.VISIBLE
            }
        })

        tabDone.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                resetBidTableOnChangeDateOrSession()
                strGameSession = STRSESSION
                tvGameSession!!.text = TITLESTRSESSION
                dialog.dismiss()
            }
        })

        dialog.show()

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
            providerResultData!!.providerName + " " + DateFormatToDisplay().parseDateToddMMyyyy(
                selectedDateObject!!.date
            )
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
            var currentDate = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(Date())
            if (currentDate == selectedDateObject!!.date) {
                submitFinalBids(walletBal, pntDeduct, dialog)
            } else {
                tabSubmit.isEnabled = true
                currentDate = DateFormatToDisplay().parseDateToddMMyyyy(currentDate)
                var selectedDate =
                    DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                Alerts.AlertDialogDate(
                    context,
                    "Today Date is $currentDate \nAnd Your Bid is For Date $selectedDate \nDo You Want to Proceed ?",
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            submitFinalBids(walletBal, pntDeduct, dialog)
                        }
                    })
            }
        }
        tabCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun submitFinalBids(
        walletBal: Int,
        pntDeduct: Int,
        dialog: Dialog
    ) {
        if (walletBal < pntDeduct) {
            dialog.dismiss()
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        } else {
            OnSubmitBidManager().submitBids(
                requireContext(),
                retrofitApiClient!!,
                cd!!,
                bidItems!!,
                providerResultData!!,
                selectedDateObject!!,
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
                                responseObject.getString("message"),""
                            )
                        } else {
                            Alerts.AlertDialogWarning(
                                context,

                                responseObject.getString("message"),""
                            )
                        }
                    }

                    override fun onFail(response: String?) {
                        Alerts.AlertDialogWarning(context, response,"")
                    }
                }
            )
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

    private fun fetchStarlineGameId() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getDashboardGameTypeData(
                Dialog(requireActivity()),
                retrofitApiClient!!.kuberMorningDashboardGameId(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val ksgModel = result!!.body() as GameTypeDashboardModal
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
                                }
                            }
                        } else {
                            Alerts.AlertDialogWarning(mContext, ksgModel.message,"")
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


    private fun spotLightEffect() {
        val targets = ArrayList<Target>()
        // first target
        val firstRoot = FrameLayout(requireContext())
        val first = layoutInflater.inflate(R.layout.layout_select_gametype, firstRoot)

        val firstTarget = Target.Builder()
            .setAnchor(tvGameSession!!)
            .setShape(RoundedRectangle(tvGameSession!!.height.toFloat(),tvGameSession!!.width.toFloat(),15.0f))
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
        val spotlight = Spotlight.Builder(requireActivity())
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

}
