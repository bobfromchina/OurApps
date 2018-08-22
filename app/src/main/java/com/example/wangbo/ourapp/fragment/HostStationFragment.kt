package com.example.wangbo.ourapp.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.bigkoo.convenientbanner.ConvenientBanner
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.HostStationAdapter
import com.example.wangbo.ourapp.base.BannerBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.utils.NetWorkImageHolderView
import com.jackmar.jframelibray.base.JBaseFg
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import java.util.ArrayList

import butterknife.BindView
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.bean.DjRecommend
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter

/**
 * Created by wangbo on 2018/7/31.
 *
 *
 * 主播电台
 */
class HostStationFragment : JBaseFg(), View.OnClickListener, RecyclerListAdapter.OnItemClickedListener<DjRecommend> {

    private lateinit var hostStationAdapter: HostStationAdapter

    private var bannerList = ArrayList<String>()

    lateinit var banner: ConvenientBanner<String>

    private lateinit var one: TextView

    private lateinit var two: TextView

    private lateinit var three: TextView

    private lateinit var four: TextView

    private lateinit var title: TextView

    val listData: ArrayList<DjRecommend> = ArrayList()

    @BindView(R.id.gv_comment)
    lateinit var gvComment: RecyclerViewHeaderAndFooter

    override fun getLayoutId(): Int {
        return R.layout.fragment_host_station
    }

    override fun initView() {

        val headerView = context.layoutInflater.inflate(R.layout.view_header_music, null, false)
        one = headerView.findViewById(R.id.one)
        two = headerView.findViewById(R.id.two)
        three = headerView.findViewById(R.id.three)
        four = headerView.findViewById(R.id.four)
        title = headerView.findViewById(R.id.tv_title)
        banner = headerView.findViewById(R.id.cb_banner)
        one.setOnClickListener(this)
        two.setOnClickListener(this)
        three.setOnClickListener(this)
        four.setOnClickListener(this)
        gvComment.addHeaderView(headerView)

        hostStationAdapter = HostStationAdapter(listData, context)

        gvComment.adapter = hostStationAdapter
        gvComment.setHasFixedSize(true)
        hostStationAdapter.setOnItemClickedListener(this)

        one.text = "电台分类"
        two.text = "电台排行"
        three.text = "付费精品"
        four.text = "小冰电台"
        title.text = "今日优选"

        initBanner()
        getBannerInfo()
    }

    private fun getBannerInfo() {
        HttpHelper.getInstance(context).homeBanner("", ProgressSubscriber(context, IOnNextListener<BannerBean> { takeEntity ->
            val list = takeEntity.getBannerList()
            if (bannerList.isEmpty()) {
                for (bean in list)
                    bannerList.add(bean.bannerImgUrl)
                banner.setPages({ NetWorkImageHolderView() }, bannerList)
            }
        }))
    }

    private fun initBanner() {
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        banner.setPageIndicator(intArrayOf(R.drawable.banner_page_indicator_ponit_uncheck, R.drawable.banner_page_indicator_point_checked))
        banner.startTurning(5000)//5秒一翻页
        banner.isCanLoop = true//无限循环
    }

    override fun initData() {
        getPageData()
    }

    private fun getPageData() {

        HttpHelper.getInstance(context).getDjCommend("", ProgressSubscriber(context, IOnNextListener<List<DjRecommend>> { dj ->
            hostStationAdapter.setData(dj)
        }))
    }

    companion object {
        fun newInstance(): HostStationFragment {
            val bundle = Bundle()
            val fragment = HostStationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.one -> {
            }
            R.id.two -> {
            }
            R.id.three -> {
            }
            R.id.four -> {
            }
        }
    }

    override fun onClicked(position: Int, t: DjRecommend?) {

    }
}
