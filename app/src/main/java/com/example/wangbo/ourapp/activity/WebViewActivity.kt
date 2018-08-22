package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.NewsBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.CommonUtils
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import butterknife.BindView
import butterknife.OnClick

/**
 * Created by handsome-Bob on 2017/9/14.
 *
 * 网页加载
 */
class WebViewActivity : JBaseAct() {

    @BindView(R.id.view_line)
    lateinit var viewLine: View

    @BindView(R.id.wb)
    lateinit var web: WebView

    @BindView(R.id.tv_titles)
    lateinit var tvTitle: TextView

    @BindView(R.id.tv_shoucang)
    lateinit var tvShouCang: TextView

    var bean: NewsBean = NewsBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_wv, true)

        tvShouCang.text = if (bean.followStatus == 1) "取消收藏" else "收藏"

        tvShouCang.setOnClickListener {
            if (tvShouCang.text.toString() == "收藏") {
                HttpHelper.getInstance(context).commentnews(bean.linkUrl, bean.title, bean.author, bean.tag, bean.images[0], UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<NewsBean> { o -> tvShouCang.text = if (o.followStatus == 1) "取消收藏" else "收藏" }))
            } else {
                HttpHelper.getInstance(context).cancelCommentNews(bean.id, UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<NewsBean> { o -> tvShouCang.text = if (o.followStatus == 1) "取消收藏" else "收藏" }))
            }
        }

        val params = viewLine.layoutParams
        params.height = CommonUtils.getStatusBarHeight(context)
        viewLine.layoutParams = params
    }

    override fun initView() {
        bean = intent.getParcelableExtra<NewsBean>(EXTRA_ID)

        tvTitle.text = "网易新闻"

        val settings = web.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        web.loadUrl(bean.linkUrl)
    }

    override fun initData() {

    }

    @OnClick(R.id.tv_return)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.tv_return -> finish()
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
