package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.CommentSoongsBean
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder

/**
 * Created by wangbo on 2018/8/15.
 *
 *
 * 推荐歌单
 */
class CommentSongsAdapter(datas: List<CommentSoongsBean>, context: Context) : RecyclerListAdapter<CommentSoongsBean>(datas, context) {

    override fun createViewHolder(position: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_comment_song, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        GlideImageLoadUtil.loadImage(mContext, bean.albumPic, viewHolder.positions)

        viewHolder.title.text = bean.musicName

        if (bean.alias != null && !bean.alias.isEmpty()) {
            viewHolder.titleAlis.text = "（" + bean.alias[0] + "）"
        } else {
            viewHolder.titleAlis.text = ""
        }

        viewHolder.name.text = bean.artName + " - " + bean.albumName
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.positions)
        lateinit var positions: RoundedImageView

        @BindView(R.id.titleAlis)
        lateinit var titleAlis: TextView

        @BindView(R.id.title)
        lateinit var title: TextView

        @BindView(R.id.name)
        lateinit var name: TextView
    }
}
