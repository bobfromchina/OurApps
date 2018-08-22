package com.example.wangbo.ourapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.IDialogCallBack
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.PreHelper
import com.jackmar.jframelibray.utils.PreferenceKey
import com.jackmar.jframelibray.utils.StrUtil


/**
 * Created by JackMar on 2018/4/11.
 *
 *
 * 引导页
 */
class SplashAct : JBaseAct(), IDialogCallBack {

    private var isfirst = false

    override fun initView() {

    }

    override fun initData() {
        Handler().postDelayed({
            isfirst = PreHelper.defaultCenter(context).getBooleanData(PreferenceKey.IS_FIRST)
            if (isfirst) {
                if (UserManager.getInstance().checkLogin(context, true)) {
                    launch(MainActivity::class.java)
                }
            } else {
                launch(WelcomeAct::class.java)
            }

            finish()
        }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.act_splash, true)
    }

    override fun onPositiveCase(tag: String) {
        //        Intent intent = new Intent();
        //        intent.setAction("android.intent.action.VIEW");
        //        Uri content_url = Uri.parse(mUpdateEntity.getVersion().getUrl());
        //        intent.setData(content_url);
        //        startActivity(intent);
        finish()
    }

    override fun onNegativeCase(tag: String) {
        finish()
    }
}
