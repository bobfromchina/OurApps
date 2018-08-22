package com.example.wangbo.ourapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.SongListDetailsAdapter
import com.example.wangbo.ourapp.bean.CommentMusicListBean
import com.example.wangbo.ourapp.bean.MusicListBean
import com.example.wangbo.ourapp.bean.MusicListDetails
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import butterknife.BindView
import butterknife.OnClick
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by wangbo on 2018/8/7.
 *
 * 获取歌单详情
 */
class SongListDetailsAct : JBaseAct(), RecyclerListAdapter.OnItemClickedListener<MusicListDetails> {

    private lateinit var bean: CommentMusicListBean

    @BindView(R.id.common_list)
    lateinit var commonList: RecyclerViewHeaderAndFooter

    private lateinit var songListDetailsAdapter: SongListDetailsAdapter

    private lateinit var num: TextView
    private lateinit var commentNum: TextView
    private lateinit var author: TextView
    private lateinit var name: TextView

    private lateinit var listImg: RoundedImageView
    private lateinit var authorImg: RoundedImageView

    private lateinit var rlBg: ImageView

    private var listData: ArrayList<MusicListDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_song_list, true)
    }

    override fun initView() {

        val headerView = layoutInflater.inflate(R.layout.activity_song_list_details, null, false)
        num = headerView.findViewById(R.id.num)
        commentNum = headerView.findViewById(R.id.comment_num)
        author = headerView.findViewById(R.id.author)
        name = headerView.findViewById(R.id.name)
        listImg = headerView.findViewById(R.id.list_img)
        authorImg = headerView.findViewById(R.id.author_img)
        rlBg = headerView.findViewById(R.id.rl_img)
        commonList.addHeaderView(headerView)

        bean = intent.getParcelableExtra(EXTRA_DASE_DATA)

        GlideImageLoadUtil.loadImage(context, bean.picUrl, listImg)

        name.text = bean.name

        Glide.with(context).load(bean.picUrl).bitmapTransform(BlurTransformation(context, 25, 3)).into(rlBg)

        songListDetailsAdapter = SongListDetailsAdapter(listData, context)
        commonList.adapter = songListDetailsAdapter
        songListDetailsAdapter.setOnItemClickedListener(this)
    }

    override fun initData() {
        getData()
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        HttpHelper.getInstance(context).getPlayListDetails(bean.id, ProgressSubscriber(context, IOnNextListener<MusicListBean> { o ->
            songListDetailsAdapter.setData(o.tracks)

            num.text = "(共" + o.tracks.size + "首)"

            if (o.commentCount < 10000) {
                commentNum.text = "+ 收藏（" + +o.commentCount + "）"
            } else if (o.commentCount > 10000 && o.commentCount < 100000) {
                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 1) + "万）"
            } else if (o.commentCount > 100000 && o.commentCount < 1000000) {
                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 2) + "万）"
            } else if (o.commentCount > 1000000 && o.commentCount < 10000000) {
                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 3) + "万）"
            } else if (o.commentCount > 10000000 && o.commentCount < 100000000) {
                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 4) + "万）"
            } else if (o.commentCount > 100000000) {
                commentNum.text = "+ 收藏（" + o.commentCount.toString().substring(0, 1) + "." + o.commentCount.toString().substring(2, 3) + "亿）"
            }

            name.text = o.name

            author.text = o.nickname

            GlideImageLoadUtil.loadImage(context, o.coverImgUrl, listImg)

            GlideImageLoadUtil.loadImage(context, o.avatarUrl, authorImg)
        }))
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
