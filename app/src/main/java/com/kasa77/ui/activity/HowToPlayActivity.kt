package com.kasa77.ui.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.kasa77.R
import com.kasa77.modal.static_data.howtoplay.Datum
import com.kasa77.modal.static_data.howtoplay.HowToPlayData
import com.kasa77.retrofit_provider.AuthHeaderRetrofitService
import com.kasa77.retrofit_provider.RetrofitApiClient
import com.kasa77.retrofit_provider.WebResponse
import com.kasa77.utils.Alerts
import com.kasa77.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_how_to_play.*
import retrofit2.Response

class HowToPlayActivity :  YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener ,View.OnClickListener {
    private var youtubeLink = ""
    var VIDEO_ID = ""
    private var mContext :Context?= null
    private var cd : ConnectionDetector? = null
    private var authRetrofitApiClient :RetrofitApiClient? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)
        mContext = this
        cd = ConnectionDetector(mContext)
        authRetrofitApiClient = AuthHeaderRetrofitService.getRetrofit()




        howToPlayApi()

      /*  try {
            VIDEO_ID  = youtubeLink.substring(youtubeLink.lastIndexOf("/"))
            Log.e("youbeId",VIDEO_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/


//        userWalletCheck()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun howToPlayApi() {
        if (cd!!.isNetworkAvailable) {
            AuthHeaderRetrofitService.howToPlay(
                mContext?.let { Dialog(it) },
                authRetrofitApiClient!!.howToPlayData(),
                object : WebResponse {
                    override fun onResponseSuccess(result: Response<*>?) {
                        val responseBody: HowToPlayData = result?.body() as HowToPlayData
                        if (responseBody.status == 1){
                            val data:  ArrayList<Datum> = responseBody.data as ArrayList<Datum>

                            tvTitle.text = data[0].title
                            tvDescription.text = data[0].description
                            youtubeLink  = data[0].videoUrl

                            try {
                                VIDEO_ID  = youtubeLink.substring(youtubeLink.lastIndexOf("/"))
                                 VIDEO_ID = VIDEO_ID.replace("/","")
                                Log.e("youbeId",VIDEO_ID)
                                youtube_view.initialize(R.string.youtube_api_key.toString(),this@HowToPlayActivity)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    override fun onResponseFailed(error: String?) {
                        Alerts.show(mContext, error)
                    }

                })
        }
    }


    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasStored: Boolean) {
        if (null == player) return

        // Start buffering
        if (!wasStored) {
            player.cueVideo(VIDEO_ID)
        }

        // Add listeners to YouTubePlayer instance
        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener { override fun onAdStarted() {}
            override fun onError(arg0: YouTubePlayer.ErrorReason) {}
            override fun onLoaded(arg0: String) {}
            override fun onLoading() {}
            override fun onVideoEnded() {}
            override fun onVideoStarted() {}
        })


        player.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
            override fun onBuffering(arg0: Boolean) {}
            override fun onPaused() {}
            override fun onPlaying() {}
            override fun onSeekTo(arg0: Int) {}
            override fun onStopped() {}
        })
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

        }

    }


    fun onBackClick(view: View) {
        onBackPressed()
    }




}
