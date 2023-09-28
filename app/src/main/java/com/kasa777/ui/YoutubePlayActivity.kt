package com.kasa777.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayerView
import com.kasa777.R
import kotlinx.android.synthetic.main.activity_youtube_play.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class YoutubePlayActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_REQUEST = 1
    private val youTubeView: YouTubePlayerView? = null
    var VIDEO_ID = "Ps4aVpIESkc"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_play)
        youtube_view.initialize(R.string.youtube_api_key.toString(),this)

        var link = ""
        val intent = intent
        if(getIntent()!= null){
            link = intent.getStringExtra("youtubeLink").toString()
        }

        try {
             VIDEO_ID  = link.substring(link.lastIndexOf("="))
            Log.e("youbeId",VIDEO_ID)
        } catch (e: Exception) {
            e.printStackTrace()
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
        player.setPlayerStateChangeListener(object : PlayerStateChangeListener { override fun onAdStarted() {}
            override fun onError(arg0: YouTubePlayer.ErrorReason) {}
            override fun onLoaded(arg0: String) {}
            override fun onLoading() {}
            override fun onVideoEnded() {}
            override fun onVideoStarted() {}
        })


        player.setPlaybackEventListener(object : PlaybackEventListener {
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
}
