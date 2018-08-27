package com.example.wangbo.ourapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.MusicListDetails

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager

/**
 * Created by wangbo on 2018/8/7.
 *
 *
 * 歌曲列表的适配器
 */
class SongListDetailsAdapter(context: Context, data: List<MusicListDetails>, layoutHelper1: LayoutHelper, i: Int) :
        DelegateAdapter.Adapter<SongListDetailsAdapter.MainViewHolder>() {

    private var mLayoutHelper: LayoutHelper = layoutHelper1

    var mContext: Context = context

    val mLayoutParams: VirtualLayoutManager.LayoutParams? = null

    private var data: List<MusicListDetails> = data

    private var mCount = i

    /**
     *  初始化视图
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_single_song, parent, false))
    }

    override fun getItemCount(): Int {
        return mCount
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder!!.itemView.layoutParams = if (mLayoutParams != null) mLayoutParams else
            VirtualLayoutManager.LayoutParams(VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolderWithOffset(viewHolder: MainViewHolder, position: Int, offsetTotal: Int) {

        /**
         *  下标
         */
//        val positions: TextView = viewHolder.itemView.findViewById(R.id.positions)
//        positions.text = (position + 1).toString()

        /**
         *  名字
         */
//        val title: TextView = viewHolder.itemView.findViewById(R.id.title)
//        title.text = data[position].name

        /**
         *  别名
         */
//        val titleAlis: TextView = viewHolder.itemView.findViewById(R.id.titleAlis)
//        titleAlis.text = if (data[position].alia != null && !data[position].alia.isEmpty()) data[position].name else ""

        /**
         *  歌手名字
         */
//        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
//        name.text = data[position].ar[0].name + " - " + data[position].al[0].name
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


//class SongListDetailsAdapter(datas: List<MusicListDetails>, context: Context) : RecyclerListAdapter<MusicListDetails>(datas, context) {
//
//    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
//        return ViewHolder(layoutInflater.inflate(R.layout.item_single_song, parent, false))
//    }
//
//    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
//        val viewHolder = holder as ViewHolder
//        val bean = datas[position]
////
////        viewHolder.positions.text = (position + 1).toString()
////
////        viewHolder.title.text = bean.name
////
////        if (bean.alia != null && !bean.alia.isEmpty()) {
////            viewHolder.titleAlis.text = "（" + bean.alia[0] + "）"
////        } else {
////            viewHolder.titleAlis.text = ""
////        }
////
////        viewHolder.name.text = bean.ar[0].name + " - " + bean.al[0].name
//    }
//
//    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {
//
//
//        @BindView(R.id.positions)
//        lateinit var positions: TextView
//
//        @BindView(R.id.titleAlis)
//        lateinit var titleAlis: TextView
//
//        @BindView(R.id.title)
//        lateinit var title: TextView
//
//        @BindView(R.id.name)
//        lateinit var name: TextView
//    }
//}
