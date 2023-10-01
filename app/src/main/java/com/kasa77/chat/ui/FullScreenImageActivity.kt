package com.kasa77.chat.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.kasa77.R
import com.kasa77.chat.FilePickUtils
import com.kasa77.chat.ImageListAdapter
import com.kasa77.utils.BaseActivity
import com.kasa77.utils.silli_croper.SiliCompressor
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import me.shaohui.advancedluban.Luban
import me.shaohui.advancedluban.OnMultiCompressListener
import java.io.File


class FullScreenImageActivity : BaseActivity() {

    private var from = ""
    private var rootView: View? = null
    private var images: ArrayList<ChosenImage>? = null
    private var images_path: ArrayList<String> = ArrayList()
    private var compressedImages: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        from = intent.extras!!.getString("FROM").toString()
        images = intent.extras!!.getParcelableArrayList("IMAGE_PATH")
        Log.e("imagePath", images!!.size.toString())
        rootView = findViewById(R.id.rootView)



        for (singleImage in images!!) {
            images_path!!.add(singleImage!!.originalPath)
        }
        initPrepareImageToSend(images_path)

        val myCustomPagerAdapter = ImagesPagerAdapter(this@FullScreenImageActivity, images_path)
        viewPager!!.setAdapter(myCustomPagerAdapter)
        viewPager!!.setPageTransformer(true, ViewImagesActivity.DepthPageTransformer())


        rv_multi_images!!.adapter =
            ImageListAdapter(this@FullScreenImageActivity, images_path,viewPager);
        rv_multi_images!!.adapter!!.notifyDataSetChanged()



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                FullScreenImageActivity.currentPosition = position
                rv_multi_images!!.adapter!!.notifyDataSetChanged()
            }


        })
    }


    private fun initPrepareImageToSend(imagesPath: ArrayList<String>?) {

        val fileList = ArrayList<File>()
        for (i in 0..imagesPath!!.size - 1) {
            val uri = Uri.parse(imagesPath.get(i))
            if (imagesPath.get(i).startsWith("content:"))
                imagesPath.set(
                    i,
                    FilePickUtils.getSmartFilePath(this@FullScreenImageActivity, uri)!!
                )


            if (!imagesPath.get(i).equals("")) {
                val file = File(imagesPath.get(i))
                fileList.add(file)
            }


        }

        compressMultiListener(fileList)


        fabConfirmImageText.isEnabled = false
        fabConfirmImageText.setOnClickListener {
            val msg = editTextMessage.text.toString()
            val returnIntent = Intent()
            returnIntent.putExtra("result", msg)
            returnIntent.putExtra("compressImagePath", compressedImages)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }


    internal inner class ImageCompressionAsyncTask(var mContext: Context) :
        AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {

            try {
                return SiliCompressor.with(mContext).compress(params[0], File(params[1]))
            } catch (e: Exception) {
                return params[0]
            }
            return ""
        }

        override fun onPostExecute(s: String) {
            if (!s.equals("")) {
                compressedImages.add(s)
            }
        }
    }

    companion object {
        @JvmField
        var currentPosition: Int = 0

    }

    private fun compressMultiListener(mFileList: List<File>) {
        if (mFileList.isEmpty()) {
            return
        }
        Luban.compress(this, mFileList)
            .putGear(Luban.THIRD_GEAR)
            .launch(object : OnMultiCompressListener {
                override fun onStart() {
                    Log.i("compress", "start")
                }

                override fun onSuccess(fileList: List<File>) {
                    var size = fileList.size
                    fabConfirmImageText.isEnabled = true
                    for (i in 0..fileList.size) {
                        Log.e("compress", fileList.get(i).path)
                        compressedImages.add(fileList.get(i).path)
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }
}


