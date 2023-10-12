package com.kasa777.ui.fragment.jackpot_fragments

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
import kotlinx.android.synthetic.main.fragment_group_jodi.ivGameDate

import kotlinx.android.synthetic.main.layout_bid_action_bottom_bar.submitBtn
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList


class GroupJodiFragment_Jackpot : Fragment(), View.OnClickListener {

    private val myList = arrayListOf<String>()
    private var onesArray = arrayOf("1", "2", "3", "4", "5")
    private var twosArray = arrayOf("6", "7", "8", "9", "0")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_jodi_digit, container, false)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        from = requireArguments().getString("From")!!

        if (from == GameTypeNames.GroupJackpot) {
            providerResultData = requireArguments().getParcelable("PROVIDER")
            fetchStarlineGameId()
        }

        initViews()

        tvFinalSubmit!!.setOnClickListener {
            if (bidItems!!.size > 0) {
                dialogBitSubmit(GameTypeNames.GroupJackpot)
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
        tabGameDate= rootView!!.findViewById(R.id.tabGameDate)
        tvGameDate = rootView!!.findViewById(R.id.tvGameDate)
        tvTotalBids = rootView!!.findViewById(R.id.tvTotalBids)
        tvTotalPoints = rootView!!.findViewById(R.id.tvTotalPoints)

        ivGameDate.setImageResource(R.drawable.calendar_green)

        tabAddBid!!.setBackgroundResource(R.drawable.green_button)
        submitBtn!!.setBackgroundResource(R.drawable.green_button)

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

      /*  val tvPannaCount = rootView!!.findViewById<TextView>(R.id.tvPannaCount)
        tvPannaCount!!.text = GameTypeNames.JodiDigit*/


    }

    private fun createBid() {
        hideKeyboard()
        val stDigits: String = actDigits!!.text.toString().trim()
        val stPoints: String = etPoints!!.text.toString().trim()
        val walletBal = AppPreference.getIntegerPreference(mContext, Constant.USER_WALLET_BALANCE)

        if (stDigits!!.isEmpty() && stDigits!!.length != 2) {
            actDigits!!.error = "Please Bid Digit!!!"
            actDigits!!.requestFocus()
        } else if (stPoints!!.isEmpty()) {
            etPoints!!.error = "Please Enter Point!!!"
            etPoints!!.requestFocus()
        }else if (stPoints!!.startsWith("0")) {
            dialogBoxMessage("Please enter valid point", "")
        } else if (stPoints!!.toInt() < GameConstantMessages.MinPointValue) {
            dialogBoxMessage(GameConstantMessages.MinPoint, "")
        } else if (stPoints!!.toInt() > GameConstantMessages.MaxPointValue) {
            dialogBoxMessage(GameConstantMessages.MaxPoint, "")
        } else if (walletBal < stPoints!!.toInt()) {
            val message = "You don't have required bid amount please add fund."
            dialogBoxMessage(message, "cancel")
        }  else if (strGameSession.isEmpty()) {
            Alerts.AlertDialogWarning(context, GameConstantMessages.SelectGameType,"green")
        } else if (providerResultData!!.gameDate.isEmpty()) {
            dialogBoxMessage("Select Date", "cancel")
        }else {
            bidAdapter = BidListToSubmitAdapter(mContext, bidItems, this,"green")
            rvBidList!!.layoutManager = LinearLayoutManager(mContext)
            rvBidList!!.adapter = bidAdapter
            bidAdapter!!.notifyDataSetChanged()
            if (stDigits!!.length == 2)
                generateJodiDigits(stDigits, stPoints)
            else {
                resetBidTableOnChangeDateOrSession()
                actDigits!!.setText("")
                etPoints!!.setText("")
                dialogBoxMessage("Please Enter valid Jodi", "Cancel")
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun generateJodiDigits(input: String, stPoints: String) {
        myList.clear()
        if (input.isNotEmpty()) {
            val d1 = input[0].toString()
            val d2 = input[1].toString()
            var r1 = ""
            var r2 = ""
            if (d1 != d2) {
                if (onesArray.contains(d1)) {
                    val i1 = onesArray.indexOf(d1)
                    r1 = twosArray[i1]
                } else if (twosArray.contains(d1)) {
                    val i2 = twosArray.indexOf(d1)
                    r1 = onesArray[i2]
                }
                if (onesArray.contains(d2)) {
                    val i3 = onesArray.indexOf(d2)
                    r2 = twosArray[i3]
                } else if (twosArray.contains(d2)) {
                    val i4 = twosArray.indexOf(d2)
                    r2 = onesArray[i4]
                }

                myList.add("$d2$d1")
                myList.add("$d2$r1")
                myList.add("$d1$d2")
                myList.add("$d1$r2")
                myList.add("$r2$d1")
                myList.add("$r2$r1")
                myList.add("$r1$d2")
                myList.add("$r1$r2")

                var newList = arrayListOf<String>()
                newList = removeDuplicates(myList!!)!!;
                myList.clear()
                myList.addAll(newList)

            } else {
                if (onesArray.contains(d1)) {
                    val i1 = onesArray.indexOf(d1)
                    r1 = twosArray[i1]
                } else if (twosArray.contains(d1)) {
                    val i2 = twosArray.indexOf(d1)
                    r1 = onesArray[i2]
                }

                myList.add("$d1$d1")
                myList.add("$d1$r1")
                myList.add("$r1$d1")
                myList.add("$r1$r1")
            }
            bidItems!!.clear()
            myList.forEach {
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
            actDigits!!.setText("")


        }
        if (bidItems!!.size == 0) {
            actDigits!!.setText("")
            etPoints!!.setText("")
            dialogBoxMessage("Please enter valid digits", "Cancel")
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

    fun <T> removeDuplicates(list: ArrayList<T>): ArrayList<T>? { // Create a new ArrayList
        val newList = ArrayList<T>()
        // Traverse through the first list
        for (element in list) { // If this element is not present in newList
// then add it
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        // return the new list
        return newList
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
                                //  Alerts.show(mContext, "Size : ${gameTypeArray.size}")
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

    /*new code modifications */
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var from = ""

    private var providerResultData: com.kasa777.ui.fragment.jackpot_fragments.modal.Result? =
        null

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

