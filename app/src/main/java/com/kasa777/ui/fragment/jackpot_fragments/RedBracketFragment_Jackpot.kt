package com.kasa777.ui.fragment.jackpot_fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
import android.widget.*
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
import kotlinx.android.synthetic.main.fragment_red_bracket.ivGameDate

import kotlinx.android.synthetic.main.layout_bid_action_bottom_bar.submitBtn
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList

class RedBracketFragment_Jackpot : Fragment(), View.OnClickListener {


    private val myList = arrayListOf<String>()

    var redBracketArray = arrayOf(
        "00", "11", "22", "33", "44", "55", "66", "77", "88", "99", "05", "16", "27", "38", "49",
        "50", "61", "72", "83", "94"
    )

    private var tabTitleBracketCount: RelativeLayout? = null
  //  private var tabBracketCount: RelativeLayout? = null
    private var cbBrackets: CheckBox? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        from = requireArguments().getString("From")!!

        if (from == GameTypeNames.RedBracketJackpot) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }
        initViews()
        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.RedBracketJackpot)
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
                tabTitleBracketCount!!.visibility = View.INVISIBLE
              //  tabBracketCount!!.visibility = View.GONE
            } else {
                tabTitleBracketCount!!.visibility = View.VISIBLE
              //  tabBracketCount!!.visibility = View.VISIBLE
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
                                    }
                                }

                            }
                        } else {
                            Alerts.AlertDialogWarning(mContext,  ksgModel.message,"green")
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
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
       // tabBracketCount = rootView!!.findViewById(R.id.tabBracketCount)
        tabTitleBracketCount = rootView!!.findViewById(R.id.tabTitleBracketCount)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        ivGameDate.setImageResource(R.drawable.calendar_green)
        tabAddBid!!.setBackgroundResource(R.drawable.green_button)
        submitBtn!!.setBackgroundResource(R.drawable.green_button)

        val selectedColor = context!!.resources.getColor(R.color.greenThemeColor)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), // Checked state
                intArrayOf(-android.R.attr.state_checked) // Unchecked state
            ),
            intArrayOf(
                selectedColor, // Color for the checked state
                Color.GRAY // Color for the unchecked state
            )

        )
        cbBrackets!!.buttonTintList = colorStateList

        actDigits!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))

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

        if (cbBrackets!!.isChecked) {
            val stPoints: String = etPoints!!.text.toString().trim()

            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

            if (stPoints!!.isEmpty()) {
                etPoints!!.error = "Please Enter Point!!!"
                etPoints!!.requestFocus()
            } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints.toInt()) {
                val message = "You don't have required bid amount please add fund."
                dialogBoxMessage(message, "cancel")
            }else if (strGameSession.isEmpty()) {
                Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType,"green")
            } else if (providerResultData!!.gameDate.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            } else {
                bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this,"green")
                rvBidList!!.layoutManager = LinearLayoutManager(mContext)
                rvBidList!!.adapter = bidAdapter
                bidAdapter!!.notifyDataSetChanged()

                redBracketArray.forEach {
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
                bidAdapter!!.notifyDataSetChanged()
                etPoints!!.setText("")
            }


        } else {
            val stDigits: String = actDigits!!.text.toString().trim()
            val stPoints: String = etPoints!!.text.toString().trim()

            val walletBal =
                AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)
            if (stDigits!!.isEmpty() && stDigits!!.length != 2) {
                actDigits!!.error = "Please enter valid Digit"
                actDigits!!.requestFocus()
            } else if (stPoints!!.isEmpty()) {
                etPoints!!.error = "Please Enter Point!!!"
                etPoints!!.requestFocus()
            }else if (stPoints!!.startsWith("0")) {
                dialogBoxMessage("Please enter valid point", "")
            }   else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints.toInt()) {
                val message = "You don't have required bid amount please add fund."
                dialogBoxMessage(message, "cancel")
            } else if (strGameSession.isEmpty()) {
                Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType,"green")
            } else if (providerResultData!!.gameDate.isEmpty()) {
                dialogBoxMessage("Select Date", "cancel")
            }else {
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
                        providerResultData!!.gameDate,
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
        actDigits!!.setText("")
        etPoints!!.setText("")
//        if (bidItems!!.size == 0) {
//            actDigits!!.setText("")
//            etPoints!!.setText("")
////            dialogBoxMessage("Please enter valid digits", "Cancel")
//        }

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

    private var providerResultData: com.kasa777.ui.fragment.jackpot_fragments.modal.Result? =
        null
    private var tabGameDate:RelativeLayout?=null
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
    private var tabAddBid: TextView? = null
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
        val confirmBtn = dialog.findViewById<RelativeLayout>(R.id.confirmBtn)
        confirmBtn.setBackgroundResource(R.drawable.green_confirm_button)
        val cancelBtn = dialog.findViewById<RelativeLayout>(R.id.cancelBtn)
        cancelBtn.setBackgroundResource(R.drawable.green_cancel_button)
        val cancelText = dialog.findViewById<TextView>(R.id.tvCancel)
        cancelText.setTextColor(ContextCompat.getColor(context!!, R.color.greenThemeColor))
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
                                        responseObject.getString("message"),"green"
                                    )
                                } else {
                                    Alerts.AlertDialogWarning(
                                        context,

                                        responseObject.getString("message"),"green"
                                    )
                                }
                            }

                            override fun onFail(response: String?) {
                                Alerts.AlertDialogWarning(context,  response,"green")
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
        btnSubmit.setBackgroundResource(R.drawable.green_confirm_button)
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
