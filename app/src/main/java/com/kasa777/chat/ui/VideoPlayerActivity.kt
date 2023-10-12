package com.kasa777.chat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kasa777.R
import com.kasa777.chat.ChatConstants
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val videoUrl = ChatConstants.mediaBaseUrl +intent.getStringExtra("VIDEO");
        fullscreenVideoView.videoUrl(videoUrl).enableAutoStart()
    }
}