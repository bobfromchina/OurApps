
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

    private static OurApplication sInstance;

//    DaoMaster.DevOpenHelper mHelper;
//
//    SQLiteDatabase db;
//
//    DaoMaster mDaoMaster;
//
//    DaoSession mDaoSession;

    // 正式环境
//    public static String hostUrl = "http://w8cjch.natappfree.cc";
    public static String hostUrl = "http:www.handsomebob.top:9998";

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
        setDatabase();
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = new DaoMaster.DevOpenHelper(this, SimpleHome.TABLE_NAME, null);
//        db = mHelper.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();
    }

//    public DaoSession getSession() {
//        return mDaoSession;
//    }
//
//    public SQLiteDatabase getDb() {
//        return db;
//    }

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
