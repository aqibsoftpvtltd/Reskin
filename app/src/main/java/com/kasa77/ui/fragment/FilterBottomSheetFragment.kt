package com.kasa77.ui.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa77.R
import com.kasa77.adapter.FilterAdapter
import com.kasa77.modal.filterdata.FilterList
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.ConnectionDetector
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_filter_bottom_sheet_dialog.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.ArrayList

class FilterBottomSheetFragment : DialogFragment() {

    private lateinit var adapter: FilterAdapter
    private var caseValue: String? = "1"
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null

    var onApplyFilter: OnApplyFilter? = null
    var gameTypeList: ArrayList<String>? = ArrayList()
    var winTypeList: ArrayList<Int>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.fragment_filter_bottom_sheet_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cd = ConnectionDetector(context)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
        caseValue = requireArguments().getString("case");

        if(caseValue.equals("3") || caseValue.equals("2")){
            ll_gameType.visibility=View.GONE
        }else{
            ll_gameType.visibility=View.VISIBLE
        }

        loadFilterList()

        btn_submit_filter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                dialog!!.dismiss()
            }
        })
        btn_cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                dialog!!.dismiss()
            }
        })

        rl_open.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!open_checkbox.isChecked) {
                    open_checkbox.isChecked = true;
                    gameTypeList!!.add("Open")
                } else {
                    open_checkbox.isChecked = false;
                    gameTypeList!!.remove("Open")
                }
            }
        })
        rl_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!close_checkbox.isChecked) {
                    close_checkbox.isChecked = true;
                    gameTypeList!!.add("Close")
                } else {
                    close_checkbox.isChecked = false;
                    gameTypeList!!.remove("Close")
                }
            }
        })
        rl_win.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!win_checkbox.isChecked) {
                    win_checkbox.isChecked = true;
                    winTypeList!!.add(1)
                } else {
                    win_checkbox.isChecked = false;
                    winTypeList!!.remove(1)
                }
            }
        })
        rl_loss.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!loss_checkbox.isChecked) {
                    loss_checkbox.isChecked = true;
                    winTypeList!!.add(2)
                } else {
                    loss_checkbox.isChecked = false;
                    winTypeList!!.remove(2)
                }
            }
        })
        rl_pending.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!pending_checkbox.isChecked) {
                    pending_checkbox.isChecked = true;
                    winTypeList!!.add(0)
                } else {
                    pending_checkbox.isChecked = false;
                    winTypeList!!.remove(0)
                }
            }
        })

        pending_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                winTypeList!!.add(0)
            } else {
                winTypeList!!.remove(0)
            }
        }
        loss_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                winTypeList!!.add(2)
            } else {
                winTypeList!!.remove(2)
            }
        }
        win_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                winTypeList!!.add(1)
            } else {
                winTypeList!!.remove(1)
            }
        }
        close_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gameTypeList!!.add("Close")
            } else {
                gameTypeList!!.remove("Close")
            }
        }

        open_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                gameTypeList!!.add("Open")
            } else {
                gameTypeList!!.remove("Open")
            }

        }


    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (context is OnApplyFilter) {
            onApplyFilter = context as OnApplyFilter
            if (adapter!!.checkedIds.size == 0 && gameTypeList!!.size==0 && winTypeList!!.size==0) {
                onApplyFilter!!.onClear(caseValue)
            } else {
                onApplyFilter!!.onApply(adapter!!.checkedIds,gameTypeList,winTypeList, caseValue)
            }
        }
    }

    private fun loadFilterList() {
        if (cd!!.isNetworkAvailable) {
            val mObject = JSONObject()
            mObject.put("case", caseValue)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            progress_loader.visibility = View.VISIBLE
            btn_submit_filter.visibility = View.GONE
            AuthHeaderRetrofitService.getServerResponse(
                null,
                retrofitApiClient!!.getFilterList(body),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        progress_loader.visibility = View.GONE
                        btn_submit_filter.visibility = View.VISIBLE
                        var filterList = Gson().fromJson(
                            jsonresponse.toString(),
                            FilterList::class.java
                        )


                        try {
                            rv_filterlist!!.setHasFixedSize(true)
                            rv_filterlist!!.layoutManager = LinearLayoutManager(context)
                            adapter = FilterAdapter(context, filterList.data)

                            rv_filterlist!!.adapter = adapter
                            adapter!!.notifyDataSetChanged()
                        } catch (e: Exception) {
                        }

                    }

                    override fun onResponseFailed(error: String?) {
                        progress_loader.visibility = View.GONE
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }


    companion object {
        fun newInstance(content: String): FilterBottomSheetFragment {
            val f = FilterBottomSheetFragment()

            // Supply num input as an argument.
            val args = Bundle()
            args.putString("case", content)
            f.arguments = args

            return f
        }
    }


    public interface OnApplyFilter {
        fun onApply(
            checkedIds: ArrayList<String>,
            gameTypeList: ArrayList<String>?,
            winTypeList: ArrayList<Int>?, caseValue: String?)
        fun onClear(caseValue: String?)

    }

}