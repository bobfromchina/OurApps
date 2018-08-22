package com.lovely3x.common.consts;

import com.lovely3x.common.CommonApplication;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 一些常量
 * Created by lovely3x on 16-4-1.
 */
public class Const {

    /**
     * 请求定位的时间间隔
     */
    public static final long LOC_INTERVAL = 5 * 60 * 1000;

    /**
     * 是否模拟位置
     */
    public static final boolean MOCK_LOCATION = false;

    /**
     * 只使用gps定位
     */
    public static final boolean LOC_ONLY_GPS = false;

    /**
     * 数据库名
     */
    public static final String DB_NAME = "db_" + CommonApplication.getInstance().getPackageName().replaceAll("\\.", "_");

    /**
     * 是否开启调试模式
     */
    public static final boolean DEBUG = true;

    /**
     * 请求器的最大网络并发线程数量
     */
    public static final int REQUESTER_MAX_CONCURRENT_THREAD = 3;


    public static final int SECOND = 1000;

    public static final int MINUTE = SECOND * 60;

    public static final int HOUR = MINUTE * 60;

    /**
     * 双击退出延迟时间
     */
    public static final long DOUBLE_TAP_EXIT_DELAY = 800;

    /**
     * 密码的最长长度
     */
    public static final int PASSWORD_MAX_LEN = 16;

    /**
     * 密码的最低长度
     */
    public static final int PASSWORD_MIN_LEN = 6;

    /**
     * 获取极光推送标识超时时长
     */
    public static final long GET_JPUSH_TIME_OUT = 1000 * 5;

    /**
     * 获取验证码的时间间隔
     */
    public static final long VERIFY_CODE_INTERVAL = 1000 * 120;

    public static final String EMPTY = "";

    /**
     * 一千米
     */
    public static final float KM = 1000;

    /**
     * 消息类型
     * 环信消息
     */
    public static final int MESSAGE_TYPE_EASE_MOB = 0x1;


    public static final File IMG_CACHE_DIR = StorageUtils.getOwnCacheDirectory(CommonApplication.getInstance(),
            CommonApplication.getInstance().getPackageName() + "/imageloader/Cache");
}
