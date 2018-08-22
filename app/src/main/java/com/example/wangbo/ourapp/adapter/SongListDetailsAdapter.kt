package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.MusicListDetails

import butterknife.BindView
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder

/**
 * Created by wangbo on 2018/8/7.
 *
 *
 * 歌曲列表的适配器
 */

class SongListDetailsAdapter(datas: List<MusicListDetails>, context: Context) : RecyclerListAdapter<MusicListDetails>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_single_song, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        viewHolder.positions.text = (position + 1).toString()

        viewHolder.title.text = bean.name

        if (bean.alia != null && !bean.alia.isEmpty()) {
            viewHolder.titleAlis.text = "（" + bean.alia[0] + "）"
        } else {
            viewHolder.titleAlis.text = ""
        }

        viewHolder.name.text = bean.ar[0].name + " - " + bean.al[0].name
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {


        @BindView(R.id.positions)
        lateinit var positions: TextView

        @BindView(R.id.titleAlis)
        lateinit var titleAlis: TextView

        @BindView(R.id.title)
        lateinit var title: TextView

        @BindView(R.id.name)
        lateinit var name: TextView
    }
}
