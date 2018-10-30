//package com.example.wangbo.ourapp.cache;
//
//
//import android.content.Context;
//
//import com.example.wangbo.ourapp.greendao.DaoMaster;
//import com.example.wangbo.ourapp.greendao.DaoSession;
//
//
///**
// * 项目名：   GreenDaoDB-maseter
// * 包名：     com.text.xuhong.greendaodb_maseter
// * 文件名：   DBManager
// * 创建者：   xuhong
// * 创建时间： 2017/8/29 16:52
// */
//
//public class DBManager {
//
//    private static DaoMaster mDaoMaster;
//    private static DaoSession mDaoSession;
//    private static DBManager mInstance;
//    private DaoMaster.DevOpenHelper openHelper;
//
//    private DBManager() {
//    }
//
//    public static DBManager init (Context mContext) {
//
//        if (mInstance == null) {
//            DaoMaster.DevOpenHelper devOpenHelper = new
//                    DaoMaster.DevOpenHelper(mContext, SimpleHome.TABLE_NAME, null);//此处为自己需要处理的表
//            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
//            mDaoSession = mDaoMaster.newSession();
//        }
//
//        if (mInstance == null) {
//            //保证异步处理安全操作
//            synchronized (DBManager.class) {
//                if (mInstance == null) {
//                    mInstance = new DBManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//
//    public static DBManager getInstance() {
//        return mInstance;
//    }
//    public DaoMaster getMaster() {
//        return mDaoMaster;
//    }
//
//    public DaoSession getSession() {
//        return mDaoSession;
//    }
//
//    public DaoSession getNewSession() {
//        mDaoSession = mDaoMaster.newSession();
//        return mDaoSession;
//    }
//
//}
