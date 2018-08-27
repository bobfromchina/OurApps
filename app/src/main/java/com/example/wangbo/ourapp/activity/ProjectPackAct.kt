package com.example.wangbo.ourapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.jackmar.jframelibray.base.JBaseAct
import java.util.ArrayList

/**
 * Created by wangbo on 2018/8/27.
 *
 *   测试文本类
 */
class ProjectPackAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<ProjectPackAct.SingleBean> {

    @BindView(R.id.common_list)
    lateinit var listCommon: RecyclerViewHeaderAndFooter

    lateinit var singleAdapter: SingleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.common_list, true, "测试文本类")
    }


    override fun initView() {
        singleAdapter = SingleAdapter(loadData, context)
        listCommon.adapter = singleAdapter
        singleAdapter.setOnItemClickedListener(this)
    }

    private val loadData: List<SingleBean>
        get() {
            val data = ArrayList<SingleBean>()
            data.add(SingleBean("VLayout", "1", "1"))
            data.add(SingleBean("NativeAndH5", "2", "1"))
            return data
        }

    override fun initData() {

    }

    override fun onClicked(position: Int, t: SingleBean) {

        when (t.id) {
            "1" ->
                launchActivity(VLayoutAct::class.java)
            "2" ->
                launchActivity(NativeTestAct::class.java)
            "3" ->
                launchActivity(KotlinTestAct::class.java)
        }
    }

    inner class SingleAdapter(datas: List<SingleBean>, context: Context) : RecyclerListAdapter<SingleBean>(datas, context) {

        override fun createViewHolder(position: Int, parent: ViewGroup): BaseRecyclerViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.item_city, parent, false))
        }

        override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
            val viewHoolder = holder as ViewHolder
            val bean = datas[position]

            viewHoolder.name.text = bean.name
        }

        internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

            @BindView(R.id.tv_name)
            lateinit var name: TextView
        }
    }

    inner class SingleBean {

        var name: String

        var id: String

        var desc: String

        constructor(name: String, id: String, desc: String) {
            this.name = name
            this.id = id
            this.desc = desc
        }
    }
}