package com.kasa777.ui.fragment.startline_game_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasa777.R
import com.kasa777.adapter.bid_list_submit.BidListFinalAdapter
import com.kasa777.adapter.bid_list_submit.BidListToSubmitAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.game_bid_data.BidData
import com.kasa777.modal.kuber_starline.game_type.GameType
import com.kasa777.modal.kuber_starline.game_type.KsGameTypeModel
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.kasa777.ui.fragment.GameConstantMessages
import com.kasa777.ui.fragment.GameTypeNames
import com.kasa777.ui.fragment.OnSubmitBid
import com.kasa777.ui.fragment.OnSubmitBidManager
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.dialog_view_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_sp_motor.ivGameDate
import kotlinx.android.synthetic.main.fragment_sp_motor.ivGameSession

import kotlinx.android.synthetic.main.layout_bid_action_bottom_bar.submitBtn
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList


class DPMotorFragment_Starline : Fragment(), View.OnClickListener {

    private var doublePannaArray = arrayOf(
        "118",
        "100",
        "110",
        "166",
        "112",
        "113",
        "114",
        "115",
        "116",
        "117",
        "226",
        "119",
        "200",
        "229",
        "220",
        "122",
        "277",
        "133",
        "224",
        "144",
        "244",
        "155",
        "228",
        "300",
        "266",
        "177",
        "330",
        "188",
        "233",
        "199",
        "299",
        "227",
        "255",
        "337",
        "338",
        "339",
        "448",
        "223",
        "288",
        "225",
        "334",
        "335",
        "336",
        "355",
        "400",
        "366",
        "466",
        "377",
        "440",
        "388",
        "488",
        "344",
        "499",
        "445",
        "446",
        "447",
        "556",
        "449",
        "477",
        "559",
        "550",
        "399",
        "660",
        "599",
        "455",
        "500",
        "600",
        "557",
        "558",
        "577",
        "668",
        "588",
        "688",
        "779",
        "699",
        "799",
        "880",
        "566",
        "800",
        "667",
        "677",
        "669",
        "778",
        "788",
        "770",
        "889",
        "899",
        "700",
        "990",
        "900"
    )
    private var endDigit = ""
    private var numberList = "0123456789"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        from = requireArguments().getString("FROM")!!
        if (from == GameTypeNames.DPMOTOR) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }

        initViews()

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.DPMOTOR)
            } else {
                val message = "Please add a bid first!!!"
                dialogBoxMessage(message, "cancel")
            }
        }

        actDigits!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (actDigits!!.text.toString().isNotEmpty()) {
                    checkNumber(s.toString())
                    val obj = actDigits!!.text.toString()
                    val substring = obj.substring(obj.length - 1)
                    val obj2 = actDigits!!.text.toString()
                    val substring2 = obj2.substring(0, obj2.length - 1)
                    val sb = StringBuilder()
                    sb.append("")
                    sb.append(substring2)
                    Log.v("Edittext value ", sb.toString())
                    val sb2 = StringBuilder()
                    sb2.append("")
                    sb2.append(substring)
                    Log.v("String  value ", sb2.toString())
                    if (obj2.length > endDigit.length) {
                        var str = obj2
                        for (i in endDigit.indices) {
                            str = str.replace(endDigit[i].toString(), "")
                        }
                        if (endDigit.contains(str)) {
                            s!!.replace(0, s.length, endDigit)
                        } else {
                            numberList = numberList.replace(str, "")
                            val sb3 = StringBuilder()
                            sb3.append("")
                            sb3.append(numberList)
                            Log.v("numberList", sb3.toString())
                        }
                    }
                    for (i2 in obj2.indices) {
                        val valueOf = obj2.get(i2).toString()
                        endDigit = endDigit.replace(valueOf, "")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                endDigit = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val sb = StringBuilder()
                sb.append("")
                sb.append(start)
                Log.v("start", sb.toString())
                val sb2 = StringBuilder()
                sb2.append("")
                sb2.append(before)
                Log.v("before", sb2.toString())
                val sb3 = StringBuilder()
                sb3.append("")
                sb3.append(count)
                Log.v("count", sb3.toString())
            }
        })

        actDigits!!.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                val sb = StringBuilder()
                sb.append("Adding :")
                sb.append(endDigit)
                Log.v("Last ", sb.toString())
                val sb2 = StringBuilder()
                sb2.append(numberList)
                sb2.append(endDigit)
                numberList = sb2.toString()
                val sb3 = StringBuilder()
                sb3.append("Added :")
                sb3.append(numberList)
                Log.v("Number List ", sb3.toString())
                endDigit = ""
            }
            false
        }

    }

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
       // tvPannaCount = rootView!!.findViewById(R.id.tvPannaCount)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)
        
        actDigits!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        ivGameDate.setImageResource(R.drawable.calendar_pink)
        ivGameSession.setImageResource(R.drawable.down_arrow_pink)
        tabAddBid!!.setBackgroundResource(R.drawable.pink_button)
        submitBtn!!.setBackgroundResource(R.drawable.pink_button)

       /* val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.DPMOTOR*/

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
                            Alerts.AlertDialogWarning(mContext,  GameConstantMessages.MaxPoint,"pink")
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


    fun checkNumber(str: String) {
        val sb = StringBuilder()
        sb.append("")
        sb.append(str)
        Log.v("In check num", sb.toString())
        val length = str.length
        val sb2 = StringBuilder()
        sb2.append("")
        sb2.append(length)
        Log.v("length", sb2.toString())
        if (this.numberList.contains(str[length - 1].toString())) {
            this.numberList = this.numberList.replace(str, "")
            val sb3 = StringBuilder()
            sb3.append("")
            sb3.append(this.numberList)
            Log.v("numberList", sb3.toString())
        }
    }


    private fun createBid() {
        hideKeyboard()
        val stDigits: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)


        if (stDigits.isEmpty() || stDigits.length < 4) {
            actDigits!!.error = "Minimum 4 Digit Motor is Required!!!"
            actDigits!!.requestFocus()
        } else if (stPoints.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        }  else if (stPoints.startsWith("0")) {
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
            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType,"pink")
        } else if (providerResultData!!.gameDate.isEmpty()) {
            dialogBoxMessage("Select Date", "cancel")
        }else {
            try {
                val validDigits = validateDigits(stDigits)
                generateDpMotor(validDigits, stPoints)
            } catch (e: Exception) {
            }
        }

    }

    private fun validateDigits(stDigits: String): String {
        var vString = stDigits
        val firstLetter = stDigits.substring(0, 1)
        if (firstLetter == "0") {
            val vs = stDigits.substring(1)
            vString = vs + "0"
        }
        return vString
    }


    @SuppressLint("SetTextI18n")
    private fun generateDpMotor(stDigits: String, stPoints: String) {
        bidItems!!.clear()
        bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this,"pink")
        rvBidList!!.layoutManager = LinearLayoutManager(mContext)
        rvBidList!!.adapter = bidAdapter
        bidAdapter!!.notifyDataSetChanged()
        doublePannaArray.forEach {
            val digits = it.toCharArray()
            val ones = digits[0].toString()
            val twos = digits[1].toString()
            val threes = digits[2].toString()
            if (stDigits.contains(ones) && stDigits.contains(twos) && stDigits.contains(threes)) {
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
        bidAdapter!!.notifyDataSetChanged()

        etPoints!!.setText("")
        actDigits!!.setText("")
        if(bidItems!!.size==0)
        {
            etPoints!!.setText("")
            actDigits!!.setText("")
            bidAdapter!!.notifyDataSetChanged()
            dialogBoxMessage("Please enter valid digits","")
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
    private var providerResultData: com.kasa777.ui.fragment.startline_game_fragment.modal.Result? = null
    private var tabGameDate: RelativeLayout?=null
    private var tabGameSession: RelativeLayout?=null
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
    private var tabAddBid: TextView? = null
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
        val confirmBtn = dialog.findViewById<RelativeLayout>(R.id.confirmBtn)
        confirmBtn.setBackgroundResource(R.drawable.pink_confirm_button)
        val cancelBtn = dialog.findViewById<RelativeLayout>(R.id.cancelBtn)
        cancelBtn.setBackgroundResource(R.drawable.pink_cancel_button)
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)
        val cancelText = dialog.findViewById<TextView>(R.id.tvCancel)
        cancelText.setTextColor(ContextCompat.getColor(context!!, R.color.pinkThemeColor))
        tvCurrentTime.text =
            "Starline "+from + " " + providerResultData!!.providerName

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
            }  else {
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
                                    responseObject.getString("message"),"pink"
                                )
                            } else {
                                Alerts.AlertDialogWarning(
                                    context,

                                    responseObject.getString("message"),"pink"
                                )
                            }
                        }

                        override fun onFail(response: String?) {
                            Alerts.AlertDialogWarning(context, response,"pink")
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
        btnSubmit.setBackgroundResource(R.drawable.pink_confirm_button)
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
                                    if (it.gameName.equals(GameTypeNames.DoublePana)) {
                                        gameId = it.id
                                        gameTypeName = it.gameName
                                        gameTypePrice = it.gamePrice.toString()
                                    }
                                }
                            }
                        } else {
                            Alerts.AlertDialogWarning(mContext,  ksgModel.message,"pink")
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.AlertDialogWarning(mContext,  "Server Error","pink")
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