package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.OneHeaderBean

import butterknife.BindView
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder

/**
 * Created by wangbo on 2018/8/2.
 *
 *
 * 首页顶部的可滑动的适配器
 */

class OneHeaderAdapter(datas: List<OneHeaderBean>, context: Context) : RecyclerListAdapter<OneHeaderBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_one_head, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]
        viewHolder.name.text = bean.getName()
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.tv_name)
        lateinit var name: TextView
    }
}
