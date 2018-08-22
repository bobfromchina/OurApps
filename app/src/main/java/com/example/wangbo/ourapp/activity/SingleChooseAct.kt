package com.example.wangbo.ourapp.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.BaseRecyclerViewHolder
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.jackmar.jframelibray.base.JBaseAct

import java.util.ArrayList

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/27.
 *
 * 单项选择列表页
 */
class SingleChooseAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<SingleChooseAct.SingleBean> {

    private lateinit var cityAdapter: CityAdapter

    @BindView(R.id.list)
    lateinit var list: RecyclerViewHeaderAndFooter

    private var extraType: Int = 0

    private val noteData: List<SingleBean>
        get() {
            val data = ArrayList<SingleBean>()
            data.add(SingleBean("心情", "1", "1"))
            data.add(SingleBean("记事", "1", "2"))
            data.add(SingleBean("提醒", "1", "3"))
            data.add(SingleBean("日程", "1", "4"))
            data.add(SingleBean("日记", "1", "5"))
            data.add(SingleBean("私密日记", "1", "6"))
            return data
        }

    private val addressData: List<SingleBean>
        get() {
            val datas = ArrayList<SingleBean>()
            datas.add(SingleBean("重庆", "1", "chongqing"))
            datas.add(SingleBean("杭州", "1", "hangzhou"))
            datas.add(SingleBean("镇江", "1", "zhenjiang"))
            datas.add(SingleBean("荣昌", "1", "rongchang"))
            datas.add(SingleBean("南川", "1", "nanchuan"))
            datas.add(SingleBean("扬中", "1", "yangzhong"))
            return datas
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_city_choose, true, "地址选择")
    }

    override fun initView() {
        extraType = intent.getIntExtra(EXTRA_SINGLE_TYPE, 0)
        cityAdapter = CityAdapter(if (extraType == 0) addressData else noteData, context)
        list.adapter = cityAdapter
        cityAdapter.setOnItemClickedListener(this)
    }

    override fun initData() {

    }

    override fun onClicked(position: Int, singleBean: SingleBean) {
        val intent = intent
        intent.putExtra("name", singleBean.name)
        intent.putExtra("desc", singleBean.desc)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    inner class CityAdapter(datas: List<SingleBean>, context: Context) : RecyclerListAdapter<SingleBean>(datas, context) {

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

    companion object {
        const val EXTRA_SINGLE_TYPE = "extra_single_type"
    }
}
