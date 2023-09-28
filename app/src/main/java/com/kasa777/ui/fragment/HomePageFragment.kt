package com.kasa777.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.kasa777.R
import com.kasa777.adapter.kuberdashboard_adapter.KuberDashboardGameAdapter
import com.kasa777.constant.Constant
import com.kasa777.modal.dashboard_gamelist.DashBoardGames
import com.kasa777.modal.kuber_dashboard_games.OpenCloseTime
import com.kasa777.modal.kuber_dashboard_games.Provider
import com.kasa777.modal.static_data.walletcontact.WalletContactData
import com.kasa777.retrofit_provider.AuthHeaderRetrofitService
import com.kasa777.retrofit_provider.RetrofitApiClient
import com.kasa777.retrofit_provider.WebResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasa777.chat.ui.ChatBoardActivity
import com.kasa777.utils.*
import kotlinx.android.synthetic.main.activity_navigation_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_select_contact_type.view.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class HomePageFragment : Fragment(), View.OnClickListener {
    private var gameList: ArrayList<Provider> = ArrayList()
    private var bidOpenCloseTime: ArrayList<OpenCloseTime> = ArrayList()
    private var dashboardAdapter: KuberDashboardGameAdapter? = null
    private var colorName = ArrayList<String>()
    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    private var updateWalletAmountListener: UpdateWalletAmountListener? = null
    private var fragmentUtils: FragmentUtils? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()
//        getAppBanner()
        tvMessage.visibility = View.GONE
        cvItem1.setOnClickListener(this)
//        llContactDialog.setOnClickListener(this)

        loadGame()
        walletContact()

        swipPage.setOnRefreshListener {
            walletContact()
            try {
                updateWalletAmountListener!!.onUpdateWallet()
            } catch (e: Exception) {
            }
            loadGame() // refresh your list contents somehow
            swipPage.isRefreshing = false
        }
        tabAdminSupport.setOnClickListener{
            startActivity(Intent(mContext, ChatBoardActivity::class.java))
        }
        tabTalegramID.setOnClickListener{

        }
        val parentActivity = requireActivity()


        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
//            R.id.llContactDialog -> {
//                val mobile = tvWalletContact.text.toString()
//                if (mobile.isNotEmpty()) {
//                    dialogContact(mobile)
//                } else {
//                    Alerts.AlertDialogWarning(mContext, "No contact information provided!!!")
//                }
//            }
        }
    }


    private fun loadGame() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.getKuberDashboardMainData(
                Dialog(requireActivity()),
                retrofitApiClient!!.kuberMorningDashboardData(),
                object :
                    WebResponse {
                    @SuppressLint("SetTextI18n", "NewApi")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonresponse = JSONObject(response.string())
                        Log.e("OnResponse", jsonresponse.toString())
                        val dashBoardGames: DashBoardGames = Gson().fromJson(
                            jsonresponse.toString(),
                            object : TypeToken<DashBoardGames?>() {}.getType()
                        )
                        if (dashBoardGames.status == 1) {
                            val resultarray = dashBoardGames.result.values.toTypedArray();
                            val list = Arrays.asList(*resultarray)
//                            Helper(context).getSortedListDashboard(list)
                            Helper(context).getSortedListDashboard(list)

                            try {
                                rvKuberMorningDashboard!!.setHasFixedSize(true)
                                rvKuberMorningDashboard!!.layoutManager =
                                    LinearLayoutManager(mContext)
                                dashboardAdapter = KuberDashboardGameAdapter(list)
                                rvKuberMorningDashboard!!.adapter = dashboardAdapter
                                dashboardAdapter!!.notifyDataSetChanged()
                            } catch (e: Exception) {
                            }
                        }

                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })
        }
    }


    @SuppressLint("InflateParams")
    private fun dialogContact(string: String) {
        val dialogBuilder = mContext?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_select_contact_type, null)
        dialogBuilder?.setView(dialogView)
        dialogBuilder?.setCancelable(true)
        val alertDialog = dialogBuilder?.create()
        alertDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val btnWhatsappContact = dialogView.llWhatsappContact
        val btnPhoneCall = dialogView.llPhoneCall
        val iv_close = dialogView.iv_close

        iv_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                alertDialog.dismiss()
            }
        })
        btnWhatsappContact.setOnClickListener {

            val contact = "+91 $string"
            val url = "https://api.whatsapp.com/send?phone=$contact"
            try {
                val pm = requireContext().packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: java.lang.Exception) {
                Alerts.AlertDialogWarning(context, "Whatsapp app not installed in your phone!!!")
                e.printStackTrace()
            }


            alertDialog.dismiss()
        }
        btnPhoneCall.setOnClickListener {
            try {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:$string")
                requireActivity().startActivity(callIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Alerts.AlertDialogWarning(context, "No Sim Card Found!!!")

            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun walletContact() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.walletContact(
                mContext?.let { Dialog(it) },
                retrofitApiClient!!.walletContact(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: WalletContactData = result?.body() as WalletContactData
                        if (responseBody.status == 1) {
                            if (responseBody.data.size > 0) {
                                try {
//                                    tvWalletContact!!.text = responseBody.data[0].number

                                    AppPreference.setStringPreference(context,"PHONE",responseBody.data[0].number)
                                } catch (e: Exception) {
                                }
                            }
                        } else {
//                            Alerts.SessionLogout(context, activity)

//                            tvWalletContact.text = responseBody.message.toString()
                        }

                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }

                })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UpdateWalletAmountListener) {
            updateWalletAmountListener = context
        }
    }


    public interface UpdateWalletAmountListener {
        public fun onUpdateWallet();
    }

    public fun getAppBanner() {
        if (cd!!.isNetworkAvailable) {
            val userLoginId = AppPreference.getStringPreference(mContext, Constant.USER_LOGIN_ID)
            val mObject = JSONObject()
            mObject.put("id", userLoginId)
            val body = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                (mObject).toString()
            )
            AuthHeaderRetrofitService.getServerResponse(
                Dialog(requireActivity()),
                AuthHeaderRetrofitService.getRetrofit()?.getAppBanner(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")

                    override fun onResponseSuccess(result: Response<*>?) {
                        val response = result!!.body() as ResponseBody
                        val jsonObject = JSONObject(response.string())
                        Log.e(
                            "Response",
                            jsonObject.toString()
                        ) ///{"status":1,"message":"Success","imagePath":"\/appBanner\/banner.png"}
                        if (jsonObject.getInt("status") == 1) {
                            val imagePath = jsonObject.getString("imagePath")
//                            Glide.with(context!!).load(RetrofitService.BASE_URL + imagePath)
//                                .into(app_banner)
                        }
                    }

                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }
                })

        }
    }
}