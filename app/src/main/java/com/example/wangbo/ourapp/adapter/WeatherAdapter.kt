package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.WeatherBean
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder

/**
 * Created by wangbo on 2018/8/21.
 *
 * 天气页面的适配器
 */
class WeatherAdapter(datas: List<WeatherBean>, context: Context) : RecyclerListAdapter<WeatherBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_weather, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val weatherBean = datas[position]

        viewHolder.date.text = weatherBean.days + "  " + weatherBean.week

        viewHolder.type.text = "天气：" + weatherBean.weather

        viewHolder.wind.text = "风向：" + weatherBean.wind + "  " + weatherBean.winp

        viewHolder.low.text = "最低气温：" + weatherBean.temp_low

        viewHolder.high.text = "最高气温：" + weatherBean.temp_high

        GlideImageLoadUtil.loadImage(mContext, weatherBean.weather_icon, viewHolder.img)

        viewHolder.line.visibility = if (datas.size - position != 1) View.VISIBLE else View.GONE
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.date)
        lateinit var date: TextView

        @BindView(R.id.type)
        lateinit var type: TextView

        @BindView(R.id.high)
        lateinit var high: TextView

        @BindView(R.id.low)
        lateinit var low: TextView

        @BindView(R.id.wind)
        lateinit var wind: TextView

        @BindView(R.id.img)
        lateinit var img: ImageView

        @BindView(R.id.line)
        lateinit var line: View
    }
}
