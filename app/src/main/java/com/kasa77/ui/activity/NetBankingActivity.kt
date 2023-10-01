package com.kasa77.ui.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kasa77.R
import kotlinx.android.synthetic.main.activity_net_banking.*


class NetBankingActivity : AppCompatActivity() {

    private  var handler: Handler?=null
    private lateinit var runnable: Runnable

   companion object{
       private var status: String = "cancel"
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_banking)

        iv_back.visibility=View.GONE

        var url = intent.getStringExtra("URL");
//        var url = "https://buy.dhanagri.com/"
        webView.setInitialScale(1);
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings!!.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.isScrollbarFadingEnabled = false
        webView.settings.setSupportMultipleWindows(true);
        webView.settings.javaScriptCanOpenWindowsAutomatically = true;

        runnable =  Runnable {
           webView.clearHistory()
              onBackPressed()
       }
        handler =  Handler()

        webView.webChromeClient = object : WebChromeClient(){
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(this@NetBankingActivity)
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.setSupportZoom(true)
                newWebView.settings.builtInZoomControls = true
                newWebView.settings.pluginState = PluginState.ON
                newWebView.settings.setSupportMultipleWindows(true)
                view!!.addView(newWebView)
                val transport = resultMsg!!.obj as WebViewTransport
                transport.webView = newWebView
                resultMsg!!.sendToTarget()

                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        view.loadUrl(url)
                        return true
                    }
                }
                return true
            }
        }


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            //Show loader on url load
            override fun onLoadResource(view: WebView, url: String) {

            }

            override fun onPageFinished(view: WebView, url: String) {
                try {


                    Log.e("url", url)
                    if (url.contains("q=success", true)) {
                        status = "success"
                        iv_back.visibility=View.VISIBLE
                       handler!!.postDelayed(runnable,10000)
                    }else   if (url.contains("q=failure", true)) {
                        status = "failure"
                        iv_back.visibility=View.VISIBLE
                        handler!!.postDelayed(runnable,10000)
                    }else if (url.contains("q=cancel", true)) {
                        status = "cancel"
                        iv_back.visibility=View.VISIBLE
                        handler!!.postDelayed(runnable,10000)
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }

        //Load url in webView

        //Load url in webView
        webView.loadUrl(url!!)
    }

    override fun onBackPressed() {
        if(handler!=null){
            handler!!.removeCallbacks(runnable)
        }

        intent.putExtra("response", status)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()

    }

    fun onBackClick(view: View) {
        onBackPressed()
    }
}