package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.SongBean

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/6.
 *
 * 我关注的歌曲列表
 */
class MineSongAdapter(datas: List<SongBean>, context: Context) : RecyclerListAdapter<SongBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_mine_song, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.ll_container)
        lateinit var ll_container: LinearLayout
    }
}
