package com.kasa777.ui.fragment

import android.os.Bundle
import android.view.View
import com.kasa777.R
import com.kasa777.utils.BaseActivity
import kotlinx.android.synthetic.main.fragment_faqs.five
import kotlinx.android.synthetic.main.fragment_faqs.fiveArrow
import kotlinx.android.synthetic.main.fragment_faqs.fiveDetail
import kotlinx.android.synthetic.main.fragment_faqs.four
import kotlinx.android.synthetic.main.fragment_faqs.fourArrow
import kotlinx.android.synthetic.main.fragment_faqs.fourDetail
import kotlinx.android.synthetic.main.fragment_faqs.goToHomePageBtn
import kotlinx.android.synthetic.main.fragment_faqs.one
import kotlinx.android.synthetic.main.fragment_faqs.oneArrow
import kotlinx.android.synthetic.main.fragment_faqs.oneDetail
import kotlinx.android.synthetic.main.fragment_faqs.six
import kotlinx.android.synthetic.main.fragment_faqs.sixArrow
import kotlinx.android.synthetic.main.fragment_faqs.sixDetail
import kotlinx.android.synthetic.main.fragment_faqs.three
import kotlinx.android.synthetic.main.fragment_faqs.threeArrow
import kotlinx.android.synthetic.main.fragment_faqs.threeDetail
import kotlinx.android.synthetic.main.fragment_faqs.two
import kotlinx.android.synthetic.main.fragment_faqs.twoArrow
import kotlinx.android.synthetic.main.fragment_faqs.twoDetail
import kotlinx.android.synthetic.main.toolbar.backBtn
import kotlinx.android.synthetic.main.toolbar.toolbarTitle


class FaqsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_faqs)


        toolbarTitle.setText("FAQs")
        backBtn.setOnClickListener { finish() }
        one.setOnClickListener {
            if (oneDetail.visibility==View.GONE){
                oneArrow.setImageResource(R.drawable.faqs_up_arrow)
                oneDetail.visibility=View.VISIBLE
            }
            else{
                oneArrow.setImageResource(R.drawable.faqs_down_arrow)
                oneDetail.visibility=View.GONE
            }
        }
        two.setOnClickListener {
            if (twoDetail.visibility==View.GONE){
                twoArrow.setImageResource(R.drawable.faqs_up_arrow)
                twoDetail.visibility=View.VISIBLE
            }
            else{
                twoArrow.setImageResource(R.drawable.faqs_down_arrow)
                twoDetail.visibility=View.GONE
            }
        }
        three.setOnClickListener {
            if (threeDetail.visibility==View.GONE){
                threeArrow.setImageResource(R.drawable.faqs_up_arrow)
                threeDetail.visibility=View.VISIBLE
            }
            else{
                threeArrow.setImageResource(R.drawable.faqs_down_arrow)
                threeDetail.visibility=View.GONE
            }
        }
        four.setOnClickListener {
            if (fourDetail.visibility==View.GONE){
                fourArrow.setImageResource(R.drawable.faqs_up_arrow)
                fourDetail.visibility=View.VISIBLE
            }
            else{
                fourArrow.setImageResource(R.drawable.faqs_down_arrow)
                fourDetail.visibility=View.GONE
            }
        }
        five.setOnClickListener {
            if (fiveDetail.visibility==View.GONE){
                fiveArrow.setImageResource(R.drawable.faqs_up_arrow)
                fiveDetail.visibility=View.VISIBLE
            }
            else{
                fiveArrow.setImageResource(R.drawable.faqs_down_arrow)
                fiveDetail.visibility=View.GONE
            }
        }
        six.setOnClickListener {
            if (sixDetail.visibility==View.GONE){
                sixArrow.setImageResource(R.drawable.faqs_up_arrow)
                sixDetail.visibility=View.VISIBLE
            }
            else{
                sixArrow.setImageResource(R.drawable.faqs_down_arrow)
                sixDetail.visibility=View.GONE
            }
        }
        goToHomePageBtn.visibility = View.VISIBLE

        goToHomePageBtn.setOnClickListener {

           finish()


        }
    }

}