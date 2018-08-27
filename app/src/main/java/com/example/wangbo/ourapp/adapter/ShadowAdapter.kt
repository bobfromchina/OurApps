package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

/**
 * Created by wangbo on 2018/8/24.
 *
 *  模糊背景的适配器
 */
class ShadowAdapter(context: Context, bean: CommentMusicListBean, layoutHelper: LinearLayoutHelper, i: Int) :
        DelegateAdapter.Adapter<ShadowAdapter.MainViewHolder>() {

    private var mLayoutHelper: LayoutHelper = layoutHelper

    var mContext: Context = context

    private var mLayoutParams: VirtualLayoutManager.LayoutParams? = null

    private var data = bean

    private var mCount = i

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_song_shadow, parent, false))
    }

    override fun getItemCount(): Int {
        return mCount
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.itemView.layoutParams = if (mLayoutParams != null) mLayoutParams else
            VirtualLayoutManager.LayoutParams(VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    override fun onBindViewHolderWithOffset(holder: MainViewHolder, position: Int, offsetTotal: Int) {

        var name: TextView = holder.itemView.findViewById(R.id.name)
        name.text = data.name

        var listImg: RoundedImageView = holder.itemView.findViewById(R.id.list_img)
        GlideImageLoadUtil.loadImage(mContext, data.picUrl, listImg)

        var authorImg: RoundedImageView = holder.itemView.findViewById(R.id.author_img)
        GlideImageLoadUtil.loadImage(mContext, data.picUrl, authorImg)

        var author: TextView = holder.itemView.findViewById(R.id.author)
        author.text = data.name
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            createdTimes++
            existing++
        }

        @Throws(Throwable::class)
        protected fun finalize() {
            existing--
        }

        companion object {

            var existing = 0

            var createdTimes = 0
        }
    }
}
