package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
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
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.jackmar.jframelibray.utils.GlideImageLoadUtil
import com.jackmar.jframelibray.utils.HeightFollowWidthImageView

/**
 * Created by wangbo on 2018/11/1.
 *
 *   歌单推荐的适配器
 */

class SongleHomeAdapter(context: Context, listData: ArrayList<CommentMusicListBean>, layoutHelper: LinearLayoutHelper, i: Int) :
        DelegateAdapter.Adapter<SongleHomeAdapter.MainViewHolder>() {

    private var mLayoutHelper: LayoutHelper = layoutHelper

    var mContext: Context = context

    private var mLayoutParams: VirtualLayoutManager.LayoutParams? = null

    private var data = listData

    private var mCount = i

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder!!.itemView.layoutParams = if (mLayoutParams != null) mLayoutParams else
            VirtualLayoutManager.LayoutParams(VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_song_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mCount
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onBindViewHolderWithOffset(holder: MainViewHolder, position: Int, offsetTotal: Int) {
        super.onBindViewHolderWithOffset(holder, position, offsetTotal)

        val listAdapter = CommentMusicListAdapter(data, mContext)
        val list: RecyclerViewHeaderAndFooter = holder.itemView.findViewById(R.id.common_list)

        val mGridLayoutManager = GridLayoutManager(mContext, 3)
        list.layoutManager = mGridLayoutManager
        list.adapter = listAdapter
        list.setHasFixedSize(true)

        /*  var pic: HeightFollowWidthImageView = holder.itemView.findViewById(R.id.pic)

          var name: TextView = holder.itemView.findViewById(R.id.name)

          var num: TextView = holder.itemView.findViewById(R.id.num)

          GlideImageLoadUtil.loadRoundImage3(mContext, data.get(position).picUrl, pic)

          name.text = data.get(position).name

          if (data.get(position).playCount < 10000) {
              num.text = "" + data.get(position).playCount
          } else if (data.get(position).playCount > 10000 && data.get(position).playCount < 100000) {
              num.text = data.get(position).playCount.toString().substring(0, 1) + "万"
          } else if (data.get(position).playCount > 100000 && data.get(position).playCount < 1000000) {
              num.text = data.get(position).playCount.toString().substring(0, 2) + "万"
          } else if (data.get(position).playCount > 1000000 && data.get(position).playCount < 10000000) {
              num.text = data.get(position).playCount.toString().substring(0, 3) + "万"
          } else if (data.get(position).playCount > 10000000 && data.get(position).playCount < 100000000) {
              num.text = data.get(position).playCount.toString().substring(0, 4) + "万"
          } else if (data.get(position).playCount > 100000000) {
              num.text = data.get(position).playCount.toString().substring(0, 1) + "." + data.get(position).playCount.toString().substring(2, 3) + "亿"
          }*/
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
