package com.example.wangbo.ourapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.SongListDetailsAdapter
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import com.example.wangbo.ourapp.bean.MusicListDetails
import com.jackmar.jframelibray.base.JBaseAct

import butterknife.BindView
import butterknife.OnClick
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.example.wangbo.ourapp.adapter.ShadowAdapter
import com.example.wangbo.ourapp.adapter.TitleAdapter
import com.example.wangbo.ourapp.utils.CommonUtils
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by wangbo on 2018/8/7.
 *
 * 获取歌单详情
 */
class SongListDetailsAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<MusicListDetails> {

    private lateinit var bean: CommentMusicListBean

    @BindView(R.id.img_bg)
    lateinit var imgBg: ImageView

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private var listData: ArrayList<MusicListDetails> = ArrayList()


    var delegateAdapter: DelegateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_song_list, true)
    }

    override fun initView() {

        /**
         *   初始化视图管理器
         */
        val vManager = VirtualLayoutManager(this)
        commonList.layoutManager = vManager

        val viewPool = RecyclerView.RecycledViewPool()
        commonList.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        delegateAdapter = DelegateAdapter(vManager, false)
        commonList.adapter = delegateAdapter

        bean = intent.getParcelableExtra(EXTRA_DASE_DATA)

        Glide.with(context).load(bean.picUrl).bitmapTransform(BlurTransformation(context, 25, 3)).into(imgBg)

        /**
         *   高斯布局
         */
        delegateAdapter!!.addAdapter(ShadowAdapter(this, bean, LinearLayoutHelper(), 1))

        /**
         *  标题列表
         */
        delegateAdapter!!.addAdapter(TitleAdapter(this, bean, StickyLayoutHelper(true), 1))

        /**
         *   列表数据
         */
        delegateAdapter!!.addAdapter(SongListDetailsAdapter(this, CommonUtils.createRangeData(MusicListDetails::class.java, 20), LinearLayoutHelper(), 20))
    }

    override fun initData() {
        getData()
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
//        TODO(这里的东西  晚一点再写  后台IP给我屏蔽了)

//        HttpHelper.getInstance(context).getPlayListDetails(bean.id, ProgressSubscriber(context, IOnNextListener<MusicListBean> { o ->
        //            songListDetailsAdapter.setData(o.tracks)

//            num.text = "(共" + o.tracks.size + "首)"
//
//            if (o.commentCount < 10000) {
//                commentNum.text = "+ 收藏（" + +o.commentCount + "）"
//            } else if (o.commentCount > 10000 && o.commentCount < 100000) {
//                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 1) + "万）"
//            } else if (o.commentCount > 100000 && o.commentCount < 1000000) {
//                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 2) + "万）"
//            } else if (o.commentCount > 1000000 && o.commentCount < 10000000) {
//                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 3) + "万）"
//            } else if (o.commentCount > 10000000 && o.commentCount < 100000000) {
//                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 4) + "万）"
//            } else if (o.commentCount > 100000000) {
//                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 1) + "." + o.commentCount.toString().substring(2, 3) + "亿）"
//            }
//
//            name.text = o.name
//
//            author.text = o.nickname
//
//            GlideImageLoadUtil.loadImage(context, o.coverImgUrl, listImg)
//
//            GlideImageLoadUtil.loadImage(context, o.avatarUrl, authorImg)
//        }))
    }

    override fun onClicked(position: Int, songBean: MusicListDetails) {
        launchActivity(PlayMusicAct::class.java, PlayMusicAct.EXTRA_BASE_DATA, songBean)
    }

    @OnClick(R.id.left_btn)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.left_btn -> finish()
        }
    }

    companion object {
        const val EXTRA_DASE_DATA = "extra_dase_data"
    }
}

