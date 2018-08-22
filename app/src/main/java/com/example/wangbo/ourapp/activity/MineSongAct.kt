package com.example.wangbo.ourapp.activity

import android.os.Bundle

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.MineSongAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.jackmar.jframelibray.base.JBaseAct

import butterknife.BindView
import com.example.wangbo.ourapp.bean.SongBean

/**
 * Created by wangbo on 2018/8/6.
 *
 *
 * 我喜欢的歌曲
 */
class MineSongAct : JBaseAct() {

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    lateinit var mineSongAdapter: MineSongAdapter

    private var listData: ArrayList<SongBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.view_single_recylerview, true, "我收藏的歌曲")
    }

    override fun initView() {
        mineSongAdapter = MineSongAdapter(listData, context)
        commonList.adapter = mineSongAdapter
    }

    override fun initData() {

    }
}
