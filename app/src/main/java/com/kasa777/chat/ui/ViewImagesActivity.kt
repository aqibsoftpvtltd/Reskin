package com.kasa777.chat.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.kasa777.R
import com.kasa777.chat.ChatConstants
import com.kasa777.chat.DBHelper
import com.kasa777.chat.model.MessageModal
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_view_images.*


class ViewImagesActivity : BaseActivity() {


    private lateinit var chatMessageList: java.util.ArrayList<MessageModal>
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_images)
        val arrayList: ArrayList<String> = ArrayList<String>()
        val imagePath = intent.extras!!.getString("IMAGE_PATH").toString()
        val imageid = intent.extras!!.getString("IMAGE_ID").toString()
        var index: Int = 0
        val intentFilename: String = imagePath.substring(
            imagePath.lastIndexOf("/") + 1
        )

        dbHelper = DBHelper(this@ViewImagesActivity)
        chatMessageList = dbHelper!!.getAllMessageFromDB()

        var counter: Int = 0
        for (chatMsg in chatMessageList) {
            if (chatMsg.messStatus != 3)
                if (chatMsg.msgContentType == 2 || chatMsg.msgContentType == 3) {
                    counter = counter + 1
                    arrayList.add(ChatConstants.mediaBaseUrl + chatMsg.images)
                    if (imageid.equals(chatMsg.id)) {
                        index = counter
                    }

                }
        }

        var isFoundInDB: Boolean = false
        for (i in 0..arrayList.size -1) {
            if (arrayList.get(i).contains(intentFilename)) {
                index = i+1
                isFoundInDB = true
                break
            }
        }

        if (!isFoundInDB) {
            arrayList.add(imagePath);
            index = arrayList.size
        }


        Log.e("index", index.toString())
        val myCustomPagerAdapter = ImagesPagerAdapter(this@ViewImagesActivity, arrayList)
        viewPager!!.setAdapter(myCustomPagerAdapter)
        viewPager!!.setCurrentItem(index - 1, true)
        viewPager!!.setPageTransformer(true, DepthPageTransformer())

    }


    class DepthPageTransformer : ViewPager.PageTransformer {

        public val MIN_SCALE = 0.75f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationX = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position

                        // Counteract the default slide transition
                        translationX = pageWidth * -position

                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
