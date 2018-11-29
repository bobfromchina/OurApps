package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bigkoo.convenientbanner.ConvenientBanner
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.CommentSongsAct
import com.example.wangbo.ourapp.activity.RankingListAct
import com.example.wangbo.ourapp.activity.SongListAct
import com.example.wangbo.ourapp.bean.YunBanner
import com.example.wangbo.ourapp.utils.NetWorkImageHolderView

/**
 * Created by wangbo on 2018/11/1.
 *
 *   个人推荐的顶部适配器
 */
class RecommendationAdapter(context: Context, listBanner: ArrayList<YunBanner>, layoutHelper: LinearLayoutHelper, i: Int) :
        DelegateAdapter.Adapter<RecommendationAdapter.MainViewHolder>() {

    private var mLayoutHelper: LayoutHelper = layoutHelper

    var mContext: Context = context

    private var mLayoutParams: VirtualLayoutManager.LayoutParams? = null

    private var data = listBanner

    private var mCount = i

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder!!.itemView.layoutParams = if (mLayoutParams != null) mLayoutParams else
            VirtualLayoutManager.LayoutParams(VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_header_music, parent, false))
    }

    override fun getItemCount(): Int {
        return mCount
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onBindViewHolderWithOffset(holder: MainViewHolder, position: Int, offsetTotal: Int) {
        super.onBindViewHolderWithOffset(holder, position, offsetTotal)

        val one: TextView = holder.itemView.findViewById(R.id.one)
        val two: TextView = holder.itemView.findViewById(R.id.two)
        val three: TextView = holder.itemView.findViewById(R.id.three)
        val four: TextView = holder.itemView.findViewById(R.id.four)
        val title: TextView = holder.itemView.findViewById(R.id.tv_title)
        val banner: ConvenientBanner<String> = holder.itemView.findViewById(R.id.cb_banner)

        //  我将在这里做出按钮的点击事件
        one.setOnClickListener {
            val intent = Intent(mContext, CommentSongsAct::class.java)
            mContext.startActivity(intent)
        }
        two.setOnClickListener {
            val intent = Intent(mContext, CommentSongsAct::class.java)
            mContext.startActivity(intent)
        }
        three.setOnClickListener {
            val intent = Intent(mContext, SongListAct::class.java)
            mContext.startActivity(intent)
        }
        four.setOnClickListener {
            val intent = Intent(mContext, RankingListAct::class.java)
            mContext.startActivity(intent)
        }

        one.text = "私人FM"
        two.text = "每日推荐"
        three.text = "歌单"
        four.text = "排行榜"
        title.text = "推荐歌单"

        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        banner.setPageIndicator(intArrayOf(R.drawable.banner_page_indicator_ponit_uncheck, R.drawable.banner_page_indicator_point_checked))
        banner.startTurning(5000)
        banner.isCanLoop = true

        val listData: ArrayList<String> = ArrayList()

        for (String in data)
            listData.add(String.picUrl)
        banner.setPages({ NetWorkImageHolderView() }, listData)
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