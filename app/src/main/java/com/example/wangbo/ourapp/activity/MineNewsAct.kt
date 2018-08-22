package com.example.wangbo.ourapp.activity

import android.os.Bundle
import butterknife.BindView
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.OneAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.bean.NewsBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import java.util.ArrayList

/**
 * Created by wangbo on 2018/8/21.
 *
 *  我的新闻
 */
class MineNewsAct : JBaseAct() {

    lateinit var adapter: OneAdapter

    @BindView(R.id.common_list)
    lateinit var list: RecyclerViewHeaderAndFooter

    private var listData: ArrayList<NewsBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.view_single_recylerview, true, "我的新闻")
    }

    override fun initView() {
        adapter = OneAdapter(listData, context)
        list.adapter = adapter
    }

    override fun initData() {
        getPageData()
    }

    private fun getPageData() {
        HttpHelper.getInstance(context).mineNewList(1, UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<List<NewsBean>> { bean -> adapter.setData(bean) }))
    }
}