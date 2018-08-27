package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.bumptech.glide.Glide
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by wangbo on 2018/8/24.
 *
 * 重写的标题栏  歌单
 *
 */
class TitleAdapter(context: Context, bean: CommentMusicListBean, stickyLayoutHelper: StickyLayoutHelper, i: Int) :
        DelegateAdapter.Adapter<TitleAdapter.MainViewHolder>() {

    private var mLayoutHelper: LayoutHelper = stickyLayoutHelper

    var mContext: Context = context

    private var mLayoutParams: VirtualLayoutManager.LayoutParams? = null

    private var data = bean

    private var mCount = i

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_song_title, parent, false))
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
        var num: TextView = holder.itemView.findViewById(R.id.num)
        num.text = "(共" + 20 + "首)"

        var listImg: ImageView = holder.itemView.findViewById(R.id.img_bg)
        Glide.with(mContext).load(data.picUrl).bitmapTransform(BlurTransformation(mContext, 25, 3)).into(listImg)
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
