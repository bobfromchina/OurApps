package com.example.wangbo.ourapp.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.FrameLayout
import android.widget.TextView

import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.listener.OnItemClickListener
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.WebViewActivity
import com.example.wangbo.ourapp.adapter.OneAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.base.PLEFragment
import com.example.wangbo.ourapp.bean.NewsBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.NetWorkImageHolderView
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

import java.util.ArrayList

import butterknife.BindView
import com.example.wangbo.ourapp.utils.OurAnimation

/**
 * Created by wangbo on 2018/8/14.
 *
 * 新闻页
 */
class NewFragment : PLEFragment(), OnItemClickListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.common_list)
    lateinit var listView: RecyclerViewHeaderAndFooter

    @BindView(R.id.refreshLayout)
    lateinit var mRefreshLayout: RefreshLayout

    @BindView(R.id.fl_fragment_list_empty_container)
    lateinit var container: FrameLayout

    private var bannerList = ArrayList<String>()

    private lateinit var oneAdapter: OneAdapter

    private lateinit var tvBannerTitle: TextView

    private lateinit var banner: ConvenientBanner<String>

    private var bannerLink: NewsBean? = null

    private var loadOnly: Int = 0

    private var pageIndex = 1

    private var listData: ArrayList<NewsBean> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.common_list
    }

    override fun initView() {

        mRefreshLayout.setOnLoadMoreListener(this)
        mRefreshLayout.setOnRefreshListener(this)

        if (loadOnly == 0) {
            val view = context.layoutInflater.inflate(R.layout.view_head_banner, null, false)

            if (listView.headerCount < 1) {
                listView.addHeaderView(view)
            }

            tvBannerTitle = view.findViewById(R.id.tv_banner_title)

            banner = view.findViewById(R.id.cb_banner)
            initBanner()

            oneAdapter = OneAdapter(listData, context)
            listView.layoutManager = LinearLayoutManager(context)
            listView.adapter = oneAdapter

            val id = arguments.getString(EXTRA_NAME)
            if (id == "1") {
                val userEntity = UserManager.getInstance().user
                val news = userEntity.news
                if (news != null && !news.isEmpty()) {
                    loadPageData(news)
                }
            }
            loadOnly = 1
        }
    }

    override fun initData() {
        loadPage(true)
    }

    private fun loadPage(isFirst: Boolean) {

        val id = arguments.getString(EXTRA_NAME)

        HttpHelper.getInstance(context).homeList(Integer.parseInt(id), pageIndex, ProgressSubscriber(context, IOnNextListener<List<NewsBean>> { news ->
            if (isFirst) {
                mRefreshLayout.finishRefresh()
            } else {
                mRefreshLayout.finishLoadMore()
            }

            if (news != null && !news.isEmpty()) {
                loadPageData(news as MutableList<NewsBean>?)
            }

            assert(id != null)
            if (id == "1") {
                val userEntity = UserManager.getInstance().user
                userEntity!!.news = news
                UserManager.getInstance().updateUser(context, userEntity, false)
            }
        }))
    }

    private fun loadPageData(bean: MutableList<NewsBean>?) {

        if (pageIndex == 1) {

            bannerLink = bean!![0]

            tvBannerTitle.text = bean[0].title

            if (!bannerList.isEmpty()) {
                bannerList.clear()
            }

            bannerList.add(bean[0].images[0])

            banner.setPages({ NetWorkImageHolderView() }, bannerList)

            bean.removeAt(0)

            for (i in bean.indices) {
                if (bean[i].images.isEmpty() || bean[i].images[0] == getString(R.string.ponitPonit)) {
                    bean.removeAt(i)
                }
            }
            oneAdapter.setData(bean)
        } else {
            oneAdapter.addDataToLast(bean)
        }

        if (pageIndex == 1) {
            OurAnimation.runLayoutAnimation(listView)
        }
    }

    private fun initBanner() {
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        banner.setPageIndicator(intArrayOf(R.drawable.banner_page_indicator_ponit_uncheck, R.drawable.banner_page_indicator_point_checked))
        banner.setOnItemClickListener(this)
        banner.startTurning(100000)//5秒一翻页
        banner.isCanLoop = false//无限循环
        banner.setPointViewVisible(false)
    }

    override fun onItemClick(position: Int) {
        if (bannerLink!!.linkUrl != null && bannerLink!!.linkUrl != "")
            launchActivity(WebViewActivity::class.java, WebViewActivity.EXTRA_ID, bannerLink)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageIndex = 1
        loadPage(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageIndex++
        loadPage(false)
    }

    companion object {

        const val EXTRA_NAME = "extra_name"

        fun getInstance(name: String): NewFragment {
            val bundle = Bundle()
            bundle.putString(NewFragment.EXTRA_NAME, name)
            val fragment = NewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
