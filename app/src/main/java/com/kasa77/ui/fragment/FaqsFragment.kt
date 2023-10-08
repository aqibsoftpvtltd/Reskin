package com.kasa77.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasa77.R
import com.kasa77.constant.Constant
import com.kasa77.ui.NavigationMainActivity
import com.kasa77.utils.FragmentUtils
import kotlinx.android.synthetic.main.app_bar_main.gameTitle
import kotlinx.android.synthetic.main.app_bar_main.gamedesc
import kotlinx.android.synthetic.main.app_bar_main.tabHomePageView
import kotlinx.android.synthetic.main.content_main.bottomBack
import kotlinx.android.synthetic.main.content_main.topMenu
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


class FaqsFragment : Fragment() {

    private var rootview: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_faqs, container, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        goToHomePageBtn.visibility = View.GONE

        goToHomePageBtn.setOnClickListener {

            /* var fragmentUtils: FragmentUtils?  = FragmentUtils(parentFragmentManager)

            topMenu.visibility = View.VISIBLE
            bottomBack.setBackgroundResource(R.drawable.home_curve_bottom_background)
            gameTitle.text = resources.getString(R.string.app_name_full)
            tabHomePageView.visibility=View.VISIBLE
            gamedesc.visibility=View.VISIBLE
            fragmentUtils?.replaceFragment(HomePageFragment(), Constant.LiveResultsFragment, R.id.home_frame)*/


        }
    }

}