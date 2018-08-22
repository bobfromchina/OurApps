package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.utils.PreHelper
import com.jackmar.jframelibray.utils.PreferenceKey
import com.jackmar.jframelibray.utils.ScreenUtil
import com.jackmar.jframelibray.utils.ViewUtil

import java.util.ArrayList

import butterknife.BindView

/**
 * handsome bob
 *
 *
 * 启动页面
 */
class WelcomeAct : JBaseAct() {

    @BindView(R.id.vp_pager)
    lateinit var mVpPager: ViewPager

    private val imageViews = ArrayList<ImageView>()

    private val res = intArrayOf(R.mipmap.img1)

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun initView() {
        for (re in res) {
            val imageView = ImageView(context)
            ViewUtil.setViewSize(imageView, ScreenUtil.defaultCenter().width, ScreenUtil.defaultCenter().height)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageResource(re)
            imageViews.add(imageView)

            imageView.setOnClickListener {
                launch(LoginAct::class.java)
                PreHelper.defaultCenter(context).setData(PreferenceKey.IS_FIRST, true)
                finish()
            }
        }
        viewPagerAdapter = ViewPagerAdapter(imageViews)
        mVpPager.adapter = viewPagerAdapter
    }

    override fun initData() {
        mVpPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    imageViews[0].setOnClickListener {
                        launch(LoginAct::class.java)
                        PreHelper.defaultCenter(context).setData(PreferenceKey.IS_FIRST, true)
                        finish()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.act_welcome, true)
    }

    inner class ViewPagerAdapter(private val list: List<ImageView>?) : PagerAdapter() {

        override fun getCount(): Int {
            return if (list != null && list.isNotEmpty()) {
                list.size
            } else {
                0
            }
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(list!![position])
            return list[position]
        }

        override fun getItemPosition(`object`: Any?): Int {
            return PagerAdapter.POSITION_NONE
        }
    }
}
