package com.example.wangbo.ourapp.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView

import com.bigkoo.convenientbanner.ConvenientBanner
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.CommentSongsAct
import com.example.wangbo.ourapp.activity.RankingListAct
import com.example.wangbo.ourapp.activity.SongListDetailsAct
import com.example.wangbo.ourapp.activity.SongListAct
import com.example.wangbo.ourapp.adapter.CommentMusicListAdapter
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import com.example.wangbo.ourapp.bean.YunBanner
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.NetWorkImageHolderView
import com.jackmar.jframelibray.base.JBaseFg
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import java.util.ArrayList

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/31.
 *
 * 个性推荐
 */
class RecommendationFragment : JBaseFg(), RecyclerListAdapter.OnItemClickedListener<CommentMusicListBean>, View.OnClickListener {

    private var bannerList: ArrayList<String> = ArrayList()

    private var listData: ArrayList<CommentMusicListBean> = ArrayList()

    private lateinit var banner: ConvenientBanner<String>

    @BindView(R.id.gv_comment)
    lateinit var gvComment: RecyclerViewHeaderAndFooter

    private lateinit var commentMusicListAdapter: CommentMusicListAdapter

    private lateinit var one: TextView

    private lateinit var two: TextView

    private lateinit var three: TextView

    private lateinit var four: TextView

    private lateinit var title: TextView

    override fun getLayoutId(): Int {
        return R.layout.fragment_recommendation
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
        commentMusicListAdapter = CommentMusicListAdapter(listData, context)
        val mGridLayoutManager = GridLayoutManager(context, 3)
        mGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) mGridLayoutManager.spanCount else 1
            }
        }
        gvComment.layoutManager = mGridLayoutManager
        gvComment.adapter = commentMusicListAdapter
        gvComment.setHasFixedSize(true)
        commentMusicListAdapter.setOnItemClickedListener(this)

        one.text = "私人FM"
        two.text = "每日推荐"
        three.text = "歌单"
        four.text = "排行榜"
        title.text = "推荐歌单"

        initBanner()
        getBannerInfo()

        val bean = UserManager.getInstance().user
        if (bean.banner != null)
            loadBanner(bean.banner)
        if (bean.music != null)
            commentMusicListAdapter.setData(bean.music)
    }

    private fun getBannerInfo() {
        HttpHelper.getInstance(context).yunBanner(ProgressSubscriber(context, IOnNextListener<List<YunBanner>> { takeEntity ->
            loadBanner(takeEntity)
        }))
    }

    private fun loadBanner(list: List<YunBanner>) {
        if (!bannerList.isEmpty()) {
            bannerList.clear()
        }
        for (bean in list)
            bannerList.add(bean.picUrl)
        banner.setPages({ NetWorkImageHolderView() }, bannerList)

        val bean = UserManager.getInstance().user
        bean.banner = list
        UserManager.getInstance().updateUser(context, bean, false)
    }

    private fun initBanner() {
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        banner.setPageIndicator(intArrayOf(R.drawable.banner_page_indicator_ponit_uncheck, R.drawable.banner_page_indicator_point_checked))
        banner.startTurning(5000)
        banner.isCanLoop = true
    }

    override fun initData() {
        getCommentList()
    }

    /**
     * 获取推荐列表
     */
    private fun getCommentList() {
        HttpHelper.getInstance(context).getCommentList(UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<List<CommentMusicListBean>> { o ->
            commentMusicListAdapter.setData(o)
            val bean = UserManager.getInstance().user
            bean.music = o
            UserManager.getInstance().updateUser(context, bean, false)
        }))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.one -> showToast("开发中...")
            R.id.two -> launchActivity(CommentSongsAct::class.java)
            R.id.three -> launchActivity(SongListAct::class.java)
            R.id.four -> launchActivity(RankingListAct::class.java)
        }
    }

    override fun onClicked(position: Int, commentMusicListBean: CommentMusicListBean) {
        launchActivity(SongListDetailsAct::class.java, SongListDetailsAct.EXTRA_DASE_DATA, commentMusicListBean)
    }

    companion object {
        fun newInstance(): RecommendationFragment {
            val bundle = Bundle()
            val fragment = RecommendationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
