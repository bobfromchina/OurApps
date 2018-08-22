
package com.example.wangbo.ourapp;


import android.app.Application;

import com.example.wangbo.ourapp.utils.ViewUtils;
import com.jackmar.jframelibray.JFrameConfig;

/**
 * Created by wangbo on 2018/7/3.
 * <p>
 * application
 */
public class OurApplication extends Application {

    // RECYLEvIEW
//    https://blog.csdn.net/tuike/article/details/79064750

    private static OurApplication sInstance;

    // 正式环境
    public static String hostUrl = "http://cu4yfq.natappfree.cc";
//    public static String hostUrl = "192.168.7.127:80";

    //单例模式中获取唯一的MyApplication实例
    public static OurApplication getInstance() {
        if (sInstance == null) {
            sInstance = new OurApplication();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();
    }

    private void init() {
        ViewUtils.init(this);
        //配置框架内样式
        JFrameConfig.getInstance(this)
                .init()
                .setBackImage(R.drawable.fanhui_baise)
                .setTitleMoreTextColor(R.color.color_white)
                .setTitleTextSize(R.dimen.text_18)
                .setTitleBackgroundColorRes(R.color.color_main)
                .setTitleCanBack(true)
                .setTitleMoreTextSize(R.dimen.text_16)
                .setTitleTextColor(R.color.color_white)
                .setDebug(true)
                .setHostUrl(hostUrl)
                .build();
    }
}
