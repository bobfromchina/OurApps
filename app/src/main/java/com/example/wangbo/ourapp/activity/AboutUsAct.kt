package com.example.wangbo.ourapp.activity

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct

import butterknife.BindView

/**
 * Created by wangbo on 2018/8/3.
 *
 *
 * 关于我们
 */
class AboutUsAct : JBaseAct() {

    @BindView(R.id.show_share)
    lateinit var showShare: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_about_us, true, "关于我们")
    }

    override fun initView() {
        val typeface = Typeface.createFromAsset(context.assets, "fonts/text_font.OTF")
        showShare.typeface = typeface
    }

    override fun initData() {

    }
}
