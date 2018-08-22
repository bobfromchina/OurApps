package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.SonglistAdapter
import com.example.wangbo.ourapp.bean.SongListBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/15.
 * 歌单
 */
class SonglistAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<SongListBean> {

    private lateinit var songlistAdapter: SonglistAdapter

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private var listData: ArrayList<SongListBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.actvity_song_list, true, "歌单")
    }

    override fun initView() {
        songlistAdapter = SonglistAdapter(listData, context)
        val mGridLayoutManager = GridLayoutManager(context, 2)
        commonList.layoutManager = mGridLayoutManager
        commonList.adapter = songlistAdapter

        songlistAdapter.setOnItemClickedListener(this)
    }

    override fun initData() {
        HttpHelper.getInstance(context).songlist("1", ProgressSubscriber(context, IOnNextListener<List<SongListBean>> { bean -> songlistAdapter.setData(bean) }))
    }

    override fun onClicked(position: Int, songListBean: SongListBean) {

    }
}
