package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RankingListAdapter
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.bean.RankingListBean
import com.jackmar.jframelibray.base.JBaseAct

import java.util.ArrayList

import butterknife.BindView
import com.example.wangbo.ourapp.utils.OurAnimation

/**
 * Created by wangbo on 2018/8/15.
 *
 *
 * 排行榜页面
 */
class RankingListAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<RankingListBean> {

    private lateinit var rankingListAdapter: RankingListAdapter

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private val data: List<RankingListBean>
        get() {
            val list = ArrayList<RankingListBean>()
            list.add(RankingListBean(3, R.drawable.biaoshengbang, "飙升榜", "每天更新"))
            list.add(RankingListBean(0, R.drawable.xingebang, "新歌榜", "每天更新"))
            list.add(RankingListBean(2, R.drawable.yuanchuangbang, "原创榜", "每周四更新"))
            list.add(RankingListBean(1, R.drawable.regebang, "热歌榜", "每周四更新"))
            list.add(RankingListBean(3, R.drawable.biaoshengbang, "飙升榜", "每天更新"))
            list.add(RankingListBean(7, R.drawable.ktv, "KTV麦榜", "每周五更新"))
            list.add(RankingListBean(14, R.drawable.huayubang, "中国TOP排行榜(港台榜)", "每周一更新"))
            list.add(RankingListBean(15, R.drawable.huayu, "中国TOP排行榜(内地榜)", "每周一更新"))
            list.add(RankingListBean(23, R.drawable.xihabang, "云音乐嘻哈榜", "每周五更新"))
            return list
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.actvity_ranking_list, true, "排行榜")
    }

    override fun initView() {
        rankingListAdapter = RankingListAdapter(data, context)
        val mGridLayoutManager = GridLayoutManager(context, 3)
        commonList.layoutManager = mGridLayoutManager
        commonList.adapter = rankingListAdapter
        OurAnimation.runLayoutAnimationFromBottom(commonList)

        rankingListAdapter.setOnItemClickedListener(this)
    }

    override fun initData() {

    }

    override fun onClicked(position: Int, rankingListBean: RankingListBean) {

    }
}
