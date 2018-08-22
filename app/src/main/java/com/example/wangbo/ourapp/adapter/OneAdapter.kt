package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.WebViewActivity
import com.example.wangbo.ourapp.bean.NewsBean
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/21.
 *
 *
 * 首页列表适配器
 */
class OneAdapter(datas: List<NewsBean>, context: Context) : RecyclerListAdapter<NewsBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_one, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        if (bean.picInfo != null && bean.picInfo != "") {
            GlideImageLoadUtil.loadImage(mContext, bean.picInfo, viewHolder.img)
        } else {
            GlideImageLoadUtil.loadImage(mContext, bean.images[0], viewHolder.img)
        }

        viewHolder.name.text = bean.title
        viewHolder.tvTime.text = bean.author
        viewHolder.tvTag.text = bean.tag

        viewHolder.llContainer.setOnClickListener {
            if (bean.linkUrl != null && bean.linkUrl != "") {
                val i = Intent(mContext, WebViewActivity::class.java)
                i.putExtra(WebViewActivity.EXTRA_ID, bean)
                mContext.startActivity(i)
            }
        }
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.ll_container)
        lateinit var llContainer: LinearLayout

        @BindView(R.id.img)
        lateinit var img: ImageView

        @BindView(R.id.tv_name)
        lateinit var name: TextView

        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView

        @BindView(R.id.tv_tag)
        lateinit var tvTag: TextView
    }
}
