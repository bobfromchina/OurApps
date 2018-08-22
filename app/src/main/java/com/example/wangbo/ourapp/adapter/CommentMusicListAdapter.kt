package com.example.wangbo.ourapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import com.jackmar.jframelibray.utils.GlideImageLoadUtil
import com.jackmar.jframelibray.utils.HeightFollowWidthImageView

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/31.
 *
 * 推荐歌单的接口展示
 */
class CommentMusicListAdapter(datas: List<CommentMusicListBean>, context: Context) : RecyclerListAdapter<CommentMusicListBean>(datas, context) {

    override fun createViewHolder(position: Int, parent: ViewGroup): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_common_wangyi_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]

        GlideImageLoadUtil.loadRoundImage3(mContext, bean.picUrl, viewHolder.pic)

        viewHolder.name.text = bean.name

        if (bean.playCount < 10000) {
            viewHolder.num.text = "" + bean.playCount
        } else if (bean.playCount > 10000 && bean.playCount < 100000) {
            viewHolder.num.text = bean.playCount.toString().substring(0, 1) + "万"
        } else if (bean.playCount > 100000 && bean.playCount < 1000000) {
            viewHolder.num.text = bean.playCount.toString().substring(0, 2) + "万"
        } else if (bean.playCount > 1000000 && bean.playCount < 10000000) {
            viewHolder.num.text = bean.playCount.toString().substring(0, 3) + "万"
        } else if (bean.playCount > 10000000 && bean.playCount < 100000000) {
            viewHolder.num.text = bean.playCount.toString().substring(0, 4) + "万"
        } else if (bean.playCount > 100000000) {
            viewHolder.num.text = bean.playCount.toString().substring(0, 1) + "." + bean.playCount.toString().substring(2, 3) + "亿"
        }
    }

    inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.pic)
        lateinit var pic: HeightFollowWidthImageView

        @BindView(R.id.name)
        lateinit var name: TextView

        @BindView(R.id.num)
        lateinit var num: TextView
    }
}
