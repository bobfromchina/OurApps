package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.RankingListBean
import com.jackmar.jframelibray.utils.GlideImageLoadUtil
import com.jackmar.jframelibray.utils.HeightFollowWidthImageView

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/15.
 *
 * 排行榜的适配器
 */
class RankingListAdapter(datas: List<RankingListBean>, context: Context) : RecyclerListAdapter<RankingListBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_rank_list, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        GlideImageLoadUtil.loadRoundImage(mContext, bean.getImg(), viewHolder.pic)

        viewHolder.name.text = bean.getName()

        viewHolder.upTime.text = bean.getUpTime()
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.pic)
        lateinit var pic: HeightFollowWidthImageView

        @BindView(R.id.name)
        lateinit var name: TextView

        @BindView(R.id.up_time)
        lateinit var upTime: TextView
    }
}
