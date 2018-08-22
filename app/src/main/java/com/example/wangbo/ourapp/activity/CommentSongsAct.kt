package com.example.wangbo.ourapp.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.CommentSongsAdapter
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.bean.CommentSoongsBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/15.
 *
 * 每日
 */
class CommentSongsAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<CommentSoongsBean> {

    private lateinit var commentSongsAdapter: CommentSongsAdapter

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private lateinit var imgHeader: ImageView


    private var listData: ArrayList<CommentSoongsBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.common_list, true, "每日推荐")
    }

    override fun initView() {
        val headerView = layoutInflater.inflate(R.layout.view_comment_songs, null, false)
        imgHeader = headerView.findViewById<View>(R.id.imgHeader) as ImageView
        val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534318973224&di=766db0423f2dc59cfd7576e1cc4ead1c&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01fea85739312e6ac72580ed798d33.jpg"
        GlideImageLoadUtil.loadImage(context, url, imgHeader)
        commonList.addHeaderView(headerView)

        commentSongsAdapter = CommentSongsAdapter(listData, context)
        commonList.adapter = commentSongsAdapter
        commentSongsAdapter.setOnItemClickedListener(this)

        val userEntity = UserManager.getInstance().user
        val music = userEntity!!.commentSong
        if (music != null && !music.isEmpty()) {
            loadPageData(music)
        }
    }

    private fun loadPageData(music: List<CommentSoongsBean>) {
        commentSongsAdapter.setData(music)
    }

    override fun initData() {

        HttpHelper.getInstance(context).getCommentSongs("", ProgressSubscriber(context, IOnNextListener<List<CommentSoongsBean>> { o ->
            loadPageData(o)
            val userEntity = UserManager.getInstance().user
            userEntity!!.commentSong = o
            UserManager.getInstance().updateUser(context as Activity, userEntity, false)
        }))
    }

    override fun onClicked(position: Int, commentSoongsBean: CommentSoongsBean) {
        launchActivity(PlayMusicAct::class.java, PlayMusicAct.EXTRA_BASE_DATA_MORE, commentSoongsBean)
    }
}
