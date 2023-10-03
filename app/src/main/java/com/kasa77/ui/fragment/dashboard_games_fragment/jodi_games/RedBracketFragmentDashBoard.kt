package com.kasa77.ui.fragment.dashboard_games_fragment.jodi_games

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
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RedBracketFragmentDashBoard : Fragment(), View.OnClickListener {

    private val myList = arrayListOf<String>()

    var redBracketArray = arrayOf(
        "00", "11", "22", "33", "44", "55", "66", "77", "88", "99", "05", "16", "27", "38", "49",
        "50", "61", "72", "83", "94"
    )

    private var tabTitleBracketCount: RelativeLayout? = null


    private var cbBrackets: CheckBox? = null
    private var redBracketId = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_red_bracket, container, false)
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
                dialogBitSubmit(GameTypeNames.RedBrackets)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }

        actDigits!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s!!.isEmpty())
                    initAutoComplete(s.toString())
            }
        })

        cbBrackets!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tabTitleBracketCount!!.visibility = View.GONE

            } else {
                tabTitleBracketCount!!.visibility = View.VISIBLE

            }
        }
    }


    private fun initViews() {
        tabAddBid = rootView!!.findViewById(R.id.tabAddBid)
        tvFinalSubmit = rootView!!.findViewById(R.id.tvFinalSubmit)
        actDigits = rootView!!.findViewById(R.id.actDigits)
        etPoints = rootView!!.findViewById(R.id.etPoints)
        rvBidList = rootView!!.findViewById(R.id.rvBidList)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        cbBrackets = rootView!!.findViewById(R.id.cbBrackets)

        tabTitleBracketCount = rootView!!.findViewById(R.id.tabTitleBracketCount)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        actDigits!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))

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


    @SuppressLint("SetTextI18n")
    private fun createBid() {
        hideKeyboard()

        if (cbBrackets!!.isChecked) {
            val stPoints: String = etPoints!!.text.toString().trim()

            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

            if (stPoints!!.isEmpty()) {
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
            } else if (selectedDateObject!!.date.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            } else {
                bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                rvBidList!!.adapter = bidAdapter
                bidAdapter!!.notifyDataSetChanged()

                redBracketArray.forEach {
                    val bidItem = BidData(
                        it,
                        stPoints,
                        gameTypeName,
                        strGameSession,
                        selectedDateObject!!.date,
                        gameTypePrice,
                        gameId
                    )
                    bidItems!!.add(bidItem)
                }

                bidAdapter!!.notifyDataSetChanged()
//                etPoints!!.setText("")
            }


        } else {
            val stDigits: String = actDigits!!.text.toString().trim()
            val stPoints: String = etPoints!!.text.toString().trim()

            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)
            if (stDigits!!.isEmpty()) {
                actDigits!!.error = "Please enter valid Jodi"
                actDigits!!.requestFocus()
            } else if (stPoints!!.isEmpty()) {
                etPoints!!.error = "Please Enter Point!!!"
            } else if (strGameSession.isEmpty()) {
                Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
            } else if (selectedDateObject!!.date.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints.toInt()) {
                val message = "You don't have required bid amount please add fund."
                dialogBoxMessage(message, "cancel")
            } else {
                bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
                rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                rvBidList!!.adapter = bidAdapter
                bidAdapter!!.notifyDataSetChanged()

                if (redBracketArray.contains(stDigits)) {
                    val bidItem = BidData(
                        stDigits,
                        stPoints,
                        gameTypeName,
                        strGameSession,
                        selectedDateObject!!.date,
                        gameTypePrice,
                        gameId
                    )
                    bidItems!!.add(bidItem)
                    bidAdapter!!.notifyDataSetChanged()
                }else{
                    actDigits!!.error = "Please enter valid Jodi"
                    actDigits!!.requestFocus()
                }
//                etPoints!!.setText("")
//                actDigits!!.setText("")


            }

        }

//        if (bidItems!!.size == 0) {
//            actDigits!!.setText("")
//            etPoints!!.setText("")
////            dialogBoxMessage("Please enter valid digits", "Cancel")
//        }
        actDigits!!.setText("")
        etPoints!!.setText("")
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

    private fun initAutoComplete(input: String) {

        generateJodiDigits(input)

        val actAdapter = ArrayAdapter<String>(mContext!!, R.layout.row_act_item, myList)
        actDigits?.threshold = 1
        actDigits?.setAdapter(actAdapter)
        actAdapter.notifyDataSetChanged()

    }

    private fun generateJodiDigits(input: String) {
        myList.clear()
        if (input.isNotEmpty()) {
            redBracketArray.forEach {
                if (it.contains(input)) {
                    myList.add(it)
                }
            }
        } else {
            actDigits?.error = "Please select valid Digit"
            actDigits!!.requestFocus()
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
                                    if (it.gameName.equals(GameTypeNames.RedBrackets)) {
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
