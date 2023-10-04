package com.kasa77.ui.fragment.startline_game_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList


class SinglePanaFragment_Starline : Fragment(), View.OnClickListener {

    val myList = arrayListOf<String>()

    private var singlePannaArray =
        arrayOf(
            "127", "136", "145", "190", "235", "280", "370", "389", "460", "479", "569", "578",

            "128", "137", "146", "236", "245", "290", "380", "470", "489", "560", "579", "678",

            "129", "138", "147", "156", "237", "246", "345", "390", "480", "570", "589", "679",

            "120", "139", "148", "157", "238", "247", "256", "346", "490", "580", "670", "689",

            "130", "149", "158", "167", "239", "248", "257", "347", "356", "590", "680", "789",

            "140", "159", "168", "230", "249", "258", "267", "348", "357", "456", "690", "780",

            "123", "150", "169", "178", "240", "259", "268", "349", "358", "367", "457", "790",

            "124", "160", "278", "179", "250", "269", "340", "359", "368", "458", "467", "890",

            "125", "134", "170", "189", "260", "279", "350", "369", "468", "378", "459", "567",

            "126", "135", "180", "234", "270", "289", "360", "379", "450", "469", "478", "568"
        )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_single_pana, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        from = requireArguments().getString("FROM")!!
        if (from == GameTypeNames.SinglePana) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }

        initViews()

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.SinglePana)
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
                initAutoComplete(s.toString())


            }
        })
    }

    private fun initAutoComplete(input: String) {
        when (input.length) {
            1 -> generateThreeDigitsStartWithSingleInput(input.toInt())
            2 -> generateThreeDigitsStartWithDoubleInput(input.toInt())
            3 -> generateThreeDigitsStartWithThreeInput(input.toInt())
        }

        val actAdapter = ArrayAdapter<String>(mContext!!, R.layout.row_act_item, myList)
        actDigits?.threshold = 1
        actDigits?.setAdapter(actAdapter)
        actAdapter.notifyDataSetChanged()

    }

    private fun generateThreeDigitsStartWithSingleInput(input: Int) {
        myList.clear()

        for(i in singlePannaArray.indices){
            if(singlePannaArray.get(i).toString().startsWith(input.toString())){
                myList.add(singlePannaArray.get(i).toString())
            }
        }

        if(input == 0){
            actDigits?.error = "Please select valid panna"
            actDigits?.setText("")
        }
    }

    private fun generateThreeDigitsStartWithDoubleInput(input: Int) {
        myList.clear()
        val digits = input.toString().toCharArray()
        val twos = Integer.parseInt(digits[1].toString())
        val ones = Integer.parseInt(digits[0].toString())
        if (ones < twos) {
            for(i in singlePannaArray.indices){
                if(singlePannaArray.get(i).toString().startsWith(input.toString())){
                    myList.add(singlePannaArray.get(i).toString())
                }
            }
        } else {
            actDigits?.error = "Please select valid panna"
        }
    }

    private fun generateThreeDigitsStartWithThreeInput(input: Int) {
        myList.clear()
        val digits = input.toString().toCharArray()
        val ones = Integer.parseInt(digits[0].toString())
        val twos = Integer.parseInt(digits[1].toString())
        val threes = Integer.parseInt(digits[2].toString())
        if (isValidDigits(input)) {
            for(i in singlePannaArray.indices){
                if(singlePannaArray.get(i).toString().startsWith(input.toString())){
                    myList.add(singlePannaArray.get(i).toString())
                }
            }
        } else {
            actDigits?.error = "Please select valid panna"
        }
    }

    private fun isValidDigits(input: Int): Boolean {
        var isTrue = false
        val digits = input.toString().toCharArray()
        val ones = Integer.parseInt(digits[0].toString())
        val twos = Integer.parseInt(digits[1].toString())
        val threes = Integer.parseInt(digits[2].toString())
        if (ones < twos) {
            if (twos < threes) {
                isTrue = true
            } else if (threes == 0) {
                isTrue = true
            }
        }
        return isTrue
    }

    @SuppressLint("SetTextI18n")
    private fun createBid() {
        hideKeyboard()
        var isFoundInList: Boolean = false
        val stDigits: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

        if (stDigits.isEmpty()) {
            actDigits!!.error = "Please Enter Bid Digits!!!"
            actDigits!!.requestFocus()
        } else if (stPoints.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        } else if (stPoints.startsWith("0")) {
            dialogBoxMessage("Please enter valid point", "cancel")
        } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "cancel")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "cancel")
        } else if (walletBal < stPoints.toInt()) {
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        } else if (strGameSession.isEmpty()) {
            tvGameSession!!.error=GameConstantMessages.SelectGameType
tvGameSession!!.requestFocus()
            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType)
        } else if (providerResultData!!.gameDate.isEmpty()) {
            dialogBoxMessage("Game Date Error", "cancel")
        } else {
            bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this)
            rvBidList!!.layoutManager = LinearLayoutManager(mContext)
            rvBidList!!.adapter = bidAdapter
            bidAdapter!!.notifyDataSetChanged()

            if (stDigits.isNotEmpty()) {
                myList.forEach {
                    if (stDigits.equals(it)) {
                        isFoundInList = true
                        val bidItem = BidData(
                            stDigits,
                            stPoints,
                            gameTypeName,
                            strGameSession,
                            providerResultData!!.gameDate,
                            gameTypePrice,
                            gameId
                        )
                        var isAddNew = false

                        if (!bidItems!!.isEmpty()) {
                            for (currentIndexBid in bidItems!!) {
                                if (currentIndexBid.gemeSession.equals(bidItem.gemeSession)) {
                                    if (currentIndexBid.digits.equals(bidItem.digits)) {
                                        currentIndexBid.points =
                                            (currentIndexBid.points.toInt() + bidItem.points.toInt()).toString()
                                        isAddNew = false
                                        break
                                    } else {
                                        isAddNew = true
                                    }
                                } else {
                                    isAddNew = true
                                }
                            }
                        } else {
                            bidItems!!.add(bidItem)
                        }

                        if (isAddNew) {
                            bidItems!!.add(bidItem)
                            isAddNew = false
                        }



                        bidAdapter!!.notifyDataSetChanged()
                    }
                }
//                etPoints!!.setText("")
//                actDigits!!.setText("")
            }
            etPoints!!.setText("")
            actDigits!!.setText("")
            if (!isFoundInList) {
                isFoundInList = false
                etPoints!!.setText("")
                actDigits!!.setText("")
                dialogBoxMessage("Please enter valid digits", "cancel")
            } else {
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
      //  tvPannaCount = rootView!!.findViewById(R.id.tvPannaCount)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

       /* val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.SinglePana*/

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
                            Alerts.AlertDialogWarning(mContext,  "Enter Valid Point!!!")
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
            tabSubmit.isEnabled =false
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
                            Alerts.AlertDialogWarning(context,  response)
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
                                        gameId = it.id
                                        gameTypeName = it.gameName
                                        gameTypePrice = it.gamePrice.toString()
                                    }
                                }
                            }
                        } else {
                            Alerts.AlertDialogWarning(mContext,  ksgModel.message)
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
