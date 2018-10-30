package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import butterknife.BindView
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.utils.CommonUtils
import com.jackmar.jframelibray.base.JBaseAct

/**
 * Created by wangbo on 2018/10/24.
 *
 * 网页测试
 */
class WebTestActivity : JBaseAct() {

    @BindView(R.id.view_line)
    lateinit var viewLine: View

    @BindView(R.id.rl_container)
    lateinit var rlContainer: RelativeLayout

    @BindView(R.id.wb)
    lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_wv, true)

        val params = viewLine.layoutParams
        params.height = CommonUtils.getStatusBarHeight(context)
        viewLine.layoutParams = params
        viewLine.background = ContextCompat.getDrawable(context,R.color.kekangan_color)

        rlContainer.visibility = View.GONE
    }

    override fun initView() {
        val settings = web.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        web.loadUrl("http://www.handsomebob.top:8082")
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        web.clearCache(true)
        web.clearHistory()
        web.destroy()
    }
}