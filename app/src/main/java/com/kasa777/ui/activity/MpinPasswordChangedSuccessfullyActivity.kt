package com.kasa777.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.kasa777.R
import kotlinx.android.synthetic.main.activity_mpin_password_changed_successfully.*

class MpinPasswordChangedSuccessfullyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpin_password_changed_successfully)

        Glide.with(this).load(R.drawable.password_success).into(imgVerified1)
        val from = intent.getStringExtra("from")
        if (from.equals("changePassword")){
            Handler().postDelayed(Runnable {
                val intent = Intent(this@MpinPasswordChangedSuccessfullyActivity, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY

                startActivity(intent)
//                finish()
            }, 2000)
        }
    }

}
