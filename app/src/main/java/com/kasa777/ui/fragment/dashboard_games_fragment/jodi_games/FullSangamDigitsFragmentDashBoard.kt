package com.kasa777.ui.fragment.dashboard_games_fragment.jodi_games

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
import com.google.gson.Gson
import com.kasa777.R
import com.kasa777.adapter.GameDateAdapter
import com.kasa777.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa777.adapter.bid_list_submit.BidListToSubmitAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.dashboard_gamelist.Result
import com.kasa777.modal.game_bid_data.BidData
import com.kasa777.modal.game_date_list.DateObject
import com.kasa777.modal.game_date_list.GameDateList
import com.kasa777.modal.kuber_dashboard_games.GameType
import com.kasa777.modal.kuber_dashboard_games.GameTypeDashboardModal
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.fragment.GameConstantMessages
import com.kasa777.ui.fragment.GameTypeNames
import com.kasa777.ui.fragment.OnSubmitBid
import com.kasa777.ui.fragment.OnSubmitBidManager
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FullSangamDigitsFragmentDashBoard : Fragment(), View.OnClickListener {

    private var pannaArray = arrayOf(
        "100",
        "110",
        "111",
        "112",
        "113",
        "114",
        "115",
        "116",
        "117",
        "118",
        "119",
        "120",
        "122",
        "123",
        "124",
        "125",
        "126",
        "127",
        "128",
        "129",
        "130",
        "133",
        "134",
        "135",
        "136",
        "137",
        "138",
        "139",
        "140",
        "144",
        "145",
        "146",
        "147",
        "148",
        "149",
        "150",
        "155",
        "156",
        "157",
        "158",
        "159",
        "160",
        "166",
        "167",
        "168",
        "169",
        "170",
        "177",
        "178",
        "179",
        "180",
        "188",
        "189",
        "190",
        "199",
        "200",
        "220",
        "222",
        "223",
        "224",
        "225",
        "226",
        "227",
        "228",
        "229",
        "230",
        "233",
        "234",
        "235",
        "236",
        "237",
        "238",
        "239",
        "240",
        "244",
        "245",
        "246",
        "247",
        "248",
        "249",
        "250",
        "255",
        "256",
        "257",
        "258",
        "259",
        "260",
        "266",
        "267",
        "268",
        "269",
        "270",
        "277",
        "278",
        "279",
        "280",
        "288",
        "289",
        "290",
        "299",
        "300",
        "330",
        "333",
        "334",
        "335",
        "336",
        "337",
        "338",
        "339",
        "340",
        "344",
        "345",
        "346",
        "347",
        "348",
        "349",
        "350",
        "355",
        "356",
        "357",
        "358",
        "359",
        "360",
        "366",
        "367",
        "368",
        "369",
        "370",
        "377",
        "378",
        "379",
        "380",
        "388",
        "389",
        "390",
        "399",
        "400",
        "440",
        "444",
        "445",
        "446",
        "447",
        "448",
        "449",
        "450",
        "455",
        "456",
        "457",
        "458",
        "459",
        "460",
        "466",
        "467",
        "468",
        "469",
        "470",
        "477",
        "478",
        "479",
        "480",
        "488",
        "489",
        "490",
        "499",
        "500",
        "550",
        "555",
        "556",
        "557",
        "558",
        "559",
        "560",
        "566",
        "567",
        "568",
        "569",
        "570",
        "577",
        "578",
        "579",
        "580",
        "588",
        "589",
        "590",
        "599",
        "600",
        "660",
        "666",
        "667",
        "668",
        "669",
        "670",
        "677",
        "678",
        "679",
        "680",
        "688",
        "689",
        "690",
        "699",
        "700",
        "770",
        "777",
        "778",
        "779",
        "780",
        "788",
        "789",
        "790",
        "799",
        "800",
        "880",
        "888",
        "889",
        "890",
        "899",
        "900",
        "990",
        "999",
        "000"
    )

    private var openArray = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val myList = arrayListOf<String>()

    private var actClosePanna: AutoCompleteTextView? = null
    private var actOpenPanna: AutoCompleteTextView? = null
    private var llOdCp: LinearLayout? = null
    private var llOpCd: LinearLayout? = null
    private var isOpenDigit = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_full_sangam_fragment, container, false)
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
                dialogBitSubmit(GameTypeNames.FullSangamDigits)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }

        actClosePanna!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                /*if (!pannaArray.contains(s.toString())){
                    actClosePanna!!.error = "Please select a valid panna"
                }*/
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                initClosePannaAutoComplete(s.toString())
            }
        })

        actOpenPanna!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                initOpenPannaAutoComplete(s.toString())
            }
        })
    }


    private fun initViews() {
        tabAddBid = rootView!!.findViewById<FrameLayout>(R.id.tabAddBid)
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        etPoints = rootView!!.findViewById(R.id.etPoints)
        rvBidList = rootView!!.findViewById(R.id.rvBidList)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        actClosePanna = rootView!!.findViewById(R.id.actClosePanna)
        actOpenPanna = rootView!!.findViewById(R.id.actOpenPanna)
        llOdCp = rootView!!.findViewById(R.id.llOdCp)
        llOpCd = rootView!!.findViewById(R.id.llOpCd)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)


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


    }


    private fun initClosePannaAutoComplete(digit: String) {
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
                actClosePanna!!.error = "Please select a valid panna"
            }
        }

        val closePannaAdapter = ArrayAdapter<String>(mContext!!, R.layout.row_act_item, myList)
        actClosePanna?.threshold = 1
        actClosePanna?.setAdapter(closePannaAdapter)
        closePannaAdapter.notifyDataSetChanged()
    }

    private fun initOpenPannaAutoComplete(digit: String) {
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
                actOpenPanna!!.error = "Please select a valid panna"
            }
        }

        val openPannaAdapter = ArrayAdapter<String>(mContext!!, R.layout.row_act_item, myList)
        actOpenPanna?.threshold = 1
        actOpenPanna?.setAdapter(openPannaAdapter)
        openPannaAdapter.notifyDataSetChanged()
    }

    private fun createBid() {
        hideKeyboard()
        if (isOpenDigit) {
            val stOpenPanna: String = actOpenPanna!!.text.toString().trim()
            val stClosePanna: String = actClosePanna!!.text.toString().trim()
            val stPoints: String = etPoints!!.text.toString().trim()
            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

            when {
                stOpenPanna.isEmpty() -> {
                    actOpenPanna!!.error = "Enter Bid Digits!!!"
                    actOpenPanna!!.requestFocus()
                }
                !pannaArray.contains(stOpenPanna) -> {
                    actOpenPanna!!.error = "Please select a valid digit"
                    actOpenPanna!!.requestFocus()
                }
                !pannaArray.contains(stClosePanna) -> {
                    actClosePanna!!.error = "Please select a valid panna"
                    actClosePanna!!.requestFocus()
                }
                stPoints.isEmpty() -> {
                    etPoints!!.error = "Please Enter Point!!!"
                    etPoints!!.requestFocus()
                }
                stPoints.startsWith("0") -> dialogBoxMessage("Please enter valid point", "")

                stPoints.toInt() < GameConstantMessages.MinPointValue -> dialogBoxMessage(GameConstantMessages.MinPoint, "")
                stPoints.toInt() > GameConstantMessages.MaxPointValue-> dialogBoxMessage(
                    GameConstantMessages.MaxPoint,
                    ""
                )
                walletBal < stPoints.toInt() -> {
                    val message = "You don't have required bid amount please add fund."
                    dialogBoxMessage(message, "cancel")
                }
                strGameSession.isEmpty() -> {
                    Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
                }
                selectedDateObject!!.date.isEmpty() -> dialogBoxMessage("Require Field", "cancel")

                else -> try {
                    bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                    rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                    rvBidList!!.adapter = bidAdapter
                    bidAdapter!!.notifyDataSetChanged()
                    generateSpMotor(stOpenPanna, stClosePanna, stPoints)
                } catch (e: Exception) {
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun generateSpMotor(open: String, close: String, stPoints: String) {

        val bidPanna = "$open-$close"

        val bidItem = BidData(
            bidPanna,
            stPoints,
            gameTypeName,
            strGameSession,
            selectedDateObject!!.date,
            gameTypePrice,
            gameId
        )
        bidItems!!.add(bidItem)

        bidAdapter!!.notifyDataSetChanged()

        actClosePanna!!.setText("")
        actOpenPanna!!.setText("")
        etPoints!!.setText("")

        if (bidItems!!.size == 0) {
            etPoints!!.setText("")
            actOpenPanna!!.setText("")
            actClosePanna!!.setText("")
            bidAdapter!!.notifyDataSetChanged()
            dialogBoxMessage("Please enter valid digits", "cancel")
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
    private var selectedDateObject: DateObject? = null
    private var gameDateList: GameDateList? = null
    private var providerResultData: Result? = null
    private var tabGameDate:RelativeLayout?=null
    private var tvGameDate: TextView? = null
    private var tvTotalBids: TextView? = null
    private var tvTotalPoints: TextView? = null
    private var strGameSession = "Close"
    private var bidItems: ArrayList<BidData>? = ArrayList()
    private var bidAdapter: BidListToSubmitAdapter? = null
    private var submitBidData: BidListFinalAdapter? = null
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

                            for (currentActivedate in gameDateList!!.date) {
                                if (currentActivedate!!.status == 1) {
                                    selectedDateObject = currentActivedate;
                                    tvGameDate!!.text =
                                        DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                                    break
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
                    if (selectedData!!.status == 3 || selectedData!!.status ==2) {
                        Alerts.AlertDialogWarning(
                            dialog.context,

                            GameConstantMessages.BidClosedForDay
                        )
                    } else {
                        dialog.dismiss()
                        selectedDateObject = selectedData
                        tvGameDate!!.text =
                            DateFormatToDisplay().parseDateToddMMyyyy(selectedDateObject!!.date)
                        resetBidTableOnChangeDateOrSession()
//                            if(dateObject.date.equals(gameDateList!!.date.get(0).date))
                    }
                }
            }

            dialog.show()

        } else {
            Alerts.AlertDialogWarning(context, GameConstantMessages.NoDateForBid)
        }
    }

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
            OnSubmitBidManager()
                .submitBids(
                    requireContext(),
                    retrofitApiClient!!,
                    cd!!,
                    bidItems!!,
                    providerResultData!!,
                    selectedDateObject!!,
                    selectedDateObject!!.gameSession, object :
                        OnSubmitBid {
                        override fun onSuccess(response: String?) {
                            val responseObject = JSONObject(response)
                            if (responseObject.getInt("status") == 1) {

                                actClosePanna!!.setText("")
                                actOpenPanna!!.setText("")
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
                                    if (it.gameName.equals(GameTypeNames.FullSangamDigits)) {
                                        gameId = it.id
                                        gameTypeName = it.gameName
                                        gameTypePrice = it.gamePrice.toString()
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
