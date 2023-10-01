package com.kasa77.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.modal.static_data.noticeboard.Datum
import com.kasa77.modal.static_data.noticeboard.NoticeBoardData
import com.kasa77.modal.static_data.walletcontact.WalletContactData
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.AppPreference
import com.kasa77.utils.ConnectionDetector
import com.kasa77.utils.LogoutUser
import kotlinx.android.synthetic.main.dialog_select_contact_type.view.*

import kotlinx.android.synthetic.main.fragment_notice_board.*
import retrofit2.Response

class NoticeBoardFragment : Fragment() {

    private var rootView: View? = null
    private var mContext: Context? = null
    private var cd: ConnectionDetector? = null
    private var retrofitApiClient: RetrofitApiClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_notice_board, container, false)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = activity
        cd = ConnectionDetector(mContext)
        retrofitApiClient = AuthHeaderRetrofitService.getRetrofit()

        tvMessage1.visibility = View.GONE

        noticeBoardApi()
        walletContact()
        rlCall.setOnClickListener {
            val mobile = tvContact.text.toString()
            if (mobile.isNotEmpty()) {
                dialogContact(mobile)
            } else {
                Alerts.show(mContext, "No contact information provided!!!")
            }  }

    }


    private fun noticeBoardApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.noticeboard(
                mContext?.let { Dialog(it) },
                retrofitApiClient!!.noticeBoardDate(),
                object : WebResponse {
                    @SuppressLint("SetTextI18n")
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: NoticeBoardData = result?.body() as NoticeBoardData
                        if (responseBody.status == 1){
                            if (responseBody.data.size>0) {

                                val data: ArrayList<Datum> = responseBody.data as ArrayList<Datum>
                                txtTitle1.text = data[0].title1
                                txtTitle2.text = data[0].title2
                                txtTitle3.text = data[0].title3
                                tvDescription1.text = data[0].description1
                                tvDescription2.text = data[0].description2
                                tvDescription3.text = data[0].description3
                            }else{
                                tvMessage1.visibility = View.VISIBLE
                                tvMessage1.text = "No data found"
                            }
                        }else{
                            logOutUser()
                            tvMessage1.visibility = View.VISIBLE
                            tvMessage1.text = responseBody.message.toString()
                        }
                    }
                    override fun onResponseFailed(error: String?) {
                        Alerts.serverError(context, error.toString())
                    }

                })
        }
    }
    private fun walletContact() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.walletContact(
                mContext?.let { Dialog(it) },
                retrofitApiClient!!.walletContact(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: WalletContactData = result?.body() as WalletContactData
                        if (responseBody.status == 1){
                            if (responseBody.data.size>0){
                                tvContact.text = responseBody.data[0].number
                            }else{
                                tvContact.text = responseBody.message.toString()
                            }
                        }else{
                            logOutUser()
                            tvContact.text = responseBody.message.toString()
                        }

                    }

                    override fun onResponseFailed(error: String?) {
//                        tvContact.text = error.toString()
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

        iv_close.setOnClickListener {
            alertDialog.dismiss()
        }
        btnWhatsappContact.setOnClickListener {

            val contact = "+91 $string"
            val url = "https://api.whatsapp.com/send?phone=$contact"
            try {
                val pm = requireActivity().packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    mContext,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(mContext, "No SIM Found", Toast.LENGTH_LONG).show()
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    private fun logOutUser():Boolean {
        var isAvailable = true
        val loginTime = AppPreference.getLongPreference(mContext, Constant.USER_LOGIN_TIME)
        val currentTime = System.currentTimeMillis()
        val difference = currentTime-loginTime
        isAvailable = false
        val logoutUser = LogoutUser(requireActivity(), activity!!)
        logoutUser.logout()

        return isAvailable
    }


}