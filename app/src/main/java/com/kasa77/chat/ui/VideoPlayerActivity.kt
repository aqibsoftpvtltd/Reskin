package com.kasa77.chat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kasa77.R
import com.kasa77.chat.ChatConstants
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val videoUrl = ChatConstants.mediaBaseUrl +intent.getStringExtra("VIDEO");
        fullscreenVideoView.videoUrl(videoUrl).enableAutoStart()
    }
}