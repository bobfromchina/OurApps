package com.example.wangbo.ourapp.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct

import butterknife.BindView
import junit.runner.Version

/**
 * Created by wangbo on 2018/8/3.
 *
 *
 * 关于我们
 */
class AboutUsAct : JBaseAct() {

    @BindView(R.id.tv_version)
    lateinit var version: TextView

    @BindView(R.id.show_share)
    lateinit var showShare: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_about_us, true, "关于我们")
    }

    override fun initView() {

        var manager: PackageManager = this.packageManager
        var info: PackageInfo
        info = manager.getPackageInfo(this.packageName, 0)
        version.text = info.versionName

        val typeface = Typeface.createFromAsset(context.assets, "fonts/text_font.OTF")
        showShare.typeface = typeface
    }

    override fun initData() {

    }
}
