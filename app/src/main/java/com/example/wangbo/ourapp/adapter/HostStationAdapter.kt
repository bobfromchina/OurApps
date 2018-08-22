package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.DjRecommend
import com.example.wangbo.ourapp.bean.HostStationBean
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/1.
 *
 *
 * 主播电台的适配器
 */
class HostStationAdapter(datas: List<DjRecommend>, context: Context) : RecyclerListAdapter<DjRecommend>(datas, context) {

    override fun createViewHolder(position: Int, parent: ViewGroup): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_host_station, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        GlideImageLoadUtil.loadRoundImage3(mContext, bean.picUrl, viewHolder.imageView)

        viewHolder.name.text = bean.name

        viewHolder.num.text = bean.category

        viewHolder.desc.text = bean.rcmdtext
    }

    inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.img)
        lateinit var imageView: ImageView

        @BindView(R.id.name)
        lateinit var name: TextView

        @BindView(R.id.num)
        lateinit var num: TextView

        @BindView(R.id.desc)
        lateinit var desc: TextView
    }
}
