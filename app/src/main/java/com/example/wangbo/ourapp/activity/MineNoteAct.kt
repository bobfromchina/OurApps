package com.example.wangbo.ourapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import butterknife.BindView
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.ThreeAdapter
import com.example.wangbo.ourapp.bean.ThreeBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

/**
 * Created by wangbo on 2018/8/21.
 *
 *  我的记事本
 */
class MineNoteAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<ThreeBean> {

    lateinit var adapter: ThreeAdapter

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private var listData: ArrayList<ThreeBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.view_single_recylerview, true, "被移除的记事本")
    }

    override fun initView() {
        adapter = ThreeAdapter(listData, context)
        commonList.adapter = adapter
        adapter.setOnItemClickedListener(this)
    }

    override fun initData() {
        getPageData()
    }

    private fun getPageData() {
        HttpHelper.getInstance(context).getOldNoteBook(1, UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<List<ThreeBean>> { o -> adapter.setData(o) }))
    }

    override fun onClicked(position: Int, t: ThreeBean?) {
        val intent = Intent(context, NoteBookDetailsAct::class.java)
        intent.putExtra(NoteBookDetailsAct.EXTRA_DATA_BASE, t)
        startActivityForResult(intent, 10000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == Activity.RESULT_OK) {
            getPageData()
        }
    }
}