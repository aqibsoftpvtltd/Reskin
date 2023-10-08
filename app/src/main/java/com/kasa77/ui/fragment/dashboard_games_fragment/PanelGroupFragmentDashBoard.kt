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
import kotlinx.android.synthetic.main.fragment_sp_motor.enterDigitLyt
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PanelGroupFragmentDashBoard : Fragment(), View.OnClickListener {


    private val pannaGroup = arrayOf(
        arrayOf("128", "137", "236", "678", "123", "268", "367", "178"),
        arrayOf("129", "147", "246", "679", "124", "179", "467", "269"),
        arrayOf("120", "157", "256", "670", "170", "567", "125", "260"),
        arrayOf("130", "158", "680", "356", "180", "568", "135", "360"),
        arrayOf("140", "159", "456", "690", "190", "569", "145", "460"),
        arrayOf("245", "240", "290", "259", "470", "457", "579", "790"),
        arrayOf("345", "890", "390", "458", "480", "340", "589", "359"),
        arrayOf("139", "189", "148", "468", "346", "369", "689", "134"),
        arrayOf("789", "379", "347", "478", "248", "289", "239", "234"),
        arrayOf("230", "280", "258", "235", "357", "578", "780", "370"),
        arrayOf("380", "880", "335", "330", "588", "358"),
        arrayOf("570", "250", "255", "557", "200", "700"),
        arrayOf("247", "477", "779", "279", "229", "224"),
        arrayOf("167", "117", "112", "126", "266", "667"),
        arrayOf("249", "244", "799", "299", "447", "479"),
        arrayOf("489", "448", "344", "899", "399", "349"),
        arrayOf("138", "368", "336", "133", "688", "188"),
        arrayOf("445", "459", "599", "990", "490", "440"),
        arrayOf("149", "144", "446", "199", "699", "469"),
        arrayOf("348", "334", "339", "488", "889", "389"),
        arrayOf("100", "600", "155", "556", "560", "150"),
        arrayOf("660", "115", "110", "566", "156", "160"),
        arrayOf("300", "800", "580", "558", "355", "350"),
        arrayOf("400", "900", "455", "559", "590", "450"),
        arrayOf("168", "136", "113", "668", "366", "118"),
        arrayOf("146", "114", "669", "466", "119", "169"),
        arrayOf("778", "278", "237", "223", "228", "377"),
        arrayOf("337", "378", "238", "288", "788", "233"),
        arrayOf("220", "225", "770", "577", "257", "270"),
        arrayOf("122", "677", "177", "127", "267", "226"),
        arrayOf("227", "277", "777", "222"),
        arrayOf("499", "449", "444", "999"),
        arrayOf("166", "116", "111", "666"),
        arrayOf("338", "388", "888", "333"),
        arrayOf("500", "550", "555", "000")
    )

    private val pannaArray = arrayOf(
        "128",
        "137",
        "236",
        "678",
        "123",
        "268",
        "367",
        "178",
        "129",
        "147",
        "246",
        "679",
        "124",
        "179",
        "467",
        "269",
        "120",
        "157",
        "256",
        "670",
        "170",
        "567",
        "125",
        "260",
        "130",
        "158",
        "680",
        "356",
        "180",
        "568",
        "135",
        "360",
        "140",
        "159",
        "456",
        "690",
        "190",
        "569",
        "145",
        "460",
        "245",
        "240",
        "345",
        "890",
        "139",
        "189",
        "789",
        "379",
        "230",
        "280",
        "290",
        "259",
        "390",
        "458",
        "148",
        "468",
        "347",
        "478",
        "258",
        "235",
        "470",
        "457",
        "480",
        "340",
        "346",
        "369",
        "248",
        "289",
        "357",
        "578",
        "579",
        "790",
        "589",
        "359",
        "689",
        "134",
        "239",
        "234",
        "780",
        "370",
        "380",
        "880",
        "570",
        "250",
        "247",
        "477",
        "167",
        "117",
        "249",
        "244",
        "335",
        "330",
        "255",
        "557",
        "779",
        "279",
        "112",
        "126",
        "799",
        "299",
        "588",
        "358",
        "200",
        "700",
        "229",
        "224",
        "266",
        "667",
        "447",
        "479",
        "489",
        "448",
        "138",
        "368",
        "445",
        "459",
        "149",
        "144",
        "348",
        "334",
        "344",
        "899",
        "336",
        "133",
        "599",
        "990",
        "446",
        "199",
        "339",
        "488",
        "399",
        "349",
        "688",
        "188",
        "490",
        "440",
        "699",
        "469",
        "889",
        "389",
        "100",
        "600",
        "660",
        "115",
        "300",
        "800",
        "400",
        "900",
        "168",
        "136",
        "155",
        "556",
        "110",
        "566",
        "580",
        "558",
        "455",
        "559",
        "113",
        "668",
        "560",
        "150",
        "156",
        "160",
        "355",
        "350",
        "590",
        "450",
        "366",
        "118",
        "146",
        "114",
        "778",
        "278",
        "337",
        "378",
        "220",
        "225",
        "122",
        "677",
        "669",
        "466",
        "237",
        "223",
        "238",
        "288",
        "770",
        "577",
        "177",
        "127",
        "119",
        "169",
        "228",
        "377",
        "788",
        "233",
        "257",
        "270",
        "267",
        "226",
        "227",
        "277",
        "499",
        "449",
        "166",
        "116",
        "338",
        "388",
        "500",
        "550",
        "777",
        "222",
        "444",
        "999",
        "111",
        "666",
        "888",
        "333",
        "555",
        "000"
    )

    private val myList = arrayListOf<String>()

    private var endDigit = ""

    private var singlePanaId = ""
    private var doublePanaId = ""
    private var triplePanaId = ""
    private var singlePanaPrice = "0"
    private var doublePanaPrice = "0"
    private var triplePanaPrice = "0"
    private var singlePanaGame = ""
    private var doublePanaGame = ""
    private var triplePanaGame = ""
    private var panaType = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sp_motor, container, false)
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
                dialogBitSubmit(GameTypeNames.PanelGroup)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }
        actDigits!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                endDigit = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addDataOfBid(s.toString())
            }
        })

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
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        enterDigitLyt.setBackgroundResource(R.drawable.pana_bg)
        actDigits!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        tabGameSession!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (selectedDateObject!=null)
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

       /* val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.PANNA*/
    }

    private fun initPannaAutoComplete(digit: String): String {
        myList.clear()
        var validatePana = ""
        val digits = digit.toCharArray()
        if (digits.size == 3) {
            val ones = Integer.parseInt(digits[0].toString())
            val twos = Integer.parseInt(digits[1].toString())
            val threes = Integer.parseInt(digits[2].toString())

            /*****************************************
             *      Single panna validate
             * ******************************************/
            if (ones != 0 && twos != 0 && threes != 0) {
                if (ones < twos) {
                    if (twos < threes) {
                        //      addDataOfBid(digit)
                        validatePana = GameTypeNames.SinglePana
                    }
                }
            } else if (threes == 0) {
                if (ones < twos) {
                    //  addDataOfBid(digit)
                    validatePana = GameTypeNames.SinglePana
                }
            }

            /*****************************************
             *      double panna validate
             * ******************************************/
            if (ones != 0) {
                if (twos == 0 && threes == 0) {
                    addDataOfBid(digit)
                    validatePana = GameTypeNames.DoublePana
                } else {
                    if (threes == 0) {
                        if (ones == twos) {
                            //    addDataOfBid(digit)
                            validatePana = GameTypeNames.DoublePana
                        }
                    } else {
                        if (ones == twos) {
                            if (twos < threes) {
                                //   addDataOfBid(digit)
                                validatePana = GameTypeNames.DoublePana
                            }
                        } else {
                            if (ones < twos) {
                                if (twos == threes) {
                                    //        addDataOfBid(digit)
                                    validatePana = GameTypeNames.DoublePana
                                }
                            }
                        }
                    }
                }
            }

            /*****************************************
             *      triple panna validate
             * ******************************************/
            if (ones == twos && twos == threes && ones == threes) {
                //  addDataOfBid(digit)
                validatePana = GameTypeNames.TriplePana
            }
        }
        panaType = validatePana
        return validatePana

    }

    private fun addDataOfBid(digit: String) {
        myList.clear()
        if (digit.length < 3) {
            pannaArray.forEach {
                if (it.contains(digit))
                    myList.add(it)
            }
        } else {
            if (pannaArray.contains(digit)) {
                myList.add(digit)
            } else {
                actDigits!!.error = "Please select a valid panna"
                actDigits!!.requestFocus()
            }
        }

        val openPannaAdapter = ArrayAdapter<String>(mContext!!, R.layout.row_act_item, myList)
        actDigits?.threshold = 1
        actDigits?.setAdapter(openPannaAdapter)
        openPannaAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun createBid() {
        hideKeyboard()
        val stOpenPanna: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()

        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

        if (stOpenPanna.isEmpty()) {
            actDigits!!.error = "Please Bid Digit!!!"
            actDigits!!.requestFocus()
        } else if (stPoints.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        }else if (stPoints.startsWith("0")) {
            dialogBoxMessage("Please enter valid point", "")
        }  else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints.toInt()) {
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        }else if (strGameSession.isEmpty()) {
            tvGameSession!!.error=GameConstantMessages.SelectGameType
tvGameSession!!.requestFocus()
//            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
            spotLightEffect()
        } else if (selectedDateObject!!.date.isEmpty()) {
            dialogBoxMessage("Select Date", "cancel")
        } else {
            bidItems!!.clear()


            pannaGroup.forEach { sg ->
                if (sg.contains(stOpenPanna)) {
                    sg.forEach {
                        initPannaAutoComplete(it)
                        when (panaType) {
                            GameTypeNames.SinglePana -> {
                                gameTypeName = singlePanaGame
                                gameId = singlePanaId
                                gameTypePrice = singlePanaPrice.toString()

                            }
                            GameTypeNames.DoublePana -> {
                                gameTypeName = doublePanaGame
                                gameId = doublePanaId
                                gameTypePrice = doublePanaPrice.toString()
                            }
                            GameTypeNames.TriplePana -> {
                                gameTypeName = triplePanaGame
                                gameId = triplePanaId
                                gameTypePrice = triplePanaPrice.toString()
                            }
                        }

                        val bidItem = BidData(
                            it,
                            stPoints,
                            gameTypeName,
                            strGameSession,
                            selectedDateObject!!.date,
                            gameTypePrice,
                            gameId
                        )
                        Log.e("BID : ",bidItem.digits+" : "+ bidItem.gameType)
                        bidItems!!.add(bidItem)
                    }
                }

            }
            actDigits!!.setText("")
            etPoints!!.setText("")
            if (bidItems!!.size == 0) {
                actDigits!!.setText("")
                etPoints!!.setText("")

                dialogBoxMessage("Please enter valid digits", "Cancel")
            }

            bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
            rvBidList!!.layoutManager = LinearLayoutManager(mContext)
            rvBidList!!.adapter = bidAdapter
            bidAdapter!!.notifyDataSetChanged()
//            etPoints!!.setText("")
//            actDigits!!.setText("")

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
    private var tabAddBid: FrameLayout? = null
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
                                if (openBidData.equals("open", true) && closeBidData.equals("open", true)) {

                                    strGameSession = "Open"
                                    tvGameSession!!.text = providerResultData!!.providerName + " " + strGameSession
                                }
                                if (openBidData.equals("close", true) && closeBidData.equals("open", true)) {

//                                    strGameSession = "Close"
//                                    tvGameSession!!.text = providerResultData!!.providerName + " " + strGameSession

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
        tvGameSession!!.error=null
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
            tv_closeBid.visibility = View.GONE
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
            tabSubmit.isEnabled =false
            var currentDate = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(Date())
            if(currentDate == selectedDateObject!!.date) {
                submitFinalBids(walletBal, pntDeduct, dialog)
            }else{
                tabSubmit.isEnabled =true
                currentDate = DateFormatToDisplay().parseDateToddMMyyyy(currentDate)
                var selectedDate = DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                Alerts.AlertDialogDate(context,"Today Date is $currentDate \nAnd Your Bid is For Date $selectedDate \nDo You Want to Proceed ?" , object : View.OnClickListener {
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
                            Alerts.AlertDialogWarning(mContext,  ksgModel.message,"")
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


    private fun dialogBoxMessage( string: String,  s: String) {
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

