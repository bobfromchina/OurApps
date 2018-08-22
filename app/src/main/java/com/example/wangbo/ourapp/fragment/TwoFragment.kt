package com.example.wangbo.ourapp.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.base.IndicatorPagerAdapter
import com.example.wangbo.ourapp.utils.CommonUtils
import com.example.wangbo.ourapp.utils.PagerSlidingTabStrip
import com.example.wangbo.ourapp.utils.UpDownTextView
import com.example.wangbo.ourapp.utils.ViewUtils
import com.jackmar.jframelibray.base.JBaseFg

import java.util.ArrayList

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/3.
 *
 *
 * 板块2
 */
class TwoFragment : JBaseFg(), ViewPager.OnPageChangeListener {

    @BindView(R.id.search_view)
    lateinit var searchView: UpDownTextView

    @BindView(R.id.siv_activity_task_details_indicator)
    lateinit var psts: PagerSlidingTabStrip

    @BindView(R.id.vp_activity_task_details_pager)
    lateinit var viewPager: ViewPager

    lateinit var adapter: IndicatorPagerAdapter

    @BindView(R.id.view_line)
    lateinit var viewLine: View

    private lateinit var listFragment: ArrayList<Fragment>

    private lateinit var listName: ArrayList<String>

    var position: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fg_two
    }

    override fun initView() {
        val params = viewLine.layoutParams
        params.height = CommonUtils.getStatusBarHeight(context)
        viewLine.layoutParams = params

        val res = arrayOf("大家都在听 爸爸去哪儿了", "大笨象会跳舞 你学会了吗")
        searchView.setResources(res)
        searchView.setTextStillTime(5000)

        listFragment = ArrayList()
        listName = ArrayList()

        listName.add("个性推荐")
        listName.add("主播电台")

        listFragment.add(RecommendationFragment.newInstance())
        listFragment.add(HostStationFragment.newInstance())

        initViewPage()
    }

    private fun initViewPage() {
        //设置对应的适配器
        adapter = IndicatorPagerAdapter(childFragmentManager, listFragment, listName)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        //配置指示器
        psts.shouldExpand = true
        psts.tabPaddingLeftRight = 0
        psts.setViewPager(viewPager)
        psts.textSize = resources.getDimension(R.dimen.small_text).toInt()
        psts.setTextColorResource(R.drawable.filter_conditions_color)
        psts.indicatorHeight = ViewUtils.dp2pxF(2f)
        psts.indicatorColor = ContextCompat.getColor(context, R.color.white)
        psts.setTypeface(null, Typeface.NORMAL)
        psts.dividerColor = Color.TRANSPARENT//未选中的指示器
        psts.setOnPageChangeListener(this)
        psts.reduceIndicatorWidth(ViewUtils.dp2pxF(40f))
        psts.underlineColor = Color.TRANSPARENT

        //作用就是调整显示的界面状态和恢复界面
        viewPager.post {
            viewPager.currentItem = position
            onPageSelected(position)
        }
    }

    override fun initData() {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(positio: Int) {
        position = positio
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
